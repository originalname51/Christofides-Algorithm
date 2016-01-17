import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.StringTokenizer;

public class mainProgram {

	public static void main(String[] args) throws IOException 
	{
		
		ArrayList<Vertex> theGraph = parseGraph(args[0]);
		
		int[][] distances = getDistances(theGraph);
		
		PrimsAlgorithm prim = new PrimsAlgorithm(theGraph, distances);
		ArrayList<Vertex> MinimumSpanningTree =  prim.Run();		

		ArrayList<Vertex> unitedList = min_weight_and_unite(MinimumSpanningTree, distances);
		
		Hierholzer Hierholzer = new Hierholzer(unitedList, distances);
		LinkedList<Vertex> eulerTour =     Hierholzer.run();		
		
		ShortCut answer = new ShortCut(eulerTour);
		ArrayList<Vertex> TSP = answer.run();
			
		FinalAnswer(TSP, distances, args[0]);
	}
	
static ArrayList<Vertex> parseGraph(String in) throws IOException
{
	BufferedReader br = new BufferedReader(new FileReader(in));	
	ArrayList<Vertex> graph = new ArrayList<Vertex>();
	String line;
	while ((line = br.readLine()) != null) 
	{
		StringTokenizer st = new StringTokenizer(line);
		int id,x,y;		
		id = Integer.parseInt(st.nextElement().toString());
		x = Integer.parseInt(st.nextElement().toString());
		y = Integer.parseInt(st.nextElement().toString());
		graph.add(new Vertex(id, x, y));
	}
	br.close();
	return graph;
}

	// function calculates distances between all points on the graph
	static int[][] getDistances(ArrayList<Vertex> graph) {
		int[][] distanceGraph = new int[graph.size()][graph.size()];
		for (int i = 0; i < graph.size(); i++) {
			for (int j = 0; j < graph.size(); j++) {
				distanceGraph[i][j] = difference(graph.get(i), graph.get(j));
			}
		}
		return distanceGraph;
	}

	// function that calculates the difference in location using A^2 + B^2 = C^2
	private static int difference(Vertex a, Vertex b) {
		int difference = (int) Math
				.round(Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2)));
		return difference;
	}
	
	/*
	This splits even and odds from each other and then creates a "perfect matching" (it doesn't actually, 
	but attempts something close to), and then reconnects the graph. Please note this creates a Eulerian Multigraph
	which means edges can be connected to each other twice. 2 edges "A-B" "A-B" can exist from the same Vertex.
	 * */
static ArrayList<Vertex> min_weight_and_unite(ArrayList<Vertex> MinimumSpanningTree, int [][] distances)
{
	ArrayList<Vertex> oddNumbers           = new ArrayList<Vertex>();		
	for(int i = 0; i < MinimumSpanningTree.size(); i++)
	{
		if(MinimumSpanningTree.get(i).connectedVertices.size() % 2 == 1)
		{
			oddNumbers.add(MinimumSpanningTree.get(i));
		}
	}

//This will create edges between two odd vertices.
	while(oddNumbers.isEmpty() == false)
	{
		int     distance      = Integer.MAX_VALUE;
		int     indexToRemove = -1;
		
		for(int i = 1; i < oddNumbers.size(); i++)
		{
			int checkDistance = distances[oddNumbers.get(0).getID()][oddNumbers.get(i).getID()];
			if(checkDistance < distance)
			{
				distance = checkDistance;
				indexToRemove = i;
			}
		}
		
<<<<<<< HEAD:src/mainProgram.java
		Edge fromZeroToPairEdge = new Edge(oddNumbers.get(0).getID(),oddNumbers.get(indexToRemove).getID(),distance, oddNumbers.get(0), oddNumbers.get(indexToRemove));
		Edge fromPairEdgeToZero = new Edge(oddNumbers.get(indexToRemove).getID(),oddNumbers.get(0).getID(),distance, oddNumbers.get(indexToRemove),oddNumbers.get(0));
		oddNumbers.get(0).connectedVertices.add(fromZeroToPairEdge);
		oddNumbers.get(indexToRemove).connectedVertices.add(fromPairEdgeToZero);

		oddNumbers.remove(indexToRemove);
		oddNumbers.remove(0);
		
	}
	
return MinimumSpanningTree;
=======
		Edge fromZeroToPairEdge = new Edge(oddNumbersUnpaired.get(0).getID(),oddNumbersUnpaired.get(indexToRemove).getID(),distance, oddNumbersUnpaired.get(0), oddNumbersUnpaired.get(indexToRemove));
		Edge fromPairEdgeToZero = new Edge(oddNumbersUnpaired.get(indexToRemove).getID(),oddNumbersUnpaired.get(0).getID(),distance, oddNumbersUnpaired.get(indexToRemove),oddNumbersUnpaired.get(0));
		
		oddNumbersUnpaired.get(0).connectedVertices.add(fromZeroToPairEdge);
		oddNumbersUnpaired.get(indexToRemove).connectedVertices.add(fromPairEdgeToZero);
		
		oddNumbersPaired.add(oddNumbersUnpaired.get(0));
		oddNumbersPaired.add(oddNumbersUnpaired.get(indexToRemove));
		
		oddNumbersUnpaired.remove(indexToRemove);
		oddNumbersUnpaired.remove(0);
	}
	
//combine the odd vertex's to even vertexes.	
	while(oddNumbersPaired.isEmpty() == false)
	{
		evenNumbers.add(oddNumbersPaired.get(0));
		oddNumbersPaired.remove(0);
	}

return evenNumbers;
>>>>>>> origin/master:src/mainProgram.java
}

static void FinalAnswer(ArrayList<Vertex> TSP, int [][] distances, String p)
{
	try {
		PrintWriter writer = new PrintWriter((p + ".tour"));
		int totalDistance = 0;
		
		int lineFormatting = 0;
		for(int i = 0; i < TSP.size()-1; i++)
		{

			System.out.print(TSP.get(i).getID()+ " ");
			if(lineFormatting ==20)
			{
				lineFormatting = 0;
				System.out.println();
			}
			lineFormatting++;
			
			totalDistance+= distances[TSP.get(i).getID()][TSP.get(i+1).getID()];
		}
		System.out.println();
		
		
		totalDistance+= distances[TSP.get(0).getID()][TSP.get(TSP.size()-1).getID()];
		System.out.println("Total distance covered is: " + totalDistance + "\n");
		writer.write(totalDistance);
		
		for(int i = 0; i < TSP.size(); i++)
		{
			writer.write(TSP.get(i).getID() + "\n");
		}
		writer.close();
	} 
	
	catch (FileNotFoundException e) {
		e.printStackTrace();
	}
}
}




