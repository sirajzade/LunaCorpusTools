package lunacorpustools.POSTagger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class UpperCase {

	private Map storage; 
	
	public  UpperCase(String UpperMat)throws IOException {
		storage = new HashMap();
		load(UpperMat);
	}
	
	public double look (String word, String tag){
		double retval = 0.0;
		
			if(Character.isUpperCase(word.charAt(0))){
				
				if (storage.containsKey(tag)) {			
					retval = (Double)storage.get(tag);
				}
			}
			else{
				if (storage.containsKey(tag)) {			
					retval = 1 - (Double)storage.get(tag);
				}
			}
				
		
		return retval;
	}
	
	
	public void load (String UpperMat)
	throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(UpperMat));
		
		String line = br.readLine();
		
		while (line != null){
			StringTokenizer st = new StringTokenizer(line);
			if (st.countTokens() < 2){
				System.err.println("Insufficient line: `"+line+"�");
			} else if (st.countTokens() == 2){
			
				String retString = st.nextToken();
				double retDouble = Double.parseDouble(st.nextToken());
				storage.put(retString, retDouble);
				
			} else {
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
	 * @param args
	 */
	public static void main(String[] args) throws IOException{
		// TODO Auto-generated method stub
		UpperCase up = new UpperCase("E:\\Promotion\\Rodange-POS\\upp.txt");
		System.out.println(up.look("hallo", "NOUN"));
	}

}
