package evacuation.sim.agent.hazard;

import evacuation.sim.agent.Agent;
import evacuation.sim.agent.Damageable;
import evacuation.sim.model.Board;

import java.util.List;

public class Fire extends Hazard{
    private float spreadInterval;
    private float incubationDelay;
    private float internalTimer;
    private boolean isIncubation;

    public Fire(int id, int logicalX, int logicalY, float damagePerSecond,
                float spreadInterval, float incubationDelay) {
        super(id, logicalX, logicalY, damagePerSecond);
        this.spreadInterval = spreadInterval;
        this.incubationDelay = incubationDelay;
        this.internalTimer = 0.0f;
        this.isIncubation = true;
    }

    @Override
    public void update(Board board, float dt) {

        // ta metoda posiada jeszcze niepełną logikę, potrzebuje dopisania


        // damage causing
        // downloading the list of agents
        List<Agent> agentsAtCell = board.getAgentsAt(this.getLogicalX(), this.getLogicalY());
        // review all agents in the field
        for (Agent agent : agentsAtCell) {
            // check if agents implements Damageable interface
            if (agent instanceof Damageable) {
                // calculate damage for this frame
                float damageForThisTick = this.getDamagePerSecond() * dt;
                // agent type casting to Damageable interface and cause damage
                ((Damageable) agent).takeDamage(damageForThisTick);
            }
        }
    }

    private void emitSmoke(Board board){
        // potrzeba napisania logiki emitowania dymu
    }

    private void propagate(Board board){
        // potrzeba napisania logiki propagowania ognia
    }
}
