package evacuation.sim.factory;
import evacuation.sim.SimSingletonConfig;
import evacuation.sim.agent.Agent;
import evacuation.sim.agent.hazard.Fire;
import evacuation.sim.agent.hazard.Smoke;
import evacuation.sim.agent.human.Follower;
import evacuation.sim.agent.human.Leader;
import evacuation.sim.agent.human.Panicked;
import evacuation.sim.routing.PathfindingStrategy;

/**
 * This class is responsible for the agent creation. Use agents builder for these purposes.
 * Realizes AgentFactory pattern. Gets info for agent creation from the config and as input data.
 * Every created with AgentFactory agent gets his personal id.
 * @author Heorhii Yartsev (293562)
 */
public class AgentFactory {
    private static int nextId = 0;
    static SimSingletonConfig config = SimSingletonConfig.getInstance();

    private AgentFactory() {}

    /**
     * Counts and gives personal id for the agent.
     * @return id of the new agent.
     */
    private static int getNextId() {
        return nextId++;
    }

    // Hazard section

    /**
     * {@link Fire} instance creator.
     * @param logicalX Integer logical x coordinate of agent, that needs to be created.
     * @param logicalY Integer logical y coordinate of agent, that needs to be created.
     * @return new {@link Fire} agent.
     */
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

    /**
     * {@link Smoke} instance creator.
     * @param logicalX Integer logical x coordinate of agent, that needs to be created.
     * @param logicalY Integer logical y coordinate of agent, that needs to be created.
     * @param density Density of a new {@link Smoke} object.
     * @return new {@link Smoke} agent.
     */
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

    /**
     * {@link Leader} instance creator.
     * @param logicalX Integer logical x coordinate of agent, that needs to be created.
     * @param logicalY Integer logical y coordinate of agent, that needs to be created.
     * @param pathfinder The path finding strategy agent must use during his evacuation.
     * @return new {@link Leader} agent.
     */
    public static Agent createLeader(int logicalX, int logicalY, PathfindingStrategy pathfinder) {
        int id = getNextId();

        float finalSpeed = AgentRandomizer.calculateValue(config.getMeanBaseSpeed(), config.getSpeedVariance());

        return new Leader.Builder()
                .setId(id)
                .setPosition(logicalX, logicalY)
                .setHealth(config.getEvacueeHealth())
                .setBaseSpeed(finalSpeed)
                .setReactionTime(config.getEvacueeReactionTime())
                .setPathfinder(pathfinder)
                .setVisionRadius(config.getEvacueeVisionRadius())
                .build();
    }

    /**
     * {@link Follower} instance creator.
     * @param logicalX Integer logical x coordinate of agent, that needs to be created.
     * @param logicalY Integer logical y coordinate of agent, that needs to be created.
     * @param pathfinder The path finding strategy agent must use during his evacuation.
     * @return new {@link Follower} agent.
     */
    public static Agent createFollower(int logicalX, int logicalY, PathfindingStrategy pathfinder) {
        int id = getNextId();

        float finalSpeed = AgentRandomizer.calculateValue(config.getMeanBaseSpeed(), config.getSpeedVariance());
        float finalPanic = AgentRandomizer.calculateValue(config.getMeanPanicThreshold(), config.getPanicVariance());
        float finalSocial = AgentRandomizer.calculateValue(config.getMeanSocialFactor(), config.getSocialVariance());

        return new Follower.Builder()
                .setId(id)
                .setPosition(logicalX, logicalY)
                .setHealth(config.getEvacueeHealth())
                .setBaseSpeed(finalSpeed)
                .setReactionTime(config.getEvacueeReactionTime())
                .setPanicThreshold(finalPanic)
                .setPathfinder(pathfinder)
                .setSocialFactor(finalSocial)
                .setVisionRadius(config.getEvacueeVisionRadius())
                .build();
    }

    /**
     * {@link Panicked} instance creator.
     * @param logicalX Integer logical x coordinate of agent, that needs to be created.
     * @param logicalY Integer logical y coordinate of agent, that needs to be created.
     * @param pathfinder The path finding strategy agent must use during his evacuation.
     * @return new {@link Panicked} agent.
     */
    public static Agent createPanicked(int logicalX, int logicalY, PathfindingStrategy pathfinder) {
        int id = getNextId();

        float finalSpeed = AgentRandomizer.calculateValue(config.getMeanBaseSpeed(), config.getSpeedVariance());
        float finalPanic = AgentRandomizer.calculateValue(config.getMeanPanicThreshold(), config.getPanicVariance());

        return new Panicked.Builder()
                .setId(id)
                .setPosition(logicalX, logicalY)
                .setHealth(config.getEvacueeHealth())
                .setBaseSpeed(finalSpeed)
                .setReactionTime(config.getEvacueeReactionTime())
                .setPanicThreshold(finalPanic)
                .setPathfinder(pathfinder)
                .setVisionRadius(config.getEvacueeVisionRadius())
                .build();

    }

    /**
     * Auxiliary method for randomizing agent's type.
     * @param logicalX Integer logical x coordinate of agent, that needs to be created.
     * @param logicalY Integer logical y coordinate of agent, that needs to be created.
     * @param pathfinder The path finding strategy agent must use during his evacuation.
     * @return Created {@link Agent} of randomly chosen type.
     */
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
