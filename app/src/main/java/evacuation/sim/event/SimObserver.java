package evacuation.sim.event;

public interface SimObserver {
    // every listener has to take info package
    void onNotify(SimEvent event);
}
