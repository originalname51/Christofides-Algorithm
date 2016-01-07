import java.io.*;
import java.util.Scanner;
import java.util.PriorityQueue;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Hashtable;

//this class acts as a test harness to check for the correctness of the 4 different find max sub-array algorithms
public class project4 {

	public static void main(String[] args) throws IOException {
		
//		String output = args[0] + ".txt.tour";
		
		
		String path = "test-input-1.txt";//args[0] + ".txt";
		
		Vertex[] theGraph = parseGraph(path);
		
		int[][] distances = getDistances(theGraph);
		
		PrimsAlgorithm prim = new PrimsAlgorithm(theGraph, distances);
		ArrayList<Vertex> MinimumSpanningTree =  prim.Run();		
		
		ArrayList<Vertex> unitedList = min_weight_and_unite(MinimumSpanningTree, distances);
		updateEdges(unitedList);
		Hierholzer Hierholzer = new Hierholzer(unitedList, distances);
		LinkedList<Vertex> eulerTour =     Hierholzer.run();
		
		System.out.println("total size of tour is : " + eulerTour.size());
		for(int i = 0; i < eulerTour.size(); i++)
		{
			System.out.println(eulerTour.get(i).getID());
		}
	
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
	 * 
	 * This takes in the minimum Spanning Tree and distances. 
	 * This is a quick and dirty version. Target version is 
	 * http://dcg.ethz.ch/publications/ctw04.pdf
	 * 
	 * The version below works by taking all odd numbered vertexes and removing all odd vertex edges. It then keeps track of even edges
	 * and through smoke and mirrors produce a graph of all even numbered vertices.
	 * 
	 * From there, it will take the closest odd vertex in order to attempt a minimum wait fully matching tree. 
	 * This is not fully matching and needs to be re-written at a later date but both Blossom algorithm and the ctw04 algorithm are difficult to 
	 * implement and the gain in performance does not seem to be huge(based off initial implementation).
	 * */
	
static ArrayList<Vertex> min_weight_and_unite(ArrayList<Vertex> MinimumSpanningTree, int [][] distances)
{
	ArrayList<Vertex> oddNumbersUnpaired           = new ArrayList<Vertex>();
	ArrayList<Vertex> oddNumbersPaired             = new ArrayList<Vertex>();
	ArrayList<Vertex> evenNumbers                  = new ArrayList<Vertex>();
	ArrayList<Integer> oddNumberVertexID           = new ArrayList<Integer>();
	ArrayList<Integer> evenNumberVertexID 		   = new ArrayList<Integer>();
	
	
	
	//This will separate the MinimumSPanningTree into an odd half and an even half of vertices.
	
	for(int i = 0; i < MinimumSpanningTree.size(); i++)
	{
		if(MinimumSpanningTree.get(i).connectedVertices.size() % 2 == 1)
		{
			oddNumbersUnpaired.add(MinimumSpanningTree.get(i));
			oddNumberVertexID.add(MinimumSpanningTree.get(i).getID());

		}
		else
		{
			evenNumbers.add(MinimumSpanningTree.get(i));
			evenNumberVertexID.add(MinimumSpanningTree.get(i).getID());

		}
	}
	

		
	
//Isolate odd vertex
	
	for(int i = 0; i < oddNumbersUnpaired.size(); i++)
	{
		oddNumbersUnpaired.get(i).connectedVertices.clear();
	}

//remove even connection to odd vertex
	for(int i = 0; i < evenNumbers.size(); i++)
	{
		for(int j = evenNumbers.get(i).connectedVertices.size()-1; j > -1; j--)
		{
			if(oddNumberVertexID.contains(evenNumbers.get(i).connectedVertices.get(j).child))
			{
				evenNumbers.get(i).connectedVertices.remove(j);
			}
		}
	}
	
	for(int i = 0; i < evenNumbers.size(); i++)
	{
		if(evenNumbers.get(i).getID() == 9)
		{
			System.out.println(evenNumbers.get(i).connectedVertices.size());
		}
	}


	
// This block of code will separate even vertexes who have been made odd (and thus need to be mated)
// and even numbers who are zero.
	ArrayList<Integer> evenNumberNowOdd        =  new ArrayList<Integer>();
	ArrayList<Integer> evenNumberNowZero       =  new ArrayList<Integer>();
	ArrayList<Vertex>  evenNumberOddVertex     = new ArrayList<Vertex>();
	ArrayList<Vertex>  evenNumberNowZeroVertex = new ArrayList<Vertex>();
	
	for(int i = evenNumbers.size()-1; i > -1; i--)
	{
		if(evenNumbers.get(i).connectedVertices.size() % 2 == 1)
		{
			evenNumberOddVertex.add(evenNumbers.get(i));
			evenNumberNowOdd.add(evenNumbers.get(i).getID());
			evenNumbers.remove(i);

		}
		else if(evenNumbers.get(i).connectedVertices.size() == 0)
		{
			evenNumberNowZeroVertex.add(evenNumbers.get(i));
			evenNumberNowZero.add(evenNumbers.get(i).getID());
			evenNumbers.remove(i);
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
	
ArrayList<Vertex> oddNumbersToCombine      = new ArrayList<Vertex>();
while(oddNumbersPaired.isEmpty() == false)
{
	int combinedDistances = Integer.MAX_VALUE;
	
	int vertexIDOne       = oddNumbersPaired.get(0).getID();	//Because index at 0 and 1 have been organized to be connected
	int vertexIDTwo 	  = oddNumbersPaired.get(1).getID();	//These are the perfect matching odd numbers.
	int indexToConnect    = -1;
    boolean evenConnected = true;
	
	for(int i = 0; i < evenNumbers.size(); i++)
	{
			int distanceOne 			= distances[vertexIDOne][evenNumbers.get(i).getID()];
			int distanceTwo 			= distances[vertexIDTwo][evenNumbers.get(i).getID()];
			int checkCombinedDistances  = distanceOne + distanceTwo;
			
			if(checkCombinedDistances < combinedDistances)
			{
				combinedDistances = checkCombinedDistances;
				indexToConnect    = i;
			}		
	}
	
	int distanceCheckOddOneIndex = -1;
	int distanceCheckOddTwoIndex = -1;
	int combinedDistancesOdd = Integer.MAX_VALUE;
	for(int i = 0; i < evenNumberOddVertex.size(); i++)
	{
		for(int j = 0; j < evenNumberOddVertex.size(); j++)
		{
			if(evenNumberOddVertex.get(j).getID() != evenNumberOddVertex.get(i).getID())
			{
				int distanceOne = distances[vertexIDOne][evenNumberOddVertex.get(i).getID()];
				int distanceTwo = distances[vertexIDTwo][evenNumberOddVertex.get(j).getID()];
				int checkOneWay = distanceOne + distanceTwo;
			
				int distanceCheckOtherOne = distances[vertexIDOne][evenNumberOddVertex.get(j).getID()];
				int distanceCheckOtherTwo = distances[vertexIDTwo][evenNumberOddVertex.get(i).getID()];
				int checkTheOtherWay      = distanceCheckOtherOne + distanceCheckOtherTwo;
			
				if(checkOneWay < checkTheOtherWay)
				{
					if(checkOneWay < combinedDistances)
					{
						evenConnected = false;
						distanceCheckOddOneIndex = i;
						distanceCheckOddTwoIndex = j;
					}
				}
				else
				{
					if(checkTheOtherWay < combinedDistances)
					{
						evenConnected = false;
						distanceCheckOddOneIndex = j;
						distanceCheckOddTwoIndex = i;
					}
				}
			}
		}
	}
	
	
	
	if(evenConnected == true)
	{	
	Edge vertexIDOneConnector 			= new Edge(vertexIDOne, evenNumbers.get(indexToConnect).getID(), distances[vertexIDOne][indexToConnect]);
	Edge vertexEvenIDOneConnector 		= new Edge(evenNumbers.get(indexToConnect).getID(), vertexIDOne, distances[vertexIDOne][indexToConnect]);
	Edge vertexIDTwoConnector			= new Edge(vertexIDTwo, evenNumbers.get(indexToConnect).getID(), distances[vertexIDTwo][indexToConnect]);		
	Edge vertexEvenIDTwoConnector       = new Edge(evenNumbers.get(indexToConnect).getID(),vertexIDTwo, distances[vertexIDTwo][indexToConnect]);
	
	oddNumbersPaired.get(0).connectedVertices.add(vertexIDOneConnector);
	oddNumbersPaired.get(1).connectedVertices.add(vertexIDTwoConnector);
	
	evenNumbers.get(indexToConnect).connectedVertices.add(vertexEvenIDOneConnector);
	evenNumbers.get(indexToConnect).connectedVertices.add(vertexEvenIDTwoConnector);

	oddNumbersToCombine.add(oddNumbersPaired.get(0));
	oddNumbersToCombine.add(oddNumbersPaired.get(1));
	
	oddNumbersPaired.remove(1);
	oddNumbersPaired.remove(0);	
	}
	else //this reconnects an odd even vertex to the graph...
	{
		Edge vertexIDOneConnector = 
		new Edge(vertexIDOne, evenNumberOddVertex.get(distanceCheckOddOneIndex).getID(), distances[vertexIDOne][evenNumberOddVertex.get(distanceCheckOddOneIndex).getID()]);
		Edge vertexEvenIDOneConnector =
		new Edge(evenNumberOddVertex.get(distanceCheckOddOneIndex).getID(),vertexIDOne, distances[vertexIDOne][evenNumberOddVertex.get(distanceCheckOddOneIndex).getID()]);
		Edge vertexIDTwoConnector =
		new Edge(vertexIDTwo, evenNumberOddVertex.get(distanceCheckOddTwoIndex).getID(), distances[vertexIDTwo][evenNumberOddVertex.get(distanceCheckOddTwoIndex).getID()]);
		Edge vertexEvenIDTwoConnector  =
		new Edge(evenNumberOddVertex.get(distanceCheckOddTwoIndex).getID(),vertexIDTwo, distances[vertexIDTwo][evenNumberOddVertex.get(distanceCheckOddTwoIndex).getID()]);

		oddNumbersPaired.get(0).connectedVertices.add(vertexIDOneConnector);
		oddNumbersPaired.get(1).connectedVertices.add(vertexIDTwoConnector);	
	
		evenNumberOddVertex.get(distanceCheckOddOneIndex).connectedVertices.add(vertexEvenIDOneConnector);
		evenNumberOddVertex.get(distanceCheckOddTwoIndex).connectedVertices.add(vertexEvenIDTwoConnector);
		
		oddNumbersToCombine.add(oddNumbersPaired.get(0));
		oddNumbersToCombine.add(oddNumbersPaired.get(1));
		
		oddNumbersPaired.remove(1);
		oddNumbersPaired.remove(0);	
		
		evenNumbers.add(evenNumberOddVertex.get(distanceCheckOddOneIndex));
		evenNumbers.add(evenNumberOddVertex.get(distanceCheckOddTwoIndex));
		
		if(distanceCheckOddOneIndex > distanceCheckOddTwoIndex)
		{
			evenNumberOddVertex.remove(distanceCheckOddOneIndex);
			evenNumberOddVertex.remove(distanceCheckOddTwoIndex);

		}
		else
		{
			evenNumberOddVertex.remove(distanceCheckOddTwoIndex);
			evenNumberOddVertex.remove(distanceCheckOddOneIndex);

		}
	}
}

while(oddNumbersToCombine.isEmpty() == false)
{
	evenNumbers.add(oddNumbersToCombine.get(0));
	oddNumbersToCombine.remove(0);
}



//make evenNumbeNowZero into an odd number.
if(evenNumberNowZeroVertex.size() % 2 == 1)
{
	evenNumberNowZeroVertex.add(evenNumbers.get(0));
	evenNumbers.remove(0);
}


ArrayList<Vertex> zeroReadyToPair = new ArrayList<Vertex>();
while(evenNumberNowZeroVertex.isEmpty() == false)
{
	int savedIndex 	 = -1;
	int combinedDistance = Integer.MAX_VALUE;

	for(int i = 1; i < evenNumberNowZeroVertex.size(); i++)
	{
		if(distances[evenNumberNowZeroVertex.get(0).getID()][evenNumberNowZeroVertex.get(i).getID()] < combinedDistance)
		{
			combinedDistance = distances[evenNumberNowZeroVertex.get(0).getID()][evenNumberNowZeroVertex.get(i).getID()];
			savedIndex = i;
		}
	}

	
	Edge connectMeToID0 =
new Edge(evenNumberNowZeroVertex.get(0).getID(), evenNumberNowZeroVertex.get(savedIndex).getID(), distances[evenNumberNowZeroVertex.get(0).getID()][evenNumberNowZeroVertex.get(savedIndex).getID()]);
	Edge connectMeToIDSaved  =
new Edge(evenNumberNowZeroVertex.get(savedIndex).getID(), evenNumberNowZeroVertex.get(0).getID(), distances[evenNumberNowZeroVertex.get(0).getID()][evenNumberNowZeroVertex.get(savedIndex).getID()]);
	
	
	evenNumberNowZeroVertex.get(0).connectedVertices.add(connectMeToID0);
	evenNumberNowZeroVertex.get(savedIndex).connectedVertices.add(connectMeToIDSaved);
	
	
	zeroReadyToPair.add(evenNumberNowZeroVertex.get(0));
	zeroReadyToPair.add(evenNumberNowZeroVertex.get(savedIndex));
	
	
	
	evenNumberNowZeroVertex.remove(savedIndex);
	evenNumberNowZeroVertex.remove(0);

}	


ArrayList<Integer> EdgeCaseNumbers = new ArrayList<Integer>(); // for while loop
ArrayList<Vertex>  ZeroReadyToAdd  = new ArrayList<Vertex>();


System.out.println(zeroReadyToPair.get(0).getID());
while(zeroReadyToPair.isEmpty() == false)
{
    int edgeCase = -1;
	//This is ONLY if the even numbers now 0 was odd and is to ensure a connection
	//to a "fake" zero node isn't made to a vertex twice.

	if(zeroReadyToPair.get(0).connectedVertices.size() > 1)
	{
		for(int i = 0; i < zeroReadyToPair.get(0).connectedVertices.size(); i++)
		{
			EdgeCaseNumbers.add(zeroReadyToPair.get(0).connectedVertices.get(i).child);
			edgeCase = zeroReadyToPair.get(0).getID();
		}
	}
	
int	vertexIDOne = zeroReadyToPair.get(0).getID();
int	vertexIDTwo = zeroReadyToPair.get(1).getID();
	
	int combinedDistances = Integer.MAX_VALUE;

	int indexToConnect    = -1;
	
	for(int i = 0; i < evenNumbers.size(); i++)
	{
		if(zeroReadyToPair.get(0).getID() != edgeCase)
		{
			int distanceOne 			= distances[vertexIDOne][evenNumbers.get(i).getID()];
			int distanceTwo 			= distances[vertexIDTwo][evenNumbers.get(i).getID()];
			int checkCombinedDistances  = distanceOne + distanceTwo;
			
			if(checkCombinedDistances < combinedDistances)
			{
				combinedDistances = checkCombinedDistances;
				indexToConnect    = i;
			}		
		}
		else
		{
			if(!EdgeCaseNumbers.contains(evenNumbers.get(i).getID()))
				{
				int distanceOne 			= distances[vertexIDOne][evenNumbers.get(i).getID()];
				int distanceTwo 			= distances[vertexIDTwo][evenNumbers.get(i).getID()];
				int checkCombinedDistances  = distanceOne + distanceTwo;
				
				if(checkCombinedDistances < combinedDistances)
				{
					combinedDistances = checkCombinedDistances;
					indexToConnect    = i;
				}		
				}
		}
	}
	
	Edge vertexIDOneConnector 			= new Edge(vertexIDOne, evenNumbers.get(indexToConnect).getID(), distances[vertexIDOne][indexToConnect]);
	Edge vertexEvenIDOneConnector 		= new Edge(evenNumbers.get(indexToConnect).getID(), vertexIDOne, distances[vertexIDOne][indexToConnect]);
	Edge vertexIDTwoConnector			= new Edge(vertexIDTwo, evenNumbers.get(indexToConnect).getID(), distances[vertexIDTwo][indexToConnect]);		
	Edge vertexEvenIDTwoConnector       = new Edge(evenNumbers.get(indexToConnect).getID(),vertexIDTwo, distances[vertexIDTwo][indexToConnect]);
	
	zeroReadyToPair.get(0).connectedVertices.add(vertexIDOneConnector);
	zeroReadyToPair.get(1).connectedVertices.add(vertexIDTwoConnector);
	
	evenNumbers.get(indexToConnect).connectedVertices.add(vertexEvenIDOneConnector);
	evenNumbers.get(indexToConnect).connectedVertices.add(vertexEvenIDTwoConnector);

	ZeroReadyToAdd.add(zeroReadyToPair.get(0));
	ZeroReadyToAdd.add(zeroReadyToPair.get(1));
	
	zeroReadyToPair.remove(1);
	zeroReadyToPair.remove(0);	
	
}
	
while(ZeroReadyToAdd.isEmpty() == false)
{
	evenNumbers.add(ZeroReadyToAdd.get(0));
	ZeroReadyToAdd.remove(0);
}

for(int i = 0; i < evenNumbers.size(); i ++)
{
	if(evenNumbers.get(i).connectedVertices.size() % 2 == 1)
	{
		System.out.println("dammit");
		
	}
}

System.out.println(evenNumberOddVertex.get(0).getID());
while(evenNumberOddVertex.isEmpty() == false)
{
	
	
	ArrayList<Integer> firstNumberConnections = new ArrayList<Integer>();
	for(int i = 0; i < evenNumberOddVertex.get(0).connectedVertices.size(); i++)
	{
		firstNumberConnections.add(evenNumberOddVertex.get(0).connectedVertices.get(i).child);
	}
	
	int  minDistance = Integer.MAX_VALUE;
	int  evenIndex   = -1;
	int  jIndex      = -1;
	
	
	ArrayList<Integer> secondNumberConnections = new ArrayList<Integer>();
	
	
	for(int i = 1; i < evenNumberOddVertex.size(); i++)
	{
		for(int j = 0; j < evenNumberOddVertex.get(i).connectedVertices.size(); j++ )
		{
			secondNumberConnections.add(evenNumberOddVertex.get(i).connectedVertices.get(j).child);
		}
		for(int k = 0; k < evenNumbers.size(); k++)
		{
			if(!(secondNumberConnections.contains(evenNumbers.get(i).getID()) || firstNumberConnections.contains(evenNumbers.get(i).getID())))
				{
				if(distances[evenNumberOddVertex.get(0).getID()][evenNumberOddVertex.get(i).getID()] < minDistance )
					{
						jIndex 	  = i;
						evenIndex = k;		
						minDistance = distances[evenNumberOddVertex.get(0).getID()][evenNumberOddVertex.get(i).getID()];
					}
				}
		}

	}
	Edge vertexIDOneConnector 			= new Edge(evenNumberOddVertex.get(0).getID(), evenNumbers.get(evenIndex).getID(), distances[evenNumberOddVertex.get(0).getID()][evenNumbers.get(evenIndex).getID()]);
	Edge vertexIDOneConnectorNumberTwo	= new Edge(evenNumbers.get(evenIndex).getID(), evenNumberOddVertex.get(0).getID(), distances[evenNumberOddVertex.get(0).getID()][evenNumbers.get(evenIndex).getID()]);

	Edge vertexIDTwoConnector			= new Edge(evenNumberOddVertex.get(jIndex).getID(), evenNumbers.get(evenIndex).getID(), distances[evenNumberOddVertex.get(jIndex).getID()][ evenNumbers.get(evenIndex).getID()]);		
	Edge vertexIDTwoConnectorNumber2	= new Edge(evenNumbers.get(evenIndex).getID(), evenNumberOddVertex.get(jIndex).getID(), distances[evenNumberOddVertex.get(jIndex).getID()][ evenNumbers.get(evenIndex).getID()]);		

	
	
	evenNumberOddVertex.get(0).connectedVertices.add(vertexIDOneConnector);
	evenNumberOddVertex.get(jIndex).connectedVertices.add(vertexIDTwoConnector);
	
	evenNumbers.get(evenIndex).connectedVertices.add(vertexIDOneConnectorNumberTwo);
	evenNumbers.get(evenIndex).connectedVertices.add(vertexIDTwoConnectorNumber2);

	evenNumbers.add(evenNumberOddVertex.get(0));
	evenNumbers.add(evenNumberOddVertex.get(jIndex));
	
	evenNumberOddVertex.remove(jIndex);
	evenNumberOddVertex.remove(0);	
}


for(int i = 0; i < evenNumbers.size(); i++)
{
	ArrayList<Integer> children = new ArrayList<Integer>();
	for(int j = 0; j < evenNumbers.get(i).connectedVertices.size(); j++)
	{
		if(children.contains(evenNumbers.get(i).connectedVertices.get(j).child))
		{
			System.out.println("oh rats");
		}
		else
		{
			children.add(evenNumbers.get(i).connectedVertices.get(j).child);
		}
		
	}
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

}