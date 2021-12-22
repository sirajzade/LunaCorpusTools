/*
 * FileIndex.java 
 */

package indexedConcordancer;

import java.util.*;
import java.io.*;

class FileIndex {

	private Map theIndex = null;
	
	/**
	 * Constructor. 
	 * A file index is read into memory.
	 * @param filename - the name of the text.
	 * @throws IOExcepton.
	 */
	public FileIndex(String filename) 
	throws IOException {
		String indexname = filename+".idx";
		
		//InputStream is = new BufferedInputStream(new FileInputStream(indexname));
		InputStreamReader is = new InputStreamReader(new BufferedInputStream(new FileInputStream(indexname)), "UTF-8");
		Properties prop = new Properties();
		prop.load(is);
		is.close();
		Iterator it = prop.keySet().iterator();
		theIndex = new HashMap((int)(1.5 * prop.size()));
		while (it.hasNext()){
			String word = (String)it.next();
			String positions = prop.getProperty(word);
			StringTokenizer st = new StringTokenizer(positions);
			int numberOfPositions = st.countTokens();
			int pos[] = new int [numberOfPositions];
			for (int i = 0; i < numberOfPositions; i++){
				pos[i] = Integer.parseInt(st.nextToken());
			}
			theIndex.put(word, pos);
		}		
	}
	
	/**
	 * Get all positions of a word in the index.
	 * If the word is not contained in the index, null is returned.
	 * @param word - the word to look up.
	 * @return an array of file positions.
	 */
	
	int [] lookup(String word){
		int retval[] = (int[])theIndex.get(word);
		return retval;
	}
	
	public static void main(String args[])throws Exception{
		FileIndex fi = new FileIndex("E:\\Promotion\\Lemmatizer\\Lux\\loeschen3.xml");
		
		System.out.println(fi.lookup("dück"));
	}
		
	
} // end of the class FileIndex
