import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;


public class hashtagcounter {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		BufferedReader brtext = null;
		Hashtable<String, HeapNode> table = new Hashtable<>();
		FibonacciHeap fib = new FibonacciHeap();
		
		try {

			String strline;
			//Reading input from the file
			FileInputStream fstream = new FileInputStream(args[0]);
			brtext = new BufferedReader(new InputStreamReader(fstream));
			int i = 0;
			while ((strline = brtext.readLine()) != null) 
			{
				if(strline.charAt(0) == '#')
				{
					String mystring[] = strline.split("#|\\s+");
					if(table.containsKey(mystring[1]))
					{
						//Call IncreaseKey if we encounter a similar hashtag
						fib.IncreaseKey(table.get(mystring[1]), Integer.parseInt(mystring[2]));
					}
					else
						//Insert new node into the fibonacci heap and store the node address in the hashtable
						table.put(mystring[1], fib.InsertHeapNodes(Integer.parseInt(mystring[2])));
				}
				else if(strline.trim().toLowerCase().equals("stop"))
				{
					System.out.print("\n\nProgram terminated");
					return;
				}
				else
				{
					String result = "";
					int count = 0;
					File fout = new File("output_file.txt");	
					FileWriter fw = new FileWriter(fout, true);
				 
					BufferedWriter bw = new BufferedWriter(fw);
					
					count = Integer.parseInt(strline);
					HeapNode[] removedNodes = new HeapNode[count];
					for(int j = 0; j < count; j++)
					{
						//store the removed node in an array for reinsertion later
						removedNodes[j] = fib.removeMax();
						
						String key= null;
						HeapNode value = removedNodes[j];
						for(Map.Entry entry: table.entrySet())
						{
							if(value.equals(entry.getValue()))
							{
								//get the matching key - hashtag for the corresponding value
								key = (String) entry.getKey();
								break;
							}
						}
						result = result + key + ",";
			        }
					if (result != null && result.length() > 0 && result.charAt(result.length()-1)==',') 
					{
						result = result.substring(0, result.length()-1);
					}
					bw.write(result);
					bw.newLine();
					bw.close();
					
					for(int k = 0; k < count; k++)
					{
						//reinsert removed node back into the heap
						fib.reinsertHeapNode(removedNodes[k]);
					}
				}
			}
			fstream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

}
