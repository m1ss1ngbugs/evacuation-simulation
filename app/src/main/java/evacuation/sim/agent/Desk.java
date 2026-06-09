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
        
        if (this.health <= 0) {
           
           Cell currentCell = board.getCell(this.getLogicalX(), this.getLogicalY());
           
           if (currentCell.getBaseType() == BaseType.FLOOR) {

              currentCell.setBaseType(BaseType.OBSTACLE);
              currentCell.setDynamicState(DynamicState.FIRE); // logic for when the desk is destroyed, e.g., it can become an obstacle for agents or it can be removed from the board
           }
        } else {
            fireSpreadTimer += dt;

            if(fireSpreadTimer >= 1.5f) {

                fireSpreadTimer = 0.0f;
            
                List<Cell> neighbors = board.getNeighbors(this.getLogicalX(), this.getLogicalY());

                for (Cell neighbor : neighbors) {
                    if (neighbor.getBaseType() == BaseType.FLOOR && neighbor.getDynamicState() != DynamicState.FIRE) {
                        if (Math.random() < 0.3) { 
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
        }
    }
}
