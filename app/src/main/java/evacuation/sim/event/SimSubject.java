package evacuation.sim.event;

/**
 * The interface implements the observer design pattern and is responsible for subjects/transmitters.
 * @author Heorhii Yartsev (293562)
 */
public interface SimSubject {
    /**
     * Adds new listeners to the list.
     * @param observer object of {@link SimObserver} class, listener which needs to be added.
     */
    void addObserver(SimObserver observer);

    /**
     * Removes listener from the list of listeners.
     * @param observer object of {@link SimObserver} class, listener which needs to removed.
     */
    void removeObserver(SimObserver observer);

    /**
     * Informs all agents on the list about the new global event.
     * @param event object of {@link SimEvent} class representing a new event that has happened in the simulation
     *             and that needs to be communicated to the audience
     */
    void notifyObservers(SimEvent event);
}
