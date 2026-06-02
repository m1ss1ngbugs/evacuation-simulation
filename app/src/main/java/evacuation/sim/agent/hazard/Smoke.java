package evacuation.sim.agent.hazard;

import evacuation.sim.model.Board;

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

    @Override
    public void update(Board board, float dt){
        // TODO: potrzeba napisania logiki updatu w kroku dt
    }

    private void duplicate(Board board){
        // TODO: potrzeba napisania logiki duplikacji
    }

    private void fade(float dt) {
        // TODO: potrzeba napisania logiki zanikania dymu
    }

    public float getDensity(){
        return density;
    }

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
