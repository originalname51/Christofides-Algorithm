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
	for(int i = 0; i < tour.size(); i++)
	{
		if(!result.contains(tour.get(i)))
		{
			result.add(tour.get(i));
		}
	}
	result.add(tour.get(0));
	result.add(tour.get(0));
	return result;
}
}
