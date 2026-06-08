package evacuation.sim.core;

import evacuation.sim.model.Board;

public class Statistics {
    private Statistics stats;
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

    public void incrementSaved(float time){
        savedCount++;
        if(savedCount == 1){
            this.firstEvacuationTime = time;
        }

        this.totalEvacuationTime = time;
    }

    public void incrementCasualtiesFire(){casualtiesFire++;}

    public void incrementCasualtiesSmoke(){casualtiesSmoke++;}

    public void generateHeatmap(Board board){
        //TODO: Heatmap
    }

    public float calculateSurvivalRate(){
        return (float) savedCount / totalPeople;
    }

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
