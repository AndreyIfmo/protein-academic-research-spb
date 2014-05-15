package ru.ifmo.ctd.proteinresearch.ordering.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by AndreyS on 20.03.14.
 */
public class LabelTextField extends JPanel {

    public JLabel label;
    public JTextField textField;

    public LabelTextField(int width, String labelText) {
        setLayout(new GridLayout(1, 2));
        this.label = new JLabel(labelText);
        this.textField = new JTextField("0", width);
        textField.setColumns(width);
        add(this.label);
        add(textField);
    }

    public LabelTextField(String label) {
        this(20, label);
    }

}
