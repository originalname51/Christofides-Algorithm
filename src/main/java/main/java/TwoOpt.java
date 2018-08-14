package main.java;

import java.util.ArrayList;
import java.util.Date;

public class TwoOpt {
	int [] [] distances;
	ArrayList<Vertex> graph;
	double timeToRun;
	
	TwoOpt(ArrayList<Vertex> g, int [] [] d, double t)
	{
		distances    = d;
		graph        = g;
		timeToRun    = t;
	}
	
	
ArrayList<Vertex> runTwoOpt(boolean b)
{
	
	long startTime = System.currentTimeMillis();
	long elapsedTime = 0;
	double	howLongToRun = timeToRun/2.0;

	do
	{
		elapsedTime = (new Date()).getTime() - startTime;
		if(elapsedTime > howLongToRun * 60 * 1000)
		{
			break;
		}
		int bestDistance = calculateTotalDistance(graph);
		for(int i =1; i < graph.size()-1; i++)
		{
			for(int k = i+1; k < graph.size()-2; k++)
			{
				ArrayList<Vertex> new_route = TwoOptSwap(graph, i,k);
				int new_distance = calculateTotalDistance(new_route);
				if(new_distance < bestDistance)
				{
					graph = new_route;
					bestDistance = new_distance;
					if(b)
					{
					break;
					}
					elapsedTime = (new Date()).getTime() - startTime;
					if(elapsedTime > howLongToRun * 60 * 1000)
					{
						
						break;
					}
				}
				//I am checking time repeatedly to ensure I do not go over alloted time.
				elapsedTime = (new Date()).getTime() - startTime;
				if(elapsedTime > howLongToRun * 60 * 1000)
				{
					
					break;
				}
			} //end inner for loop
			
			elapsedTime = (new Date()).getTime() - startTime;
			if(elapsedTime > howLongToRun * 60 * 1000)
			{
				break;
			}
		}
		
	}while(elapsedTime < howLongToRun *60*1000); 
	
	return graph;
}
ArrayList<Vertex> run()
{	
	graph = runTwoOpt(true);
	graph = runTwoOpt(false);
	return graph;
}
	
ArrayList<Vertex> TwoOptSwap(ArrayList<Vertex> route, int i, int k)
{

	ArrayList<Vertex> newRoute = new ArrayList<Vertex>();
	
	for(int first = 0; first <= i-1; first++)
	{
		newRoute.add(route.get(first));	
	}
	
	for(int last = k; last >= i; last--)
	{
		newRoute.add(route.get(last));
	}
	
	for(int end = k+1; end <= route.size()-1; end++)
	{
		newRoute.add(route.get(end));
	}
	
	return newRoute;
}
	
	
	
int		calculateTotalDistance(ArrayList<Vertex> path)
	{
	int totalDistance = 0;
	for(int i = 0; i < path.size()-1; i++)
	{
		totalDistance+= distances[path.get(i).getID()][path.get(i+1).getID()];
	}
	totalDistance+= distances[path.get(0).getID()][path.get(path.size()-1).getID()];

	return totalDistance;
	}
}
