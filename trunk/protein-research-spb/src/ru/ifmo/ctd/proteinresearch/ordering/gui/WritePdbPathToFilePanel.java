package ru.ifmo.ctd.proteinresearch.ordering.gui;


import ru.ifmo.ctd.proteinresearch.ordering.graph.Path;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationGraph;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.PathUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


/**
 * Created by Andrey Sokolov on 16.05.2014.
 */
public class WritePdbPathToFilePanel  extends JPanel{
    public final ProteinPanel mainPanel;
    public final LabelTextField path;

    public final LabelTextField outputFilename;
    public final JButton buildButton;
    public WritePdbPathToFilePanel(final ProteinPanel mainPanel) {
        this.mainPanel=mainPanel;
        path = new LabelTextField("Path:");
        outputFilename = new LabelTextField("Output File:");
        outputFilename.textField.setText("Result.pdb");
        buildButton = new JButton("Build!");
        buildButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = path.textField.getText();
                String [] vertices = text.split(",");
                int [] indexes = new int[vertices.length];
                for (int i=0; i< vertices.length; i++) {
                    try {
                        indexes [i]=Integer.parseInt(vertices[i].trim());

                    } catch (NumberFormatException e1) {
                        JOptionPane.showMessageDialog(mainPanel, "can not read path");
                    }
                }
                ConformationGraph cg = mainPanel.cg;
                try {
                    PathUtil.buildPDB(cg.matrixFileName, cg.zipArchive, cg.fileNamePattern, cg.indexOffset, outputFilename.textField.getText(), new Path(
                            indexes,
                            Double.NaN));
                } catch (Exception e1) {
                    JOptionPane.showMessageDialog(mainPanel, "path was not computed");
                }

            }
        });
        setLayout(new GridLayout(4, 1));
        add(path);
        add(outputFilename);
        add(buildButton);

    }
}
