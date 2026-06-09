package evacuation.sim.factory;

import java.util.Random;

public class AgentRandomizer {
    private static final Random random = new Random();

    public static float calculateValue(float mean, float variance){

        float modifier = random.nextFloat() * 2f - 1f;
        return mean + (modifier * variance);
    }
    
}
