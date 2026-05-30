package evacuation.sim.agent.hazard;

import evacuation.sim.agent.Agent;
import evacuation.sim.model.Board;

public abstract class Hazard extends Agent {
    private float damagePerSecond;

    public Hazard(int id, int logicalX, int logicalY, float damagePerSecond) {
        super(id, logicalX, logicalY);
        this.damagePerSecond = damagePerSecond;
    }

    @Override
    public abstract void update(Board board, float dt);

    public float getDamagePerSecond() {
        return damagePerSecond;
    }

    protected void setDamagePerSecond(float damagePerSecond) {
        this.damagePerSecond = damagePerSecond;
    }
}
