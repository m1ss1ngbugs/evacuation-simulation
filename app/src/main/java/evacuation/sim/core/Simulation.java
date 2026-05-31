package evacuation.sim.core;

import evacuation.sim.SimSingletonConfig;
import evacuation.sim.agent.Agent;
import evacuation.sim.model.Board;

import java.util.ArrayList;
import java.util.List;

public class Simulation {
    private Board board;
    private List<Agent> agents;
    private Statistics stats;
    private SimSingletonConfig config;

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

        // później trzeba też dopisać tę metodę, która stworzy ludzi i ogień na starcie symulacji
        spawnInitialAgents();

        // initialize the statistics class, passing it the starting data
        int totalPeople = this.config.getInitialEvacueeCount();
        this.stats = new Statistics(totalPeople);
    }

    public void initialize(){
        // zadania inicjalizacyjne
        // Przygotowuje środowisko – ładuje planszę, tworzy agentów i ustawia
        // parametry początkowe
    }

    public void spawnInitialAgents(){

        // NIE SKOŃCZONA

        // musi stworzyć początkowych agentów symulacji, coś takiego:

        int peopleToSpawn = config.getInitialEvacueesCount();
        int currentId = 0;

        // pętla tworząca ludzi
        for (int i = 0; i < peopleToSpawn; i++) {
            // tutaj trzeba napisać logikę losowego szukania wolnego kafelka
            // i dodawania agenta uważając na profile psychologiczne
            // (procentowa ilość ludzi z tymi profilami jest wskazana w config)

        }

        // podobnie zrobić pętle dla drugich agentów

        // NA DODATEK: trzeba pomyśleć nad tym, czy uda się wykorzystać wzorzec projektowy AgentFactory
    }

    public void run(){
        // główna pętla symulacji (też potrzebuje napisania ;) )
    }

    public void updateTick(float dt){
        // Dodać logikę aktualizacji kadru symulacji
        // To metoda wywoływana w każdej klatce. Przechodzi po liście
        // agents i dla każdego wywołuje update(board, dt)
    }

    public void addAgent(Agent a){
        // dodać logikę dodania agenta
    }

    public void removeAgent(Agent a){
        // dodać logikę usuwania agenta
    }
}
