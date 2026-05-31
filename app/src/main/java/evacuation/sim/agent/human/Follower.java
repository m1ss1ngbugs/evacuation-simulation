package evacuation.sim.agent.human;

import evacuation.sim.routing.PathfindingStrategy;

public class Follower extends Evacuee {
    private float socialFactor;

    public Follower(int id, int logicalX, int logicalY, float health, float baseSpeed,
                    PathfindingStrategy pathfinder, float panicLevel, float reactionTime, float socialFactor) {
        super(id, logicalX, logicalY, health, baseSpeed, pathfinder, panicLevel, reactionTime);
        this.socialFactor = socialFactor;
    }

    @Override
    protected void handlePanic(float dt){
        // logika radzenia sobie z paniką dla followera
    }

    @Override
    protected boolean shouldPanic(){
        boolean shouldPanic = true;

        // napisać logikę tego, kiedy agent musi panikować, a kiedy nie

        return shouldPanic;
    }

    // method of getting direction from the leader
    private void updatePathFromLeader(Leader leader){
        setPlannedPath(leader.sharePath());
    }

}
