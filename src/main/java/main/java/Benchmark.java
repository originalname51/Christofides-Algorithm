package main.java;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Benchmark
{
    private LocalDateTime start;
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
        start = LocalDateTime.now();
    }
   
    void endMark()
    {
        endCheck = true;
    }
   
    long currentTime()
    {
    	return ChronoUnit.SECONDS.between(start, LocalDateTime.now());
    }
    
    long resultTime()
    {
        if(startCheck == true && endCheck == true) {
            return ChronoUnit.MILLIS.between(start, LocalDateTime.now());
        } else {
            System.out.print("BenchMark Failed. ");
        }
        return time;
    }
   
}