package indexedConcordancer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPath;

import Tokenizer.PreLuxTokeniser;

public class XMLConcordancer {
	private Document doc = null;
	private XMLOutputter out = null;
	private OutputStream os = null;
	private List <Element>allWords = null;
	private List <Element>lineAndWords = null;
	
	public XMLConcordancer(String xmlFile, String xPathForAllWords, String xPathForWordsAndLines) 
	throws JDOMException, IOException {
		
		doc = new SAXBuilder().build(xmlFile);
		
		out = new XMLOutputter(); 
		
	//	os = new FileOutputStream(Output);	
		
		allWords =  (List<Element>) XPath.selectNodes(doc, xPathForAllWords); 
		lineAndWords = (List<Element>) XPath.selectNodes(doc, xPathForWordsAndLines);
		
		if (allWords.size() == 0 || lineAndWords.size() == 0){
			System.err.println("##Keine W�rter gefunden! X-Path Ausdruck �berpr�fen##");
		}
		}
	
	public void write (String corpus) throws IOException{
		OutputStream os = new FileOutputStream(corpus);
		out.output(doc,os);
		System.out.println();
	}
	
	public String [] getLines(String word, int left, int right) throws JDOMException{
		Vector vc = new Vector<String>();
	  
		for (int r = 0; r < allWords.size(); r++){
			Element nextWord = (Element)allWords.get(r);
			String wordText = nextWord.getText();
			String line = "";
			if (wordText.equalsIgnoreCase(word)){
				int leftside = left;
				if (r < leftside){leftside = r;}
				 
				
				int laenge = leftside;
				// this is for the left side of the word
				for (int u = 0; u < leftside; u++){
					
					line = line + (allWords.get(r - laenge).getText()) + " ";
					laenge--;
				} // the end of for statement
				
				
				// And this one is for the right side of the word
				//laenge++;
				int rightside = right;
				laenge = 0;
				if ((allWords.size() - r) < rightside ) {rightside = allWords.size() - r;}
				for (int u = 0; u < rightside; u++){
					//System.out.println("here the words: " + words.get(r - width).getText());
					line = line + (allWords.get(r + laenge).getText()) + " ";
					laenge++;
				} // the end of for statement
				
				//System.out.println("here the line: " + line);
				vc.add(line.toLowerCase());
			} // the end of if statement
		} // the end of the list loop
		String [] conclist = null;
		String [] retval = null;
		if (!vc.isEmpty()){
		conclist = new String[vc.size()];
		vc.copyInto(conclist);
		retval = normaliseConcordances(conclist, word.toLowerCase());
		}
		return retval;
	}
	
	public boolean giveBoolyPOSAnswer(String wordPOS, Element nextWord){
			boolean booly = true; 
			if (wordPOS != null){
				if (nextWord.getAttributeValue("pos").equals(wordPOS)){
					booly = true;
				}
				else {
					booly = false;
				}
			}
			return booly;
	}
	
	// this is for setMultiWordUnits
	public int [] getElenentNumbers(int position, int left, int right, int nSize){
		
		int start = position - left;
		while (start < 0){
			start++;
		}
		
		int end = position + right;
		while (end > nSize){
			end--;
		}
		
		int [] retval = new int[end - start];
			for (int a = 0; a < retval.length; a++){
				retval[a] = start;
				start++;
			}
		return retval;
	}
	
	// this is for setMultiWordUnits
	public int getIndexOfAnElement(Element Parent, Element theWord){
		int index = -1;
		List l = Parent.getChildren();
		for (int a = 0; a < l.size(); a++){
			Object ob = l.get(a);
				if (theWord.equals(ob)){
					index = a;
					}
		//	System.out.println("In der Indexberechnung: " + a + " " + getChildWithIndex(a, Parent));
				
		}
		
		// System.out.println("hier ist die Indexberechnung: "+index +" " + theWord.getValue() + " "+ Parent.getContentSize() +" "+ Parent.getAttributeValue("n")+" "+ Parent.getValue());
		
		return index;
	}
	
	// this is for setMultiWordUnits
	public Element getChildWithIndex(int index, Element p){
		List l = p.getChildren();
		Element retval = null;
		for (int t = 0; t < l.size(); t++){
			if (index == t){
				retval = (Element)l.get(t);
			}
		}
		return retval;
	}
	
	// this is for setMultiWordUnits
	public List gatherElementsInIndex(int indexW, int indexC, Element p){
			List l = new ArrayList();
			while (indexW < indexC){
			//System.out.println(indexW +" "+indexC+" '" + p.getValue()+"'");
			Element e = (Element)getChildWithIndex(indexW, p).clone();
			l.add(e);
			indexW++;
			}
			while (indexW > indexC){
			Element e = (Element)getChildWithIndex(indexC, p).clone();
			l.add(e);
			indexC++;
			}
		return l;
	}
	
	// this is for setMultiWordUnits
	public int dasKleinere (int one, int two){
		if (one > two){
			return one;
		}
		else return two;
	}
	
	// this is for setMultiWordUnits
	public void setMultiWordUnits(String theWord, String Collocate, int left, int right, double score){
		for (int a = 0; a < allWords.size(); a++){
			Element wordE = (Element)allWords.get(a);
			String word = wordE.getText();
			if (theWord.equalsIgnoreCase(word)){
				 int array [] = getElenentNumbers(a, 10, 10,allWords.size());
				 for (int d = 0; d < array.length; d++){
					 Element collyE = (Element)allWords.get(array[d]);
					 String colly = collyE.getText();
					 if (colly.equalsIgnoreCase(Collocate)){
						 Element s = (Element)wordE.getParentElement();
					//	 System.out.println(" hier s:"+s.getName() +" hier word:" + wordE.getName() );
						 int indexWord = getIndexOfAnElement(s, wordE);
						 int indexColly = getIndexOfAnElement(s, collyE);
						 if (indexWord != -1 && indexColly != -1){
						  // System.out.println(word + " " + colly);
							 List l = gatherElementsInIndex(indexWord, indexColly, s);
							 Element mw = new Element("mw");
							 mw.setAttribute("score", score+"");
							 mw.setContent(l);
							 s.addContent(dasKleinere(indexWord, indexColly), mw);
						 }
					 }
				 }
			}
		}
		
	} //the end of the method setMultiWordUnits
	
	public String [] getWordsAndLines() throws JDOMException{
		
		String array[] = new String [lineAndWords.size()];
		for (int r = 0; r < lineAndWords.size(); r++){
			Element nextElement = (Element)lineAndWords.get(r);
			String wordText = null;
			if (nextElement.getName().equals("w") || nextElement.getName().equals("c")){
			wordText = nextElement.getText();
			}
			else if (nextElement.getName().equals("l")){
			 wordText = "("+nextElement.getAttributeValue("n")+")";
			}
			array[r] = wordText;
			//System.out.println("Hete: " + wordText + " r: " + " n: " + nextElement);
			
		} //the end of for loop
		return array;
	}
	
	public String getLinesWithLineNumbers(String word1, String word2, int left, int right) throws JDOMException{
		String context = "";
		String array [] = getWordsAndLines();
		
		
		for (int l = 0; l < array.length; l++){
			if (array[l].equalsIgnoreCase(word1)){
				
				String line = "";
				// this is for the left side of the word
				
				int leftside = left;
				if (l < leftside){leftside = l;}
				 
				int laenge = leftside;
				// this is for the left side of the word
				for (int u = 0; u < leftside; u++){
					//System.out.println("here the words: " + array[l- laenge]);
					
					if (Character.isLetterOrDigit(array[l - laenge].charAt(0))){
						line = line + " " + array[l - laenge];
					} else {
						line = line + array[l - laenge];
					}
					laenge--;
				} // the end of for statement
				
				// And this one is for the right side of the word
				int rightside = right;
				laenge = 0;
				if ((array.length - l) < rightside ) {rightside = array.length - l;}
				for (int u = 0; u < rightside; u++){
					//System.out.println("here the words: " + words.get(r - width).getText());
					if (Character.isLetterOrDigit(array[l + laenge].charAt(0))){
						line = line + " "+array[l + laenge];
					} else {
						line = line +array[l + laenge];
					}
					laenge++;
				} // the end of for statement
				
				if (checkTheFriend(word2, line)){
				context = context + "\n" + line;
				}
			} // the end of if statement
		}
		return context;
	}
	
	public boolean checkTheFriend(String theWord, String line){
		boolean booly = false;
//		StringTokenizer st = new StringTokenizer(PreLuxTokeniser.tokenizeInWords(line));
//			while (st.hasMoreTokens()){
//				String word = st.nextToken();
//				if (theWord.equalsIgnoreCase(word)){
//					booly = true; 
//				}
//			}
		return booly;
	}
	
	public String[] getLinesWithPOS (String word, String wordPOS, int left, int right, String POS) throws JDOMException{
		 
		Vector vc = new Vector<String>();
		for (int r = 0; r < allWords.size(); r++){
			Element nextWord = allWords.get(r);
			String wordText = nextWord.getText();
			String line = "";
			if (wordText.equalsIgnoreCase(word) && giveBoolyPOSAnswer(wordPOS, nextWord)){
				int leftside = left;
				if(r < leftside){leftside = r;}
				
				int laenge = leftside;
				// this is for the left side of the word
				for (int u = 0; u < leftside; u++){
				//	System.out.println("here the words: " + words.get(r - laenge).getText());
					Element el = allWords.get(r - laenge);
						if (giveBoolyPOSAnswer(POS, el)){
							line = line + el.getText() + " ";
							}
						laenge--;
				} // the end of for statement
				
				// for the Word itself:
				//	line = line + allWords.get(r).getText() + " " + laenge +" ";	
				
				// the next commentary could be activated, when we wish to skip the node word 
				//laenge++;
				int rightside = right;
				laenge = 0;
				if ((allWords.size() - r) < rightside ) {rightside = allWords.size() - r;}
				// And this one is for the right side of the word
				for (int u = 0; u < rightside; u++){
				//	System.out.println("here the words: " + words.get(r + laenge).getText());
					Element el = allWords.get(r + laenge);
						if (giveBoolyPOSAnswer(POS, el)){ 
							line = line + el.getText() + " ";
						}
					 laenge++;
				} // the end of for statement
				
				//System.out.println("here the line: " + line);
				vc.add(line.toLowerCase());
			} // the end of if statement
		} // the end of the list loop
		String [] conclist = null;
		String [] retval = null;
		if (!vc.isEmpty()){
		conclist = new String[vc.size()];
		vc.copyInto(conclist);
		retval = normaliseConcordances(conclist, word.toLowerCase());
		}
		return retval;
	}
	
	public String [] normaliseConcordances(String []lines, String word){
		Vector vc = new Vector<String>();
		if (lines!=null){
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].length() > word.length() + 4){
			//	System.out.println("-" + lines[i] + "-" + word);
				String left = lines[i].substring(0, lines[i].indexOf(word));
				String right = lines[i].substring(lines [i].indexOf(word) + word.length(), lines[i].length());
			//	System.out.println("Das ist left: (" +left + ")Und das ist right: (" +right+")");
				
				int leftS = left.length();
				int rightS = right.length();
				
					if (leftS > rightS){
						for (int j = 0; j < leftS - rightS; j++) {
							right = right + " ";
						}
					}
					if (leftS < rightS){
						for (int j = 0; j < rightS- leftS; j++) {
							left = " " + left;
						}
					}	
					String ourLine = "tt " + left + word + right + " tt";
					vc.add(ourLine);
				}
			}
		}
		String retval[] = null;
		if (!vc.isEmpty()){
			retval = new String[vc.size()];
			vc.copyInto(retval);
		}
		return retval;
	}
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public static void main(String[] args) throws JDOMException, IOException {
		// TODO Auto-generated method stub
		XMLConcordancer xc = new XMLConcordancer("E:\\Promotion\\Rodange-POS\\renertNewTagged.xml", "//w", "//l[@n] | //w | //c");
		//String lines[] = xc.getLinesWithPOS("Hunneg", null, 9, 9, null);
		String lines[] = xc.getLines("jomer", 5, 5);
		for (int i = 0; i < lines.length; i++) {
			String string = lines[i];
			//System.out.println(string);
		}
		
		//String context = xc.getLinesWithLineNumbers("et", "da", 10,10);
		
	}

}
