package main.java;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class mainProgram {

    public static void main(String[] args) throws IOException {
        String filenameThatWillBeParsed = args[0];
        ChristofidesAlgorithm(filenameThatWillBeParsed);
    }

    public static ChristofidesTour ChristofidesAlgorithm(String arg) throws IOException {
        Benchmark benchmark = new Benchmark();

        benchmark.startMark();
        ArrayList<Vertex> theGraph = parseGraph(arg);
        int[][] distances = getDistances(theGraph);
        ArrayList<Vertex> MinimumSpanningTree = PrimsAlgorithm.run(theGraph, distances);
        createEvenlyVertexedEularianMultiGraphFromMST(MinimumSpanningTree, distances);
        LinkedList<Vertex> eulerTour =  HierholzerAlgorithm.run(MinimumSpanningTree);
        ArrayList<Vertex> TSP = ShortCut.run(eulerTour);

//        double howManyMinutesToRunTSP = 0.25;
//        TwoOpt TwoOpt = new TwoOpt(TSP,distances,howManyMinutesToRunTSP);
//        TSP = TwoOpt.run();
//
        ChristofidesTour finalAnswer =  FinalAnswer(TSP, distances, arg);
        benchmark.endMark();
        System.out.println("Program took: " + benchmark.resultTime() + " ms");
        return  finalAnswer;
    }

    static ArrayList<Vertex> parseGraph(String in) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(in));
        ArrayList<Vertex> graph = new ArrayList<Vertex>();
        String line;
        while ((line = br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line);
            int id, x, y;
            id = Integer.parseInt(st.nextElement().toString());
            x = Integer.parseInt(st.nextElement().toString());
            y = Integer.parseInt(st.nextElement().toString());
            graph.add(new Vertex(id, x, y));
        }
        br.close();
        return graph;
    }

    // function calculates distances between all points on the graph
    private static int[][] getDistances(ArrayList<Vertex> graph) {
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
        int difference = (int) Math
                .round(Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2)));
        return difference;
    }

    /*
    This splits even and odds from each other and then creates a "perfect matching" (it doesn't actually,
    but attempts something close to), and then reconnects the graph. Please note this creates a Eulerian Multigraph
    which means edges can be connected to each other twice. 2 edges "A-B" "A-B" can exist from the same main.java.Vertex.
     * */
    static ArrayList<Vertex> createEvenlyVertexedEularianMultiGraphFromMST(ArrayList<Vertex> minimumSpanningTree, int[][] distances) {

        ArrayList<Vertex> oddNumbers = minimumSpanningTree
                .stream()
                .filter(vertex -> vertex.connectedVertices.size() % 2 == 1)
                .collect(Collectors.toCollection(ArrayList::new));

        while (oddNumbers.isEmpty() == false) {
            int distance = Integer.MAX_VALUE;
            final Vertex parent = oddNumbers.get(0);
            oddNumbers.remove(parent);
            int minDistance = oddNumbers
                    .stream()
                    .mapToInt(vertex -> distances[parent.getID()][vertex.getID()])
                    .min()
                    .getAsInt();
            Vertex child = oddNumbers
                    .stream()
                    .filter(vertex -> distances[parent.getID()][vertex.getID()] == minDistance)
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

    static ChristofidesTour FinalAnswer(ArrayList<Vertex> TSP, int[][] distances, String p) {
        ChristofidesTour christofidesTour = new ChristofidesTour();
        List<Integer> finalTour = new ArrayList<Integer>();
        int totalDistance = 0;

        try {
            PrintWriter writer = new PrintWriter((p + ".tour"));
            int lineFormatting = 0;
            for (int i = 0; i < TSP.size() - 1; i++) {

                System.out.print(TSP.get(i).getID() + " ");
                if (lineFormatting == 20) {
                    lineFormatting = 0;
                    System.out.println();
                }
                lineFormatting++;

                totalDistance += distances[TSP.get(i).getID()][TSP.get(i + 1).getID()];
            }
            System.out.println();


            totalDistance += distances[TSP.get(0).getID()][TSP.get(TSP.size() - 1).getID()];
            System.out.println("Total distance covered of the " + TSP.size() + " vertices is: " + totalDistance);

            writer.println(totalDistance);
            for (int i = 0; i < TSP.size(); i++) {
                finalTour.add(TSP.get(i).getID());
                writer.println(TSP.get(i).getID());
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        christofidesTour.setFinalTour(finalTour);
        christofidesTour.setTourCost(totalDistance);
        return christofidesTour;
    }
}




