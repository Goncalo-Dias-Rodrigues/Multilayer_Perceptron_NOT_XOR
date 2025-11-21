package math;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author hdaniel@ualg.pt
 * @version 202511052002
 */
public class Matrix {

    private double[][] data;
    private int rows, cols;


    /**
     * Matrix constructor that only sets the dimensions of the matrix
     * @param rows
     * @param cols
     */
    public Matrix(int rows, int cols) {
        data = new double[rows][cols];
        this.rows = rows;
        this.cols = cols;
    }

    /**
     * Matrix constructor that creates a matrix from an array of values
     * @param data
     */
    public Matrix(double[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new double[rows][cols];
        for (int i = 0; i < rows; i++)
            System.arraycopy(data[i], 0, this.data[i], 0, cols);

    }

    /**
     * Randomizes the values in a matrix based on a seed and the given dimensions
     * @param rows number of rows in the matrix
     * @param cols number of columns in the matrix
     * @param seed seed that will randomize the values of the matrix
     * @return filled matrix
     */
    static public Matrix Rand(int rows, int cols, int seed) {
        Matrix out = new Matrix(rows, cols);

        if (seed < 0)
            seed = (int) System.currentTimeMillis();

        Random rand = new Random(seed);
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                out.data[i][j] = rand.nextDouble();

        return out;
    }


    /**
     * Gets the values from the coordinates in the matrix
     * @param row row of the value
     * @param col column of the value
     * @return value in the matrix
     */
    public double get(int row, int col) {
        return data[row][col];
    }

    /**
     * Gets the number of rows in the matrix
     * @return number of rows
     */
    public int rows() {
        return rows;
    }

    /**
     * Gets the number of columns of the matrix
     * @return number of columns
     */
    public int cols() {
        return cols;
    }


    //==============================================================
    //  Element operations
    //==============================================================

    /**
     * Apply a given function to all elements of the matrix
     * @param fnc function to calculate new values
     * @return matrix with new values given by the given function
     */
    private Matrix traverse(Function<Double, Double> fnc) {
        Matrix result = new Matrix(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.data[i][j] = fnc.apply(data[i][j]);
            }
        }
        return result;
    }

    public Matrix apply(Function<Double, Double> fnc) {
        return traverse(fnc);
    }

    /**
     * Multiply all values of the matrix by a scalar
     * @param scalar value to multiply all matrix values by
     * @return result matrix from the multiplication
     */
    public Matrix mult(double scalar) {
        Matrix matrixResult = new Matrix(this.data);

        for (int i = 0; i < matrixResult.rows; i++) {
            for (int j = 0; j < matrixResult.cols; j++) {
                matrixResult.data[i][j] = matrixResult.data[i][j] * scalar;
            }
        }

        return matrixResult;
    }

    /**
     * Addition to all values of the matrix by a scalar
     * @param scalar value to add to all matrix values by
     * @return result matrix from the sum
     */
    public Matrix add(double scalar) {
        Matrix matrixResult = new Matrix(this.data);

        for (int i = 0; i < matrixResult.rows; i++) {
            for (int j = 0; j < matrixResult.cols; j++) {
                matrixResult.data[i][j] = matrixResult.data[i][j] + scalar;
            }
        }

        return matrixResult;
    }

    /**
     * Subtraction all values of the matrix from scalar
     * @param scalar value to subtract all matrix values by
     * @return result matrix from the subtraction
     */
    public Matrix subFromScalar(double scalar) {
        Matrix matrixResult = new Matrix(this.data);

        for (int i = 0; i < matrixResult.rows; i++) {
            for (int j = 0; j < matrixResult.cols; j++) {
                matrixResult.data[i][j] = scalar - matrixResult.data[i][j];
            }
        }

        return matrixResult;
    }


    //==============================================================
    //  Element-wise operations between two matrices
    //==============================================================

    //Element wise operation

    /**
     * Executes a given operation to make between the current matrix and another one
     * @param other given matrix to make the operation
     * @param fnc given operation
     * @return result matrix from the matrix's and operation
     */
    private Matrix elementWise(Matrix other, BiFunction<Double, Double, Double> fnc) {
        if (this.rows != other.rows || this.cols != other.cols) {
            throw new IllegalArgumentException("Incompatible matrix sizes for element wise.");
        }

        Matrix result = new Matrix(rows, cols);

        //add element by element
        //store the result in matrix result
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                result.data[i][j] = fnc.apply(this.data[i][j], other.data[i][j]);
        }
        return result;
    }

    /**
     * Addition of 2 matrixs
     * @param other matrix to add to the current matrix
     * @return the result matrix
     */
    public Matrix add(Matrix other) {
        return this.elementWise(other, (a, b) -> a + b);
    }

    /**
     * Multiplication of 2 matrixs
     * @param other matrix to multiply by the current matrix
     * @return the result matrix
     */
    public Matrix mult(Matrix other) {
        return this.elementWise(other, (a,b) -> a * b);
    }

    /**
     * Subtraction of 2 matrixs
     * @param other matrix to subtract to the current matrix
     * @return the result matrix
     */
    public Matrix sub(Matrix other) {
        return this.elementWise(other, (a,b) -> a - b);
    }


    //==============================================================
    //  Other math operations
    //==============================================================

    /**
     * Makes the sum of all values of the matrix
     * @return result of the sum
     */
    public double sum() {
        double total = 0.0;

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                total += this.data[i][j];
            }
        }

        return total;
    }


    /**
     * Makes a matrix where each value is the sum of all values of the original matrix's column
     * @return result matrix
     */
    public Matrix sumColumns() {
        Matrix result = new Matrix(1, this.cols);

        for (int j = 0; j < this.cols; j++) {
            double sum = 0.0;
            for (int i = 0; i < this.rows; i++)
                sum += this.data[i][j];
            result.data[0][j] = sum;
        }
        return result;
    }


    /**
     * Performs matrix multiplication (dot product) between the current matrix and another one
     * @param other matrix to multiply with the current matrix
     * @return resulting matrix from the dot product
     * @throws IllegalArgumentException if the matrices have incompatible dimensions
     */
    public Matrix dot(Matrix other) {
        if (this.cols != other.rows) {
            throw new IllegalArgumentException("Incompatible matrix sizes for multiplication.");
        }

        Matrix result = new Matrix(this.rows, other.cols);

        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < other.cols; j++) {
                for (int k = 0; k < this.cols; k++) {
                    result.data[i][j] += this.data[i][k] * other.data[k][j];
                }
            }
        }
        return result;
    }


    /**
     * Adds a row vector to every row of the current matrix.
     * @param rowVector row vector to add to each row of the matrix
     * @return resulting matrix after the row-wise addition
     * @throws IllegalArgumentException if the dimensions are incompatible
     */
    public Matrix addRowVector(Matrix rowVector) {
        if (rowVector.rows() != 1 || rowVector.cols() != this.cols) {
            throw new IllegalArgumentException("Incompatible sizes for adding row vector.");
        }

        Matrix result = new Matrix(this.rows, this.cols);

        //add row vector to each row
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.cols; j++) {
                result.data[i][j] = this.data[i][j] + rowVector.data[0][j];
            }
        }
        return result;
    }


    //==============================================================
    //  Column and row operations
    //==============================================================

    /**
     * Generates the transpose of the current matrix.
     * Rows become columns and columns become rows.
     *
     * @return result matrix
     */
    public Matrix transpose() {
        Matrix result = new Matrix(cols, rows);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result.data[j][i] = data[i][j];
            }
        }
        return result;
    }


    //==============================================================
    //  Convert operations
    //==============================================================

    /**
     * Converts the matrix into a formatted string representation,
     * where each value is shown with three decimal places and rows are separated by line breaks.
     *
     * @return string representation of the matrix
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (double[] row : data) {
            for (double val : row) {
                sb.append(String.format("%.3f ", val));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    

    //==============================================================
    //  Compare operations
    //==============================================================
    /**
     * Compares the current matrix with another object.
     * Two matrices are considered equal if they have the same dimensions
     * and contain identical values in all positions.
     *
     * @param o object to compare with the matrix
     * @return true if matrices are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Matrix matrix)) return false;
        return rows == matrix.rows && cols == matrix.cols && Objects.deepEquals(data, matrix.data);
    }

    /**
     * Generates a hash code for the matrix based on its dimensions and stored values.
     *
     * @return integer hash code representing the matrix
     */
    @Override
    public int hashCode() {
        return Objects.hash(Arrays.deepHashCode(data), rows, cols);
    }
}
