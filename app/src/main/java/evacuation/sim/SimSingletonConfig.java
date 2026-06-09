package evacuation.sim;

public class SimSingletonConfig {
    private static SimSingletonConfig instance;
    // simulation parameters
    private float maxEvacuationTime;
    // initial agent counts
    private int initialEvacueesCount;
    private int initialFireHazardsCount;
    // role ratios
    private float leaderRatio;
    private float followerRatio;
    private float panickedRatio;
    // evacuee initial parameters
    private float meanBaseSpeed;
    private float speedVariance;
    private float evacueeHealth;
    private float evacueeReactionTime;
    private int evacueeVisionRadius;
    // evacuee panic
    private float meanPanicThreshold;
    private float panicVariance;
    // social evacuee
    private float meanSocialFactor;
    private float socialVariance;
    // fire
    private float fireDamagePerSecond;
    private float fireSpreadInterval;
    private float fireIncubationDelay;
    // smoke
    private float smokeDamagePerSecond;
    private float smokeSpreadInterval;
    private float smokeInitialDensity;
    private float smokeFadeRatePerSecond;
    private float smokeDuplicationThreshold;

    private String mapFilePath;

    private SimSingletonConfig() {

        this.maxEvacuationTime = 600.0f;     // max 10 minutes simulation
        this.initialEvacueesCount = 20;       // 50 people to escape

        this.leaderRatio = 0.15f;            // 15% of leaders
        this.followerRatio = 0.65f;          // 65% of followers
        this.panickedRatio = 0.20f;          // 20% of panicked people

        this.meanBaseSpeed = 1.4f;           // the average human speed is approx. 1.4 m/s
        this.speedVariance = 0.2f;           // the speed will vary from approx. 1.2 to 1.6 m/s
        this.evacueeHealth = 100.0f;         // initial evacuee health
        this.evacueeReactionTime = 3.0f;     // evacuee first reaction to hazard time = 3 sec
        this.evacueeVisionRadius = 2;        // sets evacuee vision radius to two cells

        this.meanPanicThreshold = 50.0f;     // panic threshold (e.g. on a skale of 100)
        this.panicVariance = 10.0f;          // panic threshold will vary from approx. 50.0 to 60.0

        this.meanSocialFactor = 0.5f;        // how well they stick together in a group
        this.socialVariance = 0.1f;          // variation of social factor

        this.initialFireHazardsCount = 1;    // 1 fire hazard source
        this.fireDamagePerSecond = 20.f;     // fire damage per second
        this.fireSpreadInterval = 3.0f;      // fire spreads every 3 seconds
        this.fireIncubationDelay = 3.0f;     // fire has 3 seconds incubation delay

        this.smokeDamagePerSecond = 2.5f;    // smoke damage per second
        this.smokeSpreadInterval = 1.5f;     // smoke spreads faster - every 1.5 seconds
        this.smokeInitialDensity = 100.0f;          // smoke density parametr has 100 at start
        this.smokeFadeRatePerSecond = 10.0f;  // smoke fades 10% per second
        this.smokeDuplicationThreshold = 15.0f; // smoke duplicates only if it has density > threshold

        this.mapFilePath = "mapa.txt";
    }

    public static SimSingletonConfig getInstance(){
        if (instance == null){
            instance = new SimSingletonConfig();
        }
        return instance;
    }

    public void loadFromGUI(){
        // TODO: metoda potrzebuje implementacji w przyszłości, kiedy dojdziemy do interfejsu graficznego użytkownika
    }


    // standard getters
    public float getMaxEvacuationTime() {
        return maxEvacuationTime;
    }

    public int getInitialEvacueesCount() {
        return initialEvacueesCount;
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

    public String getMapFilePath() {
        return mapFilePath;
    }

    public int getInitialFireHazardsCount() {
        return initialFireHazardsCount;
    }

    public float getEvacueeHealth() {
        return evacueeHealth;
    }

    public float getFireDamagePerSecond() {
        return fireDamagePerSecond;
    }

    public float getSmokeDamagePerSecond() {
        return smokeDamagePerSecond;
    }

    public float getFireIncubationDelay() {
        return fireIncubationDelay;
    }

    public float getSmokeInitialDensity() {
        return smokeInitialDensity;
    }

    public float getSmokeFadeRatePerSecond() {
        return smokeFadeRatePerSecond;
    }

    public float getSmokeDuplicationThreshold() {
        return smokeDuplicationThreshold;
    }

    public float getEvacueeReactionTime() {
        return evacueeReactionTime;
    }

    public int getEvacueeVisionRadius() {
        return evacueeVisionRadius;
    }

    public void setInitialEvacueesCount(int initialEvacueesCount) {
        this.initialEvacueesCount = initialEvacueesCount;
    }

    public void setMapFilePath(String mapFilePath) {
        this.mapFilePath = mapFilePath;
    }

    public void setMaxEvacuationTime(int maxEvacuationTime) {
        this.maxEvacuationTime = maxEvacuationTime;
    }

    public void setLeaderRatio(float leaderRatio) {
        this.leaderRatio = leaderRatio;
    }

    public void setFollowerRatio(float followerRatio) {
        this.followerRatio = followerRatio;
    }

    public void setPanickedRatio(float panickedRatio) {
        this.panickedRatio = panickedRatio;
    }

    public void setMeanBaseSpeed(float meanBaseSpeed) {
        this.meanBaseSpeed = meanBaseSpeed;
    }

    public void setSpeedVariance(float speedVariance) {
        this.speedVariance = speedVariance;
    }

    public void getMeanPanicThreshold(float meanPanicThreshold) {
        this.meanPanicThreshold = meanPanicThreshold;
    }

    public void setPanicVariance(float panicVariance) {
        this.panicVariance = panicVariance;
    }

    public void setMeanSocialFactor(float meanSocialFactor) {
        this.meanSocialFactor = meanSocialFactor;
    }

    public void setSocialVariance(float socialVariance) {
        this.socialVariance = socialVariance;
    }

    public void setFireSpreadInterval(float fireSpreadInterval) {
        this.fireSpreadInterval = fireSpreadInterval;
    }

    public void setSmokeSpreadInterval(float smokeSpreadInterval) {
        this.smokeSpreadInterval = smokeSpreadInterval;
    }

    public void setInitialFireHazardsCount(int initialFireHazardsCount) {
        this.initialFireHazardsCount = initialFireHazardsCount;
    }

    public void setEvacueeHealth(int evacueeHealth) {
        this.evacueeHealth = evacueeHealth;
    }

    public void setFireDamagePerSecond(float fireDamagePerSecond) {
        this.fireDamagePerSecond = fireDamagePerSecond;
    }

    public void setSmokeDamagePerSecond(float smokeDamagePerSecond) {
        this.smokeDamagePerSecond = smokeDamagePerSecond;
    }

    public void setFireIncubationDelay(float fireIncubationDelay) {
        this.fireIncubationDelay = fireIncubationDelay;
    }

    public void setSmokeInitialDensity(float smokeInitialDensity) {
        this.smokeInitialDensity = smokeInitialDensity;
    }

    public void setSmokeFadeRatePerSecond(float smokeFadeRatePerSecond) {
        this.smokeFadeRatePerSecond = smokeFadeRatePerSecond;
    }

    public void setSmokeDuplicationThreshold(float smokeDuplicationThreshold) {
        this.smokeDuplicationThreshold = smokeDuplicationThreshold;
    }

    public void setEvacueeReactionTime(float evacueeReactionTime) {
        this.evacueeReactionTime = evacueeReactionTime;
    }

    public void setEvacueeVisionRadius(int evacueeVisionRadius) {
        this.evacueeVisionRadius = evacueeVisionRadius;
    }
}
