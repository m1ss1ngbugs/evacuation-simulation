package evacuation.sim.agent.human;

import evacuation.sim.routing.PathfindingStrategy;

public class Follower extends Evacuee {
    private float socialFactor;

    private Follower(int id, int logicalX, int logicalY, float health, float baseSpeed,
                    PathfindingStrategy pathfinder, float panicLevel, float reactionTime, float socialFactor) {
        super(id, logicalX, logicalY, health, baseSpeed, pathfinder, panicLevel, reactionTime);
        this.socialFactor = socialFactor;
    }

    @Override
    protected void handlePanic(float dt){
        // TODO: logika radzenia sobie z paniką dla followera
    }

    @Override
    protected boolean shouldPanic(){
        boolean shouldPanic = true;

        // TODO: napisać logikę tego, kiedy agent musi panikować, a kiedy nie

        return shouldPanic;
    }

    // method of getting direction from the leader
    private void updatePathFromLeader(Leader leader){
        setPlannedPath(leader.sharePath());
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
        private float socialFactor;

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

        public Follower build(){
            return new Follower(id, logicalX, logicalY, health,
                    baseSpeed, pathfinder, panicThreshold, reactionTime, socialFactor);
        }
    }


}
