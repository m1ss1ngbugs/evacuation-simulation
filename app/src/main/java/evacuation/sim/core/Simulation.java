package evacuation.sim.core;

import evacuation.sim.SimSingletonConfig;
import evacuation.sim.agent.Agent;
import evacuation.sim.agent.hazard.Smoke;
import evacuation.sim.agent.human.Evacuee;
import evacuation.sim.event.SimEvent;
import evacuation.sim.event.SimObserver;
import evacuation.sim.factory.AgentFactory;
import evacuation.sim.model.Board;
import evacuation.sim.model.Cell;
import evacuation.sim.model.DynamicState;
import evacuation.sim.model.BaseType;
import evacuation.sim.routing.AStarPathfinder;
import evacuation.sim.routing.PathfindingStrategy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static evacuation.sim.factory.AgentFactory.createRandomEvacuee;

public class Simulation implements SimObserver {
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
        // this.board = board;

        // initializing the rest of the structures:
        this.agents = new ArrayList<>();
        this.agentsToAdd = new ArrayList<>();
        this.agentsToRemove = new ArrayList<>();
        this.defaultPathfinder = new AStarPathfinder();

        // initialization activation
        initialize();
    }

    public void restartSimulation() {
        this.agents.clear();
        this.agentsToAdd.clear();
        this.agentsToRemove.clear();
        this.currentTime = 0.0f;

        String mapPath = this.config.getMapFilePath();
        this.board = new Board(mapPath);

        initialize(); 
    }

    @Override
    public void onNotify(SimEvent event) {
        Cell targetCell = board.getCell(event.getX(), event.getY());

        // simulation checks the label on the package and decides what to do
        switch (event.getType()) {
            case SPAWN_FIRE:
                spawnFireAt(targetCell);
                break;
            case SPAWN_SMOKE:
                spawnSmokeAt(targetCell, event.getDensity());
                break;
        }
    }

    public void initialize() {
        // resets simulation time and state (will be useful for restart)
        this.currentTime = 0.0f;
        this.isRunning = true;
        // clear the lists in case of restart
        this.agents.clear();
        this.agentsToAdd.clear();
        this.agentsToRemove.clear();
        // People and hazards deploying
        spawnInitialAgents();
        // initialize the statistic base on config starting data
        int totalPeople = this.config.getInitialEvacueesCount();
        this.stats = new Statistics(totalPeople);
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
            if (!agentsToAdd.isEmpty()) {
                agents.addAll(agentsToAdd);
                agentsToAdd.clear();
            }
            board.updateSpatialIndex(agents); // so board can see added agents (evacuees)
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
            if (!agentsToAdd.isEmpty()) {
                agents.addAll(agentsToAdd);
                agentsToAdd.clear();
            }

            board.updateSpatialIndex(agents); // so board can see added agents (evacuees)
        }

        if (!agentsToAdd.isEmpty()) {
            agents.addAll(agentsToAdd);
            agentsToAdd.clear();
        }

        board.updateSpatialIndex(agents); // so board can see added agents (fires)
        // first simulation tick
        updateTick(0.0f);
    }

    public void run(){
        System.out.println(" === ROZPOCZĘCIE SYMULACJI === ");

        // fixed time step for simulation (1 step = 0.5 sec)
        final float dt = 0.5f;

        // security for showing so that the console doesn't lock forever
        int maxSteps = 30; // simulation will make 30 steps
        int currentStep = 0;

        while (isRunning && currentStep < maxSteps) {
            System.out.println(" --- KROK: " + currentStep + " (Czas symulacji: " + currentTime + "s) --- ");
            // agent logic update
            updateTick(dt);
            // global clock and counter update
            currentTime += dt;
            currentStep++;

            long evacueeCount = 0;
            long fireCount = 0;
            long smokeCount = 0;

            for (Agent a : agents) {
                 if (a instanceof evacuation.sim.agent.human.Evacuee) {
                    evacueeCount++;
                } else if (a instanceof evacuation.sim.agent.hazard.Smoke) {
                    smokeCount++;
                } else if (a instanceof evacuation.sim.agent.hazard.Fire) {
                    fireCount++;
                }
}

            System.out.println("-> Ludzi w budynku: " + evacueeCount);
            System.out.println("-> Zagrożenia: [Ogień: " + fireCount + " | Dym: " + smokeCount + "]");
            System.out.println("Uratowani: " + stats.getSavedCount());

            // put the loop to sleep for 500 millis
            try {
                Thread.sleep(600);
            } catch (InterruptedException e) {
                System.err.println("Symulacja przerwana!");
                isRunning = false;
            }
        }
        System.out.println("=== KONIEC SYMULACJI ===");
    }

    public void updateTick(float dt) {
        this.currentTime += dt;

        board.updateSpatialIndex(agents); // update of indexes before agents move
        // active agents make their moves
        for (Agent agent : agents) {
            agent.update(board, dt);

            board.updateSpatialIndex(agents);

            if (agent instanceof Evacuee evacueeAgent) {
                Cell cell = board.getCell(evacueeAgent.getLogicalX(), evacueeAgent.getLogicalY());

                if (cell != null) {
                    cell.addVisit();
                }

                // agent death check
                if(!agent.isActive()){
                    if (cell != null) {
                        if (cell.getDynamicState() == DynamicState.FIRE) {
                            stats.incrementCasualtiesFire();
                        } else if (cell.getDynamicState() == DynamicState.SMOKE) {
                            stats.incrementCasualtiesSmoke();
                        }
                    }
                    removeAgent(agent);
                }
                // agents successfully evacuated
                else if (cell != null && cell.getBaseType() == BaseType.EXIT) {
                    if (evacueeAgent.getRenderX() == evacueeAgent.getLogicalX() &&
                            evacueeAgent.getRenderY() == evacueeAgent.getLogicalY()) {
                        stats.incrementSaved(this.currentTime);
                        removeAgent(evacueeAgent);
                    }
                }

            }

            if (agent instanceof Smoke smokeAgent) { 
                if (smokeAgent.getDensity() <= 0.0f) {
                    Cell cell = board.getCell(smokeAgent.getLogicalX(), smokeAgent.getLogicalY());
                    // setting NONE only if the cell isn't FIRE
                        if (cell.getDynamicState() == DynamicState.SMOKE) {
                            cell.setDynamicState(DynamicState.NONE);
                        }
                    removeAgent(smokeAgent);
                }
            }
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
            a.addObserver(this); // simulation will "listen" the agent communicates
            agentsToAdd.add(a);
        }
    }

    public void removeAgent(Agent a) {
        // put agent to remove to the remove buffer
        if (a != null) {
            a.removeObserver(this);
            agentsToRemove.add(a);
        }
    }

    public void spawnFireAt(Cell cell) {
        if (cell == null || cell.getDynamicState() == DynamicState.FIRE) {
            return; // if it's already a fire - do nothing
        }

        List<Agent> agentsHere = board.getAgentsAt(cell);
        for (Agent a : agentsHere) {
            if (a instanceof Smoke) {
                removeAgent(a);
            }
        }

        // change the state of the board to FIRE, so that no one else can spawn on this Cell
        cell.setDynamicState(DynamicState.FIRE);

        // use AgentFactory to create new instance of Fire
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

    // getters/setters

    public Board getBoard() {
        return this.board;
    }

    // safe getter (encapsulation security: returns no changeable list)
    public List<Agent> getAgents() {
        return Collections.unmodifiableList(this.agents);
    }

    public Statistics getStats() {
        return stats;
    }

    public float getCurrentTime() {
        return currentTime;
    }
}
