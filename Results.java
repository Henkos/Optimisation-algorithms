import java.io.*;

public class Results 
{
 	
 //
 //dump the results into a file
 public void outputs(String aString, String FileName, boolean append)
 {
  try
  {
   FileWriter out = new FileWriter(FileName,append);
   out.append(aString);
   out.close();
  }
  catch (IOException e)
  {
   System.out.println("I/O error.");
  }
 }
 
}
