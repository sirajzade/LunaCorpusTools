package wordList;

public class WordFreq implements Comparable<WordFreq> {
	private String word;
	private int freq;	

	// Here is our constructor, to create the object
	public WordFreq (String w, int f){
		word = w;
		freq = f;
	}

	public String getWord(){
		return (word);
	}

	public int getFreq(){
		return (freq);
	}

	//Definition of the order while comparing to another object

	public int compareTo(WordFreq other) {
		return (word.compareTo(other.word));
	}
}// end of the class WordFreq
