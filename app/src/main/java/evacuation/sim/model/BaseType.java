package evacuation.sim.model;

/**
 * Enum representing the fixed, base type of cell (the object of class {@link Cell}).
 * @author Heorhii Yartsev (293562)
 */
public enum BaseType {
    /** Wall – an impassable blockade, completely cutting off line of sight and movement. */
    WALL,
    /** Floor – a clear space where agents can move freely. */
    FLOOR,
    /** Emergency exit – a destination point, reaching which means saving an agent. */
    EXIT,
    /** Obstacle (e.g., furniture) – blocks agent movement but does not cut off line of sight. */
    OBSTACLE;
}
