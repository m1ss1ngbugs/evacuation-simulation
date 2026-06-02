package evacuation.sim.core;

import evacuation.sim.SimSingletonConfig;
import evacuation.sim.agent.Agent;
import evacuation.sim.agent.human.Evacuee;
import evacuation.sim.factory.AgentFactory;
import evacuation.sim.model.BaseType;
import evacuation.sim.model.Board;
import evacuation.sim.model.Cell;
import evacuation.sim.model.DynamicState;
import evacuation.sim.routing.AStarPathfinder;
import evacuation.sim.routing.PathfindingStrategy;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private Board board;
    private Statistics stats;
    private SimSingletonConfig config;

    private List<Agent> agents;
    private List<Agent> agentsToAdd;
    private List<Agent> agentsToRemove;

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
            Cell spawnPoint = findRandomEmptyFloor();
            if (spawnPoint == null) {
                System.out.println("Ostrzeżenie: Brak wolnego miejsca na mapie dla ewakuantów!");
                break; // board is full, stop
            }

            // generate random evacuee
            Agent newEvacuee = generateRandomEvacuee(spawnPoint.getLogicalX(), spawnPoint.getLogicalY());

            // add agent to the buffer
            addAgent(newEvacuee);
        }

        // fire spacing
        for (int i = 0; i < firesToSpawn; i++) {
            // looking for a new free space
            Cell spawnPoint = findRandomEmptyFloor();
            if (spawnPoint == null) {
                System.out.println("Ostrzeżenie: Brak wolnego miejsca na mapie dla ognia!");
                break;
            }

            // use AgentFabric to create new instance of fire
            Agent newFire = AgentFactory.createFire(spawnPoint.getLogicalX(), spawnPoint.getLogicalY());

            // change the state of the board to FIRE, so that no one else can spawn on this Cell
            spawnPoint.setDynamicState(DynamicState.FIRE);

            // add fire agent to the buffer
            addAgent(newFire);
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

    // Auxiliary method for finding a safe starting point
    private Cell findRandomEmptyFloor() {
        int maxAttempts = 1000;
        int width = board.getWidth();
        int height = board.getHeight();

        for (int i = 0; i < maxAttempts; i++) {
            // draw the coordinates
            int random_x = (int) (Math.random() * width);
            int random_y = (int) (Math.random() * height);

            Cell cell = board.getCell(random_x, random_y);

            // cheks if agent can be placed here
            if (cell != null && cell.getBaseType() == BaseType.FLOOR
                    && cell.getDynamicState() == DynamicState.NONE) {

                // TODO: Opcjonalnie można tu jeszcze sprawdzić,
                // czy na tym polu nie stoi już inny agent,
                // jeśli nie chcemy, by na starcie ludzie stali "jeden na drugim".

                return cell; // place to spawn was found
            }
        }
        return null; // place can't be found after 1000 attempts
    }

    // Auxiliary method for randomizing agent type
    public Agent generateRandomEvacuee(int LogicalX, int LogicalY){
        // randomly select the agent type (from 0.0 to 1.0)
        double randomType = Math.random();
        Agent newEvacuee;

        PathfindingStrategy defaultPathfinder = new AStarPathfinder();

        // decide who the agent will be, based on configuration
        if (randomType < config.getLeaderRatio()) {
            newEvacuee = AgentFactory.createLeader(LogicalX, LogicalY, defaultPathfinder);
        } else if (randomType < config.getLeaderRatio() + config.getPanickedRatio()) {
            newEvacuee = AgentFactory.createPanicked(LogicalX, LogicalY, defaultPathfinder);
        } else {
            newEvacuee = AgentFactory.createFollower(LogicalX, LogicalY, defaultPathfinder);
        }
        return newEvacuee;
    }
}
