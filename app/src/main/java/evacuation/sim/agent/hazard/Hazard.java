package evacuation.sim.agent.hazard;

import evacuation.sim.agent.Agent;
import evacuation.sim.agent.Damageable;
import evacuation.sim.model.Board;

import java.util.List;

/**
 * Abstract base class for dynamic environmental hazards (fire, smoke).
 * <p>
 * Inherits from the {@link Agent} class.
 * This allows them to have their own lifecycle (update method) and
 * can be managed by the main loop of the simulation engine, just like evacuees.
 * This class is responsible for measuring the time until the element spreads and inflicting
 * damage to objects implementing the {@link Damageable} interface that are on the same tile.
 * @author Heorhii Yartsev (293562)
 */
public abstract class Hazard extends Agent {
    private float damagePerSecond;
    private float spreadInterval;
    private float internalTimer;

    public Hazard(int id, int logicalX, int logicalY, float damagePerSecond, float spreadInterval) {
        super(id, logicalX, logicalY);
        this.damagePerSecond = damagePerSecond;
        this.internalTimer = 0.0f;
        this.spreadInterval = spreadInterval;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract void update(Board board, float dt);

    /**
     * The logical method that using agent's internal timer to check if hazard agent is ready to spread.
     * @param dt delta time - minimal time change between simulation ticks.
     * @return true if agent is ready to spread and false otherwise.
     */
    protected boolean isReadyToSpread(float dt) {
        this.internalTimer += dt;
        if (this.internalTimer >= this.spreadInterval) {
            this.internalTimer = 0.0f; // resets stoper
            return true; // gives signal that it is time to spread
        }
        return false; // it is not ready to spread
    }

    /**
     * Method scans all agents on the cell with this {@link Hazard} object and
     * deals damage to {@link Damageable} objects.
     * @param board object of class {@link Board} on which the simulation takes place.
     * @param dt delta time - minimal time change between simulation ticks.
     */
    protected void hit(Board board, float dt){
        // downloading the list of agents
        List<Agent> agentsAtCell = board.getAgentsAt(board.getCell(this.getLogicalX(), this.getLogicalY()));
        // review all agents in the field
        for (Agent agent : agentsAtCell) {
            if (agent == this) {
                continue;
            }
            
            // check if agents implements Damageable interface
            if (agent instanceof Damageable) {
                // calculate damage for this frame
                float damageForThisTick = this.getDamagePerSecond() * dt;
                // agent type casting to Damageable interface and cause damage
                ((Damageable) agent).takeDamage(damageForThisTick);
            }
        }
    }

    // getters/setters

    public float getDamagePerSecond() {
        return damagePerSecond;
    }

    protected float getInternalTimer() {
        return internalTimer;
    }

    protected float getSpreadInterval() {
        return spreadInterval;
    }

    protected void setInternalTimer(float internalTimer) {
        this.internalTimer = internalTimer;
    }

}
