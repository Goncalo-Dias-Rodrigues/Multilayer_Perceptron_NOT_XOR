package neural;

import math.Matrix;
import neural.activation.IDifferentiableFunction;

import java.util.List;
import java.util.Random;

/**
 * @author hdaniel@ualg.pt
 * @version 202511052038
 */
public class MLP {

    private Matrix[] w;  //weights for each layer
    private Matrix[] b;  //biases for each layer
    private Matrix[] yp; //outputs for each layer
    private IDifferentiableFunction[] act; //activation functions for each layer
    private int numLayers;

    /* Create a Multi-Layer Perceptron with the given layer sizes.
     * layerSizes is an array where each element represents the number of neurons in that layer.
     * For example, new MLP(new int[]{3, 5, 2}) creates a MLP with 3 input neurons,
     * 5 hidden neurons, and 2 output neurons.
     *
     * PRE: layerSizes.length >= 2
     * PRE: act.length == layerSizes.length - 1
     */
    public MLP(int[] layerSizes, IDifferentiableFunction[] act, int seed) {
        if (seed < 0)
            seed = (int) System.currentTimeMillis();

        numLayers = layerSizes.length;

        //setup activation by layer
        this.act = act;

        //create output storage for each layer but the input layer
        yp = new Matrix[numLayers];

        //create weights and biases for each layer
        //each row in w[l] represents the weights that are input
        w = new Matrix[numLayers-1];
        b = new Matrix[numLayers-1];

        Random rnd = new Random(seed);
        for (int i=0; i < numLayers-1; i++) {
            w[i] = Matrix.Rand(layerSizes[i], layerSizes[i + 1], seed);
            b[i] = Matrix.Rand(1, layerSizes[i + 1], (int)seed);
        }
    }


    // Feed forward propagation
    // also used to predict after training the net
    // yp[0] = X
    // yp[l+1] = Sigmoid( yp[l] * w[l]+b[l] )
    public Matrix predict(Matrix X) {
        yp[0] = X;
        for (int l=0; l < numLayers-1; l++)
            yp[l + 1] = yp[l].dot(w[l]).addRowVector(b[l]).apply(act[l].fnc());
        return yp[numLayers-1];
    }


    // back propagation
    public Matrix backPropagation(Matrix X, Matrix y, double lr) {
        Matrix e = null;
        Matrix delta = null;

        //back propagation using generalised delta rule
        for (int l = numLayers-2; l >= 0; l--) {
            if (l == numLayers-2) {                     //output layer
                e = y.sub(yp[l + 1]);                   //e = y - yp[l+1]
            }
            else {                                      //propagate error to previous layer
                //      e = delta * w[l+1]^T
                e = delta.dot(w[l+1].transpose());
            }

            //      dy = yp[l+1] .* (1-yp[l+1])
            //      Note: to compute dy use Sigmoid class derivative
            //	          similarly as in predict()
            //
            //      delta = e .* dy
            Matrix dy = yp[l+1].apply(act[l].derivative());
            delta = e.mult(dy);

            //      w[l] += yp[l]^T * delta * lr
            w[l] = w[l].add(yp[l].transpose().dot(delta).mult(lr));

            // update biases
            b[l] = b[l].addRowVector(delta.sumColumns()).mult(lr);

        }
        return e;
    }


    public double[] train(Matrix X, Matrix y, double learningRate, int epochs) {
        int nSamples = X.rows();
        double[] mse = new double[epochs];

        for (int epoch=0; epoch < epochs; epoch++) {

            //forward propagation
            Matrix ypo = predict(X);

            //backward propagation
            Matrix e = backPropagation(X, y, learningRate);

            //mse
            mse[epoch] = e.dot(e.transpose()).get(0, 0) / nSamples;
        }
        return mse;
    }
    public void setWeights(Matrix[] weights, Matrix[] biases) {
        if (weights.length != this.w.length || biases.length != this.b.length) {
            throw new IllegalArgumentException("Invalid number of weight/bias matrices.");
        }
        this.w = weights;
        this.b = biases;
    }

    public Matrix[] getWeights() {
        return w;
    }
}
