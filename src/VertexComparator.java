import java.util.Comparator;


public class VertexComparator implements Comparator<Vertex>{
	

    public int compare(Vertex arg0, Vertex arg1) {
    	
    	if( arg0.edge.weight > arg1.edge.weight)
    	{
    		return 1;
    	}
    	else if(arg0.edge.weight < arg1.edge.weight)
    	{
    		return -1;
    	}
    	
    	else if(arg0.edge.weight == arg1.edge.weight)
    	{
    		return 0;
    	}
    	else
    	{
    	return 0;
    	}
    }

}
