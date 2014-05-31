package ru.ifmo.ctd.proteinresearch.ordering.gui.graph;


import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import ru.ifmo.ctd.proteinresearch.ordering.experiments.Experiment1;

import java.awt.*;
import java.io.File;
import java.util.Arrays;

public class FunctionGraphFrame extends JFrame {

    public FunctionGraphFrame() throws Exception {
        super("Tick Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Experiment1.Answer answer = Experiment1.runEdges(fileName + ".properties");
        ChartPanel chartPanel = (ChartPanel) createDemoPanelForEdges(answer);
        setContentPane(chartPanel);
        setVisible(true);
        setSize(new java.awt.Dimension(800, 800));
    }

    public static void main(String[] argv) throws Exception {
        new FunctionGraphFrame();
    }

    String fileName = "2M2Y";
    static double graphValue(double d) {
        return Math.abs(d)<0.001?0:0;
    }
    public JPanel createDemoPanelForEdges(Experiment1.Answer answer) throws Exception {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries edgesData = new XYSeries("Edges");
        XYSeries bannedEdgesData = new XYSeries("Banned Edges");

        XYSeries verticesData = new XYSeries("Vertices");


        for (int i = 0; i < answer.rangesBetweenReferenceAndEdges.length; i++) {
            System.out.println(i + " " + answer.rangesBetweenReferenceAndEdges[i] + " " +i + " " + answer.rangesBetweenReferenceAndVertices[i]);
            edgesData.add(i + 1, graphValue(answer.rangesBetweenReferenceAndEdges[i]));
            verticesData.add(i+1, graphValue(answer.rangesBetweenReferenceAndVertices[i]));
            if (answer.rangesBetweenReferenceAndBannedEdges[i]< Double.MAX_VALUE) {
                bannedEdgesData.add(i + 1, graphValue(answer.rangesBetweenReferenceAndBannedEdges[i]));
            }
        }

        dataset.addSeries(edgesData);
        dataset.addSeries(verticesData);
        dataset.addSeries(bannedEdgesData);
        System.out.println(Arrays.toString(answer.rangesBetweenReferenceAndEdges));
        System.out.println(Arrays.toString(answer.rangesBetweenReferenceAndVertices));
        System.out.println(Arrays.toString(answer.rangesBetweenReferenceAndBannedEdges));

        JFreeChart chart = ChartFactory.createXYLineChart(
                "RMSD GRAPH",  // title
                "Number of used vertices",             // x-axis label
                "RMSD",   // y-axis label
                dataset,            // data
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                true,
                true,               // tooltips
                false               // no URLs
        );


        ChartUtilities.saveChartAsPNG(new File(fileName + ".png"), chart, 800, 800);
        return new ChartPanel(chart);
    }



} //end class TickTest