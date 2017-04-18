package cz.jan.maly.main;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.core.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class EmExample {

    public static void main(String[] args) throws Exception {
        // load data
        Instances data = getTrainingSet();

        List<Instances> list = java.util.Arrays.asList(data, data, data, data);
        List<String> ems = list.parallelStream()
                .map(instances -> {
                    System.out.println("---Starting---");
                    // build clusterer
                    EM clusterer = new EM();
                    try {
                        clusterer.setMaxIterations(20);
                        clusterer.setNumKMeansRuns(5);
                        clusterer.buildClusterer(data);

                        // evaluate clusterer
                        ClusterEvaluation eval = new ClusterEvaluation();
                        eval.setClusterer(clusterer);
                        eval.evaluateClusterer(data);
                        return eval.clusterResultsToString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return "";
                }).collect(Collectors.toList());

        // print results
        ems.forEach(System.out::println);
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
