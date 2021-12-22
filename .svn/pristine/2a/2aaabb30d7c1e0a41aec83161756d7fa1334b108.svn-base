package lunacorpustools.POSTrainer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;


import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.xpath.XPath;

import wordList.FreqList;


public class TrainMatrixXML {

	private Map leftMatStorage;
	private Map middleMatStorage;
	private List words = null;
	private FreqList threetags = null;
	private FreqList bitags = null;
	private FreqList tags = null;
	private Vector <String> showTheLeftMat = null;
	private Vector <String> showTheMiddleMat = null;
	private Vector <String> showTheLeftBi = null;
	private Vector <String> showTheRigthBi = null;

	private Map bigrammLeftScore;
	private Map bigrammRightScore;

	public TrainMatrixXML(List words) throws IOException, JDOMException{

		this.words = words;

		leftMatStorage = new HashMap();
		middleMatStorage = new HashMap();
		bigrammLeftScore = new HashMap();
		bigrammRightScore = new HashMap();

		bitags = new FreqList();
		threetags = new FreqList();
		tags = new FreqList();
	}


	public void serAllFreqs(String attribut) throws JDOMException {
		Iterator wordsIt = words.iterator();

		String tag3 = null;
		String tag2 = null;
		String tag1 = null;

		while (wordsIt.hasNext()){
			Element currElement = (Element)wordsIt.next();
			String tag = currElement.getAttributeValue(attribut);
			// leere tags (pos="") vermeiden
			if (!"".trim().equals(tag)){
				tags.add(tag);

				tag1 = tag2;
				tag2 = tag3;
				tag3 = tag;
				bitags.add(tag1+tag2);
				threetags.add(tag1+tag2+tag3);
			}
		}
	}

	public void setBigramms (String alltags){
		StringTokenizer st = new StringTokenizer(alltags);
		String [] tagarray = new String[st.countTokens()];
		int t = 0;
		while (st.hasMoreTokens()){
			tagarray[t] = st.nextToken();
			t++;
		}
		showTheLeftBi = new Vector<String>(tagarray.length * tagarray.length);
		showTheRigthBi = new Vector<String>(tagarray.length * tagarray.length);
		for (int i = 0; i < tagarray.length; i++){
			for (int a = 0; a < tagarray.length; a++){
				String tagson = ""+tagarray[i] + tagarray[a];
				double p = 0.0; 
				int n = bitags.getFreq(tagson);
				int N = tags.getFreq(tagarray[i]);
				if (N != 0) {
					p =  (double)n / (double)N;
					bigrammLeftScore.put(tagson, p);
					showTheLeftBi.add(tagarray[i] + " "+tagarray[a] + " Guenstige Faelle: " + n +";    Alle Faelle: "+ N + ";    Wahrscheinlichkeit: "+ p );
				}

				int Nrigth = tags.getFreq(tagarray[a]);
				double pRight = 0.0;
				if (Nrigth != 0){
					pRight = (double)n / (double)Nrigth;
				}
				bigrammRightScore.put(tagson, pRight);
				showTheRigthBi.add(tagarray[i] + " "+tagarray[a] +" Guenstige Faelle: " + n +";    Alle Faelle: "+ Nrigth + ";    Wahrscheinlichkeit: "+ pRight );


			}
		}
	}
	public String getAllTags(){
		String retval= null;
		StringBuilder sb = new StringBuilder();
		Iterator iti = tags.iterator();
		while (iti.hasNext()){
			String pos = (String)iti.next();
			sb.append(pos + " ");
		}
		retval = sb.toString();
		return retval;
	}

	public void setMatrix() throws IOException{
		String alltags = getAllTags();
		setBigramms(alltags);
		StringTokenizer st = new StringTokenizer(alltags);
		String [] tagarray = new String[st.countTokens()];
		int t = 0;
		while (st.hasMoreTokens()){
			tagarray[t] = st.nextToken();
			t++;
		}
		showTheLeftMat = new Vector<String>(tagarray.length * tagarray.length * tagarray.length);
		showTheMiddleMat = new Vector<String>(tagarray.length * tagarray.length * tagarray.length);

		for (int i = 0; i < tagarray.length; i++){
			for (int a = 0; a < tagarray.length; a++){
				for (int b = 0; b < tagarray.length; b++){
					String tagson = ""+tagarray[i] + tagarray[a] + tagarray[b];
					double p = 0.0;
					int n = threetags.getFreq(tagson);
					int N = tags.getFreq(tagarray[i]);
					if (N != 0) {
						p =  (double)n / (double)N;
					}

					leftMatStorage.put(tagson, p);
					showTheLeftMat.add(tagarray[i] + " "+tagarray[a] +" "+ tagarray[b]+ " Günstige Fälle: " + n +";    Alle Fälle: "+ N + ";    Wahrscheinlichkeit: "+ p );

					int middleN = tags.getFreq(tagarray[a]+"");
					double pMiddle = 0.0;
					if (middleN !=0){
						pMiddle = (double)n / (double)middleN;
					}

					middleMatStorage.put(tagson, pMiddle);
					showTheMiddleMat.add(tagarray[i] + " "+tagarray[a] +" "+ tagarray[b]+ " Günstige Fälle: " + n +";    Alle Fälle: "+ middleN + ";    Wahrscheinlichkeit: "+ pMiddle );

				}

			}

		}


	}

	public String showTriLeft (){
		StringBuffer bs = new StringBuffer();
		for (int i = 0; i <showTheLeftMat.size(); i ++){
			bs.append(showTheLeftMat.elementAt(i).toString() + "\n");
		}

		return bs.toString();
	}

	public String showTriMiddle (){
		StringBuffer bs = new StringBuffer();
		for (int i = 0; i <showTheMiddleMat.size(); i ++){
			bs.append(showTheMiddleMat.elementAt(i).toString() + "\n");
		}

		return bs.toString();
	}
	public String showBiLeft (){
		StringBuffer bs = new StringBuffer();
		for (int i = 0; i <showTheLeftBi.size(); i ++){
			bs.append(showTheLeftBi.elementAt(i).toString() + "\n");
		}

		return bs.toString();
	}

	public String showBiRight (){
		StringBuffer bs = new StringBuffer();
		for (int i = 0; i <showTheRigthBi.size(); i ++){
			bs.append(showTheRigthBi.elementAt(i).toString() + "\n");
		}

		return bs.toString();
	}

	public void writeMatrixToShow(String left, String middle, String biLeft, String biRight) throws IOException{

		PrintWriter pw = new PrintWriter(new FileWriter(left));
		PrintWriter pw2 = new PrintWriter(new FileWriter(middle)); 
		PrintWriter pw3 = new PrintWriter(new FileWriter(biLeft)); 
		PrintWriter pw4 = new PrintWriter(new FileWriter(biRight)); 

		for (int i = 0; i <showTheLeftMat.size(); i ++){
			pw.println(showTheLeftMat.elementAt(i).toString());
		}

		for (int h = 0; h <showTheMiddleMat.size(); h ++){
			pw2.println(showTheMiddleMat.elementAt(h).toString());
		}

		for (int o = 0; o <showTheLeftBi.size(); o ++){
			pw3.println(showTheLeftBi.elementAt(o).toString());
		}

		for (int f = 0; f <showTheRigthBi.size(); f ++){
			pw4.println(showTheRigthBi.elementAt(f).toString());
		}

		threetags.save(pw);
		pw.close();
		pw2.close();
		pw3.close();
		pw4.close();
	}

	public String getTriLeft (){
		StringBuffer retval = new StringBuffer();
		Iterator it;
		it = leftMatStorage.keySet().iterator();
		while (it.hasNext()){
			String word = (String)it.next();
			double freq = (Double)leftMatStorage.get(word);
			retval.append(word+" "+freq + "\n");

		}

		return retval.toString();
	}

	public String getTriMiddle (){
		StringBuffer retval = new StringBuffer();
		Iterator it = middleMatStorage.keySet().iterator();
		while (it.hasNext()){
			String word = (String)it.next();
			double freq = (Double)middleMatStorage.get(word);
			retval.append(word+" "+freq + "\n");
		}

		return retval.toString();
	}

	public String getBiLeft (){
		StringBuffer retval = new StringBuffer();
		Iterator it = bigrammLeftScore.keySet().iterator();
		while (it.hasNext()){
			String word = (String)it.next();
			double freq = (Double)bigrammLeftScore.get(word);
			retval.append(word+" "+freq + "\n");
		}

		return retval.toString();
	}

	public String getBiRight (){
		StringBuffer retval = new StringBuffer();
		Iterator it = bigrammRightScore.keySet().iterator();
		while (it.hasNext()){
			String word = (String)it.next();
			double freq = (Double)bigrammRightScore.get(word);
			retval.append(word+" "+freq + "\n");
		}

		return retval.toString();
	}

	public void writeMatrix (String file1, String file2, String file3, String file4) throws IOException{
		// We only want to save the stream, therefore PrintWriter here
		PrintWriter pw = new PrintWriter(new FileWriter(file1));

		Iterator it = leftMatStorage.keySet().iterator();
		while (it.hasNext()){
			String word = (String)it.next();
			double freq = (Double)leftMatStorage.get(word);
			pw.println(word+" "+freq);
		}
		System.out.println("The left matrix is written in to the file: '" + file1+"'");
		pw.close();

		PrintWriter pw2 = new PrintWriter(new FileWriter(file2));

		Iterator it2 = middleMatStorage.keySet().iterator();
		while (it2.hasNext()){
			String word = (String)it2.next();
			double freq = (Double)middleMatStorage.get(word);
			pw2.println(word+" "+freq);
		}
		System.out.println("The middle matrix is written in to the file: '" + file2+"'");
		pw2.close();


		PrintWriter pw3 = new PrintWriter(new FileWriter(file3));

		Iterator it3 = bigrammLeftScore.keySet().iterator();
		while (it3.hasNext()){
			String word = (String)it3.next();
			double freq = (Double)bigrammLeftScore.get(word);
			pw3.println(word+" "+freq);
		}
		System.out.println("The middle matrix is written in to the file: '" + file3+"'");
		pw3.close();


		PrintWriter pw4 = new PrintWriter(new FileWriter(file4));

		Iterator it4 = bigrammRightScore.keySet().iterator();
		while (it4.hasNext()){
			String word = (String)it4.next();
			double freq = (Double)bigrammRightScore.get(word);
			pw4.println(word+" "+freq);
		}
		System.out.println("The middle matrix is written in to the file: '" + file4+"'");
		pw4.close();


	}



	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException, JDOMException{
		// TODO Auto-generated method stub

		FileReader fr = new FileReader("E:\\Backup_DirSync\\Promotion\\Rodange-POS\\renert_train.xml");
		BufferedReader br;
		br = new BufferedReader(fr);
		String line = null;
		StringBuilder sb = new StringBuilder();
		while (( line = br.readLine()) != null){
			sb.append(line);
			sb.append(System.getProperty("line.separator"));
		}



		//	TrainMatrixXML tm = new TrainMatrixXML(sb.toString(), "w | // c");
		//	tm.serAllFreqs("pos");
		//	tm.setMatrix("DET NOUN ADJ CONJ NUM PREP PRON ADV VERB PUNC NEG INFTO PREPA VERBZ");
		//        tm.setMatrix();
		//	tm.whriteMatrix("E:\\Promotion\\Rodange-POS\\posleft.txt","E:\\Promotion\\Rodange-POS\\posmiddle.txt", "E:\\Promotion\\Rodange-POS\\posbiLeft.txt", "E:\\Promotion\\Rodange-POS\\posbiRight.txt" );
		//    tm.whriteMatrixToShow("E:\\Promotion\\Rodange-POS\\showMatrix1.txt", "E:\\Promotion\\Rodange-POS\\showMilldeMatrix1.txt", "E:\\Promotion\\Rodange-POS\\showBiLeftMat1.txt", "E:\\Promotion\\Rodange-POS\\showBiRightMat1.txt");

		//        String test1 = tm.showTriLeft();
		//	System.out.print(test1);

	}

}
