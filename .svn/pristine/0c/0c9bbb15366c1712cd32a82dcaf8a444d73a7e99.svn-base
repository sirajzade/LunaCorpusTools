/*
 * FileTokeniser.java
 */

package Tokenizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;


public class FileTokeniser {

	private BufferedReader input;
	private StringTokenizer tokeniser;
	private String nextToken;

	/**
	 * Constructor.
	 * @param infile - the name of the input file.
	 * @throws IOException.
	 */
	public FileTokeniser(String infile)
			throws IOException {

		//FileInputStream is = new FileInputStream(infile);
		//input = new BufferedReader (new InputStreamReader( is, "UTF8"));
		input = new BufferedReader (new FileReader(infile));
		//	do {
		//		String line = input.readLine();
		//		//System.out.println(line);
		//		tokeniser = new StringTokenizer(PreLuxTokeniser.tokenizeInWords(line));
		//	} while (!tokeniser.hasMoreTokens());
		//	nextToken = tokeniser.nextToken();

	}


	/**
	 * Chek if more tokens are available.
	 * @return true if more tokens are available, false otherwise.
	 */
	public boolean hasMoreTokens(){
		if (nextToken == null){
			return (false);
		} else {
			return (true);
		}
	}

	/**
	 * Read the next token.
	 * @return the available token or null at the end of the file.
	 */

	public String getNextToken (){
		String retval = nextToken;
		if (tokeniser.hasMoreTokens()) { // for more available on this line
			nextToken = tokeniser.nextToken();
		} else {  // read next line
			try {
				nextToken = null;
				String line = input.readLine();
				while (line != null && nextToken == null){
					if (line != null){
						//					tokeniser = new StringTokenizer(PreLuxTokeniser.tokenizeInWords(line));
						//					if (tokeniser.hasMoreTokens()){
						//						nextToken = tokeniser.nextToken();
						//					} else {
						//						line = input.readLine();
						//					}
					}
				}
				input.close();
			} catch (IOException exc) {
				System.err.println("FileTokeniser: "+exc);
			}
		}
		return (retval);
	}

	/**
	 * Close the input file once tokenisation has finished.
	 */

	public void close()
			throws IOException {
		input.close();
		input = null;
	}

	/**
	 * Main method for testing.
	 */

	public static void main (String args[])
			throws IOException {
		if (args.length < 1) {
			System.err.println("Usage: FileTokeniser <filename>");
		} else {
			FileTokeniser ft = new FileTokeniser(args[0]);
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(args[0]+".tok")));
			while (ft.hasMoreTokens()){
				String token = ft.getNextToken();
				//System.out.println(token);
				pw.println(token);
			}
			ft.close();
			pw.close();
			System.out.println("Die Daten sind in der Datei: " + args[0] + ".tok");
		}
	}


} // end of the class FileTokeniser
