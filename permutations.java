import java.io.*;
import java.util.Arrays;

/*************************************************************************
 *************************************************************************/
public class permutations 
{
  
    public int getFitnessPermutation(int[] solution,int n,int[][] factor)
    {
     int fitness = 0;
     int i,j;
      
     for(i = 0; i < n; i++) 
       for(j = 0; j < n; j++)   
   			fitness += factor[i][j]*factor[n + solution[i]][solution[j]];
      
     return fitness;
    }
    
    void permute(int []a,int k, int n,int[][] factor) 
    {			
   	    if(k==a.length)
    	{
   	     int fit = getFitnessPermutation(a,n,factor);
   	     //System.out.println(Arrays.toString(a));
    	}
    	else for (int i = k; i < a.length; i++) 
    	{
    		int temp=a[k];
    		a[k]=a[i];
    		a[i]=temp;
    		permute(a,k+1,n,factor);
    		temp=a[k];
    		a[k]=a[i];
    		a[i]=temp;
    	}
    }
    
}