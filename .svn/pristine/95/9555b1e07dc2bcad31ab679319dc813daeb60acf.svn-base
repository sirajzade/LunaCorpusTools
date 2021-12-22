/*
 * IndexedConcordancer.java 
 */

package indexedConcordancer;

import java.io.*;
import java.util.*;

public class IndexedConcordancer {
	private FileIndex theIndex = null;
	private RandomAccessFile theText = null;
	
	/**
	 * Constructor
	 * @param filename - the name of the file
	 * @throws IOException
	 */
	
	public IndexedConcordancer(String filename)
	throws IOException {
		theIndex = new FileIndex(filename);
		theText = new RandomAccessFile(filename, "r");
	}
	
	/**
	 * Get all concordance lines for a certain word.
	 * If the word has not been found, null is returned
	 * @param word - the word to look up
	 * @param width - the width of the line
	 * @return an array of all concordance lines or null
	 * @throws IOException
	 */
	
	public String[] getLines(String word, int width)
	throws IOException {
		String retval[] = null;
		int positions[] = theIndex.lookup(word.toLowerCase());
		if (positions != null) {
			retval = new String[positions.length];
			StringBuffer line = new StringBuffer();
			int offset = (width - word.length()) / 2;
			int end = (int)theText.length();
			
			for (int i = 0; i < positions.length; i++){
				line.setLength(0);
				int start = positions[i] - offset;
				
				while (start < 0) {
					line.append(' ');
					start++;
				}
				theText.seek(start);
				while (line.length() < width) {
					if (start < end) {
						int c = theText.read();
						
						if (c > - 1) {
							char ch = (char)c;
							if (Character.isISOControl(ch)) ch = ' ';
							line.append(ch);			
						}
						start++;
					} else {
						line.append(' ');
					}
				}
				retval[i] = line.toString();
				
			}
		} else {
			System.err.println("No index positions were found for the word: "+ word);
		}
		
		// Making the concordance list lowercase by me on 16.11.2008
		
		if (retval != null){
		String lower[] = new String[retval.length];
		
		for (int u = 0; u < retval.length; u++){
			lower[u] = retval[u].toLowerCase();
		}
		return (retval);
		}
		else  return (retval);
	}
	
	public String [] getSentenceTokenisedLines(String word, int width) throws IOException{
		String retval[] = null;
		retval = getLines(word, width);
		if (retval!=null){
		for (int i = 0; i < retval.length; i++) {
			
			if (retval[i].charAt(0) == ' '){
				retval[i] = "en" + retval[i];
			}
			
			if (retval[i].charAt(retval[i].length() - 1) == ' '){
				retval[i] = retval[i] + "en";
			}
			
		}
		}
		return retval;
	}
	
	public String[] getLinesIgnoreCase(String word, int width)
	throws IOException {
		String retval[] = null;
		
		int positions[] = theIndex.lookup(word);
		int lowPositions [] = theIndex.lookup(word.toLowerCase());
			
			if (lowPositions[0] != positions[0]){
				System.arraycopy(lowPositions, 0, positions, positions.length, lowPositions.length);
			}
		
		if (positions != null) {
			retval = new String[positions.length];
			StringBuffer line = new StringBuffer();
			int offset = (width - word.length()) / 2;
			int end = (int)theText.length();
			
			for (int i = 0; i < positions.length; i++){
				line.setLength(0);
				int start = positions[i] - offset;
				
				while (start < 0) {
					line.append(' ');
					start++;
				}
				theText.seek(start);
				while (line.length() < width) {
					if (start < end) {
						int c = theText.read();
						
						if (c > - 1) {
							char ch = (char)c;
							if (Character.isISOControl(ch)) ch = ' ';
							line.append(ch);			
						}
						start++;
					} else {
						line.append(' ');
					}
				}
				retval[i] = line.toString();
				
			}
		}
		return retval;
	}
	
	/**
	 * Main method for testing
	 * @param args a filename and a word to look up
	 */
	
	public static void main (String args[])
	throws IOException {
		IndexedConcordancer ic = new IndexedConcordancer("E:\\Promotion\\Rodange-Kollokationen\\tokenisedSentences.txt");
		String theWord = "Jomer";
		String lines[] = ic.getSentenceTokenisedLines(theWord, 75);
		if (lines == null) {
			System.err.println("No lines found for `"+theWord+" ");
		} else {
			for (int i = 0; i < lines.length; i++){
				System.out.println(lines[i]);
			}
			System.err.println(lines.length+" lines found for `"+theWord+" ");
		}
	}

} // end of the class IndexedConcordancer
