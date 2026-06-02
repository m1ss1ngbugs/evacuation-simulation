package evacuation.sim.agent.hazard;

import evacuation.sim.agent.Agent;
import evacuation.sim.model.Board;

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

    @Override
    public abstract void update(Board board, float dt);

    // checks if hazard agent is ready to spread
    protected boolean isReadyToSpread(float dt) {
        this.internalTimer += dt;
        if (this.internalTimer >= this.spreadInterval) {
            this.internalTimer = 0.0f; // resets stoper
            return true; // gives signal that it is time to spread
        }
        return false; // "no, it is not ready to spread !!!"
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
