package ru.ifmo.ctd.proteinresearch.ordering.gui.clusterzingResults;

import ru.ifmo.ctd.proteinresearch.ordering.graph.Graph;
import ru.ifmo.ctd.proteinresearch.ordering.graph.GraphParser;
import ru.ifmo.ctd.proteinresearch.ordering.gui.FileChooserPanel;
import ru.ifmo.ctd.proteinresearch.ordering.gui.GraphVizUtils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by AndreyS on 21.03.14.
 */
public class ClusterizerPanel extends JPanel {
    private static final int HGAP =10;
    private static final int WGAP =10;
    JPanel bottomPanel;
    JPanel topPanel;
    private FileChooserPanel fileChooserPanel;

    public ClusterizerPanel() {
        setLayout(new BorderLayout(HGAP, WGAP));
        buildBottomPanel();
        //GraphModel
    }
    private void buildBottomPanel() {
        topPanel = new JPanel();
        bottomPanel = new JPanel();
        fileChooserPanel = new FileChooserPanel("Open matrix file");
        bottomPanel.add(fileChooserPanel);
        JButton drawButton = new JButton("Draw clusters");
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String fileName = fileChooserPanel.getFileName();
                    Graph graph = GraphParser.parseMatrixGraphFromFile(fileName);
                    String jpgFileName = fileName + ".jpg";
                    String dotFileName = fileName + ".dot";
                    GraphVizUtils.graphToDotFile(graph, dotFileName);
                    String command = "neato -Tjpg " + dotFileName + " -o " + jpgFileName;

                    Runtime.getRuntime().exec(command);
                    final BufferedImage image;

                    image = ImageIO.read(new File(jpgFileName));
                    topPanel = new JPanel() {

                        @Override
                        protected void paintComponent(Graphics g) {
                            super.paintComponent(g);
                            if (image != null) {
                                g.drawImage(image, 0, 0, null);
                            }
                        }

                    };
                } catch (Exception ex) {
                    throw new RuntimeException(ex.getMessage());
                }
            }
        });
        bottomPanel.add(drawButton);
        add(topPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

}
