package lunacorpustools.POSTrainer;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import wordList.FreqList;


/*
 * Diese Klasse trainiert die Suffixe und die Groß und Kleinschreibung
 */
public class TrainSuffixXML {

	private Map storageU;
	private Map storageS;
	private Map storageL;
	private Map storageLC;
	private Vector <String> showUpper;
	private Vector <String> showSuffix;
	private Vector <String> showLength;
	private Vector <String> showLCase;

	private FreqList allTags;
	private List words;

	private String elemente;
	private FreqList suffixTags;
	private String suffixe;
	private FreqList suffixAll;
	private int upperN = 0;
	private int lowerN = 0;
	private FreqList upperTags;
	private FreqList lowerTags;
	private FreqList tagsLU;
	private FreqList lengthInfo;
	private FreqList lengthAll;


	/*
	 * Konstruktor
	 */

	public TrainSuffixXML (List words, String Suffixe) 
			throws IOException, JDOMException{

		this.words = words;

		// Kontainer Großschreibung
		storageU = new HashMap();

		// Kontainer Suffix
		storageS = new HashMap();

		// Kontainer Wortlänge
		storageL = new HashMap();


		storageLC = new HashMap();
		allTags = new FreqList();
		suffixTags = new FreqList();
		this.suffixe = Suffixe; 
		suffixAll = new FreqList();
		upperTags = new FreqList();
		lowerTags = new FreqList();
		lengthInfo = new FreqList();
		lengthAll = new FreqList();
		tagsLU = new FreqList();


	}

	public String suffixCheck (String word){
		StringTokenizer st = new StringTokenizer(suffixe);
		String retval = null;
		while (st.hasMoreTokens()){
			String suffix = st.nextToken();
			if(word.endsWith(suffix)){
				retval = suffix;
			}
		}
		return retval;

	}

	public void upperCaseToStorage(String word, String tag){
		try {	
			if(Character.isUpperCase(word.charAt(0))) {
				upperN++;
				upperTags.add(tag);
				tagsLU.add(tag+"U");
				allTags.add(tag);
			}
			else {
				lowerN++;
				lowerTags.add(tag);
				tagsLU.add(tag+"L");
				allTags.add(tag);
			}

		} catch (IndexOutOfBoundsException ioe){
			System.out.println(word +"\n" + ioe.getMessage());
		} 
	}

	public void suffixToStorage(String word, String tag){
		String nsuffix = suffixCheck(word);
		if (nsuffix!=null){	
			suffixAll.add(nsuffix);
			suffixTags.add(nsuffix +" " + tag);
		}
	}


	public void calculateLengthInfo(String word, String tag){
		int length = word.length();
		lengthInfo.add(length+ " " +tag);
		lengthAll.add("" + length);

	}

	public void suffixAllFreqs(String attribut)
			throws JDOMException {
		Iterator wordsIt = words.iterator();
		while (wordsIt.hasNext()){
			Element currElement = (Element)wordsIt.next();
			String wort = currElement.getText();
			String tag = currElement.getAttributeValue(attribut);
			// leere tags (pos="") vermeiden
			if (!"".trim().equals(tag)){
				upperCaseToStorage(wort, tag);
				suffixToStorage(wort, tag);
				calculateLengthInfo(wort, tag);
			}
		}
	}


	public void setSuffixP(){

		Iterator it = suffixTags.iterator();
		// SuffixTags.getN() * SuffixTags.getN()
		showSuffix = new Vector<String>();
		showSuffix.add("Suffix Tag FrequenzSuffixMitDiesemTag FrequenzSuffixInsgesamt Wahrscheinlichkeit");
		while (it.hasNext()){
			String suffixT = (String)it.next();
			int tagFreq = suffixTags.getFreq(suffixT);
			String suffix = suffixT.substring(0, suffixT.indexOf(" "));

			double p = (double)tagFreq / (double)suffixAll.getFreq(suffix);

			showSuffix.add(suffixT +" " + tagFreq + " " +suffixAll.getFreq(suffix) + " "+ p);

			storageS.put(suffixT, p);
		}

		Iterator iter = upperTags.iterator();
		// upperTags.getN() * upperTags.getN()
		showUpper = new Vector<String>();
		showUpper.add("Tag AlleGroßgeschriebeneWörterMitDiesemTag AlleGroßgeschriebeneWörterInsgesamt Wahrscheinlichkeit");
		while (iter.hasNext()){
			String tag = (String)iter.next();
			int tagFreq = upperTags.getFreq(tag);
			double p = (double)tagFreq / (double)upperN;
			storageU.put(tag, p);
			showUpper.add(tag +" " + tagFreq + " " +upperN + " "+ p);
		}

		Iterator iterLow = allTags.iterator();
		// tagsLU.getN() * tagsLU.getN()
		showLCase = new Vector<String>();
		showLCase.add("Tag AlleKleingeschriebeneWörterMitDiesemTag AlleKleingeschriebenenWörterInsgesamt Wahrscheinlichkeit");
		while (iterLow.hasNext()){
			String tag = (String)iterLow.next();
			if (!tag.startsWith("PUNC")){
				int tagLoFreq = tagsLU.getFreq(tag+"L");
				double p = (double)tagLoFreq / (double)lowerN;
				//System.out.println("Neen, das m�sste sein: '"+tag +"' "+ p +" " + " " + tagLoFreq +" "+ tagFreq);
				showLCase.add(tag +" " + " " + tagLoFreq +" "+ lowerN +" "+ p);
				storageLC.put(tag, p);
			}
		}

	}


	public void writeValues (String File, Map Storage) throws IOException{
		// We only want to save the stream, therefore PrintWriter here
		PrintWriter pw = new PrintWriter(new FileWriter(File));	
		Iterator iter = Storage.keySet().iterator();
		while (iter.hasNext()){
			String word = (String)iter.next();
			double freq = (Double)Storage.get(word);
			pw.println(word+" "+freq);
		}
		pw.close();	

	}

	public String getValuesAsString (Map Storage){
		StringBuffer sb = new StringBuffer();	
		Iterator iter = Storage.keySet().iterator();
		while (iter.hasNext()){
			String word = (String)iter.next();
			double freq = (Double)Storage.get(word);
			sb.append(word+" "+freq + "\n");
		}
		return sb.toString();
	}
	public String getSuffixP(){
		return getValuesAsString(storageS);
	}

	public String getUpperP(){
		return getValuesAsString(storageU);
	}

	public String getLengthP(){
		return getValuesAsString(storageL);
	}

	public String getLowerC(){
		return getValuesAsString(storageLC);
	}

	public void writeSuffixP (String pFile) throws IOException{
		writeValues(pFile, storageS);
	}

	public void writeUpperP (String pFile) throws IOException{
		writeValues(pFile, storageU);
	}

	public void writeLengthP (String pFile) throws IOException{
		writeValues(pFile, storageL);
	}

	public void writeLowerC (String pFile) throws IOException{
		writeValues(pFile, storageLC);
	}



	public void setLengthInfo (){

		Iterator it = lengthInfo.iterator();
		// lengthInfo.getN() * lengthInfo.getN()
		showLength = new Vector<String>();
		showLength.add("Tag Wortlänge FrequenzDerWörterMitDiesemTagUndLänge FrequenzDerLängeInsgesamt Wahrscheinlichkeit");
		while (it.hasNext()){
			String lengthItem = (String)it.next();
			int tagFreq = lengthInfo.getFreq(lengthItem);
			String tag = lengthItem.substring(lengthItem.indexOf(" ")+1, lengthItem.length());
			String length = lengthItem.substring(0, lengthItem.indexOf(" "));
			int lengthFreq = lengthAll.getFreq(length);

			int tagGeneralFreq = allTags.getFreq(tag);
			double p = (double)tagFreq / (double)lengthFreq;
			showLength.add(tag+" "+ length + " " +tagFreq + " " + lengthFreq + " " + p);
			if (!lengthItem.substring(lengthItem.indexOf(' ')).equals(" PUNC")){
				//showLength.add (lengthItem.substring(lengthItem.indexOf(' ')));
				storageL.put(lengthItem, p);
			}

		}

	}
	public String getShowSuffix (){
		StringBuffer bs = new StringBuffer();
		for (int i = 0; i <showSuffix.size(); i ++){
			bs.append(showSuffix.elementAt(i).toString() + "\n");
		}

		return bs.toString();
	}

	public String getShowUpper (){
		StringBuffer bs = new StringBuffer();
		for (int i = 0; i <showUpper.size(); i ++){
			bs.append(showUpper.elementAt(i).toString() + "\n");
		}

		return bs.toString();
	}

	public String getShowLCase (){
		StringBuffer bs = new StringBuffer();
		for (int i = 0; i <showLCase.size(); i ++){
			bs.append(showLCase.elementAt(i).toString() + "\n");
		}

		return bs.toString();
	}

	public String getShowLength (){
		StringBuffer bs = new StringBuffer();
		for (int i = 0; i <showLength.size(); i ++){
			bs.append(showLength.elementAt(i).toString() + "\n");
		}

		return bs.toString();
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException , JDOMException{
		// TODO Auto-generated method stub

		//		TrainSuffixXML sm = new TrainSuffixXML("E:\\Promotion\\Rodange-POS\\renert_train.xml", "w | // c", "ek er eg lech sten éi ér s heet schaft ik ur ant ent ie es en eg eng ng");
		//		sm.SuffixAllFreqs("pos");
		//		sm.setSuffixP();
		//		sm.setLengthInfo();
		//		sm.whriteSuffixP("E:\\Promotion\\Rodange-POS\\suf.txt");
		//		sm.whriteUpperP("E:\\Promotion\\Rodange-POS\\upp.txt");
		//		sm.whriteLowerC("E:\\Promotion\\Rodange-POS\\low.txt");
		//		sm.whriteLengthP("E:\\Promotion\\Rodange-POS\\length.txt");


	}

}
