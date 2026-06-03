package evacuation.sim.agent.human;

import evacuation.sim.routing.PathfindingStrategy;

public class Follower extends Evacuee {
    private float socialFactor;

    private Follower(int id, int logicalX, int logicalY, float health,
                     float baseSpeed, PathfindingStrategy pathfinder,
                     float panicLevel, float reactionTime, float socialFactor, int visionRadius) {
        super(id, logicalX, logicalY, health, baseSpeed, pathfinder, panicLevel, reactionTime, visionRadius);
        this.socialFactor = socialFactor;
    }

    @Override
    protected void handlePanic(float dt){

        if (shouldPanic()) {
            setCurrentSpeed(getBaseSpeed() * 1.2f);

            if (getPanicLevel() > getPanicThreshold() * 1.5f && getPlannedPath() != null) {
                getPlannedPath().clear();
            }
        } else {

            setCurrentSpeed(getBaseSpeed());

            if (!sawHazard && getPanicLevel() > 0.0f) {
                setPanicLevel(Math.max(0.0f, getPanicLevel() - 0.1f * dt));
            }
        }
    }

    @Override
    protected boolean shouldPanic(){

        return getPanicLevel() > getPanicThreshold();
    }

    public void tryFollowLeader(Leader leader){

        int dx = leader.getLogicalX() - this.getLogicalX();
        int dy = leader.getLogicalY() - this.getLogicalY();
        double distanceToLeader = Math.sqrt(dx * dx + dy * dy);

        if (distanceToLeader <= this.getVisionRadius() && socialFactor > 0.5f && !shouldPanic()) {
            updatePathFromLeader(leader);
        }
    }
    // method of getting direction from the leader
    private void updatePathFromLeader(Leader leader){
        setPlannedPath(leader.sharePath());
    }

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
