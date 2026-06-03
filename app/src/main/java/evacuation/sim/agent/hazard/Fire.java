package evacuation.sim.agent.hazard;


import evacuation.sim.SimSingletonConfig;
import evacuation.sim.agent.Agent;
import evacuation.sim.agent.Damageable;
import evacuation.sim.event.SimEvent;
import evacuation.sim.model.BaseType;
import evacuation.sim.model.Board;
import evacuation.sim.model.Cell;
import evacuation.sim.model.DynamicState;
import evacuation.sim.core.Simulation;


import java.util.List;

public class Fire extends Hazard{
    private float incubationDelay;
    private boolean isIncubation;

    private Fire(int id, int logicalX, int logicalY, float damagePerSecond,
                float spreadInterval, float incubationDelay) {
        super(id, logicalX, logicalY, damagePerSecond, spreadInterval);
        this.incubationDelay = incubationDelay;
        this.isIncubation = true;
    }

    @Override
    public void update(Board board, float dt) {

        if (this.isIncubation) {
            // fire incubating block
            setInternalTimer(getInternalTimer() + dt); // update internal timer for incubation logic
            if (getInternalTimer() >= this.incubationDelay) {
                this.isIncubation = false; // end incubation phase
                this.setInternalTimer(0.0f); // reset timer for spread logic
            }
            return; // during incubation, fire does not spread or cause damage
        } else {
            // fire active state block

            // fire spreading and smoke propagation

            if (isReadyToSpread(dt)) {
                this.propagate(board); // logic for spreading fire to adjacent cells
                this.emitSmoke(board); // logic for emitting smoke, which can affect agents in the vicinity
            }

            // damage causing
            hit(board, dt);
        }
    }

    private void emitSmoke(Board board) {
        // gets neighbors of this cell
        List<Cell> neighbors = board.getNeighbors(this.getLogicalX(), this.getLogicalY());

        for (Cell cell : neighbors) {
            if (cell.getBaseType() == BaseType.FLOOR && cell.getDynamicState() == DynamicState.NONE) {
                // makes and send the package with request about new instance of Smoke spawning
                notifyObservers(new SimEvent(SimEvent.EventType.SPAWN_SMOKE, cell.getLogicalX(),
                        cell.getLogicalY(), SimSingletonConfig.getInstance().getSmokeInitialDensity()));
            }
        }
    }

    private void propagate(Board board){
        List<Cell> neighbors = board.getNeighbors(this.getLogicalX(), this.getLogicalY());

        for (Cell neighbor : neighbors) {
            if (neighbor.getBaseType() == BaseType.FLOOR && neighbor.getDynamicState() != DynamicState.FIRE) {
                if (Math.random() < 0.3) { // example condition for fire spread
                    // makes and send the package with request about new instance of Fire spawning
                    notifyObservers(new SimEvent(SimEvent.EventType.SPAWN_FIRE, neighbor.getLogicalX(),
                            neighbor.getLogicalY(), 0.0f));
                }
            }
        }
    }

    public static class Builder {
        private int id;
        private int logicalX;
        private int logicalY;
        private float spreadInterval;
        private float damagePerSecond;
        private float incubationDelay;

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setPosition(int logicalX, int logicalY) {
            this.logicalX = logicalX;
            this.logicalY = logicalY;
            return this;
        }

        public Builder setSpreadInterval(float spreadInterval) {
            this.spreadInterval = spreadInterval;
            return this;
        }

        public Builder setDamagePerSecond(float damagePerSecond) {
            this.damagePerSecond = damagePerSecond;
            return this;
        }

        public Builder setIncubationDelay(float incubationDelay) {
            this.incubationDelay = incubationDelay;
            return this;
        }

        public Fire build(){
            return new Fire(id, logicalX, logicalY, damagePerSecond,
                    spreadInterval, incubationDelay);
        }
    }

}
