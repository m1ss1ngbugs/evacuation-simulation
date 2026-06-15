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


/**
 * A class responsible for the board and the position of agents on it. It consists of Cells (composition).
 * Represents the two-dimensional space (tile grid) of the building where the evacuation is taking place.
 * Responsible for loading the map structure from text files, verifying movement boundaries, and
 * providing spatial data for pathfinding algorithms.
 * Has a built-in spatial indexing for quickly locating agents.
 * @author Heorhii Yartsev (293562)
 * @author Bartłomiej Krajewski (293439)
 */
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

    /**
     * Updates the spatial index by rebuilding the mapping between agents and their current grid positions.
     * Occurs on every simulation tick to enable neighbor lookups and collision detection.
     * @param agentsFromSimulation the list of currently alive agents in the simulation.
     */
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

    /**
     * Helps to get agents on the specific cell.
     * Uses the current spatial index for this purpose.
     * @param cell an object of class {@link Cell}, that represents
     *            the cell from which we want to get a list of agents.
     * @return list of agents (objects of class {@link Agent}) on this cell.
     */
    public List<Agent> getAgentsAt(Cell cell) {
        String key = cell.getLogicalX() + "," + cell.getLogicalY();
        return spatialIndex.getOrDefault(key, Collections.emptyList());
    }

    /**
     * Checks whether the cell is within the board's boundaries.
     * @param x the integer logical x coordinate of the cell.
     * @param y the integer logical y coordinate of the cell.
     * @return true or false depending on whether the cell is within the grid boundaries or not.
     */
    boolean inBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /**
     * Safe cell getter.
     * @param x the integer logical x coordinate of the cell.
     * @param y the integer logical y coordinate of the cell.
     * @return the cell (the object of class {@link Cell}), that has these coordinates,
     * or {@code null} if this cell is not in bounds of the grid
     */
    public Cell getCell(int x, int y) {
        if(!inBounds(x, y)){
            return null; // Return null if out of bounds
        }
        return grid[x][y];
    }

    /**
     * Helps to get the neighbors of the cell.
     * Allows to verify the surroundings in four directions: return list of 4 neighbor cells.
     * @param cell The cell whose neighbors we want to receive.
     * @return List of cells (objects of type {@link Cell}) - neighbors of the current cell.
     */
    public List<Cell> getNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        // iterates through every direction
        for (Direction dir : Direction.values()) {
            int nx = cell.getLogicalX() + dir.getDx();
            int ny = cell.getLogicalY() + dir.getDy();
            // adds the neighbor cell if it's in bounds of the board
            if (inBounds(nx, ny)) {
                neighbors.add(getCell(nx, ny));
            }
        }
        return neighbors;
    }

    /**
     * Auxiliary method for finding a safe starting point.
     * Searches the board for a safe, free spot to spawn a new agent.
     * Randomly selects the coordinates and checks whether the tile is a clean floor, not on fire,
     * and whether another agent is not already on it.
     *
     * @return an object of class {@link Cell} representing the free floor,
     * or {@code null} if no free spot has been found after 1000 attempts.
     */
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

                // check if the Cell is Empty from other Agents
                if (getAgentsAt(getCell(random_x, random_y)).isEmpty()) {
                     return cell; // place to spawn was found

                 }
            }
        }
        return null;  // place can't be found after 1000 attempts
    }

    /**
     * Method designed for loading a map from a text file.
     * It checks whether the file is empty, reads and saves the map dimensions,
     * and converts all symbols in the file into cells of various types.
     * If an error occurs, it generates a base map.
     * @param path String object, path to the .txt file with map to load.
     */
    public void loadMapFromFile(String path){
        try {
            // converts String path to Path path to the file and then reads and save all lines from the file
            List<String> lines = Files.readAllLines(Paths.get(path));

            // checks if the file is empty
            if (lines.isEmpty()) {
                throw new IOException("Map file is empty!");
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
            System.err.println("Map loading error:"  + e.getMessage() + ", creating a default 10x10 base... ");
            // generate default floor on error
            generateDefaultFloor();
        }
    }

    /**
     * Auxiliary method for loadMapFromFile method.
     * Generates default simple map for the simulation.
     */
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

    /**
     * Checks the line of sight between two points on the board.
     * Uses the Bresenham algorithm to map tiles along the sight path
     * and verifies that none of them are walls blocking the view.
     * @param x1 Integer logical x coordinate of the start cell, cell1.
     * @param y1 Integer logical y coordinate of the start cell, cell1.
     * @param x2 Integer logical x coordinate of the end cell, cell2.
     * @param y2 Integer logical y coordinate of the end cell, cell2.
     * @return Returns true if the cell2 is visible from the cell1. Otherwise, it returns false.
     */
    public boolean hasLineOfSight(int x1, int y1, int x2, int y2) { // Bresenham algorithm
        // total vertical and horizontal distance between points:
        int dx = Math.abs(x2 - x1); // distance in X
        int dy = Math.abs(y2 - y1); // distance in Y
        // Direction Step - go right or left, up or down:
        int sx = (x1 < x2) ? 1 : -1; // sign of X
        int sy = (y1 < y2) ? 1 : -1; // sign of Y
    
        int err = dx - dy; // error/deviation

        int currentX = x1;
        int currentY = y1;

        while (true) {
            // if we got to the goal, line is clear
            if (currentX == x2 && currentY == y2) {
                return true;
            }

            // checking every next tile on radius
            if (currentX != x1 || currentY != y1) {
                Cell checkCell = getCell(currentX, currentY);
                // if wall, evacuee doesn't see what's behind
                if (checkCell != null && checkCell.getBaseType() == BaseType.WALL) {
                    return false;
                }
            }

            int e2 = 2 * err;
            if (e2 > -dy) { 
                err -= dy;
                currentX += sx;
            }
            if (e2 < dx) {
                err += dx;
                currentY += sy;
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

    public Cell[][] getGrid() {
        return grid;
    }
}
