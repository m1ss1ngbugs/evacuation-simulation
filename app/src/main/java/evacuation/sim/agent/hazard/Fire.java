package evacuation.sim.agent.hazard;

import evacuation.sim.model.Board;

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
    public void update(Board board, float ft){
        // potrzeba napisania logiki updatu w kroku dt
    }

    private void emitSmoke(Board board){
        // potrzeba napisania logiki emitowania dymu
    }

    private void propagate(Board board){
        // potrzeba napisania logiki propagowania ognia
    }
}
