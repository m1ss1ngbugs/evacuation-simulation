package evacuation.sim.agent.human;

import evacuation.sim.agent.Agent;
import evacuation.sim.agent.Damageable;
import evacuation.sim.model.BaseType;
import evacuation.sim.model.Board;
import evacuation.sim.model.Cell;
import evacuation.sim.model.DynamicState;
import evacuation.sim.routing.PathfindingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for evacuee agents with different psychological types (leader, follower, panicked)
 * <p>
 * Inherits from the {@link Agent} class.
 * This allows them to have their own lifecycle (update method) and
 * can be managed by the main loop of the simulation engine, just like hazards.
 * This class is responsible for evacuees common properties and logic of evacuees such as:
 * movement, the ability to perceive the world, receiving damage, searching for exits,
 * a mental map and its management and its use to check the existing route.
 * @author Heorhii Yartsev (293562)
 * @author Bartłomiej Krajewski (293439)
 */
public abstract class Evacuee extends Agent implements Damageable {
    private float health;
    private final float baseSpeed;
    private float currentSpeed;
    private final float reactionTime;
    private float panicLevel;
    private final float panicThreshold;
    private float internalTimer;
    private Cell[][] mentalMap;
    private List<Cell> plannedPath;
    private PathfindingStrategy pathfinder;
    private int visionRadius;
    boolean sawHazard;
    private boolean isAwareOfHazard = false;

    public Evacuee(int id, int logicalX, int logicalY, float health, float baseSpeed,
                   PathfindingStrategy pathfinder, float panicThreshold, float reactionTime, int visionRadius) {
        super(id, logicalX, logicalY);
        this.health = health;
        this.baseSpeed = baseSpeed;
        this.pathfinder = pathfinder;
        this.reactionTime = reactionTime;
        this.panicThreshold = panicThreshold;
        this.visionRadius = visionRadius;

        this.currentSpeed = baseSpeed;
        this.panicLevel = 0.0f;
        this.internalTimer = 0.0f;
        this.plannedPath = null;
        this.mentalMap = null;
        this.sawHazard = false;
    }

    /**
     * {@inheritDoc}
     * Scans the environment, looks for hazards, and places them on its mental map.
     * Searches for a way out after spotting a hazard using the selected pathfinder strategy.
     * Checks the existing route, moves, and panics when the panic threshold is exceeded.
     */
    @Override
    public void update(Board board, float dt){
        this.internalTimer += dt;

        // if mental map doesn't exist
        if(this.mentalMap == null) {
            initializeMentalMap(board, dt);
        }

        perceive(board);

        if (this.sawHazard){
            this.isAwareOfHazard = true;
        }

        if (!this.isAwareOfHazard) {return;}

        if (this.internalTimer < this.reactionTime) {return;}

        if (this.plannedPath == null || this.plannedPath.isEmpty()) {
            calculatePath(); 
        }

        psychoReaction(sawHazard, dt);
        handlePanic(dt, board);
        verifyPath();
        
        move(dt, board);
    }

    /**
     * Method responsible for moving the agent across the simulation board towards the exit.
     * Prevents agents from overlapping with one another.
     * @param board Current state of the board layout (object of class {@link Board}.
     * @param dt Delta time (in seconds) gone since the last simulation frame.
     */
    protected void move(float dt, Board board) {
    if (plannedPath == null || plannedPath.isEmpty()) {
        return;
    }

    // the target is a cell we plan to go
    Cell targetCell = plannedPath.get(0);

    if (this.getLogicalX() == targetCell.getLogicalX() && this.getLogicalY() == targetCell.getLogicalY() 
        && this.getRenderX() == targetCell.getLogicalX() && this.getRenderY() == targetCell.getLogicalY()) {
        
        if (plannedPath.size() > 1) {
            Cell nextCell = plannedPath.get(1);
            
            // checking if next tile is free
            boolean isNextOccupied = false;
            Evacuee blockingEvacuee = null;
            List<Agent> agentsOnTarget = board.getAgentsAt(nextCell);
            if (agentsOnTarget != null) {
                for (Agent a : agentsOnTarget) {
                    if (a instanceof Evacuee && a.getId() != this.getId()) {
                        isNextOccupied = true;
                        
                        // letting know of a danger
                        blockingEvacuee = (Evacuee) a;
                        if (!blockingEvacuee.isAwareOfHazard) {
                            blockingEvacuee.isAwareOfHazard = true;
                            blockingEvacuee.internalTimer = 0.0f;
                        }
                        break;
                    }
                }
            }

            if (isNextOccupied) {
                // info exchange
                this.shareKnowledgeWith(blockingEvacuee);
                blockingEvacuee.shareKnowledgeWith(this);
                // path update
                this.plannedPath.clear();
                // waiting
                return; 
            }

            // deleting old tile, reserving planned one
            plannedPath.remove(0);
            targetCell = plannedPath.get(0);
            
            // assign the local position before the render gets there
            setLogicalX(targetCell.getLogicalX());
            setLogicalY(targetCell.getLogicalY());
        } else {
            plannedPath.remove(0);
            return;
        }
    }

    // useful for rendering (so evacuee can smoothly go)
    float targetX = targetCell.getLogicalX();
    float targetY = targetCell.getLogicalY();

    float dirX = targetX - this.getRenderX();
    float dirY = targetY - this.getRenderY();
    float distance = (float) Math.sqrt(dirX * dirX + dirY * dirY);

    float distanceToMove = currentSpeed * dt;

    if (distanceToMove >= distance) {
        // we came to the reserved tile
        setRenderX(targetX);
        setRenderY(targetY);
    } else {
        // we go to reserved tile
        setRenderX(getRenderX() + (dirX / distance) * distanceToMove);
        setRenderY(getRenderY() + (dirY / distance) * distanceToMove);
    }
}

    /**
     * Simulates communication between agents (transferring threat information).
     * Retrieves fire or smoke tiles from the other agent's mental map and
     * updates its own mental map.
     * @param other another evacuated agent with whom we are exchanging information
     */
    protected void shareKnowledgeWith(Evacuee other) {
        if (this.mentalMap == null || other.mentalMap == null) return;

        int width = this.mentalMap.length;
        int height = this.mentalMap[0].length;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell otherCell = other.mentalMap[x][y];

                // If the other evacuee has information about the danger in this field stored in his head
                if (otherCell != null && (otherCell.getDynamicState() == DynamicState.FIRE || otherCell.getDynamicState() == DynamicState.SMOKE)) {

                    Cell myCell = this.mentalMap[x][y];
                    // this agent info is outdated
                    if (myCell != null && myCell.getDynamicState() != otherCell.getDynamicState()) {
                        // updating the memory, replacing safe cell with a dangerous cell.
                        this.mentalMap[x][y] = new Cell(x, y, myCell.getBaseType());
                        this.mentalMap[x][y].setDynamicState(otherCell.getDynamicState());

                        this.sawHazard = true;
                        this.isAwareOfHazard = true;
                    }
                }
            }
        }
    }

    /**
     * Method psychoReaction increases panicLevel of the agent depending on
     * whether the agent sees a threat and how much time has passed.
     * @param sawHazard the agent's logical variable responsible for whether it sees fire
     * @param dt delta time - minimal time change between simulation ticks.
     */
    protected void psychoReaction(boolean sawHazard, float dt){
        // evacuee panic increases in case he sees the hazard
        panicLevel += 0.5f * dt;
    }

    /**
     * Makes deep copy of the board ({@link Board} class) and saves it like a mental map of the evacuee.
     * @param board Current state of the board layout (object of class {@link Board}.
     * @param dt Delta time (in seconds) gone since the last simulation frame.
     */
    private void initializeMentalMap(Board board, float dt){
        // make deep copy of the initial board
        int width = board.getWidth();
        int height = board.getHeight();
        this.mentalMap = new Cell[width][height];

        // iterates throw all the Cells
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Cell realCell = board.getCell(x, y);
                // copy all real Cells to the mental map
                this.mentalMap[x][y] = new Cell(x, y, realCell.getBaseType());
            }
        }
    }

    /**
     * This method updates the agent's mental map based on what it sees along a straight line
     * in its vision radius.
     * It uses the hasLineOfSight method in the {@link Board} class to check if evacuee sees this Cell.
     * @param board Current state of the board layout (object of class {@link Board}).
     */
    protected void perceive(Board board) {
        sawHazard = false;
        int myX = this.getLogicalX();
        int myY = this.getLogicalY();

        // square scanning
        for (int dx = -visionRadius; dx <= visionRadius; dx++) {
            for (int dy = -visionRadius; dy <= visionRadius; dy++) {
                // actual scan Cell coordinates
                int targetX = myX + dx;
                int targetY = myY + dy;

                // Checks if evacuee looking outside the board boundaries
                if (targetX >= 0 && targetX < board.getWidth() && targetY >= 0 && targetY < board.getHeight()) {

                    if (board.hasLineOfSight(myX, myY, targetX, targetY)) {
                        // evacuee see the Cell
                        Cell observedCell = board.getCell(targetX, targetY);

                        // saves it in his memory (updates his mental database)
                        this.mentalMap[targetX][targetY] = observedCell;

                        // checks if there are any hazards in the field of view
                        if (observedCell.getDynamicState() == DynamicState.FIRE ||
                            observedCell.getDynamicState() == DynamicState.SMOKE) {
                            sawHazard = true;
                        }
                    }
                }
            }
        }
    }

    /**
     * This method finds a path for the evacuee using a pathfinding strategy.
     * It passes to the findPath method from {@link evacuation.sim.routing.AStarPathfinder} class
     * its starting cell, exits list, and a copy of the mental map.
     */
    protected void calculatePath(){
        Cell startCell = mentalMap[this.getLogicalX()][this.getLogicalY()];
        List<Cell> exitCells = findAllExits();

        if (startCell != null && !exitCells.isEmpty()) {
            // creating virtual board, so pathfinder can work with it
            Board virtualBoard = new Board(this.mentalMap);

            this.plannedPath = pathfinder.findPath(startCell, exitCells, virtualBoard);
        }
    }

    /**
     * Checks the evacuee's route for obstacles and resets his planned route if any are noticed.
     */
    protected void verifyPath(){
        // current mental map verification
        if (plannedPath != null && !plannedPath.isEmpty()) {
            // planned path review
            for (int i = 1; i < plannedPath.size(); i++) {
                Cell pathCell = plannedPath.get(i);
                // reach to evacuee memory to check what it knows about the cell
                Cell mentalCell = this.mentalMap[pathCell.getLogicalX()][pathCell.getLogicalY()];

                // checks road safety
                if (mentalCell != null && (mentalCell.getBaseType() == BaseType.OBSTACLE ||
                        mentalCell.getDynamicState() == DynamicState.FIRE)) {
                    // the route is useless --> forgot about it
                    plannedPath.clear();
                    // evacuee must find another way
                    calculatePath();
                    break;
                }
            }
        }
    }

    /**
     * Gets a list of all exits the agent has in memory
     * @return list of cells (objects of class {@link Cell}) with exits
     */
    private List<Cell> findAllExits() {
        List<Cell> exits = new ArrayList<>();
        if (mentalMap == null) return exits;

        for (Cell[] cells : mentalMap) {
            for (Cell cell : cells) {
                if (cell != null && cell.getBaseType() == BaseType.EXIT) {
                    exits.add(cell);
                }
            }
        }
        return exits;
    }

    /**
     * {@inheritDoc}
     * Checks if it's alive.
     */
    @Override
    public void takeDamage(float amount) {
        // health reduce by a certain amount
        float newHealth = this.getHealth() - amount;
        this.setHealth(newHealth);

        // logic checking agent death
        if (this.getHealth() <= 0) {
            this.deactivate();
        }
    }

    /**
     * Responsible for the logic that the agent executes after exceeding the panic threshold
     * (entering a panic state)
     * @param board Current state of the board layout (object of class {@link Board}).
     * @param dt Delta time (in seconds) gone since the last simulation frame.
     */
    protected abstract void handlePanic(float dt, Board board);

    /**
     * Checks if the agent's panic level has exceeded his panic threshold.
     * @return {@code true} if he exceeded; {@code false} otherwise.
     */
    protected abstract boolean shouldPanic();

    // getters and setters below

    public float getHealth() {
        return health;
    }

    protected void setHealth(float health) {
        this.health = health;
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    protected void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public float getInternalTimer() {
        return internalTimer;
    }

    protected void setInternalTimer(float internalTimer) {
        this.internalTimer = internalTimer;
    }

    public float getPanicLevel() {
        return panicLevel;
    }

    protected void setPanicLevel(float panicLevel) {
        this.panicLevel = panicLevel;
    }

    public float getPanicThreshold() {
        return panicThreshold;
    }

    public float getBaseSpeed() {
        return baseSpeed;
    }

    public float getReactionTime() {
        return reactionTime;
    }

    public PathfindingStrategy getPathfinder() {
        return pathfinder;
    }

    public List<Cell> getPlannedPath() {
        return plannedPath;
    }

    protected void setPlannedPath(List<Cell> plannedPath) {
        this.plannedPath = plannedPath;
    }

    public Cell[][] getMentalMap() {
        return mentalMap;
    }

    public int getVisionRadius() {
        return visionRadius;
    }
}
