package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

/** Plots the line graph showing execution time as a function of n-input */
//@source: https://wwwphy.princeton.edu//~meyers/phy209/2004/lab06/PlotPoints.java
//Princeton CS plotting program  --  modified for homework
public class PlotPoints extends JPanel {
    final static float dash1[] = {10.0f};
    final static BasicStroke dashed = new BasicStroke(1.0f,
            BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER,
            10.0f, dash1, 0.0f);
    //PA = Plot Area, as fraction of Panel size.
    final static double PA_XO = 0.15, PA_YO = 0.10; // Origin
    final static double PA_XS = 0.80, PA_YS = 0.80; // Size

    final static double dotSize = 5.; //Plotted point size, in pixels

    //Input data
    int nPts;
    double[] x, y, err;

    //Plot area in user coordinates (x-y, not pixel, space)
    double xMin, xMax, yMin, yMax;

    //Constructor with user-defined plot area
    PlotPoints(double[] xdata, double[] ydata,
               double xMn, double xMx, double yMn, double yMx) {
        nPts = xdata.length;
        x = new double[nPts];
        y = new double[nPts];
        err = new double[nPts];
        for(int i=0; i<nPts; i++){
            x[i] = xdata[i];
            y[i] = ydata[i];
        }
        xMin = xMn;
        xMax = xMx;
        yMin = yMn;
        yMax = yMx;
    }

    /** Passes in graphics drawing it on the panel
     * Automatically gets called when a panel is created
     * */
    public void paintComponent(Graphics g) {

        super.paintComponent(g);  //Paint background
        Graphics2D g2 = (Graphics2D) g; //Cast Graphics object to Graphics2D

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        Dimension d = getSize();

        //Defines the corners of the plot area in pixels
        int iMin = (int) (PA_XO * d.width);
        int iMax = (int) ((PA_XO + PA_XS) * d.width);
        int jMin = (int) (PA_YO * d.height);
        int jMax = (int) ((PA_YO + PA_YS) * d.height);

        //Define the scaling between the plot area in x-y and the plot area in pixels
        double xScale = (double) (iMax - iMin) / (xMax - xMin);
        double yScale = (double) (jMax - jMin) / (yMax - yMin);

        //Draws x-axis and y-axis
        g2.setColor(Color.black); //Sets color of axes to black
        g2.draw(new Rectangle2D.Double(PA_XO * d.width, PA_YO * d.height,
                PA_XS * d.width, PA_YS * d.height));

        double ix;
        double iy;
        for (int i = 0; i < nPts; i++) {
            //Draws the points (JPanel y starts at TOP of the panel)
            ix = iMin + ((x[i] - xMin) * xScale);
            iy = jMax - ((y[i] - yMin) * yScale);
            g2.setColor(Color.red); //Sets color of data points to red
            g2.fill(new Ellipse2D.Double(ix - dotSize / 2., iy - dotSize / 2.,
                    dotSize, dotSize));

        }

        // Adds labels of xMin, xMax, yMin, yMax to drawing and positions them on the screen
        // String.valueOf(xMin) turns the number xMin into a String that you can draw.
        g2.setColor(Color.black);
        g2.drawString("Input Size (n)", (float) (PA_XO * d.width + 150),
                (float) ((PA_YO + PA_YS) * d.height + 20));
        g2.drawString("Time (ms)", (float) (PA_XO * d.width - 70),
                (float) ((PA_YO + PA_YS) * d.height - 200));
        g2.drawString(String.valueOf(xMin), (float) (PA_XO * d.width - 10.),
                (float) ((PA_YO + PA_YS) * d.height + 15.));
        g2.drawString(String.valueOf(xMax),
                (float) ((PA_XO + PA_XS) * d.width - 20.),
                (float) ((PA_YO + PA_YS) * d.height + 15.));
        g2.drawString(String.valueOf(yMin), (float) (PA_XO * d.width - 50.),
                (float) ((PA_YO + PA_YS) * d.height + 5.));
        g2.drawString(String.valueOf(yMax), (float) (PA_XO * d.width - 50.),
                (float) (PA_YO * d.height + 5.));
    }
}
