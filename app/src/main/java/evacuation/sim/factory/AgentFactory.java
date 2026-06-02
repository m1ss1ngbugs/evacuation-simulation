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
                .setSpreadInterval(SimSingletonConfig.getInstance().getFireSpreadInterval())
                .setDamagePerSecond(SimSingletonConfig.getInstance().getFireDamagePerSecond())
                .setIncubationDelay(SimSingletonConfig.getInstance().getFireIncubationDelay())
                .build();
    }

    public static Agent createSmoke(int logicalX, int logicalY, float density) {
        int id = getNextId();
        return new Smoke.Builder()
                .setId(id)
                .setPosition(logicalX, logicalY)
                .setDensity(density)
                .setFadeRatePerSecond(SimSingletonConfig.getInstance().getSmokeFadeRatePerSecond())
                .setSpreadInterval(SimSingletonConfig.getInstance().getSmokeSpreadInterval())
                .setDuplicationThreshold(SimSingletonConfig.getInstance().getSmokeDuplicationThreshold())
                .setDamagePerSecond(SimSingletonConfig.getInstance().getSmokeDamagePerSecond())
                .build();
    }

    // Evacuee section

    public static Agent createLeader(int logicalX, int logicalY, PathfindingStrategy pathfinder) {
        int id = getNextId();
        return new Leader.Builder()
                .setId(id)
                .setPosition(logicalX, logicalY)
                .setHealth(SimSingletonConfig.getInstance().getEvacueeHealth())
                .setBaseSpeed(SimSingletonConfig.getInstance().getMeanBaseSpeed())
                .setReactionTime(SimSingletonConfig.getInstance().getEvacueeReactionTime())
                // TODO: otrzymuje średni: MeanPanicThreshold - potencjalnie poprawić
                .setPanicThreshold(SimSingletonConfig.getInstance().getMeanPanicThreshold())
                .setPathfinder(pathfinder)
                .build();
    }

    public static Agent createFollower(int logicalX, int logicalY, PathfindingStrategy pathfinder) {
        int id = getNextId();
        return new Follower.Builder()
                .setId(id)
                .setPosition(logicalX, logicalY)
                .setHealth(SimSingletonConfig.getInstance().getEvacueeHealth())
                .setBaseSpeed(SimSingletonConfig.getInstance().getMeanBaseSpeed())
                .setReactionTime(SimSingletonConfig.getInstance().getEvacueeReactionTime())
                // TODO: otrzymuje średni: MeanPanicThreshold - potencjalnie poprawić
                .setPanicThreshold(SimSingletonConfig.getInstance().getMeanPanicThreshold())
                .setPathfinder(pathfinder)
                // TODO: otrzymuje średni: MeanSocialFactor - potencjalnie poprawić
                .setSocialFactor(SimSingletonConfig.getInstance().getMeanSocialFactor())
                .build();
    }

    public static Agent createPanicked(int logicalX, int logicalY, PathfindingStrategy pathfinder) {
        int id = getNextId();
        // TODO: podłączyć EvacueeBuilder tu w przyszłości
        return new Panicked.Builder()
                .setId(id)
                .setPosition(logicalX, logicalY)
                .setHealth(SimSingletonConfig.getInstance().getEvacueeHealth())
                .setBaseSpeed(SimSingletonConfig.getInstance().getMeanBaseSpeed())
                .setReactionTime(SimSingletonConfig.getInstance().getEvacueeReactionTime())
                // TODO: otrzymuje średni: MeanPanicThreshold - potencjalnie poprawić
                .setPanicThreshold(SimSingletonConfig.getInstance().getMeanPanicThreshold())
                .setPathfinder(pathfinder)
                .build();

    }
}
