package evacuation.sim.agent.hazard;

import evacuation.sim.event.SimEvent;
import evacuation.sim.model.Board;
import evacuation.sim.model.BaseType;
import evacuation.sim.model.DynamicState;
import evacuation.sim.model.Cell;
import evacuation.sim.agent.Agent;
import java.util.List;

/**
 * Smoke class is responsible for smoke hazard agents.
 * Manage smoke agents tick and spread logic.
 * The Smoke class represents an agent that actually exists on the board.
 * It has its own builder.
 * It can send messages to the Simulation class
 * to provide information about a new smoke agent that needs to be created.
 *  @author Heorhii Yartsev (293562)
 *  @author Bartłomiej Krajewski (293439)
 */
public class Smoke extends Hazard{
    private float density;
    private float fadeRatePerSecond;
    private float duplicationThreshold;

    private Smoke(int id, int logicalX, int logicalY, float damagePerSecond,
                 float density, float fadeRatePerSecond, float spreadInterval, float duplicationThreshold) {
        super(id, logicalX, logicalY, damagePerSecond, spreadInterval);
        this.density = density;
        this.fadeRatePerSecond = fadeRatePerSecond;
        this.duplicationThreshold = duplicationThreshold;
    }

    /**
     * {@inheritDoc}
     * The smoke fades a little each tick,
     * tries to multiply if it has enough density, and deals damage to evacuees.
     */
    @Override
    public void update(Board board, float dt){
        fade(dt);

        if (isReadyToSpread(dt) && this.density > this.duplicationThreshold) {
            duplicate(board); 
        }


        // damage causing
        hit(board, dt);
    }

    /**
     * The duplicate method is responsible for smoke propagation logic.
     * Check neighbors sells, sends request to spawn a new smoke agent if the cell is free and
     * increases neighbor smoke agent density if the neighbor cell is busy by it.
     * @param board Current state of the board layout (object of class {@link Board})
     */
    private void duplicate(Board board){
        List<Cell> neighborCells = board.getNeighbors(board.getCell(this.getLogicalX(), this.getLogicalY()));

        for (Cell cell : neighborCells) {

            if (cell.getBaseType() == BaseType.WALL) {
                continue; // Skip walls
            }

            if (cell.getDynamicState() == DynamicState.FIRE) {
                continue; // Skip cells on fire
            }

            Smoke existingSmoke = null;
            List<Agent> agentsAtCell = board.getAgentsAt(cell);

            for (Agent agent : agentsAtCell) {
                if (agent instanceof Smoke) {
                    existingSmoke = (Smoke) agent;
                    break;
                }
            }

            if (existingSmoke != null) { // on this sell is already existing Smoke agent

                float newDensity = existingSmoke.getDensity() + this.density * 0.6f;
                existingSmoke.density = Math.min(newDensity, 1.0f); // Cap density at 1.0f
            } else { // there is no Smoke on this sell - need to make a new one
                notifyObservers(new SimEvent(SimEvent.EventType.SPAWN_SMOKE, cell.getLogicalX(),
                        cell.getLogicalY(), this.density*0.6f));
            }
        }
    }

    /**
     * The fade method is responsible for smoke fade logic.
     * It reduces the density of smoke in each tick.
     * @param dt delta time - minimal time change between simulation ticks.
     */
    private void fade(float dt) {
        this.density -= this.fadeRatePerSecond * dt; // decrease density based on fade rate and time
        if (this.density < 0.0f) {
            this.density = 0.0f; // ensure density does not go below zero
        }
    }

    /**
     * Smoke density getter.
     * @return density of the smoke.
     */
    public float getDensity(){
        return density;
    }

    /**
     * An internal class responsible for creating new smoke agents via the agent factory.
     */
    public static class Builder {
        private int id;
        private int logicalX;
        private int logicalY;
        private float density;
        private float fadeRatePerSecond;
        private float spreadInterval;
        private float duplicationThreshold;
        private float damagePerSecond;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setPosition(int logicalX, int logicalY) {
            this.logicalX = logicalX;
            this.logicalY = logicalY;
            return this;
        }

        public Builder setDensity(float density) {
            this.density = density;
            return this;
        }

        public Builder setFadeRatePerSecond(float fadeRatePerSecond) {
            this.fadeRatePerSecond = fadeRatePerSecond;
            return this;
        }

        public Builder setSpreadInterval(float spreadInterval) {
            this.spreadInterval = spreadInterval;
            return this;
        }

        public Builder setDuplicationThreshold(float duplicationThreshold) {
            this.duplicationThreshold = duplicationThreshold;
            return this;
        }

        public Builder setDamagePerSecond(float damagePerSecond) {
            this.damagePerSecond = damagePerSecond;
            return this;
        }

        public Smoke build(){
            return new Smoke(id, logicalX, logicalY, damagePerSecond,
                    density, fadeRatePerSecond, spreadInterval, duplicationThreshold);
        }
    }
}
