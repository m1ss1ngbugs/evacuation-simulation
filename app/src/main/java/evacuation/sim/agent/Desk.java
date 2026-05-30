package evacuation.sim.agent;

import evacuation.sim.model.Board;

public class Desk extends Agent implements Damageable {
    private float health;

    public Desk(int id, int logicalX, int logicalY, float health) {
        super(id, logicalX, logicalY);
        this.health = health;
    }

    @Override
    public void takeDamage(float amount){
        // logika otrzymania obrażeń przez biurko
    }

    @Override
    public void update(Board board, float dt){
        // logika działania biurka w kroku symulacji dt
    }
}
