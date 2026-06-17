package evacuation.sim.core;

import evacuation.sim.model.Board;
import evacuation.sim.model.Cell;
import javafx.scene.paint.Color;

/**
 * This is a class that manages statistics.
 * It calculates and records all statistics.
 * It allows to receive and display this information later.
 * @author Bartłomiej Krajewski (293439)
 */
public class Statistics {
    private int totalPeople;
    private int savedCount;
    private int casualtiesFire;
    private int casualtiesSmoke;
    private float firstEvacuationTime;
    private float totalEvacuationTime;

    public Statistics(int totalPeople) {
        this.totalPeople = totalPeople;
        this.savedCount = 0;
        this.casualtiesSmoke = 0;
        this.casualtiesFire = 0;
        this.firstEvacuationTime = 0.0f;
        this.totalEvacuationTime = 0.0f;
    }

    /**
     * Counts the number of agents who successfully evacuated.
     * Records the time of the last and first evacuation.
     * @param time internal time of the simulation
     */
    public void incrementSaved(float time){
        savedCount++;
        if(savedCount == 1){
            this.firstEvacuationTime = time;
        }

        this.totalEvacuationTime = time;
    }

    /**
     * Counts the number of agents dead from fire.
     */
    public void incrementCasualtiesFire(){casualtiesFire++;}

    /**
     * Counts the number of agents dead from smoke.
     */
    public void incrementCasualtiesSmoke(){casualtiesSmoke++;}

    /**
     * Generates the heatmap using visits count of each cell.
     * @param board Current state of the board layout (object of class {@link Board}).
     * @return Color[][] grid, which represents the heatmap.
     */
    public Color[][] generateHeatmap(Board board){
        int width = board.getWidth();
        int height = board.getHeight();

        Color[][] heatmapColors = new Color[height][width];

        // looking for max count visit to scale colors
        int maxVisits = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = board.getCell(x, y);
                
                if (cell != null && cell.getVisitCount() > maxVisits) {
                    maxVisits = cell.getVisitCount();
                }
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = board.getCell(x, y);

                if (cell == null) {
                    heatmapColors[y][x] = Color.TRANSPARENT;
                    continue;
                }

                if (cell.getBaseType() == evacuation.sim.model.BaseType.WALL) {
                heatmapColors[y][x] = Color.BLACK;
                continue;
                }

                int visits = cell.getVisitCount();

                if (visits == 0) {
                    heatmapColors[y][x] = Color.LIGHTGRAY; // no one stood here
                } else if (maxVisits == 0) {
                    heatmapColors[y][x] = Color.GREEN;
                } else {
                    // scale of intensity (0.0 - 1.0)
                    float intensity = (float) visits / maxVisits;

                    // for coloring (using HSB)
                    double hue = 120.0 * (1.0 - intensity);
                    heatmapColors[y][x] = Color.hsb(hue, 1.0, 1.0, 0.85);
                }
            }
        }

        return heatmapColors;
    }

    /**
     * Calculates current survival rate of the simulation.
     * @return Current survival rate.
     */
    public float calculateSurvivalRate(){
        return (float) savedCount / totalPeople;
    }

    /**
     * Calculates the evacuation scenario using current survival rate.
     * @return current evacuation scenario.
     */
    public String determineFinalScenario(){
        if (savedCount == totalPeople) {
            return "Best Scenario: All Evacuees Escaped";
        } else if (calculateSurvivalRate() >= 0.66f) {
            return "Neutral Scenario: Majority of Evacuees Escaped";
        } else {
            return "Worst Scenario: Many Evacuees Lost";
        }
    }

    public int getSavedCount() {
        return savedCount;
    }

    public int getCasualtiesFire() {
        return casualtiesFire;
    }

    public int getCasualtiesSmoke() {
        return casualtiesSmoke;
    }

    public float getFirstEvacuationTime() {
        return firstEvacuationTime;
    }

    public float getTotalEvacuationTime() {
        return totalEvacuationTime;
    }
}
