package evacuation.sim.factory;

import java.util.Random;

/**
 * A helper class created to randomize the initial parameters of the agent.
 * @author Bartłomiej Krajewski (293439)
 */
public class AgentRandomizer {
    private static final Random random = new Random();

    /**
     * Randomizes evacuee parameters using mean and variance.
     * @param mean Mean value of the parameter.
     * @param variance Variance of the parameter.
     * @return Randomly got parameter of the evacuee.
     */
    public static float calculateValue(float mean, float variance){

        float modifier = random.nextFloat() * 2f - 1f;
        return mean + (modifier * variance);
    }
    
}
