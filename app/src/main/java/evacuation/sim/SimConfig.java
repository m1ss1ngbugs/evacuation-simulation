package evacuation.sim;

public class SimConfig {
    private float maxEvacuationTime;
    private int initialEvacueeCount;
    private float leaderRatio;
    private float followerRatio;
    private float panickedRatio;
    private float meanBaseSpeed;
    private float speedVariance;
    private float meanPanicThreshold;
    private float panicVariance;
    private float meanSocialFactor;
    private float socialVariance;
    private float fireSpreadInterval;
    private float smokeSpreadInterval;

    public SimConfig() {
        this.maxEvacuationTime = 600.0f;     // max 10 minutes simulation
        this.initialEvacueeCount = 50;       // 50 people to escape
        this.leaderRatio = 0.15f;            // 15% of leaders
        this.followerRatio = 0.65f;          // 65% of followers
        this.panickedRatio = 0.20f;          // 20% of panicked people

        this.meanBaseSpeed = 1.4f;           // the average human speed is approx. 1.4 m/s
        this.speedVariance = 0.2f;           // the speed will vary from approx. 1.0 to 1.8 m/s
        this.meanPanicThreshold = 50.0f;     // panic threshold (e.g. on a skale of 100)
        this.panicVariance = 10.0f;          // panic threshold will vary from approx. 50.0 to 60.0

        this.meanSocialFactor = 0.5f;        // how well they stick together in a group
        this.socialVariance = 0.1f;          // variation of social factor

        this.fireSpreadInterval = 3.0f;      // fire spreads every 3 seconds
        this.smokeSpreadInterval = 1.5f;     // smoke spreads faster - every 1.5 seconds

    }

    public void loadFromGUI(){

    }

    public float getMaxEvacuationTime() {
        return maxEvacuationTime;
    }

    public int getInitialEvacueeCount() {
        return initialEvacueeCount;
    }

    public float getLeaderRatio() {
        return leaderRatio;
    }

    public float getFollowerRatio() {
        return followerRatio;
    }

    public float getPanickedRatio() {
        return panickedRatio;
    }

    public float getMeanBaseSpeed() {
        return meanBaseSpeed;
    }

    public float getSpeedVariance() {
        return speedVariance;
    }

    public float getMeanPanicThreshold() {
        return meanPanicThreshold;
    }

    public float getPanicVariance() {
        return panicVariance;
    }

    public float getMeanSocialFactor() {
        return meanSocialFactor;
    }

    public float getSocialVariance() {
        return socialVariance;
    }

    public float getFireSpreadInterval() {
        return fireSpreadInterval;
    }

    public float getSmokeSpreadInterval() {
        return smokeSpreadInterval;
    }
}
