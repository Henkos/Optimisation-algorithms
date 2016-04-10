import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

public class main 
{
	public static Properties props1 = new Properties();
	
	/**
	 * @param args
	 * 
	 * 
	 */
    static Procedure p = new Procedure();
    static Results rf = new Results();
    
	
	public static void main(String[] args)  throws Exception
	{
		loadProps(args[0]);
		//problem type 1 for minimisation,-1 for maximisation
		int ptype = Integer.parseInt(props1.getProperty("problem type"));
		//objective function name for fitness calculation
		String obfuname = props1.getProperty("objective function name");
		//statistical significance level
		double signi = Double.parseDouble(props1.getProperty("significance"));
		//z-score for the statistical significance level
		double zsigni = Double.parseDouble(props1.getProperty("z-score for significance"));
		//minimum sample size for the neigbhourhood exploring
		int neimins = Integer.parseInt(props1.getProperty("neighbourhoodMinSample"));
		//predictor acceptance threshold that determines how similar are two predictors
		double pacct = Double.parseDouble(props1.getProperty("Predictor acceptance threshold"));	
		//different indexes for different problems	
		int n = Integer.parseInt(props1.getProperty("jobs"));
		int m = Integer.parseInt(props1.getProperty("machines"));
		int o = Integer.parseInt(props1.getProperty("matrix cardinality"));
		//validation sample
		int v = Integer.parseInt(props1.getProperty("validationSample"));
		//instance file name
		String fname = props1.getProperty("PinstanceName");
		//best file name
		String fnamebest = props1.getProperty("PBestName");
		//number of rounds for statistical significance
		int rounds = Integer.parseInt(props1.getProperty("rounds"));
		//seed
		long seed = Long.parseLong(props1.getProperty("seed"));
		//
		Random R = new Random(seed);
	    //ptype= minimisation/maximisation, 
	    //parameter 1, parameter 2 specify matrix sizes n*m or n*n and used in fitness evaluation 
	    //parameter 3 is used if second matrix is used to specify the size
	    //QAP instances 2 matrices n*n : o,o,2*o
	    //LOP instances 1 matrix n*n : o,o,o
	    //PFSP instances 1 matrix m*n : n,m,m
		 for(int i = 0; i < rounds; i++)
		 {
			 if(obfuname.equals("PFSP")) p.run(ptype,n,m,m, v, R, i, fname,fnamebest,obfuname,signi,zsigni,neimins,pacct);
			  else if(obfuname.equals("LOP")) p.run(ptype,o,o,o, v, R, i, fname,fnamebest,obfuname,signi,zsigni,neimins,pacct);
			   else p.run(ptype,o,o,2*o, v, R, i, fname,fnamebest,obfuname,signi,zsigni,neimins,pacct);
		    System.out.println(" Round: " + i + "\n");
		 }	 	
	}

	public static void loadProps(String propName)
	{
		try
		{
			props1.loadFromXML(new FileInputStream(propName));
		} 
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} 
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
