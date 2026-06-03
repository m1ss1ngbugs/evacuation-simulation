package evacuation.sim.event;

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
