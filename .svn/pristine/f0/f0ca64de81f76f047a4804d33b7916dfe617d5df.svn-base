/*
 * PreTokeniser.java
 */

package Tokenizer;

import java.util.StringTokenizer;

public class PreTokeniser {

	public static final String PUNCT = "!$\"\u00a3%^&*()_+=#{}[];:`/?,. \t\n";

	public static String tokenise (String line){
		StringTokenizer st = new StringTokenizer (line, PUNCT, true);
		String tok1 = " ";
		String tok2 = " ";
		boolean skip = false;
		StringBuffer retval = new StringBuffer();

		while (st.hasMoreTokens()){
			tok1 = tok2;
			tok2 = st.nextToken();
			if (",".equals(tok1)
					||  ".".equals(tok1)
					||  "-".equals(tok1)
					||  "'".equals(tok1))  { // special punctuation mark
				if (Character.isWhitespace(tok2.charAt(0))){
					tok1 = " "+tok1;			
				} else {
					skip = true;
				}

			} else {
				if (skip == false) {
					tok1 = " "+tok1;
				}
				skip = false;
			}

			retval.append(tok1);
		}
		if (skip == false) {
			tok2 = " "+tok2;
		}

		retval.append(tok2);

		//the String method to lower case added by me on 14.11.2008

		return (retval.toString().toLowerCase());

	}

	public static void main (String args[]){
		for (int i = 0; i < args.length; i++){
			System.out.println(PreTokeniser.tokenise(args[i]));			
		}
	}

} // end of the class PreTokeniser
