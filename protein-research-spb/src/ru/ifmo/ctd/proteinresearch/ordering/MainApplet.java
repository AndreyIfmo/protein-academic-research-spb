package ru.ifmo.ctd.proteinresearch.ordering;

/*

Jmol.jar needs to be in your classpath for this example to work.
You can get it from http://jmol.sourceforge.net

*/

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolSimpleViewer;
import org.jmol.api.JmolViewer;
import ru.ifmo.ctd.proteinresearch.ordering.gui.JmolExtensionPanel;


public class MainApplet {
    JmolSimpleViewer viewer;
    JPanel mainPanel;
    JmolPanel jmolPanel;
    JFrame frame;
    JmolExtensionPanel extension;

    public static void main(String[] args){
        try {
            MainApplet ex = new MainApplet();
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public MainApplet() {
        frame = new JFrame();
        frame.setTitle("Protein laboratory");
        mainPanel = new JPanel();
        extension = new JmolExtensionPanel(this);
        frame.addWindowListener(new ApplicationCloser());
        Container contentPane = frame.getContentPane();
        jmolPanel = new JmolPanel();
        jmolPanel.setPreferredSize(new Dimension(500,500));
        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        mainPanel.add(jmolPanel);
        gbc.gridy = 1;
        gbc.weighty=5;
        mainPanel.add(extension);
        contentPane.add(mainPanel);
        frame.pack();
        frame.setVisible(true);
    }
    public void setStructure(String fileName) {
        JmolViewer viewer = jmolPanel.getViewer();
        viewer.openFile(fileName);
        this.viewer = viewer;
    }

    public JmolViewer getViewer(){
        return jmolPanel.getViewer();
    }


    static class ApplicationCloser extends WindowAdapter {
        public void windowClosing(WindowEvent e) {
            System.exit(0);
        }
    }

    static class JmolPanel extends JPanel {
        /**
         *
         */
        private static final long serialVersionUID = -3661941083797644242L;
        JmolViewer viewer;
        JmolAdapter adapter;
        JmolPanel() {
            adapter = new SmarterJmolAdapter();
            viewer = JmolViewer.allocateViewer(this, adapter);

        }

        public JmolViewer getViewer() {
            return viewer;
        }

        final Dimension currentSize = new Dimension();
        final Rectangle rectClip = new Rectangle();

        public void paint(Graphics g) {
            getSize(currentSize);
            g.getClipBounds(rectClip);
            viewer.renderScreenImage(g, 500, 500);
        }
    }

}