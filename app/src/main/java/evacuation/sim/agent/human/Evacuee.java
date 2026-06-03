package evacuation.sim.agent.human;

import evacuation.sim.agent.Agent;
import evacuation.sim.agent.Damageable;
import evacuation.sim.model.BaseType;
import evacuation.sim.model.Board;
import evacuation.sim.model.Cell;
import evacuation.sim.model.DynamicState;
import evacuation.sim.routing.AStarPathfinder;
import evacuation.sim.routing.PathfindingStrategy;

import java.util.List;

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

    @Override
    public void update(Board board, float dt){
        // if mental map doesn't exist
        if(this.mentalMap == null) {
            initializeMentalMap(board, dt);
        }
        perceive(board);
        psychoReaction(sawHazard, dt);
        verifyPath();
        move(dt);
    }

    protected void move(float dt){
        // TODO: napisz metodę dla ruchu: agent musi poruszać się currentSpeed*dt w potrzebną strone
        // (algorytm calculatePath znalazł by kierunek, jeżeliby zgadzały parametry pathfinder.findPath z diagramem)
    }

    protected void psychoReaction(boolean sawHazard, float dt){
        // evacuee panic increases in case he sees the hazard
        panicLevel += 0.5f * dt;
    }

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

    protected void calculatePath(){
        Cell startCell = mentalMap[this.getLogicalX()][this.getLogicalY()];
        Cell endCell = findClosestExit();

        if (startCell != null && endCell != null) {
            // TODO: sprawdź algorytm poszukiwania drogi, coś jest tu źle
         //   plannedPath = pathfinder.findPath(startCell, endCell, mentalMap);
        }
    }

    protected void verifyPath(){
        // 4. current mental map verification
        if (plannedPath != null && !plannedPath.isEmpty()) {

            // planned path review
            for (Cell pathCell : plannedPath) {
                // reach to evacuee memory to check what it knows about the cell
                Cell mentalCell = this.mentalMap[pathCell.getLogicalX()][pathCell.getLogicalY()];

                // checks road safety...
                if (mentalCell != null &&
                        (mentalCell.getDynamicState() == DynamicState.FIRE ||
                                mentalCell.getBaseType() == BaseType.OBSTACLE)) {
                    // the route is useless --> forgot about it
                    plannedPath.clear();
                    // evacuee must find another way
                    calculatePath();
                    break;
                }
            }
        }
    }

    private Cell findClosestExit() {
        Cell closestExit = null;
        double minDistance = Double.MAX_VALUE; // start from plus infinity

        // search the agents mental map
        for (int x = 0; x < mentalMap.length; x++) {
            for (int y = 0; y < mentalMap[x].length; y++) {
                Cell cell = mentalMap[x][y];

                // is the Cell the output Cell?
                if (cell.getBaseType() == BaseType.EXIT) {

                    // calculate the distance from the agent to the exit
                    double dx = x - this.getLogicalX();
                    double dy = y - this.getLogicalY();
                    double distance = Math.sqrt(dx * dx + dy * dy);

                    // saving the exit cell if it is closer than the last one found
                    if (distance < minDistance) {
                        minDistance = distance;
                        closestExit = cell;
                    }
                }
            }
        }
        return closestExit;
    }

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

    protected abstract void handlePanic(float dt);
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
}
