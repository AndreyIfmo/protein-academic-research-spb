package ru.ifmo.ctd.proteinresearch.ordering;

/*

Jmol.jar needs to be in your classpath for this example to work.
You can get it from http://jmol.sourceforge.net

*/

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.biojava.bio.structure.Structure;
import org.biojava.bio.structure.io.PDBFileReader;
import org.jmol.adapter.smarter.SmarterJmolAdapter;
import org.jmol.api.JmolAdapter;
import org.jmol.api.JmolSimpleViewer;
import org.jmol.api.JmolViewer;


public class SimpleJmolExample {
    JmolSimpleViewer viewer;
    Structure structure;

    JmolPanel jmolPanel;
    JFrame frame ;

    public static void main(String[] args){
        try {

            PDBFileReader pdbr = new PDBFileReader();
            File f = new File("tmp");
            pdbr.setPath(f.getCanonicalPath().replace("tmp", ""));

            String pdbCode = "Result_optim";

            Structure struc = pdbr.getStructureById(pdbCode);

            SimpleJmolExample ex = new SimpleJmolExample();
            ex.setStructure(struc);


        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public SimpleJmolExample() {
        frame = new JFrame();
        frame.addWindowListener(new ApplicationCloser());
        Container contentPane = frame.getContentPane();
        jmolPanel = new JmolPanel();

        jmolPanel.setPreferredSize(new Dimension(500,500));
        contentPane.add(jmolPanel);

        frame.pack();
        frame.setVisible(true);

    }
    public void setStructure(Structure s) {

        frame.setName(s.getPDBCode());

        // actually this is very simple
        // just convert the structure to a PDB file

        String pdb = s.toPDB();

        structure = s;
        JmolViewer viewer = jmolPanel.getViewer();

        // Jmol could also read the file directly from your file system
        viewer.openFile("Result_optim.pdb");

        // send the PDB file to Jmol.
        // there are also other ways to interact with Jmol, but they require more
        // code. See the link to SPICE above...

        this.viewer = viewer;

    }

    public void setTitle(String label){
        frame.setTitle(label);
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

        public void executeCmd(String rasmolScript){
            viewer.evalString(rasmolScript);
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