package evacuation.sim.agent.human;

import java.util.List;

import evacuation.sim.agent.Agent;
import evacuation.sim.model.BaseType;
import evacuation.sim.routing.PathfindingStrategy;
import evacuation.sim.model.Board;
import evacuation.sim.model.Cell;

/**
 * Follower class is responsible for psychological follower profile of evacuee type agents.
 * Manage Follower agents panic handling logic.
 * The Follower class represents an agent that actually exists on the board.
 * It has its own builder.
 * @author Heorhii Yartsev (293562)
 * @author Bartłomiej Krajewski (293439)
 */
public class Follower extends Evacuee {
    private float socialFactor;

    private Follower(int id, int logicalX, int logicalY, float health,
                     float baseSpeed, PathfindingStrategy pathfinder,
                     float panicLevel, float reactionTime, float socialFactor, int visionRadius) {
        super(id, logicalX, logicalY, health, baseSpeed, pathfinder, panicLevel, reactionTime, visionRadius);
        this.socialFactor = socialFactor;
    }

    /**
     * {@inheritDoc}
     * Tries to follow a leader if he sees him.
     * Increases agent speed when panicking.
     * Forces the agent to take random steps when panicking and has no path.
     */
    @Override
    protected void handlePanic(float dt, Board board){
        if (shouldPanic()) {
            setCurrentSpeed(getBaseSpeed() * 1.2f);

            Leader nearbyLeader = findNearbyLeader(board);
            if (nearbyLeader != null) {
                // If he sees a leader, he tries to follow him.
                tryFollowLeader(nearbyLeader);
            }

            if (getPanicLevel() > getPanicThreshold() * 1.5f && (getPlannedPath() == null ||
                    getPlannedPath().isEmpty())) {
                if (getRenderX() == getLogicalX() && getRenderY() == getLogicalY()) { // if he is on tile
                
                    List<Cell> neighbors = board.getNeighbors(board.getCell(this.getLogicalX(), this.getLogicalY()));
                    List<Cell> validChoices = new java.util.ArrayList<>();

                    for (Cell neighbor : neighbors) {
                        if (neighbor.getBaseType() != BaseType.WALL && neighbor.getBaseType() != BaseType.OBSTACLE) {
                            if (board.getAgentsAt(neighbor).isEmpty()) {
                                validChoices.add(neighbor);
                            }
                        }
                    }

                    // If found, random direction
                    if (!validChoices.isEmpty()) {
                        int randomIndex = (int)(Math.random() * validChoices.size());
                        Cell randomDestination = validChoices.get(randomIndex);

                        if(getPlannedPath() == null) {
                            setPlannedPath(new java.util.ArrayList<>());
                        }

                        getPlannedPath().add(board.getCell((int)getLogicalX(), (int)getLogicalY()));
                        getPlannedPath().add(randomDestination);
                    }
                }
            }
        } else {

            setCurrentSpeed(getBaseSpeed());

            if (!sawHazard && getPanicLevel() > 0.0f) {
                setPanicLevel(Math.max(0.0f, getPanicLevel() - 0.1f * dt));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean shouldPanic(){

        return getPanicLevel() > getPanicThreshold();
    }

    public void tryFollowLeader(Leader leader){

        int dx = leader.getLogicalX() - this.getLogicalX();
        int dy = leader.getLogicalY() - this.getLogicalY();
        double distanceToLeader = Math.sqrt(dx * dx + dy * dy);

        if (distanceToLeader <= this.getVisionRadius() && socialFactor > 0.5f && shouldPanic()) {
            updatePathFromLeader(leader);
        }
    }

    /**
     * Method of getting direction from the leader.
     * @param leader leader agent ({@link Leader} class) from whom we want to get the way.
     */
    private void updatePathFromLeader(Leader leader){
        List<Cell> pathFromLeader = leader.sharePath();

        // Protection against NullPointerException
        if (pathFromLeader != null && !pathFromLeader.isEmpty()) {
            setPlannedPath(new java.util.ArrayList<>(pathFromLeader));

            // the sight of the leader slightly reduces the agent's panic
            setPanicLevel(getPanicThreshold() * 0.9f);
        }
    }

    /**
     * Scans the environment for Leader-class agents.
     * Uses the sight radius and the Line of Sight algorithm.
     *
     * @param board board mockup with access to the spatial index
     * @return the closest visible Leader, or null if there are no Leaders
     */
    private Leader findNearbyLeader(Board board) {
        int myX = this.getLogicalX();
        int myY = this.getLogicalY();
        int radius = this.getVisionRadius();

        // Scanning the area around the agent (square)
        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int targetX = myX + dx;
                int targetY = myY + dy;

                if (targetX >= 0 && targetX < board.getWidth() &&
                        targetY >= 0 && targetY < board.getHeight() &&
                        board.hasLineOfSight(myX, myY, targetX, targetY)) {

                    // Gets the list of agents standing on this tile
                    List<Agent> agentsAtTarget = board.getAgentsAt(board.getCell(targetX, targetY));

                    // Looking for a Leader in the crowd
                    for (Agent a : agentsAtTarget) {
                        if (a instanceof Leader) {
                            return (Leader) a;
                        }
                    }
                }
            }
        }
        return null; // No leader in sight
    }

    /**
     * An internal class responsible for creating new Follower agents via the agent factory.
     */
    public static class Builder {
        private int id;
        private int logicalX;
        private int logicalY;
        private float health = 100.0f;
        private float baseSpeed = 1.0f;
        private float reactionTime = 0.3f;
        private float panicThreshold = 5.0f;
        private PathfindingStrategy pathfinder;
        private float socialFactor = 0.5f;
        private int visionRadius = 4;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setPosition(int logicalX, int logicalY) {
            this.logicalX = logicalX;
            this.logicalY = logicalY;
            return this;
        }

        public Builder setHealth(float health) {
            this.health = health;
            return this;
        }

        public Builder setBaseSpeed(float baseSpeed) {
            this.baseSpeed = baseSpeed;
            return this;
        }

        public Builder setReactionTime(float reactionTime) {
            this.reactionTime = reactionTime;
            return this;
        }

        public Builder setPanicThreshold(float panicThreshold) {
            this.panicThreshold = panicThreshold;
            return this;
        }

        public Builder setPathfinder(PathfindingStrategy pathfinder) {
            this.pathfinder = pathfinder;
            return this;
        }

        public Builder setSocialFactor(float socialFactor) {
            this.socialFactor = socialFactor;
            return this;
        }

        public Builder setVisionRadius(int visionRadius) {
            this.visionRadius = visionRadius;
            return this;
        }

        public Follower build(){
            if (pathfinder == null) {
                throw new IllegalStateException("Pathfinder must be set for Follower agent");
            }
            return new Follower(id, logicalX, logicalY, health,
                    baseSpeed, pathfinder, panicThreshold, reactionTime, socialFactor, visionRadius);
        }
    }


}
