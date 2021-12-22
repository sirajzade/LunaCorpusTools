/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lunacorpustools.POSTrainer;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import wordList.FreqList;

/**
 *
 * @author joshgun.sirajzade
 */
public class TrainLexicon {
	private List words;
        private Vector <String> showLexicon;
	private Vector <String> writeLexicon;
	private FreqList lexemPOS;
        private FreqList allLexems;
	private FreqList allPOS;

	public TrainLexicon (List words) throws JDOMException, IOException{
		this.words = words;
		lexemPOS = new FreqList();
                allLexems = new FreqList();
                allPOS = new FreqList();
	}

	public void setWords (String attPos){
		
		Iterator iti = words.iterator();
		while (iti.hasNext()){
			Element el = (Element)iti.next();
			String word = el.getText();
			String pos = el.getAttributeValue(attPos);
			if (word.length() != 0 && pos.length() != 0){
				lexemPOS.add(word+"@@@"+pos);
				allPOS.add(pos);
                                allLexems.add(word);
			}
		}
	}

	public void setLexicon (){
		showLexicon = new Vector<String>();
		showLexicon.add("Wort POS FrequenzWortMitPOS FrequenzWortMitAnderenPOS Wahrscheinlichkeit");
		writeLexicon = new Vector<String>();

		Iterator iti = lexemPOS.iterator();
		while (iti.hasNext()){
			String wordPOS = (String)iti.next();
			int freqLexemPOS = lexemPOS.getFreq(wordPOS);
			String pos[] = wordPOS.split("@@@");
			String word = pos[0];
			//             System.out.println(wordPOS);
			String currentTag = pos[1];

			int freqLexemAnderePOS = allLexems.getFreq(word);
			// Weiß nicht, was ich mir damals gedacht habe
                        /* 
			Iterator allPOSiti = allPOS.iterator();
			while (allPOSiti.hasNext()){
				String tag = (String)allPOSiti.next();
				int freqPOS = lexemPOS.getFreq(word + "@@@" + tag);
				freqLexemAnderePOS = freqLexemAnderePOS + freqPOS;
			}
                        */
			double p = (double)freqLexemPOS / (double)freqLexemAnderePOS;
			showLexicon.add(word + " " + currentTag + " " + freqLexemPOS + " " + freqLexemAnderePOS + " " + p);
			writeLexicon.add(word + " " + currentTag + " " + p);
		}


	}

	public String getLexikon()  {

		StringBuffer retval = new StringBuffer();
		for (int i = 0; i < writeLexicon.size(); i++) {
			String string = writeLexicon.get(i);
			retval.append(string + "\n");
		}
		return retval.toString();
	}


	public String getShowLexikon()  {

		StringBuffer retval = new StringBuffer();
		for (int i = 0; i < showLexicon.size(); i++) {
			String string = showLexicon.get(i);
			retval.append(string + "\n");
		}
		return retval.toString();
	}
        
        public String getAllTags(){
		String retval= null;
		StringBuilder sb = new StringBuilder();
		Iterator iti = allPOS.iterator();
		while (iti.hasNext()){
			String pos = (String)iti.next();
			sb.append(pos + " ");
		}
		retval = sb.toString();
		return retval;
	}

}