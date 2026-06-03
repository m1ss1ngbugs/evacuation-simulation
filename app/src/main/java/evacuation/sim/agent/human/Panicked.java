package evacuation.sim.agent.human;

import evacuation.sim.routing.PathfindingStrategy;

public class Panicked extends Evacuee{

    private Panicked(int id, int logicalX, int logicalY, float health, float baseSpeed,
                    PathfindingStrategy pathfinder, float panicThreshold, float reactionTime, int visionRadius) {
        super(id, logicalX, logicalY, health, baseSpeed, pathfinder, panicThreshold, reactionTime, visionRadius);
    }

    @Override
    protected void handlePanic(float dt){
        
        if (shouldPanic()) {
            setCurrentSpeed(getBaseSpeed() * 1.6f);

            if (getPlannedPath() != null && !getPlannedPath().isEmpty()) {
                getPlannedPath().clear();
            }
        } else {
            setCurrentSpeed(getBaseSpeed());
        }
    }

    @Override
    protected boolean shouldPanic(){

        return getPanicLevel() > getPanicThreshold();
    }

    public static class Builder {
        private int id;
        private int logicalX;
        private int logicalY;
        private float health = 100.0f;
        private float baseSpeed = 1.1f;
        private float reactionTime = 0.05f;
        private float panicThreshold = 1.5f;
        private PathfindingStrategy pathfinder;
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

        public Builder setPathfinder(PathfindingStrategy pathfinder) {
            this.pathfinder = pathfinder;
            return this;
        }

        public Builder setPanicThreshold(float panicThreshold) {
            this.panicThreshold = panicThreshold;
            return this;
        }

        public Builder setVisionRadius(int visionRadius) {
            this.visionRadius = visionRadius;
            return this;
        }

        public Panicked build(){
            if (pathfinder == null) {
                throw new IllegalStateException("Pathfinder must be setfor Panicked agent");
            }
            return new Panicked(id, logicalX, logicalY, health,
                    baseSpeed, pathfinder, panicThreshold, reactionTime, visionRadius);
        }
    }
}
