package ru.ifmo.ctd.proteinresearch.ordering.gui;

import org.biojava.bio.structure.StructureException;
import ru.ifmo.ctd.proteinresearch.ordering.graph.Path;
import ru.ifmo.ctd.proteinresearch.ordering.solvers.EdgeSwitchLimitationSolver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

/**
 * Created by AndreyS on 14.03.14.
 */
public class SmoothMotionsBuilderDialog extends JFrame {

    private AllFilesChooserPanel allFilesChooserPanel;
    EdgeSwitchLimitationSolver solver;
    public SmoothMotionsBuilderDialog() {
        solver = new EdgeSwitchLimitationSolver();
        initComponents();

    }

    private LabelTextField fromLabeledField;
    private JButton myBuildButton;
    private LabelTextField toLabeledField;


    private void initComponents() {
        GridBagConstraints c = new GridBagConstraints();

        fromLabeledField = new LabelTextField("from");
        toLabeledField = new LabelTextField("to");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Smooth Motions Builder");
        setLayout(new GridBagLayout());

        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets=new Insets(10, 10, 10, 10);

        createBuildButton();
        c.gridx = 0;
        c.gridy = 0;

        add(fromLabeledField, c);
        c.gridy++;
        add(toLabeledField, c);

        c.gridy++;
        JButton calc = new JButton("Calculate shortest smooth path");
        calc.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try (PrintWriter pdb = new PrintWriter("New.pdb")) {
                    int fromNum = Integer.parseInt(fromLabeledField.textField.getText());int toNum = Integer.parseInt(toLabeledField.textField.getText());
                    pdb.print(solver.cg.forPath(new Path(solver.get(fromNum,toNum), solver.weight(fromNum, toNum))));
                    System.out.println(Arrays.toString(solver.get(fromNum,toNum)));
                } catch (FileNotFoundException | StructureException e1) {
                    e1.printStackTrace();
                }
                solver.get(Integer.parseInt(fromLabeledField.textField.getText()), Integer.parseInt(toLabeledField.textField.getText()));
            }
        });
        add(calc,c);
        c.gridwidth=1;
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 1;
        c.weightx = 1;
        allFilesChooserPanel = new AllFilesChooserPanel();
        add(allFilesChooserPanel, c);
        c.gridx = 0;
        c.gridy ++;
        c.gridwidth = 1;
        c.weightx = 1;
        add(myBuildButton, c);
        setSize(600, 400);


    }

    public void createBuildButton() {
        myBuildButton = new JButton();
        myBuildButton.setText("Build!");
        myBuildButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    solver.evaluate(allFilesChooserPanel.getMatrixFile(), allFilesChooserPanel.getArchiveFile(), allFilesChooserPanel.getFilePattern(), 0.001, 10, 7);

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
    }


    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SmoothMotionsBuilderDialog().setVisible(true);
            }
        });
    }
}
