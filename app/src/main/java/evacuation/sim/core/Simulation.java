package evacuation.sim.core;

import evacuation.sim.SimSingletonConfig;
import evacuation.sim.agent.Agent;
import evacuation.sim.model.Board;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private Board board;
    private List<Agent> agents;
    private Statistics statistics;
    private boolean isRunning;
    private float currentTime;
    private SimSingletonConfig config;

    public Simulation() {
        // gets the configuration using Singleton
        this.config = SimSingletonConfig.getInstance();

        // gets the path to the map from the configuration
        String mapPath = this.config.getMapFilePath();

        // creates a board that will automatically adjust to this file
        this.board = new Board(mapPath);

        // initializing the rest of the structures:

        this.agents = new ArrayList<>();

        // później trzeba też wywołać tu metodę, która stworzy ludzi i ogień na starcie symulacji
    }

}
