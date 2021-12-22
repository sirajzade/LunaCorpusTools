/*
 * IndexCreator.java
 */

package indexedConcordancer;


import java.io.*;
import java.util.*;

public class IndexCreator implements WordPositionReceiver {

	private HashMap wordPositions;

	public IndexCreator (String filename)
			throws IOException {

		//FileInputStream is = new FileInputStream(filename);
		//BufferedReader input = new BufferedReader (new InputStreamReader( is, "UTF-8"));

		BufferedReader input = new BufferedReader(new FileReader(filename));
		wordPositions = new HashMap();
		PositionLister pl = new PositionLister(input,this);
		input.close();
	}

	public void writeIndex (PrintWriter output) {
		Iterator it = wordPositions.keySet().iterator();
		while (it.hasNext()){
			String key = (String)it.next();
			Vector positions = (Vector)wordPositions.get(key);
			output.print(key+"=");
			for (int i = 0; i < positions.size(); i++){
				output.print(positions.elementAt(i)+" ");
			}
			output.println(" ");
		}
	}


	public void receiveWordPosition(String word, int position) {
		// this is here for ignoring Case 
		Vector list = (Vector)wordPositions.get(word.toLowerCase());
		if (list == null) {
			list = new Vector();
			wordPositions.put(word.toLowerCase(),list);
		}
		list.addElement(new Integer(position));
	}

	public static void main (String args[])
			throws IOException {
		if (args.length != 1) {
			System.err.println("uasge: IndexCreator filename");
		} else {
			IndexCreator ic = new IndexCreator(args[0]);
			//PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(args[0]+".idx")));

			BufferedWriter out = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(args[0]+".idx"), "UTF-8"));
			PrintWriter pw = new PrintWriter(out);

			ic.writeIndex(pw);
			pw.close();
			System.out.println(args[0]+".idx");
		}
	}

} // end of the class IndexCreator
