package apps;

import math.Matrix;
import neural.MLP;
import neural.activation.IDifferentiableFunction;
import neural.activation.Sigmoid;
import neural.activation.Step;

/**
 * Task 3: Implement and test the single neuron configured in 2.
 */
public class SingleNeuron {

    public static void main(String[] args) {
        // Task 1: Dataset (OR function)
        Matrix X = new Matrix(new double[][]{
                {0, 0},
                {0, 1},
                {1, 0},
                {1, 1}
        });
        Matrix Y = new Matrix(new double[][]{
                {0},
                {1},
                {1},
                {1}
        });

        // Task 2: Weights for Single Neuron (OR)
        // w1 = 1, w2 = 1, b = -0.5
        // Sigmoid(1*x1 + 1*x2 - 0.5) >= 0.5 <-> x1 + x2 - 0.5 >= 0
        // 0,0 -> -0.5 < 0 -> 0
        // 0,1 -> 0.5 >= 0 -> 1
        // 1,0 -> 0.5 >= 0 -> 1
        // 1,1 -> 1.5 >= 0 -> 1
        
        // Topology: 2 inputs, 1 output
        int[] topology = {2, 1};
        MLP neuron = new MLP(topology, new IDifferentiableFunction[]{new Sigmoid()}, -1);

        // Manually set weights
        Matrix[] weights = new Matrix[1];
        Matrix[] biases = new Matrix[1];

        weights[0] = new Matrix(new double[][]{
                {1}, // w1
                {1}  // w2
        });
        biases[0] = new Matrix(new double[][]{
                {-0.5} // b
        });

        neuron.setWeights(weights, biases);

        // Test
        System.out.println("Testing Single Neuron (OR function) with manual weights:");
        Matrix pred = neuron.predict(X);
        Matrix predClass = pred.apply(new Step().fnc());

        for (int i = 0; i < X.rows(); i++) {
            double x1 = X.get(i, 0);
            double x2 = X.get(i, 1);
            double y = Y.get(i, 0);
            double p = predClass.get(i, 0);
            System.out.printf("Input: (%.0f, %.0f) Target: %.0f Predicted: %.0f -> %s\n", 
                    x1, x2, y, p, (y == p ? "OK" : "FAIL"));
        }
    }
}
