package neural.activation;

import java.util.function.Function;

/**
 * @author hdaniel@ualg.pt
 * @version 202511051244
 */
public class Step implements IDifferentiableFunction {

    static private double threshold = 0.5;

    @Override
    public Function<Double, Double> fnc() {

        return (z) -> z >= threshold ? 1.0 : 0.0;
    }

    @Override
    public Function<Double, Double> derivative() {
        throw new UnsupportedOperationException("Step function is not differentiable.");
    }

}
