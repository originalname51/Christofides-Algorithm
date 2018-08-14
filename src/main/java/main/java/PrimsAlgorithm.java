package main.java;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class PrimsAlgorithm {
	
	int [] [] distances;
	ArrayList<Vertex> unsortedList;
	
	PrimsAlgorithm(ArrayList<Vertex> g, int [][] distance)
	{
		distances = distance;
		unsortedList = g;
	}
	
/*
1: Initialize a tree with a single vertex, chosen arbitrarily from the graph. The vertex choosen in unsortedList(index 0)
2: Grow the tree by one edge: of the edges that connect the tree to vertices not yet in the tree, 
find the minimum-weight edge, and transfer it to the tree.
3: Repeat step 2 (until all vertices are in the tree).
* */	
ArrayList<Vertex>  Run()
{
	ArrayList<Vertex> sortedList     = new ArrayList<Vertex>();
	Comparator<Vertex> compareMethod = new VertexComparator();
	
//This will initialize all vertex shortest edge to the initial base vertex. This is important because it creates a new edge for all
// vertices. It is required as it seeds the next part of the algorithm (the while loop)
// It satisfies the "Initialize of the tree with a single vertex" part of Prim's Algorithm.

	for(int i = 0; i < unsortedList.size(); i++)
	{	
		unsortedList.get(i).edge = 
		new Edge(unsortedList.get(i).getID(), unsortedList.get(0).getID(), distances[unsortedList.get(i).getID()][unsortedList.get(0).getID()], unsortedList.get(i), unsortedList.get(0));
		//main.java.Edge constructor is parentID, ChildID, Weight
	}

	Collections.sort(unsortedList, compareMethod);


//This implements prim's algorithm. It "pops" off and adds to the tree the minimum edge and then 
//sorts and checks each edge to make sure it is the smallest.
//Sort is run BEFORE the new weights are assigned to insure that the new graph will be connected.
//This is mainly because the initial initialization happens before the while loop.
	while(!unsortedList.isEmpty())
	{	
	    unsortedList.get(0).connectedVertices.add(unsortedList.get(0).edge); 
   
	    
	    //The following just adds a childEdge helping the graph stay connected.
	    Edge parentEdge = unsortedList.get(0).edge;
	    if(sortedList.size() > 0)
	    {	
	    	for(int i = 0; i < sortedList.size(); i++)
	    	{
	    		if(parentEdge.child == sortedList.get(i).getID())
	    		{
	    			Edge childEdge = new Edge(parentEdge.child, parentEdge.parent, parentEdge.weight, parentEdge.Child, parentEdge.Owner);
	    			sortedList.get(i).connectedVertices.add(childEdge);
	    		}
	    	}
	    }
	    else //special case for root node
	    {
	    	unsortedList.get(0).connectedVertices.remove(0);
	    	
	    	Edge updateEdge = unsortedList.get(1).edge;
	    	unsortedList.get(0).edge.parent = unsortedList.get(0).getID();
	    	unsortedList.get(0).edge.child = updateEdge.parent;
	    	unsortedList.get(0).edge.Owner = unsortedList.get(0);
	    	unsortedList.get(0).edge.Child = unsortedList.get(1);	    	
	    	unsortedList.get(0).edge.weight = updateEdge.weight;
	    	unsortedList.get(0).connectedVertices.add(unsortedList.get(0).edge);
	    	Edge childEdge = new Edge(unsortedList.get(0).edge.child, unsortedList.get(0).edge.parent, unsortedList.get(0).edge.weight, unsortedList.get(0).edge.Child, unsortedList.get(0).edge.Owner);
	    	unsortedList.get(1).connectedVertices.add(childEdge);
	    	
	    }
	    
		sortedList.add(unsortedList.get(0));		
		unsortedList.remove(0);
		Collections.sort(unsortedList, compareMethod); //lowest weight connected to MST comes to the top

		for(int i = 1; i < unsortedList.size(); i++)
		{
			 int checkNewWeight = distances[unsortedList.get(i).getID()][unsortedList.get(0).getID()];
			 
			 if(checkNewWeight < unsortedList.get(i).edge.weight)
			 {
				 unsortedList.get(i).edge.child = unsortedList.get(0).getID();
				 unsortedList.get(i).edge.Child = unsortedList.get(0);
				 unsortedList.get(i).edge.weight = checkNewWeight;
			 }
		}		
	}
	return sortedList;
}


}
