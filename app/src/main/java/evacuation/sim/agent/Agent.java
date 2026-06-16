package evacuation.sim.agent;

import evacuation.sim.event.SimEvent;
import evacuation.sim.event.SimObserver;
import evacuation.sim.event.SimSubject;
import evacuation.sim.model.Board;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the abstract class responsible for all agents in the simulation.
 * All secondary classes responsible for different types of agents inherit from this class.
 * They communicate with the environment via the Simulation class,
 * from which they receive information about the board ({@link Board} class) and
 * dt - delta time (min time change between simulation ticks).
 * It implements the SimSubject interface - agents are transmitters for the Simulation.
 * @author Heorhii Yartsev (293562)
 */
public abstract class Agent implements SimSubject {
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

    // implementation of methods from interface SimSubject

    /**
     * {@inheritDoc}
     * <p>
     * Adds a new observer to the list if it hasn't been added before.
     */
    @Override
    public void addObserver(SimObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeObserver(SimObserver observer) {
        observers.remove(observer);
    }

    /**
     * {@inheritDoc}
     * <p>
     * For example, if we want the simulation to create a new agent.
     */
    @Override
    public void notifyObservers(SimEvent event) {
        for (SimObserver observer : observers) {
            observer.onNotify(event); // pushing the package out to the listener
        }
    }

    /**
     * This is the agent's main lifecycle method, invoked periodically in each simulation frame.
     * Updates the agent's logical and physical state by a specified time step.
     * It is responsible for implementing the agent logic at every tick of the simulation.
     * @param board Current state of the board layout (object of class {@link Board} from the Simulation class),
     *             allowing for checking the environment and collisions.
     * @param dt Delta time (in seconds) gone since the last simulation frame.
     *           Used for agents logic calculations.
     */
    public abstract void update(Board board, float dt);

    /**
     * Deactivates agent (in purpose to delete).
     */
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
}
