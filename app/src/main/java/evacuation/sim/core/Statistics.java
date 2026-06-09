package evacuation.sim.core;

import evacuation.sim.model.Board;

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

    public void incrementSaved(float time){
        savedCount++;
        if(savedCount == 1){
            this.firstEvacuationTime = time;
        }
    }

    public void incrementCasualtiesFire(){casualtiesFire++;}

    public void incrementCasualtiesSmoke(){casualtiesSmoke++;}

    public void generateHeatmap(Board board){
        
    }

    public float calculateSurvivalRate(){
        return (float) savedCount / totalPeople;
    }

    public String determineFinalScenario(){
        return "Hello World!";
    }

    public int getSavedCount() {
        return savedCount;
    }
}
