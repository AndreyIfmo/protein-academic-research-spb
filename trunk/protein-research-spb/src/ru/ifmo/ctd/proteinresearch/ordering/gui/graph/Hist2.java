package ru.ifmo.ctd.proteinresearch.ordering.gui.graph;

/**
 * Created by Andrey Sokolov on 16.05.2014.
 */


import java.awt.*;


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.IntervalXYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;
import ru.ifmo.ctd.proteinresearch.ordering.experiments.AngleExperiment;

public class Hist2 extends ApplicationFrame {

    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public Hist2(final String title, double[] distrbution) {
        super(title);
        IntervalXYDataset dataset = createDataset(distrbution);
        JFreeChart chart = createChart(dataset);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    public static void main(String[] args) throws Exception {

        double[] distrbution = AngleExperiment.runEdges("2LJI.properties", 1, 5);/*
        for (int i=0; i<distrbution.length; i++) {
            distrbution[i] = distrbution[i]>3?distrbution[i]:0;
        }*/
        Hist2 frame = new Hist2("Hi", distrbution);
        double sum = 0;
        for (double aDistrbution : distrbution) {
            sum += aDistrbution;
        }
        System.out.print(sum);
        frame.setSize(new Dimension(800,600));
        frame.setVisible(true);
    }

    /**
     * Creates a sample dataset.
     *
     * @return A sample dataset.
     */
    private IntervalXYDataset createDataset(double [] distrbution) {
        final XYSeries series = new XYSeries("Распределение");
        for (int i=0; i<distrbution.length; i++) {
            series.add(i,distrbution[i]);
        }
        return new XYSeriesCollection(series);
    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return A sample chart.
     */
    private JFreeChart createChart(IntervalXYDataset dataset) {
        final JFreeChart chart = ChartFactory.createXYBarChart(
                "Амплитуда улгов",
                "Номер Вершины",
                false,
                "",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        XYPlot plot = (XYPlot) chart.getPlot();
        final IntervalMarker target = new IntervalMarker(400.0, 700.0);
        target.setLabel("Target Range");
        target.setLabelFont(new Font("SansSerif", Font.ITALIC, 11));
        target.setLabelAnchor(RectangleAnchor.LEFT);
        target.setLabelTextAnchor(TextAnchor.CENTER_LEFT);
        target.setPaint(new Color(255, 255, 255));
        plot.addRangeMarker(target, Layer.BACKGROUND);
        return chart;
    }

}
