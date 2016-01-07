import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

public class Hierholzer 
{
	ArrayList<Vertex> vertexGraph;
	int [][] distances;
	Hierholzer(ArrayList<Vertex> v, int [] [] d)
	{
		vertexGraph = v;
		distances = d;
	}
	
	
	LinkedList<Vertex> run()
{
		boolean graphConnected = false;
		LinkedList<Vertex> firstPath = new LinkedList<Vertex>();

		while (graphConnected == false) 
		{

			LinkedList<Vertex> currentTour = new LinkedList<Vertex>(vertexGraph);
			ArrayList<Edge>  edgeList = new ArrayList<Edge>();
			for(int i = 0; i < currentTour.size(); i++)
			{
				for(int j = 0; j < currentTour.get(i).connectedVertices.size(); j++)
				{
					Edge copyEdge1 = new Edge(currentTour.get(i).connectedVertices.get(j).parent,currentTour.get(i).connectedVertices.get(j).child, currentTour.get(i).connectedVertices.get(j).weight);
					copyEdge1.Owner = currentTour.get(i);
					for(int k = 0; k < currentTour.size(); k++)
					{
						if(copyEdge1.child == currentTour.get(k).getID())
						{
							copyEdge1.Child = currentTour.get(k);
							break;
						}
					}
					edgeList.add(copyEdge1);
				}
			}

			firstPath = new LinkedList<Vertex>();

			while (currentTour.get(0).connectedVertices.size() != 0) {
				firstPath = returnAPath(currentTour.get(0));
				firstPath = runHelper(firstPath);
			}
			
			
			graphConnected = true;
			ArrayList<Vertex> needsConnection = new ArrayList<Vertex>();
			for(int i = 0; i < currentTour.size(); i++)
			{
				boolean foundit = false;
				for(int j = 0; j < firstPath.size(); j++)		
				{
					if(currentTour.get(i).getID() == firstPath.get(j).getID())
					{
						foundit = true;
					}
				}
				if(foundit == false)
				{
					System.out.println("ID : " + currentTour.get(i).getID() + " not in tour");
					graphConnected = false;
					needsConnection.add(currentTour.get(i));
				}
			}
			
			if(graphConnected == false)
			{
				
				if(needsConnection.size() %2 == 1)
				{
					needsConnection.add(currentTour.get(0));
				}
				
				ArrayList<Integer> firstNumberConnections = new ArrayList<Integer>();
				for(int i = 0; i < needsConnection.get(0).connectedVertices.size(); i++)
				{
					firstNumberConnections.add(needsConnection.get(0).connectedVertices.get(i).child);
				}
				
				int  minDistance      = Integer.MAX_VALUE;
			
				int  jIndex      	  = -1;
				
				for(int i = 1; i < needsConnection.size(); i++)
				{
													
					if(distances[needsConnection.get(0).getID()][needsConnection.get(i).getID()] < minDistance )
						{
							jIndex 	  = i;	
							minDistance = distances[needsConnection.get(0).getID()][needsConnection.get(i).getID()];
						}
						
					}
				
				Edge vertexIDOneConnector 			= new Edge(needsConnection.get(0).getID(), needsConnection.get(jIndex).getID(), distances[needsConnection.get(0).getID()][needsConnection.get(jIndex).getID()]);
				Edge vertexIDOneConnectorNumberTwo	= new Edge(needsConnection.get(jIndex).getID(), needsConnection.get(0).getID(), distances[needsConnection.get(0).getID()][needsConnection.get(jIndex).getID()]);

				vertexIDOneConnector.Owner = needsConnection.get(0);
				vertexIDOneConnector.Child = needsConnection.get(jIndex);
				
				vertexIDOneConnectorNumberTwo.Owner = needsConnection.get(jIndex);
				vertexIDOneConnectorNumberTwo.Child = needsConnection.get(0);
				
				edgeList.add(vertexIDOneConnector);
				edgeList.add(vertexIDOneConnectorNumberTwo);
				
				
				int connector = -1;
				int distance  = Integer.MAX_VALUE;
				for(int i = 0; i < firstPath.size(); i++)
				{
					int check = distances[firstPath.get(i).getID()][needsConnection.get(jIndex).getID()] + distances[firstPath.get(i).getID()][needsConnection.get(0).getID()];
					if(check < distance && !firstNumberConnections.contains(firstPath.get(i).getID()))
					{
						distance = check;
						connector = i;
						System.out.println("i'm here");					
						}
					
				}
				
				Edge vertexToGraph1 = new Edge(needsConnection.get(0).getID(), firstPath.get(connector).getID(), distances[needsConnection.get(0).getID()][firstPath.get(connector).getID()] );
				Edge vertexToGraph2 = new Edge(needsConnection.get(jIndex).getID(), firstPath.get(connector).getID(), distances[needsConnection.get(jIndex).getID()][firstPath.get(connector).getID()] ); 
				
				Edge graphToVertex1  =  new Edge(firstPath.get(connector).getID(), needsConnection.get(0).getID(), distances[needsConnection.get(0).getID()][firstPath.get(connector).getID()] );
				Edge graphToVertex2  =  new Edge(firstPath.get(connector).getID(), needsConnection.get(jIndex).getID(), distances[needsConnection.get(jIndex).getID()][firstPath.get(connector).getID()] );
				
				
				vertexToGraph1.Owner = needsConnection.get(0);
				vertexToGraph1.Child = firstPath.get(connector);
				
				vertexToGraph1.Owner = needsConnection.get(jIndex);
				vertexToGraph1.Child = firstPath.get(connector);
				
				graphToVertex1.Owner = firstPath.get(connector);
				graphToVertex1.Child = needsConnection.get(0);
				graphToVertex2.Owner = firstPath.get(connector);
				graphToVertex2.Child = needsConnection.get(jIndex);
				
				edgeList.add(vertexToGraph1);
				edgeList.add(vertexToGraph2);
				edgeList.add(graphToVertex1);
				edgeList.add(graphToVertex2);
				
				
				for(int i = 0; i < vertexGraph.size(); i++)
				{
					vertexGraph.get(i).connectedVertices.clear();
					for(int j = 0; j < edgeList.size(); j++)
					{
						if(edgeList.get(j).parent == vertexGraph.get(i).getID())
						{
							vertexGraph.get(i).connectedVertices.add(edgeList.get(j));
						}
					}
				}
			
					
				}

				
				
			
			
				
			
			
			
			
		}
	
	return firstPath;
}

boolean graphConnected(LinkedList<Vertex> testkey, LinkedList<Vertex> masterKey)
{
boolean graphConnected = true;	

for(int i = 0; i < masterKey.size(); i++)
{
	boolean foundit = false;
	for(int j = 0; j < testkey.size(); j++)		
	{
		if(masterKey.get(i).getID() == testkey.get(j).getID())
		{
			foundit = true;
		}
	}
	if(foundit == false)
	{
		System.out.println("ID : " + masterKey.get(i).getID() + " not in tour");
		graphConnected = false;
		break;
	}
}



return graphConnected;

}
	
LinkedList<Vertex>   runHelper(LinkedList<Vertex> firstPath)
{
	
	boolean boolEmpty = false;
	while(boolEmpty == false)
	{
		for(int i = 0; i < firstPath.size(); i++)
		{
			if(firstPath.get(i).connectedVertices.size() > 0)
			{
				LinkedList<Vertex> addToPath = new LinkedList<Vertex>();
				addToPath = returnAPath(firstPath.get(i));
				int indexSaved = i+1;
				int addPathSize = addToPath.size();
				for(int j = 1; j < addPathSize; j++)
				{
					firstPath.add(indexSaved, addToPath.get(0));
					addToPath.remove(0);
				}
			}
		}
		boolean makeTrue = true;
		for(int i = 1; i < firstPath.size(); i++)
		{
			if(firstPath.get(i).connectedVertices.size() > 0)
			{
				makeTrue = false;
			}
		}
		boolEmpty = makeTrue;
	}
	
	return firstPath;
}
	
	//Needs to always be able to come home.
LinkedList<Vertex>   returnAPath(Vertex pathStart)
{
	LinkedList<Vertex> returnValue = new LinkedList<Vertex>();
	
	Vertex pathFinish = pathStart.connectedVertices.get(0).Child;
	Vertex holder = pathStart;
	while(holder != pathFinish)
	{
		pathFinish = pathStart.connectedVertices.get(0).Child;
		
		System.out.println(pathStart.connectedVertices.get(0).child);

		pathStart.connectedVertices.remove(0);
		
		
		
		for(int i = 0; i < pathFinish.connectedVertices.size(); i++)
		{
			if(pathFinish.connectedVertices.get(i).child == pathStart.getID())
			{
				pathFinish.connectedVertices.remove(i);
				break;
			}
		}
		
		returnValue.add(pathStart);
		pathStart = pathFinish;
	}
	returnValue.add(pathFinish);
	
	return returnValue;
}
	
}
