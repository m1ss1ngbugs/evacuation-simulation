package evacuation.sim.agent.human;

import evacuation.sim.model.Cell;
import evacuation.sim.routing.PathfindingStrategy;

import java.util.List;

public class Leader extends Evacuee{

    private Leader(int id, int logicalX, int logicalY, float health, float baseSpeed,
                    PathfindingStrategy pathfinder, float panicLevel, float reactionTime) {
        super(id, logicalX, logicalY, health, baseSpeed, pathfinder, panicLevel, reactionTime);
    }

    @Override
    protected void handlePanic(float dt){
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
        private float health;
        private float baseSpeed;
        private float reactionTime;
        private float panicThreshold;
        private PathfindingStrategy pathfinder;

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

        public Leader build(){
            return new Leader(id, logicalX, logicalY, health,
                    baseSpeed, pathfinder, panicThreshold, reactionTime);
        }
    }
}
