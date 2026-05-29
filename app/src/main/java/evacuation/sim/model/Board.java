package evacuation.sim.model;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Board {
    private int width;
    private int height;
    private Cell[][] grid;
    // A quick map (spatial indexing) that assigns each agent to a specific cell (updates every dt)
    private Map<String, List<Agent>> spatialIndex = new HashMap<>();

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[width][height];
    }

    // Method which updates spatial index (makes map: agent - position on the board)
    public void updateSpatialIndex(List<Agent> agentsFromSimulation) {
        spatialIndex.clear();
        for (Agent agent : agentsFromSimulation) {
            String key = agent.getLogicalX() + "," + agent.getLogicalY();

            // if the agent is the first one on the cell
            if (!spatialIndex.containsKey(key)) {
                spatialIndex.put(key, new ArrayList<>());
            }

            // add agent to the cell
            spatialIndex.get(key).add(agent);
        }
    }

    public List<Agent> getAgentsAt(int x, int y) {
        String key = x + "," + y;
        return spatialIndex.getOrDefault(key, Collections.emptyList());
    }

    boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Cell getCell(int x, int y) {
        return grid[x][y];
    }

    public List<Cell> getNeighbors(int x, int y) {
        List<Cell> neighbors = new ArrayList<>();

        for (Direction dir : Direction.values()) {
            int nx = x + dir.getDx();
            int ny = y + dir.getDy();

            if (inBounds(nx, ny)) {
                neighbors.add(getCell(nx, ny));
            }
        }
        return neighbors;
    }

    public void loadMapFromFile(String path){
    // trzeba później zaimplementować tę metodę sczytywanie mapy z pliku (np. tekstowego)
    }
}
