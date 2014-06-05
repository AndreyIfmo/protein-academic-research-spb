package ru.ifmo.ctd.proteinresearch.ordering.gui.graph;

/**
 * Created by Andrey Sokolov on 16.05.2014.
 */


import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
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

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * This demo shows a simple bar chart created using the {@link XYSeriesCollection} dataset.
 *
 */
public class HistogramFrame extends ApplicationFrame {

    /**
     * Creates a new demo instance.
     *
     * @param title  the frame title.
     */
    public HistogramFrame(final String title, int[] distrbution) throws IOException {
        super(title);
        IntervalXYDataset dataset = createDataset(distrbution);
        JFreeChart chart = createChart(dataset);
        ChartUtilities.saveChartAsPNG(new File(title + ".png"), chart, 800, 800);
        final ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
    }

    /**
     * Creates a sample dataset.
     *
     * @return A sample dataset.
     */
    private IntervalXYDataset createDataset(int [] distrbution) {
        final XYSeries series = new XYSeries("Распределение");
        for (int i=0; i<distrbution.length; i++) {
            series.add(i,distrbution[i]);
        }
        final XYSeriesCollection dataset = new XYSeriesCollection(series);
        return dataset;
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
                "Количество попаданий вершины, как промежуточной на траекторию движения",
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
        target.setPaint(new Color(222, 222, 255, 128));
        plot.addRangeMarker(target, Layer.BACKGROUND);
        return chart;
    }

}
