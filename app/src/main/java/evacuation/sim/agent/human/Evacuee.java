package evacuation.sim.agent.human;

import evacuation.sim.agent.Agent;
import evacuation.sim.agent.Damageable;
import evacuation.sim.model.Board;
import evacuation.sim.model.Cell;
import evacuation.sim.routing.PathfindingStrategy;

import java.util.List;

public abstract class Evacuee extends Agent implements Damageable, PathfindingStrategy {
    private float health;
    private float baseSpeed;
    private float currentSpeed;
    private float reactionTime;
    private float panicLevel;
    private float internalTimer;
    private Cell[][] mentalMap;
    private List<Cell> plannedPath;
    private PathfindingStrategy pathfinder;

    public Evacuee(int id, int logicalX, int logicalY, float health, float baseSpeed,
                   PathfindingStrategy pathfinder, float panicLevel, float reactionTime) {
        super(id, logicalX, logicalY);
        this.health = health;
        this.baseSpeed = baseSpeed;
        this.pathfinder = pathfinder;
        this.panicLevel = panicLevel;
        this.reactionTime = reactionTime;
    }

    @Override
    public void update(Board board, float dt){

    }

    protected void move(float dt){
        // napisać logikę przemieszczania się ewakuanta w czasie
    }

    protected void perceive(Board board){
        // napisać mechanikę postrzegania świata przez agenta
    }

    protected void calculatePath(){
        // napisać metodę wyliczającą drogę dla agenta, która korzysta ze strategii poszukiwania drogi
    }

    @Override
    public void takeDamage(float amount) {
        // health reduce by a certain amount
        float newHealth = this.getHealth() - amount;
        this.setHealth(newHealth);

        // logic checking agent death
        if (this.getHealth() <= 0) {
            this.deactivate();
        }
    }

    protected abstract void handlePanic(float dt);
    protected abstract boolean shouldPanic();

    // getters and setters below

    public float getHealth() {
        return health;
    }

    protected void setHealth(float health) {
        this.health = health;
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    protected void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public float getInternalTimer() {
        return internalTimer;
    }

    protected void setInternalTimer(float internalTimer) {
        this.internalTimer = internalTimer;
    }

    public float getPanicLevel() {
        return panicLevel;
    }

    protected void setPanicLevel(float panicLevel) {
        this.panicLevel = panicLevel;
    }

    public float getBaseSpeed() {
        return baseSpeed;
    }

    public float getReactionTime() {
        return reactionTime;
    }

    public PathfindingStrategy getPathfinder() {
        return pathfinder;
    }

    public List<Cell> getPlannedPath() {
        return plannedPath;
    }

    public Cell[][] getMentalMap() {
        return mentalMap;
    }
}
