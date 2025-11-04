public class Perceptron {

    double weight1;
    double weight2;
    double bias;

    public Perceptron(){
        this.weight1 = 1;
        this.weight2 = 1;
        this.bias = 1;
    }

    public double input(double x1, double x2){
        return this.sigmoid(this.weight1 * x1 + this.weight2 * x2);
    }

    public double sigmoid(double z){
        return 1/ (1 + Math.exp(-z));
    }

    private void backpropagation(){

    }
}
