package ru.ifmo.ctd.proteinresearch.ordering.gui;


import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationGraph;
import ru.ifmo.ctd.proteinresearch.ordering.util.PropertiesParser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

/**
 * Created by AndreyS on 13.05.2014.
 */
public class ProteinPanel extends JPanel {
    ConformationGraph cg;
    FileChooserPanel fileChooserPanel;
    ProteinEvaluationPanel proteinPanel;
    String fileName;
    boolean isComputed;
    public ProteinPanel() {
        setLayout(new GridBagLayout());
        fileChooserPanel = new FileChooserPanel("Open properties file");
        fileChooserPanel.setLaunchActionFromfile("Compute", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    fileName = fileChooserPanel.getFileName();
                    if(new File(fileName).isFile()) {
                        cg = PropertiesParser.getGraphData(fileName);
                        proteinPanel.textField.textField.setText("" + PropertiesParser.getBorderValue(fileName));
                        isComputed = true;
                    } else {
                        JOptionPane.showMessageDialog(proteinPanel, "file name is not correct");
                    }
                } catch (IOException e1) {
                    throw new RuntimeException(e1);
                }
            }
        });
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weightx=1;
        gbc.weighty=1;
        add(fileChooserPanel, gbc);
        proteinPanel = new ProteinEvaluationPanel(this);
        gbc.gridy=1;
        gbc.weighty=5;
        add(proteinPanel, gbc);

    }
}
