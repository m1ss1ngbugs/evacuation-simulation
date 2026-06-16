package evacuation.sim.agent.human;

import evacuation.sim.model.Cell;
import evacuation.sim.routing.PathfindingStrategy;
import evacuation.sim.model.Board;

import java.util.List;

/**
 * Leader class is responsible for psychological leader profile of evacuee type agents.
 * Manage Leader agents panic handling logic.
 * The Leader class represents an agent that actually exists on the board.
 * It has its own builder.
 * @author Heorhii Yartsev (293562)
 * @author Bartłomiej Krajewski (293439)
 */
public class Leader extends Evacuee{

    private Leader(int id, int logicalX, int logicalY, float health, float baseSpeed,
                    PathfindingStrategy pathfinder, float panicThreshold , float reactionTime, int visionRadius) {
        super(id, logicalX, logicalY, health, baseSpeed, pathfinder, panicThreshold, reactionTime, visionRadius);
    }

    /**
     * {@inheritDoc}
     * Leader never panics.
     */
    @Override
    protected void handlePanic(float dt, Board board){

        setPanicLevel(0.0f);
        // doesn't need this logic, because he never panics
    }

    /**
     * {@inheritDoc}
     * Leader never panics.
     */
    @Override
    protected boolean shouldPanic(){
        // leader never panics
        return false;
    }

    /**
     * This method is responsible for getting path of the leader.
     * @return list of cells ({@link Cell class}}, path of the evacuee
     */
    public List<Cell> sharePath(){
        return getPlannedPath();
    }

    /**
     * An internal class responsible for creating new Leader agents via the agent factory.
     */
    public static class Builder {
        private int id;
        private int logicalX;
        private int logicalY;
        private float health = 100.0f;
        private float baseSpeed = 1.3f;
        private float reactionTime = 0.1f;
        // private float panicThreshold;
        private PathfindingStrategy pathfinder;
        private int visionRadius = 5;

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

        public Builder setPathfinder(PathfindingStrategy pathfinder) {
            this.pathfinder = pathfinder;
            return this;
        }

        public Builder setVisionRadius(int visionRadius) {
            this.visionRadius = visionRadius;
            return this;
        }

        public Leader build(){
            float panicThreshold = 0.0f;
            if (pathfinder == null) {
                throw new IllegalStateException("Pathfinder must be set for Leader agent");
            }
            return new Leader(id, logicalX, logicalY, health,
                    baseSpeed, pathfinder, panicThreshold, reactionTime, visionRadius);
        }
    }
}
