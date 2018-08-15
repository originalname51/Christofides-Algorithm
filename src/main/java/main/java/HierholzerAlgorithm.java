package main.java;

import java.util.ArrayList;
import java.util.LinkedList;

public class HierholzerAlgorithm {
    private HierholzerAlgorithm() {
    }

    public static LinkedList<Vertex> run(ArrayList<Vertex> vertexGraph) {
        LinkedList<Vertex> firstPath;
        LinkedList<Vertex> currentTour = new LinkedList<>(vertexGraph);
        firstPath = new LinkedList<>();
        while (currentTour.get(0).connectedVertices.size() != 0) {
            LinkedList<Vertex> returnedPath = returnAPath(currentTour.get(0));
            firstPath = runHelper(returnedPath);
        }
        return firstPath;
    }

    private static LinkedList<Vertex> runHelper(LinkedList<Vertex> firstPath) {
        for (int i = 0; i < firstPath.size(); i++) {
            if (firstPath.get(i).connectedVertices.size() > 0) {
                LinkedList<Vertex> addToPath;
                addToPath = returnAPath(firstPath.get(i));
                int indexSaved = i + 1;
                int addPathSize = addToPath.size();
                for (int j = 1; j < addPathSize; j++) {
                    firstPath.add(indexSaved, addToPath.get(0));
                    addToPath.remove(0);
                }
            }
        }

        return firstPath;
    }


    private static LinkedList<Vertex> returnAPath(Vertex pathStart) {
        LinkedList<Vertex> returnValue = new LinkedList<>();
        Vertex pathFinish = null;
        Vertex firstNode = pathStart;
        while (firstNode != pathFinish) {
            pathFinish = pathStart.connectedVertices.get(0).Child;
            pathStart.connectedVertices.remove(0);
            //remove the parent edge from child node
            for (int i = 0; i < pathFinish.connectedVertices.size(); i++) {
                if (pathFinish.connectedVertices.get(i).child == pathStart.getID()) {
                    pathFinish.connectedVertices.remove(i);
                    break;
                }
            }
            returnValue.add(pathStart);
            pathStart = pathFinish;
        }
        returnValue.add(pathFinish); //This needs to be added to "complete" the loop.
        return returnValue;
    }

}
