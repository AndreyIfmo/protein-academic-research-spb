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

import java.io.File;

public class FunctionGraphFrame extends JFrame {

    public FunctionGraphFrame() throws Exception {
        super("Tick Demo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ChartPanel chartPanel = (ChartPanel) createDemoPanel();
        setContentPane(chartPanel);
        setVisible(true);
        setSize(new java.awt.Dimension(800,800));
    }

    public static void main(String[] argv) throws Exception { new FunctionGraphFrame(); }
    String fileName="1BTB";
    public JPanel createDemoPanel() throws Exception {
        XYSeriesCollection dataset=new XYSeriesCollection();
        XYSeries xys=new XYSeries((Comparable)(new Double(0.0)));
      double[]ans=Experiment1.run(fileName+".properties");

        for (int i=1; i< ans.length; i++) {
            System.out.println(i+" " +ans[i]);
            xys.add(i,ans[i]);
        }

        dataset.addSeries(xys);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "XYLineChart",  // title
                "Angle (degrees)",             // x-axis label
                "Low Amplitude Data",   // y-axis label
                dataset,            // data
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                false,               // no legend
                true,               // tooltips
                false               // no URLs
        );



        ChartUtilities.saveChartAsPNG(new File(fileName+".png"),chart, 800, 800);
        return new ChartPanel(chart);
    }

} //end class TickTest