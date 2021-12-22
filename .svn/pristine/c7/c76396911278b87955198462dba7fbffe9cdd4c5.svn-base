/*
 * Suffix.java
 */

package lunacorpustools.POSTagger;

import lunacorpustools.POSTagger.LengthComparator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import wordList.FreqList;


/**
 * This class implements a suffix-matching routine for the
 * POSTagger.  The algorithm is as described in McEnery & Wilson (1996),
 * page 142.
 */

class Suffix {

	private Map<String, Double> storage;
	private String [] allSuffix;

	public Suffix (BufferedReader suffixMat) throws IOException{
		storage = new HashMap<String, Double>();
		load(suffixMat);
		allSuffix = getAllSuffix();
	}


	public String [] getAllSuffix() {

		FreqList fl = new FreqList();
		Iterator<String> it = storage.keySet().iterator();

		while (it.hasNext()){
			String entry = it.next();
			fl.add(entry.substring(0, entry.indexOf(" ")));
		}

		Iterator iter = fl.iterator();
		String s [] = new String[fl.getT()];
		int i = 0;
		while (iter.hasNext()){
			s[i] = (String)iter.next();
			i++;
		}

		Comparator lengthC = new LengthComparator();	
		Arrays.sort(s, lengthC);
		return s;
	}

	public double look (String word, String tag){
		double retval = 0.0;
		for (int a = 0; a < allSuffix.length; a++){

			if(word.endsWith(allSuffix[a])){

				if (storage.containsKey(allSuffix[a] + " " +tag)) {
					retval = storage.get(allSuffix[a] + " "+tag);
				}
			}
		}

		return retval;
	}



	public void load (BufferedReader br) throws IOException {
		String line = br.readLine();

		while (line != null){
			StringTokenizer st = new StringTokenizer(line);
			if (st.countTokens() < 2){
				System.err.println("Insufficient line: `"+line+"�");
			} else if (st.countTokens() == 2){

				String retString = st.nextToken();
				double retDouble = Double.parseDouble(st.nextToken());
				storage.put(retString, retDouble);

			} 
			else if (st.countTokens() == 3){
				String retString = st.nextToken();
				String retTag = st.nextToken();
				double retDouble = Double.parseDouble(st.nextToken());
				storage.put(retString + " " + retTag, retDouble);


			}



			else {
				StringBuffer word = new StringBuffer(st.nextToken());
				while (st.countTokens() > 2) {
					word.append(' ');
					word.append(st.nextToken());
				}
				String retString = word.toString();
				double retDouble = Double.parseDouble(st.nextToken());
				storage.put (retString, retDouble);
				System.out.println(retString + " _ " + retDouble);
			}
			line = br.readLine();

		}
	}

	/**
	 * Match a suffix.
	 * The word form to be matched is analysed with respect to its ending,
	 * and if a matching suffix rule has been found, a String describing
	 * the likely tag choices for that word is returned.  If no rule could
	 * be matched, null is returned.  The rules are applied using longest
	 * match.  The default match is "V J N".
	 * @param word the word form to match.
	 * @param lexicon the lexicon for subsequent searches.
	 * @return a String describing the possible tags or null.
	 */
	public static String match(String word, Lexicon lexicon) {
		if(word.endsWith("schaft")) return("N");
		if(word.endsWith("keet")) return("N");
		if(word.endsWith("ung")) return("N V");
		if(word.endsWith("ant")) return("N V");
		if(word.endsWith("ismus")) return("N");
		if(word.endsWith("anz")) return("N R");
		if(word.endsWith("sten")) return("N A");
		/*
	if(word.endsWith("ant")) return("J N");
	if(word.endsWith("ed")) return("V J@");
	if(word.endsWith("er")) return("N V J");
	if(word.endsWith("ers")) return("N V");
	if(word.endsWith("est")) return("J");
	if(word.endsWith("ful")) return("J N%");
	if(word.endsWith("ing")) return("N V J@");
	if(word.endsWith("ings")) return("N");
	if(word.endsWith("ity")) return("N");
	if(word.endsWith("ive")) return(null);
	if(word.endsWith("less")) return("J");
	if(word.endsWith("ly")) return("R J@");
	if(word.endsWith("s")) {
		return(lexicon.lookup(word.substring(0,word.length()-2)));
	}
		 */
		return(null);
	}


	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("E:\\Promotion\\Rodange-POS\\suf.txt"));
		Suffix suf = new Suffix(br);
		System.out.println(suf.look("eeschtler", "VERB"));
	}




}	// end of class Suffix
