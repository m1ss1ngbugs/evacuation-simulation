package evacuation.sim.event;

/**
 * A class that represents an event that occurred in the simulation - the appearance of a new agent.
 * It allows you to create a new object representing an event using a constructor,
 * which can then be passed from the transmitter (Agent) to the listener (Simulation) signed to it.
 * @author Heorhii Yartsev (293562)
 */
public class SimEvent {
    public enum EventType {
        SPAWN_FIRE,
        SPAWN_SMOKE
    }

    private EventType type;
    private int x;
    private int y;
    private float density; // for example density

    public SimEvent(EventType type, int x, int y, float density) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.density = density;
    }

    // getters for Simulation
    public EventType getType() { return type; }
    public int getX() { return x; }
    public int getY() { return y; }
    public float getDensity() { return density; }
}
