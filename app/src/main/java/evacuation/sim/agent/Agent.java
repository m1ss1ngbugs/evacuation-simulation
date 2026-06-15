package evacuation.sim.agent;

import evacuation.sim.event.SimEvent;
import evacuation.sim.event.SimObserver;
import evacuation.sim.event.SimSubject;
import evacuation.sim.model.Board;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the class responsible for all agents in the simulation.
 * All secondary classes responsible for different types of agents inherit from this class.
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
     * Adds a new observer to the list if it hasn't been added before.
     * @param observer An object of class {@link SimObserver} that must listen for messages from the transmitter.
     */
    @Override
    public void addObserver(SimObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    /**
     * Removes an agent from the list of observers.
     * @param observer Object of class {@link SimObserver} that needs to be removed from the list.
     */
    @Override
    public void removeObserver(SimObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all the observers about the event.
     * For example, if we want the simulation to create a new agent.
     * @param event An event (object of class {@link SimEvent}) that needs to be reported.
     */
    @Override
    public void notifyObservers(SimEvent event) {
        for (SimObserver observer : observers) {
            observer.onNotify(event); // Wypychamy paczkę do słuchacza!
        }
    }

    /**
     * This is the agent's main lifecycle method, invoked periodically in each simulation frame.
     * Updates the agent's logical and physical state by a specified time step.
     * It is responsible for triggering environmental perception processes,
     * deciding on the direction of escape, and physically moving objects on the board.
     * @param board Current state of the board layout (object of class {@link Board} from the Simulation class),
     *             allowing for checking the environment and collisions.
     * @param dt Delta time (in seconds) gone since the last simulation frame.
     *           Used for smooth speed and movement calculations.
     */
    public abstract void update(Board board, float dt);

    /**
     * Deactivates (in purpose to delete) agent.
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
