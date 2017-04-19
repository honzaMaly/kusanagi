package cz.jan.maly.main;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.core.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EMExample {

    public static void main(String[] args) throws Exception {
        // load data
        Instances data = getTrainingSet();

        EM clusterer = new EM();
        clusterer.setMaxIterations(20);
        clusterer.setNumKMeansRuns(5);
        clusterer.buildClusterer(data);

        // evaluate clusterer
        ClusterEvaluation eval = new ClusterEvaluation();
        eval.setClusterer(clusterer);
        eval.evaluateClusterer(data);


        // print results
        System.out.println(eval.clusterResultsToString());
    }

    private static Instances getTrainingSet() {

        // Declare two numeric attributes
        Attribute Attribute1 = new Attribute("x");
        Attribute Attribute2 = new Attribute("y");

        // Declare the feature vector
        FastVector fvWekaAttributes = new FastVector(2);
        fvWekaAttributes.addElement(Attribute1);
        fvWekaAttributes.addElement(Attribute2);

        Instances dataRaw = new Instances("TestInstances", fvWekaAttributes, 0);

        //read file into stream, try-with-resources
        try (Scanner scanner = new Scanner(new File("D31.txt"))) {
            while (scanner.hasNext()) {
                String[] split = scanner.nextLine().split("\t");

                // Create the instance
                Instance iExample = new DenseInstance(2);
                iExample.setValue((Attribute) fvWekaAttributes.elementAt(0), Double.valueOf(split[0]));
                iExample.setValue((Attribute) fvWekaAttributes.elementAt(1), Double.valueOf(split[1]));

                // add the instance
                dataRaw.add(iExample);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataRaw;
    }
}
