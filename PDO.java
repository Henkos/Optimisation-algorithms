import java.lang.reflect.Array;
import java.util.*;

public class PDO
{
	//	
	Fit f = new Fit();
	Problem pr = new Problem();
    Results r = new Results();
    Sampling s = new Sampling();

	public PDO()
	{

	}
	//exploration based on predictive local search moves
	public double[][] create(int ptype,int n, int m, Random R,int k,int round,int[][] factor,String obfuname,double signi,double zsigni,int neimins,double pacct)
	{
		int[] randomS = new int[n];
		int[] step = new int[n];

		int howOften = 100;
		int numbPred = 1;//
		double fit1 = 0,fit0 = 0,fitk;//
		int count_LS = 0;
		double[][] predcoef = new double[10000][3];
		double saveFit;

		     randomS = pr.Permutation(n, R);
			 fit0 = f.getFitnessPermutation(randomS,n,m,factor,obfuname);
			 while(fit0 == 0)
			 {
			  randomS = pr.Permutation(n, R);
			  fit0 = f.getFitnessPermutation(randomS,n,m,factor,obfuname);
			 }
			 step = s.moveSsize(ptype,randomS,k,n,m,R,round,factor,obfuname,signi,zsigni,neimins);
			 fit1 = f.getFitnessPermutation(step,n,m,factor,obfuname);
			 while(fit1 == fit0)
			 { 	 
			  randomS = pr.Permutation(n, R);
			  fit0 = f.getFitnessPermutation(randomS,n,m,factor,obfuname);
			  step = s.moveSsize(ptype,randomS,k,n,m,R,round,factor,obfuname,signi,zsigni,neimins);
			  fit1 = f.getFitnessPermutation(step,n,m,factor,obfuname);
			 } 
		     fitk = ptype*fit1;
			 saveFit = ptype*fit0;
			while(fitk < saveFit ) //
			{
				saveFit = fitk;
				step = s.moveSsize(ptype,step,k,n,m,R,round,factor,obfuname,signi,zsigni,neimins);
				fitk = ptype*f.getFitnessPermutation(step,n,m,factor,obfuname);
			}
		predcoef[numbPred][0] = ptype*(fit0 - fit1)/(fit0/n);
		predcoef[numbPred][1] = (fit0 - ptype*fitk)/(fit0 - fit1);
		predcoef[numbPred][2] = ptype*fitk;
		
		double combtolerance = 0;//
		double newcombtolerance = 0;//
		double initial = 0;
		double measure = 0;
		double best_prediction = 0;
		double minMeasure;
		int closest = 1,predSelected = 1;
		boolean createnew;
		double simila;
		double avessimi,savessimi=0;
		//define the tolerance level for creating new predictors 
		//as the fitness of an initial(random) solution divided to the difference between an initial solution
		//and an optimised solution(after the SD)
		double tolLevel = fit0/(ptype*fit0-fitk);
        int countP = numbPred;
        int lastcountP = 0;
        
		while(countP != lastcountP)
		{	  
			int[] mem_sol = new int[n];
			double coef1 = 0,saveCoef1 = 0,predCoef2,coef2,bestcoef2=0;
			double predFitness;
            minMeasure = 100;
            best_prediction = ptype*f.getFitnessPermutation(pr.Permutation(n, R),n,m,factor,obfuname);
				 randomS = pr.Permutation(n, R);
				 fit0 = f.getFitnessPermutation(randomS,n,m,factor,obfuname);
				 while(fit0 == 0)
				 {
				  randomS = pr.Permutation(n, R);
				  fit0 = f.getFitnessPermutation(randomS,n,m,factor,obfuname);
				 } 
				 step = s.moveSsize(ptype,randomS,k,n,m,R,round,factor,obfuname,signi,zsigni,neimins);
				 fit1 = f.getFitnessPermutation(step,n,m,factor,obfuname);
				 while(fit1 == fit0)
				 { 
				  randomS = pr.Permutation(n, R);
			      fit0 = f.getFitnessPermutation(randomS,n,m,factor,obfuname);
				  step = s.moveSsize(ptype,randomS,k,n,m,R,round,factor,obfuname,signi,zsigni,neimins);
				  fit1 = f.getFitnessPermutation(step,n,m,factor,obfuname);
				 } 
		
				 coef1 = ptype*((fit0 - fit1)/(fit0/n));
			    
			saveFit = ptype*fit0;
			fitk = ptype*fit1;
			while(fitk < saveFit )
			{
				saveFit = fitk;
				step = s.moveSsize(ptype,step,k,n,m,R,round,factor,obfuname,signi,zsigni,neimins);//mem_sol=step
				fitk = ptype*f.getFitnessPermutation(step,n,m,factor,obfuname);
			}
			coef2 = ptype*(fit0 - ptype*fitk)/(fit0/n);//initial=fit0
			//********************************
			count_LS ++;
			//similarity distances
			createnew = true;
			double ssimila = 0;
			for(int i=1;i<=numbPred;i++)
			{		
				//canberra distance
				simila = Math.abs(coef1 - predcoef[i][0])/(Math.abs(coef1) + Math.abs(predcoef[i][0])) + Math.abs(coef2 - predcoef[i][0]*predcoef[i][1])/(Math.abs(coef2) + Math.abs(predcoef[i][0]*predcoef[i][1]));
				ssimila += simila;
				if(simila < pacct) createnew = false;		
			}
			avessimi = (double)ssimila/numbPred;
			if(createnew)
				{	  
					   numbPred++;
					   predcoef[numbPred][0] = coef1;
					   predcoef[numbPred][1] = coef2/coef1;
					   savessimi += avessimi;
					   predcoef[numbPred][2] = ptype*fitk;      
				} 
	        
			//every HowOften LocalSearches check if new predictors are created
		    if((count_LS%howOften) == 0) 
		    {
		    	lastcountP = countP;
		    	countP = numbPred;
		    }
		}
	  
	  return predcoef;	
	}
	
}
