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

    public Board(Cell[][] grid) {
        this.grid = grid;
        this.width = grid.length;
        this.height = grid[0].length;
    }

    public Board(int width, int height) {
        this.width = width;
        this.height = height;
        this.grid = new Cell[width][height];
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
        if(!inBounds(x, y)){
            return null; // Return null if out of bounds
        }
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

    // Auxiliary method for finding a safe starting point
    public Cell getRandomEmptyFloor() {
        int maxAttempts = 1000;
        for (int i = 0; i < maxAttempts; i++) {
            // draw the coordinates
            int random_x = (int) (Math.random() * this.width);
            int random_y = (int) (Math.random() * this.height);

            Cell cell = getCell(random_x, random_y);

            // checks if agent can be placed here
            if (cell != null && cell.getBaseType() == BaseType.FLOOR
                    && cell.getDynamicState() == DynamicState.NONE) {

                    List<Agent> agentsAtCell = getAgentsAt(random_x, random_y);
                // check if the Cell is Empty from other Agents
                if (agentsAtCell.isEmpty() || agentsAtCell == null) {
                     return cell; // place to spawn was found

                 }
            }
        }
        return null;  // place can't be found after 1000 attempts
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
                        case 'O' -> BaseType.OBSTACLE;
                        case '#' -> BaseType.WALL;
                        case 'E' -> BaseType.EXIT;
                        default -> BaseType.FLOOR;
                    };
                    // initialization of a new cell
                    grid[x][y] = new Cell(x, y, type);
                }
            }
        } catch (IOException e) {
            System.err.println("Błąd ładowania mapy"  + e.getMessage() + ", tworzę awaryjną planszę 10x10... ");
            // generate default floor on error
            generateDefaultFloor();
        }
    }

    // auxiliary method for loadMapFromFile method
    private void generateDefaultFloor() {
        this.width = 10;
        this.height = 10;
        this.grid = new Cell[width][height];
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                if(grid[x][y] == null){
                    if(x == 0 || x == width - 1 || y == 0 || y == height - 1){
                        grid[x][y] = new Cell(x, y, BaseType.WALL);
                    } else {
                        grid[x][y] = new Cell(x, y, BaseType.FLOOR);
                    }
                }
            }
        }

        grid[7][0] = new Cell(7, 0, BaseType.EXIT); // Add an exit
        grid[2][height-1] = new Cell(2, height-1, BaseType.EXIT); // Add another exit
    }

    // standard getters

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Cell[][] getGrid() {
        return grid;
    }
}
