package evacuation.sim.factory;
import evacuation.sim.agent.Agent;

public class AgentFactory {
    private static int nextId = 0;

    private AgentFactory() {}

    private static int getNextId() {
        return nextId++;
    }

    // Hazard section

    public static Agent createFire(int logicalX, int logicalY) {
        int id = getNextId();
        // TODO: W przszłości trzeba podłączyć tu klasę HazardBuilder
        // return new HazardBuilder().setId(id).setPosition(logicalX, logicalY)...buildFire();
        return null; // Zwracamy null tylko na chwilę, żeby szkielet się kompilował
    }

    public static Agent createSmoke(int logicalX, int logicalY) {
        int id = getNextId();
        // TODO: Dopisać logikę hazard builderu dla dymu
        // return new HazardBuilder().setId(id).setPosition(logicalX, logicalY)...buildFire();
        return null;
    }

    // Evacuee section

    public static Agent createLeader(int logicalX, int logicalY) {
        int id = getNextId();
        // TODO: podłączyć EvacueeBuilder tu w przyszłości
        // return new EvacueeBuilder().setId(id).setPosition(logicalX, logicalY)...buildLeader();
        return null;
    }

    public static Agent createFollower(int logicalX, int logicalY) {
        int id = getNextId();
        // TODO: podłączyć EvacueeBuilder tu w przyszłości
        // return new EvacueeBuilder().setId(id).setPosition(logicalX, logicalY)...buildLeader();
        return null;
    }

    public static Agent createPanicked(int logicalX, int logicalY) {
        int id = getNextId();
        // TODO: podłączyć EvacueeBuilder tu w przyszłości
        // return new EvacueeBuilder().setId(id).setPosition(logicalX, logicalY)...buildLeader();
        return null;
    }
}
