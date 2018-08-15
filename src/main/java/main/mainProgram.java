package main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.toIntExact;

public class mainProgram {

    public static void main(String[] args) throws IOException {
        String filenameThatWillBeParsed = args[0];
        int secondsToRunTwoOpt = args.length != 2 ? 0 : Integer.parseInt(args[1]);
        christofidesAlgorithm(filenameThatWillBeParsed, secondsToRunTwoOpt);
    }

    public static ChristofidesTour christofidesAlgorithm(String inputFilePath, int secondsToRunTwoOpt) throws IOException {
        Benchmark benchmark = new Benchmark();
        benchmark.startMark();
        List<Vertex> theGraph = parseGraph(inputFilePath);
        int[][] distances = getDistances(theGraph);
        List<Vertex> minimumSpanningTree = PrimsAlgorithm.run(theGraph, distances);
        createEvenlyVertexedEularianMultiGraphFromMST(minimumSpanningTree, distances);
        List<Vertex> eulerTour = HierholzerAlgorithm.run(minimumSpanningTree);
        List<Vertex> travelingSalesPath = ShortCut.run(eulerTour);
        TwoOpt twoOpt = new TwoOpt(travelingSalesPath, distances, secondsToRunTwoOpt);
        travelingSalesPath = twoOpt.run();
        ChristofidesTour finalAnswer = finalAnswer(travelingSalesPath, distances, inputFilePath);
        benchmark.endMark();
        System.out.println("Program took: " + benchmark.resultTime() + " ms");
        return finalAnswer;
    }

  private static List<Vertex> parseGraph(String fileName) throws IOException {
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            return stream.map(line -> {
                String brokenUpLine[] = line.trim().split("\\s+");  //trim leading whitespace before splitting on spaces.
                int id, x, y;
                id = Integer.parseInt(brokenUpLine[0]);
                x = Integer.parseInt(brokenUpLine[1]);
                y = Integer.parseInt(brokenUpLine[2]);
                return new Vertex(id, x, y);
            }).collect(Collectors.toCollection(ArrayList::new));
        }
    }

    // function calculates distances between all points on the graph
    private static int[][] getDistances(List<Vertex> graph) {
        int[][] distanceGraph = new int[graph.size()][graph.size()];
        for (int i = 0; i < graph.size(); i++) {
            for (int j = 0; j < graph.size(); j++) {
                distanceGraph[i][j] = difference(graph.get(i), graph.get(j));
            }
        }
        return distanceGraph;
    }

    // function that calculates the difference in location using A^2 + B^2 = C^2
    private static int difference(Vertex a, Vertex b) {
        long difference = Math
                .round(Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2)));
        return toIntExact(difference);
    }

    /*
    This splits even and odds from each other and then creates a "perfect matching" (it doesn't actually,
    but attempts something close to), and then reconnects the graph. Please note this creates a Eulerian Multigraph
    which means edges can be connected to each other twice. 2 edges "A-B" "A-B" can exist from the same main.java.Vertex.
     * */
    private static List<Vertex> createEvenlyVertexedEularianMultiGraphFromMST(List<Vertex> minimumSpanningTree, int[][] distances) {

        List<Vertex> oddNumbers = minimumSpanningTree
                .stream()
                .filter(vertex -> vertex.connectedVertices.size() % 2 == 1)
                .collect(Collectors.toCollection(ArrayList::new));

        while (!oddNumbers.isEmpty()) {
            int distance = Integer.MAX_VALUE;
            final Vertex parent = oddNumbers.get(0);
            oddNumbers.remove(parent);
            int minDistanceToNextNode = oddNumbers
                    .stream()
                    .mapToInt(vertex -> distances[parent.getID()][vertex.getID()])
                    .min()
                    .getAsInt();
            Vertex child = oddNumbers
                    .stream()
                    .filter(vertex -> distances[parent.getID()][vertex.getID()] == minDistanceToNextNode)
                    .findFirst()
                    .get();

            Edge fromParentToChildEdge = new Edge(parent, child, distance);
            Edge fromChildToParentEdge = new Edge(child, parent, distance);
            parent.connectedVertices.add(fromParentToChildEdge);
            child.connectedVertices.add(fromChildToParentEdge);
            oddNumbers.remove(parent);
            oddNumbers.remove(child);
        }
        return minimumSpanningTree;
    }

    private static ChristofidesTour finalAnswer(List<Vertex> TSP, int[][] distances, String p) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter((p + ".tour"))){
            int lineFormatting = 0;
            for (Vertex vertex : TSP) {
                System.out.print(vertex.getID() + " ");
                if (lineFormatting == 20) {
                    lineFormatting = 0;
                    System.out.println();
                }
                lineFormatting++;
            }
            System.out.println();

            int totalDistance = TSP.stream()
                    .mapToInt
                            (vertex -> TSP.indexOf(vertex) == TSP.size() - 1 ?
                                    distances[TSP.get(0).getID()][TSP.get(TSP.size() - 1).getID()] //logic to connect end node to start node.
                                  : distances[vertex.getID()][TSP.get(TSP.indexOf(vertex) + 1).getID()])
                    .sum();
            System.out.println("Total distance covered of the " + TSP.size() + " vertices is: " + totalDistance);
            writer.println(totalDistance);

            List<Integer> finalTour = TSP
                    .stream()
                    .map(vertex -> {
                        writer.println(vertex.getID());
                        return vertex.getID();
                    }).collect(Collectors.toCollection(ArrayList::new));
            return new ChristofidesTour(finalTour, totalDistance);
        }
    }
}




