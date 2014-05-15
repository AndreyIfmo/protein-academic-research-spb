package ru.ifmo.ctd.proteinresearch.ordering.gui;

import ru.ifmo.ctd.proteinresearch.ordering.solvers.EdgeSwitchLimitationSolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

/**
 * Created by AndreyS on 15.05.2014.
 */
public class ProteinEvaluationPanel extends JPanel {
    public JButton buildButton;
    public JTextArea textArea;
    public LabelTextField textField;
    public ProteinPanel mainPanel;
    public ProteinEvaluationPanel(final ProteinPanel panel) {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        mainPanel=panel;
        buildButton = new JButton("Build graph of paths");
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.weightx=1;
        gbc.weighty=1;
        add(buildButton, gbc);
        textField = new LabelTextField(10, "Border");
        textArea = new JTextArea();

        textArea.setEditable(false);
        textArea.setColumns(30);
        textArea.setRows(30);
        textArea.setPreferredSize(new Dimension(500,500));
        textArea.setMinimumSize(new Dimension(500,500));
        gbc.gridx=1;
        gbc.gridy=0;
        gbc.weighty=1;
        add(textField, gbc);
        gbc.gridy=2;
        gbc.weighty=3;
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setMinimumSize(textArea.getMinimumSize());

        add(scrollPane, gbc);

        buildButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EdgeSwitchLimitationSolver esls = new EdgeSwitchLimitationSolver(panel.cg);
                try {
                    esls.run(Double.parseDouble(textField.textField.getText()));
                    int[][][]paths = esls.getPaths();
                    int n = esls.cg.graph.getN();
                    textArea.setRows(textArea.getRows()+n*n);
                    for (int i = 0; i < n; i++) {
                        for (int j = 0; j < n; j++) {
                            textArea.append(Arrays.toString(paths[i][j])+"\n");
                        }
                    }

                } catch (Exception e1) {
                   e1.printStackTrace();
                }
            }
        });

    }
}
