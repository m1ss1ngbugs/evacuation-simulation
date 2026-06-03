package evacuation.sim.event;

public interface SimSubject {
    void addObserver(SimObserver observer);
    void removeObserver(SimObserver observer);
    void notifyObservers(SimEvent event);
}
