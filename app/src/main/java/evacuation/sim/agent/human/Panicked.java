package evacuation.sim.agent.human;

import evacuation.sim.routing.PathfindingStrategy;

public class Panicked extends Evacuee{

    private Panicked(int id, int logicalX, int logicalY, float health, float baseSpeed,
                    PathfindingStrategy pathfinder, float panicLevel, float reactionTime, float panicThreshold) {
        super(id, logicalX, logicalY, health, baseSpeed, pathfinder, panicLevel, reactionTime);
    }

    @Override
    protected void handlePanic(float dt){
        // TODO: dopisać to, jak ten agent będzie radził sobie z paniką
    }

    @Override
    protected boolean shouldPanic(){
        boolean shouldPanic = true;

        // TODO: napisać logikę tego, kiedy agent musi panikować, a kiedy nie

        return shouldPanic;
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

        public Builder setPathfinder(PathfindingStrategy pathfinder) {
            this.pathfinder = pathfinder;
            return this;
        }

        public Builder setPanicThreshold(float panicThreshold) {
            this.panicThreshold = panicThreshold;
            return this;
        }

        public Panicked build(){
            return new Panicked(id, logicalX, logicalY, health,
                    baseSpeed, pathfinder, panicThreshold, reactionTime, panicThreshold);
        }
    }
}
