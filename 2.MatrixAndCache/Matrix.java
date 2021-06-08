package com.company;

import javax.swing.*;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Matrix {
    //@source: Haraken's skeleton code for initializing the 3 arrays and calculating execution time
    public static float matrixMultiply(int n) { //Method takes in "n" matrix dimensions (column size and row size are n)
        double[][] a = new double[n][n]; //Matrix A
        double[][] b = new double[n][n]; //Matrix B
        double[][] c = new double[n][n]; //Matrix C

        //Initialize the matrices to some values.
        int i, j;
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                a[i][j] = i * n + j;
                b[i][j] = j * n + i;
                c[i][j] = 0;
            }
        }

        long begin = System.currentTimeMillis(); //Gets start time

        /**** Write your own matrix multiply code****/
        int k;
        for (i = 0; i < n; i++) { //Loops until row size n is reached
            for (j = 0; j < n; j++) { //Loops until column size n is reached
                c[i][j] = 0; //Initializes matrix c's sum
                for (k = 0; k < n; k++) { //Loops until dimension n to ensure correct dot products of each row and column are calculated
                    c[i][j] += a[i][k] * b[k][j]; //Performs matrix multiplication
                }
            }
        }
        /**** Matrix multiply code ends here****/

        long end = System.currentTimeMillis(); //Gets end time
        float executiontime = end - begin; //Calculates execution time

        //Print C for debugging. Comment out the print before measuring the execution time.
        double sum = 0;
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                sum += c[i][j];
                //System.out.printf("c[%d][%d]=%f\n", i, j, c[i][j]);
            }
        }
        //Print out the sum of all values in C.
        //This should be 450 for N=3, 3680 for N=4, and 18250 for N=5.
        System.out.printf("input " + n);
        System.out.printf(" time: %.6f ms ", executiontime);
        System.out.printf(" sum: %.0f \n", sum);
        return executiontime;
    }

    /** Used to test matrix multiplication implementation */
    public static void main(String[] args) {

        //Haraken's tests
        System.out.println(matrixMultiply(3)); //Should print 450 under sum
        System.out.println(matrixMultiply(4)); //Should print 3680 under sum
        System.out.println(matrixMultiply(5)); //Should print 18250 under sum

        int n;
        double[] x = new double [500]; //Initializes array for n-input values
        double [] y = new double [500]; //Initializes array for execution time values
        for (n = 1;  n < 500; n++) { //Run matrixMultiply() for n = 1 to n = 500
            double executiontime = matrixMultiply(n);
            //Store input and execution time in an array for plotting graph
            x[n] = n;
            y[n] = executiontime;
        }

        //@source: https://wwwphy.princeton.edu//~meyers/phy209/2004/lab06/PlotPoints.java
        //Princeton CS scatter plot graphing program —— modified for homework

        //Initializes the panel where graph will be drawn
        JFrame frame = new JFrame("Matrix Multiplication Time Complexity");
        frame.setSize(500,500);
        frame.setLocation(100,100);

        //Deals with opening and closing panel window
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //Creates graph from arrays that store n-input (x) and execution (y)
        PlotPoints mainPane = new PlotPoints(x, y, 0., 500., 0., 100.);

        //Deals with panel display
        mainPane.setBackground(Color.white);
        frame.setContentPane(mainPane);
        frame.setVisible(true);
    }
}
