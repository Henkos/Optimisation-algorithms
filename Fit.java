import java.util.*;

public class Fit 
{
 
 public Fit()
 {
	 
 }
 
//calculates the quality of solution(fitness value) for 3 problems: QAP, LOP and FSSP.
 public double getFitnessPermutation(int[] solution,int n,int m,int[][] factor,String obfuname)
 {
  double fitness = 0.0;
  int i,j,tmp;
  int SM = 0; 
  int [][ ] cmax = new int[m][solution.length];
   
  if(obfuname.equals("PFSP"))
  {  
	  for(i=0;i < m;i++)
			cmax[i][0] = 0;
		for(j=0;j < solution.length;j++)
			cmax[0][j] = 0;
		
		cmax[0][0] = factor[0][solution[0]];
		for(j =1;j < solution.length;j++)
			cmax[0][j] = factor[0][solution[j]] + cmax[0][j-1];
		
	    for(i=1;i < m;i++)//	
	     for(j=1;j < n;j++)//
	    	 if(cmax[i][j-1] > cmax[i-1][j]) cmax[i][j] = factor[i][solution[j]] + cmax[i][j-1];
	    	   else cmax[i][j] = factor[i][solution[j]] + cmax[i-1][j];
	    fitness = cmax[m-1][solution.length - 1];
  }
  else if(obfuname.equals("LOP"))
  {
	  for(i=0;i<n;i++)//	
		   for(j=0;j<n;j++)//
			   SM += factor[i][j];
	  
	  for(i = 0; i < n; i++) 
	   for(j = 0; j <= i; j++) 
		  fitness += factor[solution[i]][solution[j]];//
  }
  else if(obfuname.equals("QAP"))
  {
	   for(i = 0; i < n; i++) 
		    for(j = 0; j < n; j++) 
		     {	  
			  fitness += factor[i][j]*factor[n + solution[i]][solution[j]];//
		     }  
  }
   
  return fitness;
 }
 
}
