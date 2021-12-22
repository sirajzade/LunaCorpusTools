package lunacorpustools.POSTagger;

import java.util.Comparator;

import wordList.WordFreq;



public class LengthComparator implements Comparator {

	
	public int compare(Object o1, Object o2) {
		int retval = 0;
		if (o1 instanceof String && o2 instanceof String) {
			
			String c1 = (String) o1;
			String c2 = (String) o2;
			
			if (c1.length() > c2.length()) retval = -1;
			if (c1.length() < c2.length()) retval =  1;
	}
		else {
			throw new ClassCastException("LengthComparator: illegal arguments.");
		}
		return (retval);
		
	}

	
}
