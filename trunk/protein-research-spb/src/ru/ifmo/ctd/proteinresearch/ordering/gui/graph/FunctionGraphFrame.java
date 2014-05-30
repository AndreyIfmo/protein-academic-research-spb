package ru.ifmo.ctd.proteinresearch.ordering.gui.graph;


import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import ru.ifmo.ctd.proteinresearch.ordering.experiments.Experiment1;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.io.File;

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

    String fileName = "2LJI";

    public JPanel createDemoPanelForEdges(Experiment1.Answer answer) throws Exception {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries edgesData = new XYSeries((Comparable) (new Double(0.0)));
        XYSeries bannedEdgesData = new XYSeries((Comparable) (new Double(0.0)));

        XYSeries verticesData = new XYSeries((Comparable) (new Double(0.0)));


        for (int i = 1; i < answer.rangesBetweenReferenceAndEdges.length; i++) {
            System.out.println(i + " " + answer.rangesBetweenReferenceAndEdges[i] + " " +i + " " + answer.rangesBetweenReferenceAndVertices[i]);
            edgesData.add(i + 1, answer.rangesBetweenReferenceAndEdges[i]);
            verticesData.add(i+1, answer.rangesBetweenReferenceAndVertices[i]);
            if (answer.rangesBetweenReferenceAndBannedEdges[i]< Double.MAX_VALUE) {
                bannedEdgesData.add(i + 1, answer.rangesBetweenReferenceAndBannedEdges[i]);
            }
        }

        dataset.addSeries(edgesData);
        dataset.addSeries(verticesData);
        dataset.addSeries(bannedEdgesData);

        JFreeChart chart = ChartFactory.createXYLineChart(
                "RMSD GRAPH",  // title
                "Number of used vertices",             // x-axis label
                "RMSD",   // y-axis label
                dataset,            // data
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                false,               // no legend
                true,               // tooltips
                false               // no URLs
        );


        ChartUtilities.saveChartAsPNG(new File(fileName + ".png"), chart, 800, 800);
        return new ChartPanel(chart);
    }

    public JPanel createDemoPanelForVertices(Experiment1.Answer answer) throws Exception {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries xys = new XYSeries((Comparable) (new Double(0.0)));

        for (int i = 1; i < answer.rangesBetweenReferenceAndVertices.length; i++) {
            System.out.println(i + " " + answer.rangesBetweenReferenceAndVertices[i]);
            xys.add(i + 1, answer.rangesBetweenReferenceAndVertices[i]);
        }

        dataset.addSeries(xys);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "RMSD GRAPH",  // title
                "Number of used vertices",             // x-axis label
                "RMSD",   // y-axis label
                dataset,            // data
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                false,               // no legend
                true,               // tooltips
                false               // no URLs
        );


        ChartUtilities.saveChartAsPNG(new File(fileName + ".png"), chart, 800, 800);
        return new ChartPanel(chart);
    }

} //end class TickTest