package main;

public class Edge {
	
	int parent;
	int child;
	int weight;
	Vertex Owner;
	Vertex Child;

public Edge(Vertex parentEdge, Vertex childEdge, int distance) {
	parent = parentEdge.getID();
	child = childEdge.getID();
	weight = distance;
	Owner = parentEdge;
	Child = childEdge;
}

}
