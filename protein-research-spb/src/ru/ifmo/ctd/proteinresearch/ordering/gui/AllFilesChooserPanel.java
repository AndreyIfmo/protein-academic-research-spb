package ru.ifmo.ctd.proteinresearch.ordering.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by AndreyS on 18.03.14.
 */
public class AllFilesChooserPanel extends JPanel {
    private static JTextField filePatternTextField;
    FileChooserPanel[] panels = new FileChooserPanel[2];
    private static final String FILE_NAME_PATTERN = "2LJI_optim/2LJI_optim%d_%d.pdb";

    public AllFilesChooserPanel() {
        setLayout(new GridBagLayout());
        filePatternTextField = new JTextField(FILE_NAME_PATTERN);
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panels[0] = new FileChooserPanel("Open Matrix file");
        add(panels[0], c);
        panels[1] = new FileChooserPanel("Zip Archive file");
        c.gridy = 1;
        add(panels[1], c);
        c.weightx = 1;
        c.gridy = 2;
        add(createFilePatternPanel(), c);

    }

    private static JPanel createFilePatternPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(10, 10, 10, 10);

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(new JButton("Reset file pattern"), c);
        c.gridx = 1;
        panel.add(filePatternTextField, c);
        return panel;
    }

    public String getMatrixFile() {
        return panels[0].getFileName();
    }

    public String getArchiveFile() {
        return panels[1].getFileName();
    }

    public String getFilePattern() {
        return filePatternTextField.getText();
    }

}
