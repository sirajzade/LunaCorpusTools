/*
 * Lexicon.java
 */

package lunacorpustools.POSTagger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * This class implements a basic lexicon.
 */

public class Lexicon {

	private Map storage;

	/**
	 * Constructor.
	 */
	public Lexicon(BufferedReader file) throws IOException {
		storage = new HashMap();
		load(file);
	}

	/**
	 * Load a lexicon file.
	 * The lexicon is initialised from a file.  The file should contain
	 * one line per entry, with the data separated from the entry with
	 * an equal-sign (the standard Java property format).
	 * @param filename the name of the data file.
	 * @throws IOException if something goes wrong.
	 */
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
  //                              System.out.println("lexikon wird geladen 2");
			} 
			else if (st.countTokens() == 3){
				String retString = st.nextToken();
				String retTag = st.nextToken();
				double retDouble = Double.parseDouble(st.nextToken());
				storage.put(retString + " " + retTag, retDouble);
   //                             System.out.println("lexikon wird geladen 3" + retString);

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
	 * Look up a word in the dictionary.
	 * If the given word form is contained in the dictionary, the associated
	 * entry is returned as a String, otherwise null.  If the word is not
	 * found in the dictionary, suffix-related rules are applied if possible.
	 * These are defined in the class Suffix.
	 * @param word the word form to look up.
	 * @return the entry for the word if found, or null otherwise.
	 * @see Suffix.
	 */

	public double look(String word, String tag) {
		double retval = 0.0;
                // Zuerst schauen, on man das Token im Wörterbuch findet
		retval = looking(word, tag);
		if (retval != 0.0) {
			return retval;
		} 
                // Wenn nicht, dann analysieren
                else {
                        retval = analyse(word, tag);
		}
		return retval;
	}


	private double analyse (String word, String Tag){
		if(Character.isDigit(word.charAt(0)) && Tag.equals("NUM")){
			return 1.0;
		}
		else if(word.length() < 2 && !Character.isLetter(word.charAt(0)) && !Character.isDigit(word.charAt(0)) && Tag.equals("$")){
			return 1.0;
		}
		else {
                        return 0.0;
            }
	}

	private String eiflerRegel (String word){
		String retval = word;
		// for avoiding the Eifler Regel
		if(word.charAt(word.length()-1)== 'e' ||
				word.charAt(word.length()-1)== 'a' ||
				word.charAt(word.length()-1)== 'u' ||
				word.charAt(word.length()-1)== 'o' ||
                                word.charAt(word.length()-1)== 'ë' ||
				word.charAt(word.length()-1)== 'i' ) {

			retval = word.concat("n");
		}
		return retval;
	}

	private double storageAbrage(String word, String tag) {
		double retval = 0.0;
		if (storage.containsKey(word + " " + tag)) {
			retval = (Double) storage.get(word + " " + tag);
		}
		return retval;
	}


	private double looking (String word, String tag){

		double entry = 0.0;

		entry = storageAbrage(word, tag);

		if(entry == 0.0) {
			entry = storageAbrage(eiflerRegel(word), tag);
		}
//		if(entry == 0.0) {
//			if (Character.isUpperCase(word.charAt(0))){
//				entry = storageAbrage(word.toLowerCase(), tag);
//				if (entry == 0.0){
//					entry = storageAbrage(eiflerRegel(word.toLowerCase()), tag);
//				}
//			}
//			else {
//				String upperWord = Character.toUpperCase(word.charAt(0)) + word.substring(1);
//				entry = storageAbrage(upperWord, tag);
//				if (entry == 0.0){
//					entry = storageAbrage(eiflerRegel(upperWord), tag);
//				}
//			}
//		}
		return entry;	
	}




	public static String readFile( String file ) throws IOException {
		BufferedReader reader = new BufferedReader( new FileReader (file));
		String         line = null;
		StringBuilder  stringBuilder = new StringBuilder();
		String         ls = System.getProperty("line.separator");

		while( ( line = reader.readLine() ) != null ) {
			stringBuilder.append( line );
			stringBuilder.append( ls );
		}

		return stringBuilder.toString();
	}

	public static void main(String args[]) throws IOException{
		//	Lexicon lex = new Lexicon(readFile("C:\\Users\\joshgun.sirajzade\\Documents\\NetBeansProjects\\LunaCorpusTools\\src\\lunacorpustools\\POSTagger\\newlexicon.prop"));


		//        String possTags = lex.looking(".");
		//	System.out.println("here: " + lex.look("en", "NOUN"));
		// Nur ein Test
	}


}	// end of class Lexicon
