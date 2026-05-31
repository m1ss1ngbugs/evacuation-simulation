package evacuation.sim.agent.human;

import evacuation.sim.model.Cell;
import evacuation.sim.routing.PathfindingStrategy;

import java.util.List;

public class Leader extends Evacuee{

    public Leader(int id, int logicalX, int logicalY, float health, float baseSpeed,
                    PathfindingStrategy pathfinder, float panicLevel, float reactionTime, float panicThreshold) {
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
}
