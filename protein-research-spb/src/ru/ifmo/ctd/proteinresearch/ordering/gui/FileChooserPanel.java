package ru.ifmo.ctd.proteinresearch.ordering.gui;



import org.jetbrains.annotations.NotNull;

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
    private JButton launchActionFromFile;

    public FileChooserPanel(String fileName) {
        openFileButton = new JButton(fileName);
        fileTextField = new JTextField();
        setLayout(new GridLayout());
        createChoseFilePanel();
    }

    public void setLaunchActionFromfile(@NotNull String label, @NotNull ActionListener listener) {
        launchActionFromFile = new JButton(label);
        launchActionFromFile.addActionListener(listener);
        add(launchActionFromFile);
    }

    private void createChoseFilePanel() {
        add(openFileButton);
        add(fileTextField);
        openFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser myOpenFileButton = new JFileChooser(System.getProperty("user.dir"));
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
