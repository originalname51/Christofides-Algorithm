package main.java;

public class Edge {
	
	int parent = -1;
	int child  = -1;
	int weight = -1;
	Vertex Owner;
	Vertex Child;
	
public Edge(int p, int c, int w)
{
	parent = p;
	child  = c;
	weight = w;
	Owner = null;
	Child = null;
}

public Edge(int p, int c, int w, Vertex ov, Vertex cv)
{
	parent = p;
	child  = c;
	weight = w;
	Owner = ov;
	Child = cv;
}

public Edge(Vertex parentEdge, Vertex childEdge, int distance) {
	parent = parentEdge.getID();
	child = childEdge.getID();
	weight = distance;
	Owner = parentEdge;
	Child = childEdge;
}

public void setParentChild(Vertex p, Vertex c)
{
	 Owner = p;
	 Child = c;
}

}
