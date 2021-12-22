package wordList;

import java.util.*;

public class FreqComparator implements Comparator {

	public int compare(Object o1, Object o2){
		int retval = 0;
		if (o1 instanceof WordFreq && o2 instanceof WordFreq) {

			WordFreq c1 = (WordFreq) o1;
			WordFreq c2 = (WordFreq) o2;

			if (c1.getFreq() > c2.getFreq()) retval = -1;
			if (c1.getFreq() < c2.getFreq()) retval =  1;
		}
		else {
			throw new ClassCastException("FreqComparator: illegal arguments.");
		}
		return (retval);
	}

} // the end of the class FreqComparator
