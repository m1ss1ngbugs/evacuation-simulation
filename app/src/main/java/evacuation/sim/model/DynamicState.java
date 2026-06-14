package evacuation.sim.model;

/**
 * Enum representing dynamic environmental hazards
 * that may be on the Cell during a fire.
 * @author Heorhii Yartsev (293562)
 */
public enum DynamicState {
    /** No threat – a clean and safe cell. */
    NONE,
    /** Smoke – reduces the agent's speed and deals minor health damage. */
    SMOKE,
    /** Fire – drastically increases the cost of the A* path and deals big damage. */
    FIRE;
}
