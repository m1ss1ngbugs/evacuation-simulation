package evacuation.sim.factory;
import evacuation.sim.SimSingletonConfig;
import evacuation.sim.agent.Agent;
import evacuation.sim.agent.hazard.Fire;
import evacuation.sim.agent.hazard.Smoke;
import evacuation.sim.agent.human.Follower;
import evacuation.sim.agent.human.Leader;
import evacuation.sim.agent.human.Panicked;
import evacuation.sim.routing.PathfindingStrategy;

public class AgentFactory {
    private static int nextId = 0;
    static SimSingletonConfig config = SimSingletonConfig.getInstance();

    private AgentFactory() {}

    private static int getNextId() {
        return nextId++;
    }

    // Hazard section

    public static Agent createFire(int logicalX, int logicalY) {
        int id = getNextId();

        return new Fire.Builder()
                .setId(id)
                .setPosition(logicalX, logicalY)
                .setSpreadInterval(config.getFireSpreadInterval())
                .setDamagePerSecond(config.getFireDamagePerSecond())
                .setIncubationDelay(config.getFireIncubationDelay())
                .build();
    }

    public static Agent createSmoke(int logicalX, int logicalY, float density) {
        int id = getNextId();
        return new Smoke.Builder()
                .setId(id)
                .setPosition(logicalX, logicalY)
                .setDensity(density)
                .setFadeRatePerSecond(config.getSmokeFadeRatePerSecond())
                .setSpreadInterval(config.getSmokeSpreadInterval())
                .setDuplicationThreshold(config.getSmokeDuplicationThreshold())
                .setDamagePerSecond(config.getSmokeDamagePerSecond())
                .build();
    }

    // Evacuee section

    public static Agent createLeader(int logicalX, int logicalY, PathfindingStrategy pathfinder) {
        int id = getNextId();
        return new Leader.Builder()
                .setId(id)
                .setPosition(logicalX, logicalY)
                .setHealth(config.getEvacueeHealth())
                .setBaseSpeed(config.getMeanBaseSpeed())
                .setReactionTime(config.getEvacueeReactionTime())
                .setPathfinder(pathfinder)
                .setVisionRadius(config.getEvacueeVisionRadius())
                .build();
    }

    public static Agent createFollower(int logicalX, int logicalY, PathfindingStrategy pathfinder) {
        int id = getNextId();
        return new Follower.Builder()
                .setId(id)
                .setPosition(logicalX, logicalY)
                .setHealth(config.getEvacueeHealth())
                .setBaseSpeed(config.getMeanBaseSpeed())
                .setReactionTime(config.getEvacueeReactionTime())
                // TODO: otrzymuje średni: MeanPanicThreshold - potencjalnie poprawić
                .setPanicThreshold(config.getMeanPanicThreshold())
                .setPathfinder(pathfinder)
                // TODO: otrzymuje średni: MeanSocialFactor - potencjalnie poprawić
                .setSocialFactor(config.getMeanSocialFactor())
                .setVisionRadius(config.getEvacueeVisionRadius())
                .build();
    }

    public static Agent createPanicked(int logicalX, int logicalY, PathfindingStrategy pathfinder) {
        int id = getNextId();
        return new Panicked.Builder()
                .setId(id)
                .setPosition(logicalX, logicalY)
                .setHealth(config.getEvacueeHealth())
                .setBaseSpeed(config.getMeanBaseSpeed())
                .setReactionTime(config.getEvacueeReactionTime())
                // TODO: otrzymuje średni: MeanPanicThreshold - potencjalnie poprawić
                .setPanicThreshold(config.getMeanPanicThreshold())
                .setPathfinder(pathfinder)
                .setVisionRadius(config.getEvacueeVisionRadius())
                .build();

    }

    // Auxiliary method for randomizing agent type
    public static Agent createRandomEvacuee(int logicalX, int logicalY, PathfindingStrategy pathfinder) {
        // randomly select the agent type (from 0.0 to 1.0)
        double randomType = Math.random();
        // decide who the agent will be, based on configuration
        if (randomType < config.getLeaderRatio()) {
            return createLeader(logicalX, logicalY, pathfinder);
        } else if (randomType < config.getLeaderRatio() + config.getPanickedRatio()) {
            return createPanicked(logicalX, logicalY, pathfinder);
        } else {
            return createFollower(logicalX, logicalY, pathfinder);
        }
    }
}
