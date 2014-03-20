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
        createChoseFilePanel();
    }
    private void createChoseFilePanel() {
        add(openFileButton);
        add(fileTextField);
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
