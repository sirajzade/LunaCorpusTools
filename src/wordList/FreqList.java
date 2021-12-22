/*
 * FreqList.java
 */

package wordList;

import java.util.*;
import java.io.*;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPath;

import Tokenizer.FileTokeniser;
import wordList.FreqComparator;
import wordList.WordFreq;

public class FreqList {
	private Map storage;
	private int total;

	public FreqList(){
		storage = new HashMap();
	}

	public void add (String wordtoken){

		// case sensitive or not, just switch  
		String word = wordtoken; //.toLowerCase();

		int value[] = (int[])storage.get(word);
		if (value == null) {
			value = new int[1];
			storage.put(word, value);
		}
		value[0]++;
		total++;
	}


	public int getFreq (String word){
		int retval = 0;
		int value[] = (int[])storage.get(word);
		if (value != null){
			retval = value[0];
		}
		return (retval);
	}

	// The method getN() returns the sum of all frequencies. In statistics such a value 
	// is usually called N, so is the name of the method.

	public int getN(){
		return (total);
	}

	public int getT(){ // this method return the number of Types
		return ((int)storage.size());
	}


	public Iterator iterator(){
		return (storage.keySet().iterator());
	}

	public void save(PrintWriter pw){
		Iterator it = storage.keySet().iterator();
		while (it.hasNext()){
			String word = (String)it.next();
			int freq = getFreq(word);
			pw.println(word+" "+freq);
		}
	}

	public void showToConsole(){
		Iterator it = storage.keySet().iterator();
		while (it.hasNext()){
			String word = (String)it.next();
			int freq = getFreq(word);
			System.out.println(word+" "+freq);
		}
	}

	public void load (BufferedReader br)
			throws IOException {
		String line = br.readLine();

		while (line != null){
			StringTokenizer st = new StringTokenizer(line);
			if (st.countTokens() < 2){
				System.err.println("Insufficient line: `"+line+"�");
			} else if (st.countTokens() == 2){
				insert(st.nextToken(), Integer.parseInt(st.nextToken()));
			} else {
				StringBuffer word = new StringBuffer(st.nextToken());
				while (st.countTokens() > 2) {
					word.append(' ');
					word.append(st.nextToken());
				}
				insert (word.toString(), Integer.parseInt(st.nextToken()));
			}
			line = br.readLine();

		}
	}

	public void insert (String word, int freq){
		int value[] = new int[1];
		total += freq;
		freq += getFreq(word);
		value[0] = freq;
		storage.put(word, value);
	}

	public void creatorOfPOS (String filename, String POS) throws JDOMException, IOException{
		Document doc = new SAXBuilder().build(filename);

		List lines = XPath.selectNodes(doc, "//"+POS);
		Iterator it = lines.iterator();
		while (it.hasNext()){
			Element wordElement = (Element)it.next();
			String word = wordElement.getText();
			add(word.toLowerCase());
		}
	}

	// A new class to create a FreqList. Here is the class object a container. 
	public void creator (String filename) throws IOException{

		FileTokeniser ft = new FileTokeniser(filename);
		while (ft.hasMoreTokens()){
			add(ft.getNextToken().toLowerCase());
			//	System.out.println(ft.getNextToken());
		}
		ft.close();
	}

	// A second new class to sort the entries by frequencies

	public WordFreq[] sortFrequencies (){
		Iterator fl = this.iterator();
		WordFreq[] wf = new WordFreq[this.getT()]; 
		int i = 0;
		while (fl.hasNext()){
			String word = (String)fl.next();
			int freq = getFreq(word);				
			wf[i] = new WordFreq(word, freq);
			i++;			
		}
		// next line does the sorting
		Comparator freqC = new FreqComparator();	
		Arrays.sort(wf, freqC);
		return wf;

		/*
	// delete old storage, the HashMap storage is kept as a data type instead of WordFreq, 
	// because it is much quicker and uses better running time algorithm 
	storage.clear();

	for (int g = 0; g < wf.length; g++){
		insert(wf[g].getWord(), wf[g].getFreq());
	}
		 */

	}


} // end of the class FreqList
