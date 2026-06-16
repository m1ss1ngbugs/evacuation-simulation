package evacuation.sim.agent;

/**
 * This is the functional interface intended for damageable agents.
 * It is responsible for taking damage by hitable objects.
 * @author Heorhii Yartsev (293562)
 */
@FunctionalInterface
public interface Damageable {
    /**
     * Reduces agent's health by a certain amount.
     * @param amount the variable responsible for amount of damage agent needs to take.
     */
    void takeDamage(float amount);
}
