package apps;

import math.Matrix;
import neural.activation.IDifferentiableFunction;
import neural.MLP;
import neural.activation.Sigmoid;
import neural.activation.Step;
import java.util.Scanner;

/**
 * @author hdaniel@ualg.pt
 * @version 202511050300
 */
public class MLPNXOR {

    public static void main(String[] args) {

        double lr    = 0.5; //define a learning rate in range [0, 1] 
        int   epochs = 20000; //define the number of epochs in the order of thousands
        int[] topology = {2, 2, 1};

        //Dataset
        Matrix trX = new Matrix(
                new double[][]{
                        {0, 0},
                        {0, 1},
                        {1, 0},
                        {1, 1}});

        Matrix trY = new Matrix(
                new double[][]{
                        {1},
                        {0},
                        {0},
                        {1}});


        //Get input and create evaluation Matrix
        Scanner sc = new Scanner(System.in);
        sc.useLocale(java.util.Locale.US);
        if (sc.hasNextInt()) {
            int n = sc.nextInt();
            double[][] input = new double[n][2];

            for (int i = 0; i < n; i++) {
                input[i][0] = sc.nextDouble();
                input[i][1] = sc.nextDouble();
            }
            Matrix evX = new Matrix(input);

            //Train the MLP
            MLP mlp = new MLP(topology,
                    new IDifferentiableFunction[]{
                            new Sigmoid(),
                            new Sigmoid(),},
                    -1);
            
            // Task 5: Train and Plot MSE (Printing MSE to stderr to avoid polluting output)
            double[] mse = mlp.train(trX, trY, lr, epochs);
            // for(int i=0; i<mse.length; i+=1000) System.err.println("Epoch " + i + ": " + mse[i]);

            //Predict and output results
            Matrix pred = mlp.predict(evX);

            //convert probabilities to integer classes: 0 or 1
            pred = pred.apply(new Step().fnc());

            //print output
            //insert code here to print the pred Matrix as integers
            for (int i = 0; i < pred.rows(); i++) {
                System.out.printf("%.0f\n", pred.get(i, 0));
            }
            
            // Task 5: Compare weights (Printing to stderr)
            // Matrix[] weights = mlp.getWeights();
            // System.err.println("Trained Weights:");
            // for(Matrix w : weights) System.err.println(w);
        }
        sc.close();

        sc.close();

    }
}
