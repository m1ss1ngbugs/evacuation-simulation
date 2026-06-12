package evacuation.sim.agent;

import evacuation.sim.event.SimEvent;
import evacuation.sim.event.SimObserver;
import evacuation.sim.event.SimSubject;
import evacuation.sim.model.Board;

import java.util.ArrayList;
import java.util.List;

public abstract class  Agent implements SimSubject {
    private final int id;
    private int logicalX;
    private int logicalY;
    private float renderX;
    private float renderY;
    private boolean isActive;

    // list of subscribers in case if we decide to add more than one (Simulation) observer later
    private List<SimObserver> observers;

    public Agent(int id, int logicalX, int logicalY) {
        this.id = id;
        this.logicalX = logicalX;
        this.logicalY = logicalY;
        this.renderX = logicalX;
        this.renderY = logicalY;
        this.isActive = true;

        this.observers = new ArrayList<>();
    }

    // adding methods from interface SimSubject

    @Override
    public void addObserver(SimObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(SimObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(SimEvent event) {
        for (SimObserver observer : observers) {
            observer.onNotify(event); // Wypychamy paczkę do słuchacza!
        }
    }

    public abstract void update(Board board, float dt);

    // deactivates (in purpose to delete) agent
    public void deactivate() {
        this.isActive = false;
    }

    // standard getters and setters

    public int getId() {
        return id;
    }

    public int getLogicalX() {
        return logicalX;
    }

    public int getLogicalY() {
        return logicalY;
    }

    public float getRenderX() {
        return renderX;
    }

    public float getRenderY() {
        return renderY;
    }

    public boolean isActive() {
        return isActive;
    }

    protected void setLogicalX(int logicalX) {
        this.logicalX = logicalX;
    }

    protected void setLogicalY(int logicalY) {
        this.logicalY = logicalY;
    }

    protected void setRenderX(float renderX) {
        this.renderX = renderX;
    }

    protected void setRenderY(float renderY) {
        this.renderY = renderY;
    }

    protected void setActive(boolean active) {
        isActive = active;
    }
}
