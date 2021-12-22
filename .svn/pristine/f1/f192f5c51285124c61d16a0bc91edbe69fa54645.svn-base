/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wordList;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.xpath.XPath;

/**
 *
 * @author joshgun.sirajzade
 */
public class SortedWordList {
	private WordFreq[] wf;

	public SortedWordList (Document doc, String xPathForWord) throws JDOMException{
		FreqList fl = new FreqList();
		List<Element> wordliste = (List)XPath.selectNodes(doc, xPathForWord);
		Iterator it = wordliste.iterator();
		while (it.hasNext()){
			Element nextWord = (Element)it.next();
			String wordText = nextWord.getText();
			fl.add(wordText.toLowerCase());
		}
		createSortedWordList(fl);
	}

	public SortedWordList (FreqList fl){
		createSortedWordList(fl);
	}

	public void createSortedWordList (FreqList fl){

		wf = new WordFreq[fl.getT()];
		Iterator it = fl.iterator();
		int zahler = 0;
		while (it.hasNext()){
			String word = (String)it.next();
			int freq = 0;
			freq = fl.getFreq(word);    	  		
			WordFreq wordfreq = new WordFreq(word, freq);
			wf[zahler] = wordfreq;
			zahler++;
		}

		Comparator comp = new FreqComparator();
		Arrays.sort(wf, comp);
	}

	public String getSortedWordListAsString (){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < wf.length; i++) {
			WordFreq wordFreq = wf[i];
			sb.append(wordFreq.getWord() + " " + wordFreq.getFreq()+ "\n");
		}
		return sb.toString();
	}

	public String getSortedWordsAsString (){
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < wf.length; i++) {
			WordFreq wordFreq = wf[i];
			sb.append(wordFreq.getWord() + "\n");
		}
		return sb.toString();
	}

	public String[] getSortedWordsAsStringArray (){
		return getSortedWordsAsString().split("\n");        
	}

	public WordFreq[] getSortedWordList(){
		return wf;
	}
}
