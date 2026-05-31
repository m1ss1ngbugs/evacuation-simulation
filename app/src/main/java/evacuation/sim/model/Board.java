package evacuation.sim.model;

import evacuation.sim.agent.Agent;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class Board {
    private int width;
    private int height;
    private Cell[][] grid;
    // A quick map (spatial indexing) that assigns each agent to a specific cell (updates every dt)
    private Map<String, List<Agent>> spatialIndex = new HashMap<>();

    public Board(String mapFilePath) {
        loadMapFromFile(mapFilePath);
    }

    // Method which updates spatial index (makes map: agent - position on the board)
    public void updateSpatialIndex(List<Agent> agentsFromSimulation) {
        spatialIndex.clear();
        // iterates through the list of agents
        for (Agent agent : agentsFromSimulation) {
            // save coordinates of agent in the form of a key
            String key = agent.getLogicalX() + "," + agent.getLogicalY();
            // if "coordinate" key is mentioned for the first time
            if (!spatialIndex.containsKey(key)) {
                spatialIndex.put(key, new ArrayList<>());
            }
            // connects agent with the specific key in the map
            spatialIndex.get(key).add(agent);
        }
    }

    // helps to get an agent on the specific cell (using coordinates)
    public List<Agent> getAgentsAt(int x, int y) {
        String key = x + "," + y;
        return spatialIndex.getOrDefault(key, Collections.emptyList());
    }

    // check whether the cell is within the board's boundaries
    boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public Cell getCell(int x, int y) {
        return grid[x][y];
    }

    // helps to get the neighbors of the cell
    public List<Cell> getNeighbors(int x, int y) {
        List<Cell> neighbors = new ArrayList<>();
        // iterates through every direction
        for (Direction dir : Direction.values()) {
            int nx = x + dir.getDx();
            int ny = y + dir.getDy();
            // adds the neighbor cell if it's in bounds of the board
            if (inBounds(nx, ny)) {
                neighbors.add(getCell(nx, ny));
            }
        }
        return neighbors;
    }

    // method designed for loading a map from a text file
    public void loadMapFromFile(String path){
        try {
            // converts String path to Path path to the file and then reads and save all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(path));

            // checks if the file is empty
            if (lines.isEmpty()) {
                throw new IOException("Plik mapy jest pusty!");
            }

            // learn the sizes directly from the file
            this.height = lines.size();
            this.width = lines.getFirst().length();
            // initialize grid
            this.grid = new Cell[width][height];

            // a loop that converts characters to Cell objects
            for (int y = 0; y < height; y++) {
                String line = lines.get(y);
                // iterate through each character in a given line (X axis)
                for (int x = 0; x < width; x++) {
                    char c = line.charAt(x);

                    // assigning tile type
                    BaseType type = switch (c) {
                        case '#' -> BaseType.WALL;
                        case 'E' -> BaseType.EXIT;
                        default -> BaseType.FLOOR;
                    };
                    // initialization of a new cell
                    grid[x][y] = new Cell(x, y, type);
                }
            }
        } catch (IOException e) {
            System.err.println("Błąd ładowania mapy, tworzę awaryjną planszę 10x10... " + e.getMessage());
            // generate default floor on error
            generateDefaultFloor();
        }
    }

    // auxiliary method for loadMapFromFile method
    private void generateDefaultFloor() {
        this.width = 10;
        this.height = 10;
        this.grid = new Cell[width][height];
        for(int y = 0; y <= height; y++){
            for(int x = 0; x <= width; x++){
                if(grid[x][y] == null){
                    grid[x][y] = new Cell(x, y, BaseType.FLOOR);
                }
            }
        }
    }

    // standard getters

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
