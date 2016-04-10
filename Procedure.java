import java.util.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.*;

public class Procedure
{

	Fit f = new Fit();
	Problem pr = new Problem();
	PDO pdo = new PDO();
	Results r = new Results();
	Sampling s = new Sampling();
	permutations permu = new permutations();
	
	public Procedure()
	{

	}
	///////////////
	public void run(int ptype,int n,int m, int q,int v,Random R,int round,String fname,String fnamebest,String obfuname,double signi,double zsigni,int neimins,double pacct) throws Exception
	{
		//
		int howOften = 100;
		double score1 = 0,score2 = 0,score3 = 0,score4 = 0;
		int[] select2 = new int[10000];
		int[] select3 = new int[10000];
		int[] select4 = new int[10000];
		int[] select5 = new int[10000];
		int[] perform2 = new int[10000];
		int[] perform3 = new int[10000];
		int[] perform4 = new int[10000];
		int[] perform5 = new int[10000];
		//************ 
		 int[][] factor = pr.readInstancePermutation(n,q,fname);
		 
		//PREDICTOR CREATION PHASE 
		double[][] coef2opt = pdo.create(ptype,n,m,v,R,2,round,factor,obfuname,signi,zsigni,neimins,pacct);
		double[][] coef3opt = pdo.create(ptype,n,m,v,R,3,round,factor,obfuname,signi,zsigni,neimins,pacct);
		double[][] coefkopt = pdo.create(ptype,n,m,v,R,4,round,factor,obfuname,signi,zsigni,neimins,pacct);
		double[][] coefkoptd = pdo.create(ptype,n,m,v,R,5,round,factor,obfuname,signi,zsigni,neimins,pacct);
		 //*******************************end ofPHASE
		//read the global
		 DataInputStream localDataInputStream = new DataInputStream(new FileInputStream(fnamebest));
		 BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localDataInputStream));
		 String strLine;
		 double global_fitness = Double.parseDouble(localBufferedReader.readLine());
		 int[] global = new int[n];
		 int idx = 0;
		 while( (strLine = localBufferedReader.readLine()) != null )
		 {
			global[idx] = Integer.parseInt(strLine) - 1;
			idx++;
		 }
		 
		int[] pair_solution = new int[n];
		int bestS = -1;
		int lastbestS = 2;
		int countCS = 0;
		int expire = 1000;
		double minScore = 0;
		int minS = 2;
		while(countCS <= expire)
		{
			pair_solution = pr.Permutation(n, R);
			score1 += test_candidate_on_group(ptype,pair_solution,coef2opt,select2,perform2,n,m,R,2,round,factor,obfuname,signi,zsigni,neimins,global,countCS);
			score2 += test_candidate_on_group(ptype,pair_solution,coef3opt,select3,perform3,n,m,R,3,round,factor,obfuname,signi,zsigni,neimins,global,countCS);
			score3 += test_candidate_on_group(ptype,pair_solution,coefkopt,select4,perform4,n,m,R,4,round,factor,obfuname,signi,zsigni,neimins,global,countCS);
			score4 += test_candidate_on_group(ptype,pair_solution,coefkoptd,select5,perform5,n,m,R,5,round,factor,obfuname,signi,zsigni,neimins,global,countCS);
			countCS ++;
			//every HowOften candidate solutions check the minimal score, if stays steady exit
			if((countCS%howOften) == 0)
			{
				minScore = score1;
				minS = 2;
				if(minScore > score2) {minScore = score2;minS = 3;}
				if(minScore > score3) {minScore = score3;minS = 4;}
				if(minScore > score4) {minScore = score4;minS = 5;}
				lastbestS = bestS;
				bestS = minS;
			}
		}
			
		
	}
	
	public double test_candidate_on_group(int ptype,int[] pair_solution, double[][] coef,int[] select,int[] perform,int n,int m,Random R,int k,
			int round,int[][] factor,String obfuname,double signi,double zsigni,int neimins,int[] global, int countCS)
	{
	    double score = 100,otherScore;
		int[] step = new int[n];
		double fit0 = 0,fit1 = 0,fitk,fit_global;
		double r1 = 0,prediction,min_distance;
		int selection = 1;
		int numsteps;
		double saveFit = 0;
		//percentage to maintain the performance of the predictor group
		int accuracy =5;
		int descentlength=10;
		double[] writesteps = new double[descentlength*descentlength];
		
		//
		int[] initialsolution = pair_solution.clone();
		fit0 = f.getFitnessPermutation(pair_solution,n,m,factor,obfuname);
		numsteps = 0;
		fit_global = f.getFitnessPermutation(global,n,m,factor,obfuname);
		writesteps[numsteps] = fit0;
		step = s.moveSsize(ptype,pair_solution,k,n,m,R,round,factor,obfuname,signi,zsigni,neimins);
		fit1 = f.getFitnessPermutation(step,n,m,factor,obfuname);
		numsteps++;
		writesteps[numsteps]=fit1;
		r1 = ptype*(fit0 - fit1)/(fit0/n);
		min_distance = Math.abs(r1 - coef[1][0]);
	    int predPos=2;
		while(coef[predPos][0] != 0)
	    {
		  if(Math.abs(r1-coef[predPos][0]) < min_distance) 
			  {
			   min_distance = Math.abs(r1-coef[predPos][0]);
			   selection = predPos;
			  }
		  predPos++;
	    }
	    prediction = fit0 - coef[selection][1]*(fit0 - fit1);
	    
	    saveFit = ptype*fit0;
		fitk = ptype*fit1;
	    while(fitk < saveFit ) 
		{
			saveFit = fitk;
			step = s.moveSsize(ptype,step,k,n,m,R,round,factor,obfuname,signi,zsigni,neimins);
			fitk = ptype*f.getFitnessPermutation(step,n,m,factor,obfuname);
			numsteps ++;
			writesteps[numsteps] = fitk;
		}
	      
	    if(fitk != 0) score = Math.abs(prediction/(ptype*fitk) -1)*100;
	     else score = 100;
	    
	    double bestClose=prediction;
	    int mem =selection;
	    if(score > accuracy)
	    {
	      for(int i=1;i<predPos;i++)
		  {
	    	  prediction = fit0 - coef[i][1]*(fit0 - fit1);
	    	  if(fitk != 0) otherScore = Math.abs(prediction/(ptype*fitk) -1)*100;
	    	   else otherScore = 100;
    		  
	    	  if(Math.abs(r1/coef[i][0]-1) < 0.1)
	    	  {
	    		  //*********pick the best prediction from the vicinity
	    		  if(prediction <= bestClose) 
			    	{
	    			 mem=i; 
			    	 score = otherScore;
			    	 bestClose = prediction;
			    	}
	    		  //*************
	    	  }
	      }
	    }
	 	r.outputs("\nFitness found: " + (ptype*fitk) + " \n","Results", true);
	 	r.outputs("\nPrediction error(%): " + score + " using predictor " + selection + " out of " + (predPos-1) + " predictors \n","Results", true);
	 	r.outputs("\nSolution: " + Arrays.toString(step) + " \n","Results", true);
	    
	 return score;
	}
		
}

 

