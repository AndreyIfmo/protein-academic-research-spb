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
        textArea = new JTextArea(30,30);
        gbc.gridy=1;
        gbc.weighty=1;
        add(textField, gbc);
        gbc.gridy=2;
        gbc.weighty=3;
        add(new JScrollPane(textArea), gbc);

        buildButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EdgeSwitchLimitationSolver esls = new EdgeSwitchLimitationSolver(panel.cg);
                try {
                    esls.run(Double.parseDouble(textField.textField.getText()));
                    int[][][]paths = esls.getPaths();
                    String answer="";
                    for (int i = 0; i < esls.cg.graph.getN(); i++) {
                        for (int j = 0; j < esls.cg.graph.getN(); j++) {
                            answer+=(Arrays.toString(paths[i][j])+"\n");
                        }
                    }
                    textArea.setText(answer);
                } catch (Exception e1) {
                   e1.printStackTrace();
                }
            }
        });

    }
}
