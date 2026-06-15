package evacuation.sim.event;

/**
 * The interface implements the observer design pattern and is responsible for listeners.
 * @author Heorhii Yartsev (293562)
 */
public interface SimObserver {
    /**
     * Is responsible for pushing info about the global event (object of {@link SimEvent} class)
     * from transmitters to listeners.
     * @param event the object of {@link SimEvent} class, that represents an event
     *             that happened in the simulation and that needs to be communicated to the audience.
     */
    void onNotify(SimEvent event);
}
