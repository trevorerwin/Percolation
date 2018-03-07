import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    
    private Percolation per; //instance of percolation class to perform computation
    private int gridSize; // n-by-n grid
    private int experCount; //# of experiments performed
    private double[] estimate; //estimation of percolation threshold, holding the estimates from each T experiment
    
    
    public PercolationStats(int n, int T){
        if(n <= 0 || T <= 0) throw new IllegalArgumentException("Argument is outside perscribed range");
        
        gridSize = n; 
        experCount = T;
        
        estimate = new double[experCount];
        //Perform the experiment. Must perform experiment T times
        //experiment involves constantly opening sites until the system percolates
        //This is one experiment. So we need a for loop to go through each
        //experiment. Also need a while loop to add sites until system
        //percolates. 
        for(int currExper = 0; currExper < experCount; currExper++){
            per = new Percolation(gridSize);
            int siteCount = 0;
            while(!per.percolates()){
                //System.out.println("hi");
                
                int i = StdRandom.uniform(0, gridSize);
                int j = StdRandom.uniform(0, gridSize);
                if(!per.isOpen(i, j)){
                    per.open(i, j);
                    siteCount++;
                }
                
            }
            double fraction = (double) siteCount / (gridSize*gridSize);
            estimate[currExper] = fraction;
        }
    }
    
    public double mean(){
        return StdStats.mean(estimate);
    }
    
    public double stddev(){
        return StdStats.stddev(estimate);
    }
    
    public double confidenceLow(){
        return mean() - ((1.96 * stddev()) / Math.sqrt(experCount));
    }
    
    public double confidenceHigh(){
        return mean() + ((1.96 * stddev()) / Math.sqrt(experCount));
    }
    
    public static void main(String[] args){
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        
        PercolationStats stats = new PercolationStats(n, t);
               
        String confidence = stats.confidenceLow() + "," + stats.confidenceHigh();
        
        System.out.println("mean =                " + stats.mean());
        System.out.println("stddev =              " + stats.stddev());
        System.out.println("mean =                " + stats.mean());
        System.out.println("confidence interval = " + confidence);
        
    }
}