
public class Edge {
	
	int parent = -1;
	int child  = -1;
	int weight = -1;
	Vertex Owner;
	Vertex Child;
	boolean usedEdge;
	
public Edge(int p, int c, int w)
{
	parent = p;
	child  = c;
	weight = w;
	Owner = null;
	Child = null;
	usedEdge = false;
}

public void setParentChild(Vertex p, Vertex c)
{
	Vertex Owner = p;
	Vertex Child = c;
}

}
