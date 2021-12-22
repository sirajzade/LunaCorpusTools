package lunacorpustools.POSTagger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

public class WordLength {

	Vector<WordLengthData> storage = new Vector<WordLengthData>();
	
	public WordLength (BufferedReader file) throws IOException{
		load(file);
	}
	
	public void load (BufferedReader br)
	throws IOException {
		
		String line = br.readLine();
		
		while (line != null){
			StringTokenizer st = new StringTokenizer(line);
			if (st.countTokens() < 3){
				System.err.println("Insufficient line: `"+line+"�");
			} else {
				int length = Integer.parseInt(st.nextToken());
				String pos = st.nextToken();
				double p = Double.parseDouble(st.nextToken());
				WordLengthData wL = new WordLengthData(length, pos, p);
				storage.add(wL);
			}
			line = br.readLine();

		} // the end of while
	} // the end of the method load
	
	
	public double look (String word, String pos){
	double p = 0.0;
		Iterator<WordLengthData> it = storage.iterator();
			while (it.hasNext()){
				WordLengthData wL = it.next();
				if (wL.getLength() == word.length() && wL.getPos().equals(pos)){
					p = wL.getP();
                                        if (word.length() == 1 && Character.isDigit(word.charAt(0)) && pos.equals("NUM")){
                                            p = 1.0; 
                                        }
				}
			}
	return p;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		String file = "E:\\Promotion\\Rodange-POS\\length.txt";
		BufferedReader br = new BufferedReader(new FileReader(file));
		
                WordLength test = new WordLength(br);
		System.out.println(test.look("a", "PUNC"));
	}

}
