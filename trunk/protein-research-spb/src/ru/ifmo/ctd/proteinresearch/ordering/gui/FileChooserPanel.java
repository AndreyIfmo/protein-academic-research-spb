package ru.ifmo.ctd.proteinresearch.ordering.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created by AndreyS on 18.03.14.
 */
public class FileChooserPanel extends JPanel {
    private JTextField fileTextField;
    private JButton openFileButton;

    public FileChooserPanel(String fileName) {
        openFileButton = new JButton(fileName);
        fileTextField = new JTextField();
        setLayout(new GridLayout());
        createChoseFilePanel(new GridBagConstraints());
    }
    private void createChoseFilePanel(final GridBagConstraints c) {
        c.insets=new Insets(10, 10, 10, 10);
        c.gridx=0;
        c.gridy=0;

        c.weightx=0.1;
        c.fill = GridBagConstraints.BOTH;
        add(openFileButton,c);
        c.gridx=1;
        c.weightx=1;
        add(fileTextField,c);
        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser myOpenFileButton = new JFileChooser();
                int ret = myOpenFileButton.showDialog(null, "Open file");
                if (ret == JFileChooser.APPROVE_OPTION) {
                    File file = myOpenFileButton.getSelectedFile();
                    fileTextField.setText(file.getPath());
                }
            }
        });
    }

    public String getFileName() {
        return fileTextField.getText();
    }
}
