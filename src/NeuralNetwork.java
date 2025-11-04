import java.util.ArrayList;

public class NeuralNetwork {

    ArrayList<Perceptron> neurons;

    public NeuralNetwork(ArrayList<Perceptron> neurons){
        this.neurons = neurons;
    }

    public double input(int x1, int x2){

        double output1;
        double output2;
        double output3;

        output1 = neurons.get(0).input(x1, x2);
        output2 = neurons.get(1).input(x1, x2);

        output3 = neurons.get(2).input(output1, output2);

        return ;
    }



}
