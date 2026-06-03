package evacuation.sim.agent.human;

import evacuation.sim.model.Cell;
import evacuation.sim.routing.PathfindingStrategy;

import java.util.List;

public class Leader extends Evacuee{

    private Leader(int id, int logicalX, int logicalY, float health, float baseSpeed,
                    PathfindingStrategy pathfinder, float panicThreshold , float reactionTime, int visionRadius) {
        super(id, logicalX, logicalY, health, baseSpeed, pathfinder, panicThreshold, reactionTime, visionRadius);
    }

    @Override
    protected void handlePanic(float dt){

        setPanicLevel(0.0f);
        // doesn't need this logic, because he never panics
    }

    @Override
    protected boolean shouldPanic(){
        // leader never panic

        boolean shouldPanic = false;

        return shouldPanic;
    }

    public List<Cell> sharePath(){
        return getPlannedPath();
    }

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
