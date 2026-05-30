package evacuation.sim.agent.hazard;

import evacuation.sim.model.Board;

public class Smoke extends Hazard{
    private float density;
    private float fadeRatePerSecond;
    private float spreadInterval;
    private float internalTimer;
    private float threshold;

    public Smoke(int id, int logicalX, int logicalY, float damagePerSecond,
                 float density, float fadeRatePerSecond, float spreadInterval, float threshold) {
        super(id, logicalX, logicalY, damagePerSecond);
        this.density = density;
        this.fadeRatePerSecond = fadeRatePerSecond;
        this.spreadInterval = spreadInterval;
        this.threshold = threshold;
        this.internalTimer = 0.0f;
    }

    @Override
    public void update(Board board, float dt){
        // potrzeba napisania logiki updatu w kroku dt
    }

    private void duplicate(Board board){
        // potrzeba napisania logiki duplikacji
    }

    private void fade(float dt) {
        // potrzeba napisania logiki zanikania dymu
    }

    public float getDensity(){
        return density;
    }
}
