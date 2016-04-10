import java.util.*;
import java.text.*;
import java.io.*;

public class Problem 
{
 Fit f = new Fit();
 Results r = new Results();
 Distribution D = new Distribution();
 
 public Problem() 
 {

 }

 
 public int[] Permutation(int n, Random R)
 {
  //  
  int[] solution = new int[n];
  for(int i = 0; i < n; i++)
    solution[i] = i;
	 
  for(int i=n-1;i>=1;i--)
  {  
   int j = R.nextInt(i);
   solution[j] = solution[i]^solution[j];
   solution[i] = solution[i]^solution[j];
   solution[j] = solution[i]^solution[j];
  }	  
  return solution;
}
 
 public int[] mutate2(int[] newsolution, int swap1, int swap2) 
	{	
	   newsolution[swap1] = newsolution[swap1]^newsolution[swap2];
	   newsolution[swap2] = newsolution[swap1]^newsolution[swap2];
	   newsolution[swap1] = newsolution[swap1]^newsolution[swap2]; 	
	  return newsolution;
	}
	
	
	// Mutation by swapping 3 elements in a solution--3opt
	public int[] mutate3(int[] newsolution, int n, Random R,int swap1, int swap2) 
	{	
	   int swap3 = R.nextInt(n);
	
	   newsolution[swap1] = newsolution[swap1]^newsolution[swap2];
	   newsolution[swap2] = newsolution[swap1]^newsolution[swap2];
	   newsolution[swap1] = newsolution[swap1]^newsolution[swap2]; 
	   while((swap1 == swap3)|(swap2 == swap3))
	   {
		  swap3 = R.nextInt(n);
	   }
	   newsolution[swap2] = newsolution[swap2]^newsolution[swap3];
	   newsolution[swap3] = newsolution[swap2]^newsolution[swap3];
	   newsolution[swap2] = newsolution[swap2]^newsolution[swap3]; 
	  return newsolution;
	}
	//
	
	// Mutation by swapping k elements in a solution until no improvement is observed-->k-opt
	public int[] mutatek(int ptype,int[] newsolution, int n,int m, Random R,int swap0, int swap1,int[][] factor,String obfuname)
	{	
	   int[] swap = new int[n];
	   int[] sol_oldfit = new int[n];
	   int lastswap,newswap,idx=0,swaps_count;
	   double oldfit,newfit;
	   
	   swap[0] = swap0;
	   swap[1] = swap1;
	   oldfit = ptype*f.getFitnessPermutation(newsolution,n,m,factor,obfuname);
	   newsolution[swap[0]] = newsolution[swap[0]]^newsolution[swap[1]];
	   newsolution[swap[1]] = newsolution[swap[0]]^newsolution[swap[1]];
	   newsolution[swap[0]] = newsolution[swap[0]]^newsolution[swap[1]]; 
	   sol_oldfit = newsolution.clone();
	   newfit = ptype*f.getFitnessPermutation(newsolution,n,m,factor,obfuname);
	   newswap = swap[1];
	   swaps_count = 2;
	   while((newfit < oldfit)&(swaps_count != n))
		{
			 oldfit = newfit;
			 sol_oldfit = newsolution.clone();
			 lastswap = newswap;
			 //check for not having duplicates
			 while(idx != swaps_count)
			 {
				newswap = R.nextInt(n);
				idx = 0;
				for(int i=0;i < swaps_count;i++)
					if(newswap != swap[i]) idx++; 
			 }
			 //
			 newsolution[lastswap] = newsolution[lastswap]^newsolution[newswap];
			 newsolution[newswap] = newsolution[lastswap]^newsolution[newswap];
			 newsolution[lastswap] = newsolution[lastswap]^newsolution[newswap]; 	 
			 newfit = ptype*f.getFitnessPermutation(newsolution,n,m,factor,obfuname);
			 swap[swaps_count] = newswap;
			 swaps_count++;
		 }	 
	  return sol_oldfit;
			
	}
	
	//k-opt deteriorating as k-opt move but if fitness still improves compared with the second-last candidate, then carry on
		public int[] mutatek_deterio(int ptype,int[] newsolution, int n,int m, Random R,int swap0, int swap1,int[][] factor,String obfuname)
		{
			//
			   int[] swap = new int[n];
			   int[] sol_oldfit = new int[n];
			   int lastswap,newswap,idx=0,swaps_count;
			   double best,oldfit,newfit,second_oldfit = 0;
			   
			   swap[0] = swap0;
			   swap[1] = swap1;
			   oldfit = ptype*f.getFitnessPermutation(newsolution,n,m,factor,obfuname);
			   //System.out.println(oldfit);
			   sol_oldfit = newsolution.clone();
			   best = oldfit;
			   newsolution[swap[0]] = newsolution[swap[0]]^newsolution[swap[1]];
			   newsolution[swap[1]] = newsolution[swap[0]]^newsolution[swap[1]];
			   newsolution[swap[0]] = newsolution[swap[0]]^newsolution[swap[1]];   
			   newfit = ptype*f.getFitnessPermutation(newsolution,n,m,factor,obfuname);
			   //System.out.println(newfit);
			   if(best > newfit) {best = newfit;sol_oldfit = newsolution.clone();}
			   newswap = swap[1];
			   swaps_count = 2;
			   while(((newfit < oldfit)|(newfit < second_oldfit))&(swaps_count != n))
				{
				     second_oldfit = oldfit;
					 oldfit = newfit;
					 //sol_oldfit = newsolution.clone();
					 lastswap = newswap;
					 //check for not having duplicates
					 while(idx != swaps_count)
					 {
						newswap = R.nextInt(n);
						idx = 0;
						for(int i=0;i < swaps_count;i++)
							if(newswap != swap[i]) idx++; 
					 }
					 //
					 newsolution[lastswap] = newsolution[lastswap]^newsolution[newswap];
					 newsolution[newswap] = newsolution[lastswap]^newsolution[newswap];
					 newsolution[lastswap] = newsolution[lastswap]^newsolution[newswap];
					 newfit = ptype*f.getFitnessPermutation(newsolution,n,m,factor,obfuname);
					 //System.out.println(newfit);
					 if(best > newfit)
					 {
					  best = newfit;
					  //System.out.println(best + "\n");
					  sol_oldfit = newsolution.clone();
					 }
					 swap[swaps_count] = newswap;
					 swaps_count++;
				 }
			   //System.out.println("\n");
			  return sol_oldfit;
		}		
	 
 public int[][] readInstancePermutation(int n, int m, String fname) throws Exception
 {
  int[][] factor = new int[m][n];
  
  DataInputStream localDataInputStream = new DataInputStream(new FileInputStream(fname));
  BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localDataInputStream));
  String s = null;
  int czero = 0;
  int squareflag = 0;

   for(int i=0;i<m;i++)//	
   for(int j=0;j<n;j++)// 
	  {
	 //first matrix sparsity
	   if((i==n)&(j==0)) 
	    {
		 czero=0;squareflag =1;
		}
	   factor[i][j] = Integer.parseInt(localBufferedReader.readLine());
	   if(factor[i][j] == 0) czero ++;
	  }
   
  localDataInputStream.close();
  
  return factor;
 }
 
 public int[][] readInstanceModifySparsityPermutation(int n, int m, String fname,Random R,int round) throws Exception
 {
  int[][] factor = new int[m][n];
  
  DataInputStream localDataInputStream = new DataInputStream(new FileInputStream(fname));
  BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localDataInputStream));
  String s = null;
  double replaceRate = 0.2;
  int cnon = 0;
  int creplace = 0,r1,r2;
  //the value you want to replace in matrices
  int replace = 0;//
  int czero = 0;
  //produce instances with different level of zeroisation and different placements of zeroids

   for(int i=0;i<m;i++)//	
   for(int j=0;j<n;j++)// 
	{
	   factor[i][j] = Integer.parseInt(localBufferedReader.readLine());
	   if(factor[i][j] == replace) czero ++;
	}
   double spar = (double)(czero)/(double)(n*m);
   double remain = Math.abs((spar - replaceRate)*(n*m));
   r.outputs(replaceRate*100 + " ","dumpVcoeSkspar", true);
   
   while(creplace < remain)
	{
	   r1 = R.nextInt(m);
	   r2 = R.nextInt(n);
	   if(factor[r1][r2] != replace) {factor[r1][r2] = 0; creplace ++;} 
	}
   
  localDataInputStream.close();
  
  return factor;
 }
 
 public int[][] readInstanceModifyVCPermutation(int n, int m,Random R,int round) throws Exception
 {
  int[][] factor = new int[m][n];
  
  int cst = 1;
  int czero = 0;
  //produce instances with different level of zeroisation and different placements of zeroids
  int Gvalue;
   for(int i=0;i<m;i++)//
   {
    for(int j=0;j<n;j++)// 
	 {
       Gvalue = (int)R.nextGaussian();
	   if(Gvalue < 0) Gvalue *= -100; 
	   factor[i][j] = Gvalue;
	   if(factor[i][j] == 0) czero ++;
	   r.outputs(Integer.toString(factor[i][j]) + " ", Integer.toString(round + 1000), true);
	 }
    r.outputs("\n", Integer.toString(round + 1000), true);
    if(i == (n-1)) r.outputs("\n", Integer.toString(round + 1000), true);
   }
   double spar = (double)(czero)/(double)(n*m);
   
  return factor;
 }
 
 public class Distribution extends Random 
 {
	 public  int nextPoisson(double lambda) 
	 { 
	  double elambda = Math.exp(-1*lambda);
	  double product = 1;
	  int count =  0;
	  int result=0;
	  while (product >= elambda) 
	   {
	 	product *= nextDouble();
	 	result = count;
	 	count++; // keep result one behind
	   }
	  
	  return result;
	}

	 public  double nextExponential(double b) 
	 {
	  double randx;
	  double result;
	 	randx = nextDouble();
	 	result = -1*b*Math.log(randx);
	  
	  return result;
	 }
 }
 
 public int hamd(int[] str1,int[] str2)
	{
		int hamd = 0;
		for(int i=0; i< str1.length; i++)
			if(str1[i] != str2[i]) hamd++;
		
		return hamd;
	}
 
 public void fdc(int counts, int k, double fit[], int dist[]) throws Exception
 {
	 double fdc = 0;
	 double sfit,sdist,avefit,avedist;
	 double squf,squd,stdf,stdd,sfdp;
	 int i,j;
	 //fdc
	  
	 sfit = 0;
	 sdist = 0;
	 for(i=0;i < counts; i++)
	 {
		 sfit += fit[i];
		 sdist += dist[i];
	 }
	 avefit = (double)sfit/counts;
	 avedist = (double)sdist/counts;
	 squf = 0;
	 squd = 0;
	 sfdp = 0;
	 for(i=0;i < counts; i++)
	 {
		 squf += (fit[i] - avefit)*(fit[i] - avefit);
		 squd += (dist[i] - avedist)*(dist[i] - avedist);
		 sfdp += (fit[i] - avefit)*(dist[i] - avedist);
	 }
	 stdf = Math.sqrt(squf/counts);
	 stdd = Math.sqrt(squd/counts);
	 fdc = (double)sfdp/(counts*stdf*stdd);
	 r.outputs(fdc + "\n",Integer.toString(k + 6000), true);
	 
 }

  public void autocf(int counts, double fit[], double fit_after_steps[], int k)
 {
        //from wcci2012 paper definition
  		double fit0,fit1;
  		int i;
  			 
  		//compute autocorelation coefficient
  		double avg_fit = 0, avg_fitsteps = 0;
  		for(i =0;i< counts; i++)
  		{
  			avg_fit += fit[i];
  			avg_fitsteps += fit_after_steps[i];
  		}		
  		avg_fit /= counts;
  		avg_fitsteps /= counts;
  		double square_fit = 0, square_fitsteps = 0;
  		for(i = 0; i < counts; i++)
  		{	
  		    square_fit += (fit[i] - avg_fit)*(fit[i] - avg_fit);
  		    square_fitsteps += (fit_after_steps[i] - avg_fitsteps)*(fit_after_steps[i] - avg_fitsteps);
  		}
  		double std_fit = Math.sqrt(square_fit/counts);
  		double std_fitsteps = Math.sqrt(square_fitsteps/counts);
  		double ro=0;
  		for(i = 0; i < counts; i++)
  		  ro += (avg_fit - fit[i])*(avg_fitsteps - fit_after_steps[i]);
  		
  		double autocf = (double)ro/(counts*std_fit*std_fitsteps);
  		r.outputs(autocf + "\n",Integer.toString(k + 5000), true); 
  }
 
}
