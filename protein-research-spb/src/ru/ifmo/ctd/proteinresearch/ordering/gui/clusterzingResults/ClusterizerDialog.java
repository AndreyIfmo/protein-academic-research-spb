package ru.ifmo.ctd.proteinresearch.ordering.gui.clusterzingResults;

import javax.swing.*;
import java.awt.*;

/**
 * Created by AndreyS on 21.03.14.
 */
public class ClusterizerDialog extends JFrame{
    public ClusterizerDialog() throws HeadlessException {
        add(new ClusterizerPanel());
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(600, 400);

    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClusterizerDialog().setVisible(true);
            }
        });
    }
}
