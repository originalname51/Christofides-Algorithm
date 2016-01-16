import java.util.ArrayList;
import java.util.LinkedList;

public class ShortCut {
	LinkedList<Vertex> tour;
	ArrayList<Vertex> result;
	
	
	ShortCut(LinkedList<Vertex> t)
	{
		tour = t;
		result = new ArrayList<Vertex>();
	}
	
ArrayList<Vertex> run()
{
	ArrayList<Vertex> tor = new ArrayList<Vertex>(tour);
	for(int i = 0; i < tor.size(); i++)
	{
		if(!result.contains(tour.get(i)))
		{
			result.add(tour.get(i));
		}
	}
	
	return result;
}
}
