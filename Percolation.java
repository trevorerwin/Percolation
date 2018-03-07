import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    
    private int gridSize, siteCount;
    private WeightedQuickUnionUF uf, full;
    private int virtualTop, virtualBottom;
    private boolean[] grid;
        
    public Percolation(int n){
        if(n<=0) throw new IllegalArgumentException("n must be greater than 0");
        
        gridSize = n;
        siteCount = 0;
        uf = new WeightedQuickUnionUF(gridSize*gridSize + 2);
        full = new WeightedQuickUnionUF(gridSize*gridSize + 1); //account for backwash
        virtualTop = gridSize*gridSize;
        virtualBottom = gridSize*gridSize + 1;
        
        grid = new boolean[gridSize*gridSize];
                      
    }
    
    //open the site (row, col) if it's not already open
    public void open(int row, int col){
        
        if(row < 0 || row >= gridSize || col < 0 || col >= gridSize)
            throw new IndexOutOfBoundsException("argument is outside perscribed range");
        
        //First check if site is open already. if so, do nothing, otherwise
        //proceed
        if(isOpen(row, col)){
           return; 
        }
        
        //update boolean array to be true + siteCount
        grid[xyTo1D(row,col)] = true;
        siteCount++;
        
        //If site is next to adjacent open sites, must union them together
        //    virtualTop, virtualBottom, left, right, top, bottom
        int index = xyTo1D(row,col);
        
        if(row == 0){ 
            uf.union(index, virtualTop);
            full.union(index, virtualTop);
        }
        if(row == gridSize-1){
            uf.union(index, virtualBottom); //only union with uf object, to prevent backwash
        }
        if(col > 0 && isOpen(row, col-1)){ 
            uf.union(index, xyTo1D(row, col-1));
            full.union(index, xyTo1D(row, col-1));
        }
        if(col < gridSize-1 && isOpen(row, col+1)){ 
            uf.union(index, xyTo1D(row, col+1));
            full.union(index, xyTo1D(row, col+1));
        }
        if(row > 0 && isOpen(row-1, col)){ 
            uf.union(index, xyTo1D(row-1,col));
            full.union(index, xyTo1D(row-1, col));
        }
        if(row < gridSize-1 && isOpen(row+1, col)){ 
            
            uf.union(index, xyTo1D(row+1, col));
            full.union(index, xyTo1D(row+1, col));
            
        }
        
    }
    
    //is the site open
    public boolean isOpen(int row, int col){
        if(row < 0 || row >= gridSize || col < 0 || col >= gridSize){
            throw new IndexOutOfBoundsException();
        } 
            
        return grid[xyTo1D(row,col)];
        
    }
    
    //is the site full
    public boolean isFull(int row, int col){
        //must check if row,col is connected to the virtualTop
        if(row < 0 || row >= gridSize || col < 0 || col >= gridSize){
            throw new IndexOutOfBoundsException("Argument is outside perscribed range");
        } 
            return full.connected(xyTo1D(row,col), virtualTop);
        
    }
    
    //number of open sites
    public int numberOfOpenSites(){
        return siteCount;
    }
    
    //does the system percolate
    public boolean percolates(){
        //System percolates if virtualTop is connected to virtualBottom
        return uf.connected(virtualBottom, virtualTop);
    }
    
    //changes site grid from a 2D site to 1D, for UF
    private int xyTo1D (int i, int j){
        return gridSize*i + j;
    }
    
    //unit testing
    public static void main(String[]args){
        
    }
}