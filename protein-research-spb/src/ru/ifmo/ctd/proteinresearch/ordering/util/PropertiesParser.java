package ru.ifmo.ctd.proteinresearch.ordering.util;

import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationGraph;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by AndreyS on 06.05.2014.
 */
public class PropertiesParser {
    public static ConformationGraph getGraphData(String fileName) throws IOException {
        return getGraphData(fileName, false);
    }
    public static ConformationGraph getGraphData(String fileName, boolean isInApplet) throws IOException {
        Properties proteinProperties = new Properties();
        File file = new File(fileName);
        proteinProperties.load(new FileInputStream(file));
        String banned = proteinProperties.getProperty("banned");
        String indexOffsetString = proteinProperties.getProperty("indexOffset");
        int indexOffset = 0;
        if (indexOffsetString != null) {
            indexOffset = Integer.parseInt(indexOffsetString);
        }
        String prefix=isInApplet?file.getParentFile().getAbsolutePath() + "/":"";
        return new ConformationGraph(prefix+proteinProperties.getProperty("matrix"),
                prefix+proteinProperties.getProperty("archive"),
                proteinProperties.getProperty("mask"), indexOffset, banned != null ? banned.split(",") : new String[0]);
    }

    public static double getThresoldValue(String fileName) throws IOException {
        Properties proteinProperties = new Properties();
        proteinProperties.load(new FileInputStream(fileName));
        String thresold = proteinProperties.getProperty("thresold");
        return thresold!=null?Double.parseDouble(thresold):0;
    }
}
