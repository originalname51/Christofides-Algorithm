package main.java;

public class Benchmark
{
    private long start = 0;
    private long end = 0;
    private long time = 0;
    private boolean startCheck = false;
    private boolean endCheck = false;

public Benchmark()
{
   
}
   
    void startMark()
    {
        startCheck = true;
        start = System.currentTimeMillis();
        
    }
   
    void endMark()
    {
        endCheck = true;
        end = System.currentTimeMillis();
    }
   
    long currentTime()
    {
    	return end-start;
    }
    
    long resultTime()
    {
        if(startCheck == true && endCheck == true)
            time = end - start;
        else
            System.out.print("BenchMark Failed. ");
       
        return time;
    }
   
}