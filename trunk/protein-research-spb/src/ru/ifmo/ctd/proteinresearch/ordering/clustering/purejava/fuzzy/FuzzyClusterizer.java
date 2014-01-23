package ru.ifmo.ctd.proteinresearch.ordering.clustering.purejava.fuzzy;

/**
 * @author Andrey Sokolov {@link "mailto:ansokolmail@gmail.com"}
 *         Date: 19.01.14
 *         Time: 3:03
 */
public class FuzzyClusterizer {

    int numOfClusters;
    double[][] distanceMatrix;
    private int[] centers;
    public int[] answerCenters;
    public double exp;
    public double M;

    public FuzzyClusterizer(double[][] distanceMatrix, int numOfClusters) {
        this.distanceMatrix = distanceMatrix;
        this.numOfClusters = numOfClusters;
        this.answerCenters = new int[numOfClusters];
        exp = 2;
        M = 2;
    }

    /**
     * http://logic.pdmi.ras.ru/~sergey/teaching/ml/12-cluster2.pdf*
     */
    public double errorFunc(int[] clusterCenters) {
        double sum = 0;
        for (int i = 0; i < numOfClusters; i++) {
            for (int j = 0; j < distanceMatrix.length; j++) {
                sum += Math.pow(w(i, j), M) * Math.pow(distance(clusterCenters[i], j), M);
            }
        }
        return sum;
    }

    /**
     * Evaluates degree of  element belongs to cluster
     */  /*
    public double w(int clusterNumber, int elementNumber) {
        double answer = 0;
        double sum = 0;
        for (int i = 0; i < numOfClusters; i++) {
            sum += distance(centers[i], elementNumber);
        }
        answer = Math.pow((1 - distance(centers[clusterNumber], elementNumber) / sum), exp);
        double sumOfSimilarity = 0;
        for (int i = 0; i < numOfClusters; i++) {
            sumOfSimilarity += Math.pow(1 - distance(centers[i], elementNumber) / sum, exp);
        }
        return answer / sumOfSimilarity;
    }      */
    public double w(int clusterNumber, int elementNumber) {
        double answer = 0;
        double sum = 0;
        if (centers[clusterNumber] == elementNumber) {
            return 1;
        }
        for (int i = 0; i < numOfClusters; i++) {
            sum += Math.pow(distance(centers[clusterNumber], elementNumber) / distance(centers[i], elementNumber), 2 / (exp - 1));
        }
        answer = 1 / sum;

        return answer;
    }

    public double distance(int entityNumber1, int entityNumber2) {
        return distanceMatrix[entityNumber1][entityNumber2];
    }

    public double[][] evaluate() {
        double[][] similarity = new double[distanceMatrix.length][numOfClusters];
        double min = Double.MAX_VALUE;
        boolean[] vector = new boolean[distanceMatrix.length];
        do {
            if ((numOfElements(vector) == numOfClusters)) {
                centers = getCenters(vector);
                double errorFuncValue = errorFunc(centers);
                if (errorFuncValue < min) {
                    min = errorFuncValue;
                    for (int i = 0; i < distanceMatrix.length; i++) {
                        for (int j = 0; j < numOfClusters; j++) {
                            similarity[i][j] = w(j, i);
                        }
                    }
                    for (int i = 0; i < centers.length; i++) {
                        answerCenters[i] = centers[i];
                    }
                }
            }
        } while (next(vector));
        return similarity;
    }

    public boolean next(boolean[] vector) {
        int i = distanceMatrix.length - 1;
        while (vector[i] && i > 0) {
            vector[i] = false;
            i--;
        }
        if (i == 0 && vector[i]) {
            return false;
        }
        vector[i] = true;
        return true;
    }

    public int[] getCenters(boolean[] vector) {
        int[] centers = new int[numOfClusters];
        int counter = 0;
        for (int i = 0; i < vector.length; i++) {
            if (vector[i]) {
                centers[counter] = i;
                counter++;
            }
        }
        return centers;
    }

    public int numOfElements(boolean[] vector) {
        int counter = 0;
        for (int i = 0; i < vector.length; i++) {
            if (vector[i]) {
                counter++;
            }
        }
        return counter;
    }
}
