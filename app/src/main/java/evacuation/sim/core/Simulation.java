package evacuation.sim.core;

import evacuation.sim.SimSingletonConfig;
import evacuation.sim.agent.Agent;
import evacuation.sim.factory.AgentFactory;
import evacuation.sim.model.Board;
import evacuation.sim.model.Cell;
import evacuation.sim.model.DynamicState;
import evacuation.sim.routing.AStarPathfinder;
import evacuation.sim.routing.PathfindingStrategy;

import java.util.ArrayList;
import java.util.List;

import static evacuation.sim.factory.AgentFactory.createRandomEvacuee;

public class Simulation {
    private Board board;
    private Statistics stats;
    private SimSingletonConfig config;

    private List<Agent> agents;
    private List<Agent> agentsToAdd;
    private List<Agent> agentsToRemove;
    PathfindingStrategy defaultPathfinder;

    private boolean isRunning;
    private float currentTime;

    public Simulation() {
        // gets the configuration using Singleton
        this.config = SimSingletonConfig.getInstance();

        // gets the path to the map from the configuration
        String mapPath = this.config.getMapFilePath();

        // creates a board that will automatically adjust to this file
        this.board = new Board(mapPath);

        // initializing the rest of the structures:

        this.agents = new ArrayList<>();
        this.agentsToAdd = new ArrayList<>();
        this.agentsToRemove = new ArrayList<>();
        this.defaultPathfinder = new AStarPathfinder();

        // TODO: później trzeba też dopisać tę metodę, która stworzy ludzi i ogień na starcie symulacji
        initialize();

        // initialize the statistics class, passing it the starting data
        int totalPeople = this.config.getInitialEvacueesCount();
        this.stats = new Statistics(totalPeople);
    }

    public void initialize(){
        // TODO: zadania inicjalizacyjne
        // Przygotowuje środowisko – ładuje planszę, tworzy agentów i ustawia
        // parametry początkowe
        spawnInitialAgents();
    }

    public void spawnInitialAgents() {
        int peopleToSpawn = config.getInitialEvacueesCount();
        int firesToSpawn = config.getInitialFireHazardsCount();

        // adding all new evacuees
        for (int i = 0; i < peopleToSpawn; i++) {
            // finding free space
            Cell spawnPoint = board.getRandomEmptyFloor();
            if (spawnPoint == null) {
                System.err.println("Ostrzeżenie: Brak wolnego miejsca na mapie dla ewakuantów!");
                break; // board is full, stop
            }
            // generate random evacuee
            Agent newEvacuee = createRandomEvacuee(spawnPoint.getLogicalX(),
                    spawnPoint.getLogicalY(), defaultPathfinder);
            // add agent to the buffer
            addAgent(newEvacuee);
        }

        // fire spacing
        for (int i = 0; i < firesToSpawn; i++) {
            // looking for a new free space
            Cell spawnPoint = board.getRandomEmptyFloor();
            if (spawnPoint == null) {
                System.err.println("Ostrzeżenie: Brak wolnego miejsca na mapie dla ognia!");
                break;
            }
            spawnFireAt(spawnPoint);
        }
        // first simulation tick
        updateTick(0.0f);
    }

    public void run(){
        // TODO: główna pętla symulacji (też potrzebuje napisania ;) )
    }

    public void updateTick(float dt) {
        // active agents make their moves
        for (Agent agent : agents) {
            agent.update(board, dt);
        }

        // introduce agents from the waiting room to the main list of agents
        if (!agentsToAdd.isEmpty()) {
            agents.addAll(agentsToAdd);
            agentsToAdd.clear();        // make waiting room empty
        }

        // remove agents in the deletion buffer from the main list of agents
        if (!agentsToRemove.isEmpty()) {
            agents.removeAll(agentsToRemove);
            agentsToRemove.clear();           // empty trash list
        }
    }

    public void addAgent(Agent a) {
        // put new agent to add to the add buffer
        if (a != null) {
            agentsToAdd.add(a);
        }
    }

    public void removeAgent(Agent a) {
        // put agent to remove to the remove buffer
        if (a != null) {
            agentsToRemove.add(a);
        }
    }

    public void spawnFireAt(Cell cell) {
        if (cell == null || cell.getDynamicState() == DynamicState.FIRE) {
            return; // if it's already a fire - do nothing
        }
        // change the state of the board to FIRE, so that no one else can spawn on this Cell
        cell.setDynamicState(DynamicState.FIRE);
        // use AgentFabric to create new instance of Fire
        Agent newFire = AgentFactory.createFire(cell.getLogicalX(), cell.getLogicalY());
        // add fire agent to the buffer
        addAgent(newFire);
    }

    public void spawnSmokeAt(Cell cell, float density) {
        if (cell == null || cell.getDynamicState() == DynamicState.SMOKE) {
            return; // if it's already a smoke - do nothing
        }
        // change the state of the board to SMOKE, so that no one else can spawn on this Cell
        cell.setDynamicState(DynamicState.SMOKE);
        // use AgentFabric to create new instance of Smoke
        Agent newSmoke = AgentFactory.createSmoke(cell.getLogicalX(), cell.getLogicalY(), density);
        // add smoke agent to the buffer
        addAgent(newSmoke);
    }
}
