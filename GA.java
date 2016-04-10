import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.*;

public class GA
{
	    Results r = new Results();
	    Problem pr = new Problem();
	    Fit f = new Fit();
	    
	    public GA()
	    {
		
	    }
		//pList is the list of patterns from the objective
		public void run(int n, int m, String obfuname, String fnamebest, int iterations, int poolSize, Random R, int[][] factor, int ptype, double globalbest) throws Exception 
		{
			int count_iterations = 0;
			double worst_fit = 0.0;
			int retrieve_max = 0;
			int retrieve_loc_opt = 0;
			ArrayList<int[]> list_loc_max = new ArrayList<int[]>(); 
			double[] loc_distance = new double[iterations];
			double[] loc_fitness = new double[iterations];
			int step = 1;
			double s_pool = 0.0;	
			double best_pool;
			
			// Create the pool
			ArrayList<int[]> pool = new ArrayList<int[]>(poolSize);
			int[] child = new int[n];
     		//global best
     		DataInputStream localDataInputStream = new DataInputStream(new FileInputStream(fnamebest));
   		    BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(localDataInputStream));
   		    String strLine;
   		    double global_fitness = Double.parseDouble(localBufferedReader.readLine());
   		    int[] global = new int[n];
   		    int[] n1 = new int[n];
   		    int[] n2 = new int[n];
   		    int idx = 0;
   		    int mem1,mem2;
   		    while( (strLine = localBufferedReader.readLine()) != null )
   		    {
   			 global[idx] = Integer.parseInt(strLine) - 1;
   			 idx++;
   		    }
			//fill the pool with random solutions
			for (int x=0;x<poolSize;x++) pool.add(pr.Permutation(n, R));
		   	//loop the GA's operations for a fixed number of iterations
			while(count_iterations < iterations) //OR (count_matches != q)
			{
				// Select two members
				mem1 = R.nextInt(pool.size());
				mem2 = R.nextInt(pool.size());
				while(mem1 == mem2)
				{
			      mem2 = R.nextInt(pool.size());
				}
				n1 = pool.get(mem1);
				n2 = pool.get(mem2);
				// CX preserving information Cross over the selected members
				child = CX(n1, n2, n, R);					
			  //if the child have a better fitness than the worst fit of the pool, add them
			  pool = updateWorstFitSolution(pool, mutate(child, R), n, m, factor, obfuname);	
			 count_iterations++; 
			}
			s_pool = 0;
			best_pool = f.getFitnessPermutation(pool.get(0),n,m,factor,obfuname);
			for (int x= 1;x < pool.size();x++) 
			if(best_pool > f.getFitnessPermutation(pool.get(x),n,m,factor,obfuname)) 
			  best_pool = f.getFitnessPermutation(pool.get(x),n,m,factor,obfuname); 
			double normalised = (double)globalbest/best_pool;
			if(normalised > 1) normalised = (double)1/normalised;
		    r.outputs(normalised + "\n",Integer.toString(800), true);
			//FileWriter out;
			//out = new FileWriter(Integer.toString(800),false);
		}		

			
		//update the pool by removing the worst fit if any of the children are better and add them to the pool
		public ArrayList<int[]>  updateWorstFitSolution(ArrayList<int[]> pool, int[] solution, int n, int m, int[][] factor, String obfuname)
		{
			double worst = f.getFitnessPermutation(pool.get(0),n,m,factor,obfuname);
			int foundWorst = 0;
			
			for (int x=1;x < pool.size();x++) 
			{				
			  if(worst < f.getFitnessPermutation(pool.get(x),n,m,factor,obfuname))
				  {
				   foundWorst = x;
				   worst = f.getFitnessPermutation(pool.get(x),n,m,factor,obfuname);			
				  }
			}
			if(f.getFitnessPermutation(solution,n,m,factor,obfuname) < worst)
			{
				pool.remove(foundWorst);
				pool.add(solution);
			}
			
			return pool;
		}
		
		// CX Crossover preserving information from the parents in permutation
				public int[] CX(int[] parent1, int[] parent2, int n, Random R) 
				{
					int[] child = new int[n];
					int[] flag = new int[2];
					int pick,cflag1,cflag2,assign,move;
					
		            //do the CX? operator	
					//first assignment
					if (parent1[0]==parent2[0]) child[0]=parent1[0];
					else
					{
					  pick = R.nextInt(2);		  
					  if(pick == 0) child[0] = parent1[0];
					    else child[0] = parent2[0];
					}
					//do the rest of assignments
					for(int i=1;i<n;i++)
					 if (parent1[i]==parent2[i]) child[i]=parent1[i];
					 else
					 {
					  cflag1 = 0;
					  cflag2 = 0;
					  for(int j=0;j<i;j++)
					    {
						  if(parent1[i] != child[j]) cflag1++;
						  if(parent2[i] != child[j]) cflag2++;
					    }	  
					  //System.out.println(cflag1 + " " + cflag2 + " " + i);
					  if((cflag1  == i)&(cflag2 == i))	 
					  {	  
					    pick = R.nextInt(2);		  
					    if(pick == 0) child[i] = parent1[i];
					      else child[i] = parent2[i];
					  }
					  else if(cflag1 == i) child[i] = parent1[i];
					  else if(cflag2 == i) child[i] = parent2[i];
					  else
					     {
						  assign = 0;
						  move = 0;
						  while(assign != 1)
						  {
						   if(parent1[move] == parent2[move]) move++;
						   else
						   {
						    cflag1 = 0;
						    cflag2 = 0;
						    for(int j=0;j<i;j++)
							 {
								  if(parent1[move] != child[j]) cflag1++;
								  if(parent2[move] != child[j]) cflag2++;
							 }
						    if(cflag1 == i) {child[i] = parent1[move];assign = 1;}
						     else if(cflag2 == i) {child[i] = parent2[move];assign = 1;}
						      else move ++; 
						   }
					      }
					     }
					 }
					
					return child;
				}
		
		
		// Mutation by replacing an element in a solution with another element
		public int[] mutate(int[] solution, Random R) 
		{	
			//int mutationPoint = R.nextInt(solution.length);
			int p1,p2,p3,p4,p5;
			
			 if (R.nextInt(1000) > 994) //0.5% the mutation rate
				{
				 //do distance preserving mutation dist=5
				 //chose a pair for swap
				 p1 = R.nextInt(solution.length);
				 p2 = R.nextInt(solution.length);
				 //System.out.println(Arrays.toString(solution));
				 while(p1 == p2)
				 {
				  p2 = R.nextInt(solution.length);
				 }
				 //do a swap
				 solution[p1] = solution[p1]^solution[p2];
				 solution[p2] = solution[p1]^solution[p2];
				 solution[p1] = solution[p1]^solution[p2];
				 //System.out.println(Arrays.toString(solution));
				 //keep the second
				 //chose the 3rd different than 2nd and first one
				 p3 = R.nextInt(solution.length);
				 while((p2 == p3)|(p3 == p1))
				 {
				  p3 = R.nextInt(solution.length);
				 }
				 //do a swap
				 solution[p2] = solution[p2]^solution[p3];
				 solution[p3] = solution[p2]^solution[p3];
				 solution[p2] = solution[p2]^solution[p3];
				 //System.out.println(Arrays.toString(solution));
				 //keep the 3rd
				 //chose the 4th different than 3rd and second
				 p4 = R.nextInt(solution.length);
				 while((p4 == p3)|(p4 == p2))
				 {
				  p4 = R.nextInt(solution.length);
				 }
				 //do a swap
				 solution[p3] = solution[p3]^solution[p4];
				 solution[p4] = solution[p3]^solution[p4];
				 solution[p3] = solution[p3]^solution[p4];
				//keep the 4th
				 //chose the 5th different than 4th and 3rd
				 p5 = R.nextInt(solution.length);
				 while((p5 == p4)|(p5 == p3))
				 {
				  p5 = R.nextInt(solution.length);
				 }
				 //do a swap
				 solution[p4] = solution[p4]^solution[p5];
				 solution[p5] = solution[p4]^solution[p5];
				 solution[p4] = solution[p4]^solution[p5];
				}
				
		  return solution;
		}
	//				
}
