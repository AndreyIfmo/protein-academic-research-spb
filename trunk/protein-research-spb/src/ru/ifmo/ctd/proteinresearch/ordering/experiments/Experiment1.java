package ru.ifmo.ctd.proteinresearch.ordering.experiments;

import ru.ifmo.ctd.proteinresearch.ordering.solvers.EdgeSwitchLimitationSolver;
import ru.ifmo.ctd.proteinresearch.ordering.util.PropertiesParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * ansokolmail@gmail.com
 */
public class Experiment1 {
    public void run(String propertiesFileName) throws Exception {

        EdgeSwitchLimitationSolver esls = new EdgeSwitchLimitationSolver(propertiesFileName);
        double thresoldValue = PropertiesParser.getThresoldValue(propertiesFileName);
        List<Integer> bannedVertices = new ArrayList<>();
        for (int i=0; i<esls.getN(); i++) {
            bannedVertices.add(i);
        }
        for (int i = 0; i < esls.getN(); i++) {
            bannedVertices.remove(i);
            esls.run(thresoldValue, bannedVertices);
            int [][][] paths = esls.getPaths();
            for (int j= 0; j< i+1; j++) {
                for (int k= 0; k< i+1; k++) {
                    int [] path = paths[j][k];
                    System.out.println(Arrays.toString(path));
                }
            }
            System.out.println();

        }

    }
    public static void main(String[] args) throws Exception {
        new Experiment1().run("2LJI.properties");
    }
}
