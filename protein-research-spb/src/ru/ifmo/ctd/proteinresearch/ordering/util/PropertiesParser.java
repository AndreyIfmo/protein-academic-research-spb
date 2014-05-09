package ru.ifmo.ctd.proteinresearch.ordering.util;

import ru.ifmo.ctd.proteinresearch.ordering.solvers.ConformationGraph;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by AndreyS on 06.05.2014.
 */
public class PropertiesParser {
    public static ConformationGraph getGraphData (String fileName) throws IOException {
        Properties proteinProperties = new Properties();
        proteinProperties.load(new FileInputStream(fileName));
        String banned=proteinProperties.getProperty("banned");
        String indexOffsetString = proteinProperties.getProperty("indexOffset");
        int indexOffset = 0;
        if (indexOffsetString != null) {
            indexOffset = Integer.parseInt(indexOffsetString);
        }
        return new ConformationGraph(proteinProperties.getProperty("matrix"),
        proteinProperties.getProperty("archive"),
        proteinProperties.getProperty("mask"), indexOffset ,banned!=null? banned.split(","):new String[0]);
    }
}
