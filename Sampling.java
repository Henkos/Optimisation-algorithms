import java.util.*;
//sampling with dynamic stopping criteria-the best from the sample is tracked
public class Sampling
{	    
	    Problem pr = new Problem();
	    Fit f = new Fit();
	    Results r = new Results();
	    
	    public Sampling()
	    {
		
	    }	
	    
	    public int[] moveSsize(int ptype,int[] movesolution,int k, int n,int m, Random R,int round,int[][] factor,String obfuname,double signi,double zsigni,int neimins)
	    {
		 int[] move = new int[n];
		 int[] memove = movesolution.clone();
		 int[] copy = new int[n];
		 int sample = 0;
		 double s_ratio = 0,s_ratio2 = 0;
		 double dif_average = 1;
		 double wr = 1;
		 double avg,avg2;
		 int sw1,sw2;
		 int[][] pairs = new int[n*(n-1)/2][3];
		 int nodp=-1;
		 double current, best=ptype*f.getFitnessPermutation(movesolution,n,m,factor,obfuname);
		 double diff;
		 int membestsample = 0;
		 //int threshold = 500, patience=0;
		
		 sw1 = R.nextInt(n);
		 sw2 = R.nextInt(n);
		 while(sw1 == sw2)
		 {
	      sw2 = R.nextInt(n);
		 } 
		 pairs[sample][0]=sw1;
		 pairs[sample][1]=sw2;
		
		 //old sampling
  	     while((wr > signi)&(sample < (n*(n-1)/2-1)))	 
		 {	  
			//do the sampling random?
			//or deterministically in sequence starting it position (i,i+1)....until (n-1,n)
			copy = movesolution.clone();
			if(k == 2) move = pr.mutate2(copy,sw1,sw2);
			 else if(k == 3) move = pr.mutate3(copy, n, R,sw1,sw2);
			  else if(k == 4) move = pr.mutatek(ptype,copy, n,m, R,sw1,sw2,factor,obfuname);
		       else move = pr.mutatek_deterio(ptype,copy, n,m, R,sw1,sw2,factor,obfuname);
			pairs[sample][2]=ptype*(int)f.getFitnessPermutation(move,n,m,factor,obfuname);
			if(sample > 0)
			{		
			 if(best > pairs[sample][2]) 
			 {
			  best = pairs[sample][2];
			  memove =  move.clone();
			 }
			s_ratio += ptype*best;
			s_ratio2 += best*best;
			avg = s_ratio/sample;
			//System.out.println(avg);
			avg2 = s_ratio2/sample;
			if(sample >= neimins)
				 wr = (2*zsigni)/Math.sqrt(sample)*Math.sqrt(avg2-avg*avg)/avg;
			}
			//check for duplicates
			while(nodp < (sample+1))
			{
				sw1 = R.nextInt(n);
				sw2 = R.nextInt(n);
				while(sw1 == sw2)
				{
				 sw2 = R.nextInt(n);
				} 
				nodp=0;
				for(int c=0;c<=sample;c++)
				{
					if((sw1 != pairs[c][0])|(sw2 != pairs[c][1])) 
						{
						 if((sw1 != pairs[c][1])|(sw2 != pairs[c][0])) nodp++;
						}
				}
			}
			sample ++;		
			pairs[sample][0]=sw1;
			pairs[sample][1]=sw2;
	     }	
  	     		 
		 return memove;
	    } 
}	    