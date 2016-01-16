import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

//this class acts as a test harness to check for the correctness of the 4 different find max sub-array algorithms
public class project4 {

	public static void main(String[] args) throws IOException 
	{
		
//		String output = args[0] + ".txt.tour";
		
		
		String path = args[0];
		
		Vertex[] theGraph = parseGraph(path);
		
		int[][] distances = getDistances(theGraph);
		
		PrimsAlgorithm prim = new PrimsAlgorithm(theGraph, distances);
		ArrayList<Vertex> MinimumSpanningTree =  prim.Run();		
		
		ArrayList<Vertex> unitedList = min_weight_and_unite(MinimumSpanningTree, distances);
		updateEdges(unitedList);
		
		Hierholzer Hierholzer = new Hierholzer(unitedList, distances);
		LinkedList<Vertex> eulerTour =     Hierholzer.run();		
		System.out.println("total size of tour is : " + eulerTour.size());	
		
		ShortCut answer = new ShortCut(eulerTour);
		
		ArrayList<Vertex> TSP = answer.run();
			
		System.out.println("Total size of the TSP is: " + TSP.size());
		FinalAnswer(TSP, distances, args[0]);
	
	}
	
/*
 * intakes string containing file name. Take the data from the file 
 * and then returns a string of the vertex's in the graph.
 * */
static Vertex [] parseGraph(String in) throws IOException
{
	BufferedReader br = new BufferedReader(new FileReader(in));	

	String line;

	int numLines = 0;

	// while loop collects the number of lines present in the file of
	// interest
	while ((line = br.readLine()) != null) {

		// prevents empty lines from breaking the program logic
		if (line.length() == 0) {
			break;
		}
		// increment the number of lines present
		numLines++;
	}
	// buffered reader is closed and reset by creating a new buffered reader
	br.close();
	br = new BufferedReader(new FileReader(in));

	// create an array of vertices the size of the input file's lines
	Vertex[] theGraph = new Vertex[numLines];

	int currentAdd = 0;

	while ((line = br.readLine()) != null) {

		// prevents empty lines from breaking the program logic
		if (line.length() == 0) {
			break;
		}

		// the string is split by white spaces
		String[] splitLine = line.split(" +");

		// iterate through the number of discrete entities on a line

		// the individual vertices are constructed and added to the graph
		if (splitLine.length >= 3) 
		{
			Vertex temp;
			if (splitLine[0].length() > 0) {
				temp = new Vertex(Integer.parseInt(splitLine[0]), Integer.parseInt(splitLine[1]),
						Integer.parseInt(splitLine[2]));
			} else {
				temp = new Vertex(Integer.parseInt(splitLine[1]), Integer.parseInt(splitLine[2]),
						Integer.parseInt(splitLine[3]));
			}
			theGraph[currentAdd] = temp;
			currentAdd++;
		} 
		
	}

	return theGraph;
}

	// function calculates distances between all points on the graph
	static int[][] getDistances(Vertex[] graph) {

		// new matrix created the size of the graph length by itself
		int[][] distanceGraph = new int[graph.length][graph.length];

		// cycle through all comparisions and use the difference function to
		// calculate the differences in nodes
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph.length; j++) {
				distanceGraph[i][j] = difference(graph[i], graph[j]);
			}
		}
		// return the matrix of differences
		return distanceGraph;
	}

	// function that calculates the difference in location using A^2 + B^2 = C^2
	private static int difference(Vertex a, Vertex b) {

		// the quadratic formula is applied, and then the number is rounded to
		// closest whole integer
		int difference = (int) Math
				.round(Math.sqrt(Math.pow((a.getX() - b.getX()), 2) + Math.pow((a.getY() - b.getY()), 2)));

		// rounded number is returned by the function
		return difference;
	}

	

	/*
	This splits even and odds from each other and then creates a "perfect matching" (it doesn't actually, 
	but attempts something close to), and then reconnects the graph. Please note this creates a Eulerian Multigraph
	which means edges can be connected to each other twice. A-B A-B can exist.
	 * */
	
static ArrayList<Vertex> min_weight_and_unite(ArrayList<Vertex> MinimumSpanningTree, int [][] distances)
{
	ArrayList<Vertex> oddNumbersUnpaired           = new ArrayList<Vertex>();
	ArrayList<Vertex> oddNumbersPaired             = new ArrayList<Vertex>();
	ArrayList<Vertex> evenNumbers                  = new ArrayList<Vertex>();
	
	//This will separate the MinimumSPanningTree into an odd half and an even half of vertices.
	
	for(int i = 0; i < MinimumSpanningTree.size(); i++)
	{
		if(MinimumSpanningTree.get(i).connectedVertices.size() % 2 == 1)
		{
			oddNumbersUnpaired.add(MinimumSpanningTree.get(i));
			MinimumSpanningTree.get(i).evenEdge = false;
		}
		else
		{
			evenNumbers.add(MinimumSpanningTree.get(i));
			MinimumSpanningTree.get(i).evenEdge = true;
		}
	}

//This will create edges between two odd vertices.
	while(oddNumbersUnpaired.isEmpty() == false)
	{
		int     distance      = Integer.MAX_VALUE;
		int     indexToRemove = -1;
		
		for(int i = 1; i < oddNumbersUnpaired.size(); i++)
		{
			int checkDistance = distances[oddNumbersUnpaired.get(0).getID()][oddNumbersUnpaired.get(i).getID()];
			if(checkDistance < distance)
			{
				distance = checkDistance;
				indexToRemove = i;
			}
		}
		
	
	
		Edge fromZeroToPairEdge = new Edge(oddNumbersUnpaired.get(0).getID(),oddNumbersUnpaired.get(indexToRemove).getID(),distance);
		Edge fromPairEdgeToZero = new Edge(oddNumbersUnpaired.get(indexToRemove).getID(),oddNumbersUnpaired.get(0).getID(),distance);
		
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
}

static void updateEdges(ArrayList<Vertex> updateEdges)
{
	for(int i = 0; i < updateEdges.size(); i++)
	{
		for(int j = 0; j < updateEdges.get(i).connectedVertices.size(); j++)
		{
			for(int k = 0; k < updateEdges.size(); k++)
			{
				if(updateEdges.get(k).getID() == updateEdges.get(i).connectedVertices.get(j).child)
				{
					updateEdges.get(i).connectedVertices.get(j).Child = updateEdges.get(k);
					updateEdges.get(i).connectedVertices.get(j).Owner = updateEdges.get(i);
					break;
				}
			}
		}
	}
}
static void FinalAnswer(ArrayList<Vertex> TSP, int [][] distances, String p)
{
	try {
		PrintWriter writer = new PrintWriter((p + ".tour"));
		int totalDistance = 0;
		for(int i = 0; i < TSP.size()-1; i++)
		{
			System.out.println(TSP.get(i).getID());
			totalDistance+= distances[TSP.get(i).getID()][TSP.get(i+1).getID()];
		}

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



