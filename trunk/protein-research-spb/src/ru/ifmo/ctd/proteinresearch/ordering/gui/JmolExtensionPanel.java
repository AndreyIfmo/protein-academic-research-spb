package ru.ifmo.ctd.proteinresearch.ordering.gui;

import ru.ifmo.ctd.proteinresearch.ordering.MainApplet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by AndreyS on 13.05.2014.
 */
public class JmolExtensionPanel extends JPanel {
    FileChooserPanel fileToDrawPanel;
    ProteinPanel proteinPanel;

    public JmolExtensionPanel(final MainApplet applet) {
        setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weighty = 1;
        fileToDrawPanel = new FileChooserPanel("Open File in viewer");
        fileToDrawPanel.setLaunchActionFromfile("Draw!", new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                applet.setStructure(fileToDrawPanel.getFileName());
            }
        });
        proteinPanel = new ProteinPanel();
        add(fileToDrawPanel, constraints);
        constraints.gridy=1;
        add(proteinPanel, constraints);
        constraints.gridy=2;
    }


    public String getFileToDraw() {
        return fileToDrawPanel.getFileName();
    }
}
