package ru.ifmo.ctd.proteinresearch.ordering.gui.graph;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import ru.ifmo.ctd.proteinresearch.ordering.experiments.Experiment1;

import javax.swing.*;
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

    String fileName = "2LJI";
    static double graphValue(double d) {
        return Math.abs(d)<0.001?0:d;
    }
    public JPanel createDemoPanelForEdges(Experiment1.Answer answer) throws Exception {
        XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries edgesData = new XYSeries("До частичного графа траекторий");
        XYSeries bannedEdgesData = new XYSeries("Banned Edges");

        XYSeries verticesData = new XYSeries("До базовых конформаций в этом графе");


        for (int i = 1; i < answer.rangesBetweenReferenceAndEdges.length-1; i++) {
            System.out.println(i + " " + answer.rangesBetweenReferenceAndEdges[i] + " " +i + " " + answer.rangesBetweenReferenceAndVertices[i]);
            edgesData.add(i + 1, graphValue(answer.rangesBetweenReferenceAndEdges[i]));
            verticesData.add(i+1, graphValue(answer.rangesBetweenReferenceAndVertices[i]));
            if (answer.rangesBetweenReferenceAndBannedEdges[i]< Double.MAX_VALUE) {
                double value = graphValue(answer.rangesBetweenReferenceAndBannedEdges[i]);
                if (value>0) {
                    bannedEdgesData.add(i + 1, value);
                }
            }
        }

        dataset.addSeries(edgesData);
        dataset.addSeries(verticesData);
      //  dataset.addSeries(bannedEdgesData);
        System.out.println(Arrays.toString(answer.rangesBetweenReferenceAndEdges));
        System.out.println(Arrays.toString(answer.rangesBetweenReferenceAndVertices));
        System.out.println(Arrays.toString(answer.rangesBetweenReferenceAndBannedEdges));

        JFreeChart chart = ChartFactory.createXYLineChart(
                "",  // title
                "Количество вершин",             // x-axis label
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