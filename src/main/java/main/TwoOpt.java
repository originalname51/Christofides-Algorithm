package main;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class TwoOpt {
  private int[][] distances;
  private List<Vertex> graph;
  private int secondsToRun;
  private LocalDateTime start;

    TwoOpt(List<Vertex> g, int[][] d, int t) {
        distances = d;
        graph = g;
        secondsToRun = t/2; // divide by two as impl has two opt running twice.
    }

   private List<Vertex> runTwoOpt(boolean twoOptIterationB) {
        start = LocalDateTime.now();
        do {
            if (timeIsUp()) {
                break;
            }
            int bestDistance = calculateTotalDistance(graph);
            for (int i = 1; i < graph.size() - 1; i++) {
                for (int k = i + 1; k < graph.size() - 2; k++) {
                    ArrayList<Vertex> newRoute = TwoOptSwap(graph, i, k);
                    int newDistance = calculateTotalDistance(newRoute);
                    if (newDistance < bestDistance) {
                        graph = newRoute;
                        bestDistance = newDistance;
                        if (twoOptIterationB) {
                            break;
                        }
                        if (timeIsUp()) {
                            break;
                        }
                    }
                    if (timeIsUp()) {
                        break;
                    }
                }
                if (timeIsUp()) {
                    break;
                }
            }
        } while (!timeIsUp());

        return graph;
    }

    private boolean timeIsUp() {
        long elapsedTime;
        elapsedTime = ChronoUnit.SECONDS.between(start, LocalDateTime.now());
        return elapsedTime >= secondsToRun;
    }

    public List<Vertex> run() {
        graph = runTwoOpt(true);
        graph = runTwoOpt(false);
        return graph;
    }

   private ArrayList<Vertex> TwoOptSwap(List<Vertex> route, int i, int k) {

        ArrayList<Vertex> newRoute = new ArrayList<>();

        for (int first = 0; first <= i - 1; first++) {
            newRoute.add(route.get(first));
        }

        for (int last = k; last >= i; last--) {
            newRoute.add(route.get(last));
        }

        for (int end = k + 1; end <= route.size() - 1; end++) {
            newRoute.add(route.get(end));
        }

        return newRoute;
    }


   private int calculateTotalDistance(List<Vertex> path) {
        int totalDistance = 0;
        for (int i = 0; i < path.size() - 1; i++) {
            totalDistance += distances[path.get(i).getID()][path.get(i + 1).getID()];
        }
        totalDistance += distances[path.get(0).getID()][path.get(path.size() - 1).getID()];

        return totalDistance;
    }
}
