package evacuation.sim.agent;

import evacuation.sim.model.Board;
import evacuation.sim.model.Cell;
import evacuation.sim.model.DynamicState;
import evacuation.sim.event.SimEvent;
import evacuation.sim.model.BaseType;
import java.util.List;

public class Desk extends Agent implements Damageable {
    private float health;
    private float fireSpreadTimer = 0.0f;

    public Desk(int id, int logicalX, int logicalY, float health) {

        super(id, logicalX, logicalY);
        this.health = health;
    }

    @Override
    public void takeDamage(float amount){ this.health -= amount; } // logic of taking damage by the desk from fire only

    @Override
    public void update(Board board, float dt){
         Cell currentCell = board.getCell(this.getLogicalX(), this.getLogicalY());
        if (currentCell == null) {return;}

        if (this.health <= 0) {
            currentCell.setBaseType(BaseType.FLOOR);
            currentCell.setDynamicState(DynamicState.FIRE);
            return;
        }

        if (currentCell.getDynamicState() == DynamicState.FIRE) {
            this.takeDamage(15.0f * dt);

            fireSpreadTimer += dt;

            if (fireSpreadTimer >= 1.5f) {
                fireSpreadTimer = 0.0f;

                List<Cell> neighbors = board.getNeighbors(this.getLogicalX(), this.getLogicalY());

                for (Cell neighbor : neighbors) {
                    if (neighbor.getBaseType() == BaseType.FLOOR && neighbor.getDynamicState() != DynamicState.FIRE) {
                        if (Math.random() < 0.4) { 
                            notifyObservers(new SimEvent(
                                SimEvent.EventType.SPAWN_FIRE,
                                neighbor.getLogicalX(),
                                neighbor.getLogicalY(),
                                0.0f
                            ));
                        }
                    }
                }
            }
        } else {
            fireSpreadTimer = 0.0f;
        }
    }
}
