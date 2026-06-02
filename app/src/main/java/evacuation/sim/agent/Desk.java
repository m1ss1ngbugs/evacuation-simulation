package evacuation.sim.agent;

import evacuation.sim.model.Board;
import evacuation.sim.model.Cell;
import evacuation.sim.model.DynamicState;
import evacuation.sim.model.BaseType;
import java.util.List;

public class Desk extends Agent implements Damageable {
    private float health;

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
            
            List<Cell> neighbors = board.getNeighbors(this.getLogicalX(), this.getLogicalY());

            for (Cell neighbor : neighbors) {
                if (neighbor.getBaseType() == BaseType.FLOOR && neighbor.getDynamicState() != DynamicState.FIRE) {
                    if (Math.random() < 0.5 * dt) { 
                        neighbor.setDynamicState(DynamicState.FIRE); // example condition for desk interaction
                        // logic for how the desk can interact with neighboring cells, e.g., it can provide cover for agents or it can be a point of interest for agents to move towards
                    }
                }
            }
            // logic for when the desk is still intact, e.g., it can provide some cover for agents or it can be a point of interest for agents to move towards
        }
        // desk operation logic, e.g., if the desk is on fire, it can spread fire to adjacent cells, or if it's damaged, it can affect the movement of agents around it.
        // logika działania biurka w kroku symulacji dt
    }
}
