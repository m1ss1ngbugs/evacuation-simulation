package evacuation.sim.agent.human;

import evacuation.sim.routing.PathfindingStrategy;

public class Panicked extends Evacuee{
    private float panicThreshold;

    public Panicked(int id, int logicalX, int logicalY, float health, float baseSpeed,
                    PathfindingStrategy pathfinder, float panicLevel, float reactionTime, float panicThreshold) {
        super(id, logicalX, logicalY, health, baseSpeed, pathfinder, panicLevel, reactionTime);
        this.panicThreshold = panicThreshold;
    }

    @Override
    protected void handlePanic(float dt){
        // dopisać to, jak ten agent będzie radził sobie z paniką
    }

    @Override
    protected boolean shouldPanic(){
        boolean shouldPanic = true;

        // napisać logikę tego, kiedy agent musi panikować, a kiedy nie

        return shouldPanic;
    }
}
