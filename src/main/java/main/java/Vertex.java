package main.java;

import java.util.ArrayList;


public class Vertex 
{
	   private int id;
	   private int x_var;
	   private int y_var;	
	   public ArrayList<Edge> connectedVertices;
	   public   Edge 	   edge; //used for prims algorithm

	   boolean evenEdge;
	   
	   public Vertex(int id, int x_var, int y_var)
	   {	   
		   this.id = id;
		   this.x_var = x_var;
		   this.y_var = y_var;
		   this.connectedVertices = new ArrayList<Edge>();
		   this.edge       = null;  
	   }
	   
	   int getID()
	   {
		      return id;
	   }

	   int getX()
	   {
		   	return x_var;
	    }
		   
	    int getY()
	    {
		   return y_var;
		}
	    
		   
}