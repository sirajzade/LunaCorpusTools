/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Morphal;

/**
 *
 * @author joshgun.sirajzade
 */

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.JPanel;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPath;
import wordList.FreqList;

public class CountSuffix {

	private Document doc = null;
	private TextWords sTable = null;
	private TextWords psTable = null;
	private HashMap LinesWithNumbers = null;
	
	public CountSuffix (String TaggedXmlFile) throws JDOMException, IOException {
		
                doc = new SAXBuilder().build(new StringReader(TaggedXmlFile));
	        sTable = new TextWords();
		psTable = new TextWords();
		LinesWithNumbers = getLines();
	}
	
	// this method is simply for getting lines and its numbers
	// for efficient and rapid query
	private HashMap<String, Element> getLines() throws JDOMException{
	List lines = XPath.selectNodes(doc, "//s");
	HashMap<String, Element> retval = new HashMap<String, Element>();
	Iterator it = lines.iterator();
	while (it.hasNext()){
		Element line = (Element)it.next();
		String number = line.getAttributeValue("n");
		retval.put(number, line);
		}
	return retval;
	} // the end of the method

        
        public List getThreeContexLines (String lineNumber){
               System.out.println(lineNumber);
               int numberItself = 0;
               if (lineNumber!=null){
		 numberItself = Integer.parseInt(lineNumber); 
               } 
		List l = new ArrayList<Element>();
		
		Element line1 = ((Element) LinesWithNumbers.get(numberItself - 1));
		Element line2 = ((Element) LinesWithNumbers.get(lineNumber));
		Element line3 = ((Element) LinesWithNumbers.get(numberItself + 1));
		if (line1 != null) l.add(line1);
		if (line2 != null) l.add(line2);
		if (line3 != null) l.add(line3);
		
		return l;
	}
        
        public void printToGUI (JPanel panel){
            sTable.printToGUI(panel);
        }
        
        public String printExportText (){
            return sTable.printTextExport();
        }
	
	public String getLineContextAlt(String lineNumber) throws JDOMException {

        //List lineN = XPath.selectNodes(doc, "//l[@n='"+lineNumber+"']/preceding-sibling::l[1] | //l[@n='"+lineNumber+"']| //l[@n='"+lineNumber+"']/following-sibling::l[1] ");
        List lineN = getThreeContexLines(lineNumber);
        Iterator ita = lineN.iterator();

        String context = "";
        while (ita.hasNext()) {
            Element line = (Element) ita.next();
            String number = line.getAttributeValue("n");

            //Element reg = line.getChild("reg");
            List woerter = line.getChildren();
            Iterator piti = woerter.iterator();
            String zeile = "";
            while (piti.hasNext()) {
                Element word = (Element) piti.next();
                String wordText = null;
                //	System.out.println(word.getAttributeValue("pos"));
                //	System.out.println(word.getText());
                if (word.getName().equals("w")) {
                    if (word.getAttributeValue("pos").equals("PUNC")) {
                        wordText = word.getTextTrim();
                    } else {
                        wordText = word.getText();
                    }
                }// the end of the first if
                else {
                    wordText = word.getText();
                }
                if (wordText.length() != 0) {
                    if (wordText.charAt(wordText.length() - 1) == '\'') {
                        zeile = zeile + wordText;
                        } else {
                        zeile = zeile + wordText + " ";
                    }

                }
            }
            // to montage the line
            String lineWithNumber = "(" + number + ")" + zeile;
            context = context + " " + lineWithNumber;
        }
        return context;
    }
        
	public String getLineContext(String lineNumber) throws JDOMException {
        
        //List lineN = XPath.selectNodes(doc, "//l[@n='"+lineNumber+"']/preceding-sibling::l[1] | //l[@n='"+lineNumber+"']| //l[@n='"+lineNumber+"']/following-sibling::l[1] ");
        List lineN = getThreeContexLines(lineNumber);
        Iterator ita = lineN.iterator();

        String context = "";
        while (ita.hasNext()) {
            Element line = (Element) ita.next();
            String number = line.getAttributeValue("n");

            
            List woerter = line.getChildren();
            Iterator piti = woerter.iterator();
            String zeile = "";
            while (piti.hasNext()) {
                Element word = (Element) piti.next();
                String wordText = "";
                //	System.out.println(word.getAttributeValue("pos"));
                //	System.out.println(word.getText());
                if (word.getName().equals("w")) {
                        wordText = word.getText();
                 }// the end of the first if
                else {
                 //   wordText = word.getText();
                }
                
                if (wordText.length() != 0) {
                    if (wordText.charAt(wordText.length() - 1) == '\'') {
                        zeile = zeile + wordText;
                    } else if (!Character.isLetterOrDigit(wordText.charAt(wordText.length() - 1))) {

                        if (zeile.length() > 3) {
                            zeile = zeile.substring(0, zeile.length() - 1);
                        }
                        zeile = zeile + wordText + " ";

                    } else {
                        zeile = zeile + wordText + " ";
                    }

                }
            }
            // to montage the line
            String lineWithNumber = "(" + number + ")" + zeile;
            context = context + " " + lineWithNumber;
        }
        return context;
    }
       
        private String getFullContextNew(String lineNumber) throws JDOMException{
             int numberBefore = Integer.parseInt(lineNumber) - 1;
             int numberAfter = Integer.parseInt(lineNumber) + 1;
             
             List wordsBefore = XPath.selectNodes(doc, "//*[@sen='" + numberBefore + "']");
             List words = XPath.selectNodes(doc, "//*[@sen='" + lineNumber + "']");
             List wordsAfter = XPath.selectNodes(doc, "//*[@sen='" + numberAfter + "']");
             
             
             String anfang = getSentenceWords(wordsBefore);
             String mitte = getSentenceWords(words);
             String ende = getSentenceWords(wordsAfter);
             
             return anfang + mitte + ende;
        }
        
        private String getSentenceWords(List words){
             StringBuffer sb = new StringBuffer();
             Iterator iti = words.iterator();
             while (iti.hasNext()){
                  Element word = (Element)iti.next();
                  String wordAsString = word.getText();
                  if (word.getName()!="c"){
                       sb.append(" ");
                  }
                  sb.append(wordAsString);
             }
             return sb.toString();
        }
        
        
        
        private String getFullContext(Element myElement) throws JDOMException {
        Element prevElement = null;
        Element nextElement = null;
        
        String prevLineNumber = "0";
        String nextLineNumber = "0";
        String currentLineNumber = "0";
        //System.out.println("Satzname: " + lineE.getName());

        Element parent = myElement.getParentElement();
        List children = parent.getChildren();
        int myIndex = children.indexOf(myElement);
        //get prevSibling
        if (myIndex > 0 && myIndex < children.size()) {
            prevElement = (Element) children.get(myIndex - 1);
            if (prevElement.getName().equals("s")) {
                prevLineNumber = prevElement.getAttributeValue("n");
            }
        }
        //get nextSibling
        if (myIndex >= 0 && myIndex < children.size() - 1) {
            nextElement = (Element) children.get(myIndex + 1);
            if (nextElement.getName().equals("s")) {
                nextLineNumber = nextElement.getAttributeValue("n");
            }
        }

        if (myElement.getName().equals("s")) {
                currentLineNumber = myElement.getAttributeValue("n");
            }
        
        String context1 = getLineContext(prevLineNumber);
        
        String context2 = getLineContext(currentLineNumber);
       
        String context3 = getLineContext(nextLineNumber);

        return context1 + context2 + context3;
    }


	public void setAffixAndCount (String Prefix, String Suffix, String Infix, String POS, String AffixArt) throws JDOMException{
		// Diese Methode kann im Laufe des Programms mehrmals aufgerufen werden, und mehrere 
		// Affixe können in den Speicher geladen werden.
                // System.out.println("Set wurde erreicht");
            
                List words = new ArrayList(); 
				 
                if (POS.equals("Alle") || POS.equals("Wortklassen")){
                     // X-Path für alle Wörter //w[@pos]
                     words = XPath.selectNodes(doc, "//w"); 
                } else {
                    words = XPath.selectNodes(doc, "//w[@pos='"+ POS +"']"); 
                }
                
                Iterator wordsIt = words.iterator();
			while (wordsIt.hasNext()){
				
				Element nextWord = (Element)wordsIt.next();
                                String wordNormal = nextWord.getTextTrim();
				String word = wordNormal.toLowerCase();				
				// here we changed endsWith to startsWith
				//System.out.println(words.toString() + " Progress: " + word + " AffixArt: " + AffixArt);
				 
				if (wordAnalyse(word, Prefix, Suffix, Infix, AffixArt)){
                                    
                                   // System.out.println("if liefert ja");
				
					
					String lineNumber = "0";
                                        String context = " ";
                                        lineNumber = nextWord.getAttributeValue("sen");
                                        
                                        // Sollte nicht der Fall sein, ist aber hin und wieder
                                        if (lineNumber != null){
					    context = getFullContextNew(lineNumber);
                                        
                                           sTable.add(Prefix, Suffix, Infix, AffixArt, wordNormal, lineNumber, context);
                                        }
				}
			}
		//sTable.showTable();
	}
	
	public boolean wordAnalyse (String word, String Prefix, String Suffix, String Infix, String Affixart){
		
		boolean retbooly = false;
		
		if (Affixart.equals("suffix")){
                    System.out.println(word);
			if (word.endsWith(Suffix)){
				retbooly = true;
			}
		}
		else if (Affixart.equals("prefix")){
			if (word.startsWith(Prefix)){
				retbooly = true;
			}
		}
		else if (Affixart.equals("infix")){
			String Array [] = Infix.split(" ");
			if (word.indexOf(Array[1])!=-1){
				retbooly = true;
			}
		}
		else if (Affixart.equals("zirkumfix")){
			if (word.startsWith(Prefix) && word.endsWith(Suffix)){
				retbooly = true;
			}
		}
		else if (Affixart.equals("infixPlusSuffix")){
			String Array[] = Infix.split(" ");
			if (word.indexOf(Array[1])!=-1  && word.endsWith(Suffix)){
				retbooly = true;
			}
		}
		
		else if (Affixart.equals("infixPlusPrefix")){
			String Array[] = Infix.split(" ");
			if (word.indexOf(Array[1])!=-1  && word.startsWith(Prefix)){
				retbooly = true;
			}
		}
		
		else if (Affixart.equals("infixPlusZirkumfix")){
			String Array[] = Infix.split(" ");
			if (word.indexOf(Array[1])!=-1  && word.endsWith(Suffix) && word.startsWith(Prefix)){
				retbooly = true;
			}
		}
		
		else if (Affixart.equals("nullmorphem")){
			retbooly = true;
		}
		
		return retbooly;
	}
	
	
	public void checkTheStemm (String POS, String endung, boolean Ersetzen) throws JDOMException{
		List<?> words = XPath.selectNodes(doc, "//w[@pos='"+ POS+"']"); 
		FreqList fl = new FreqList();
		
		Iterator wordsIt = words.iterator();
			while (wordsIt.hasNext()){
				Element nextWord = (Element)wordsIt.next();
				String word = nextWord.getText();
				fl.add(word);
			}
			
			Iterator wit = fl.iterator();
			
			while (wit.hasNext()){
				String word = (String)wit.next();
				String wordOrig = word;
				/*
				if (word.length() > 3 ){
				word = word.substring(0,word.length()-1) + word.charAt(word.length()-2);
				}
				*/
				
				List <String> kandidates = sTable.getStemmsOfWords();
				Iterator kandit = kandidates.iterator();
					while (kandit.hasNext()){
						String kandidate = (String)kandit.next();// + "f";
						String lineNumber = sTable.getVorkommen(kandidate);
						String AffixArt = sTable.getAffixArt(kandidate);
						if (kandidate.equalsIgnoreCase("deeg")){
							System.out.println("######################## ade hier #######################: "+ word);
						}
						String kandidateOrig = kandidate;
						String context = sTable.getContextLine(kandidate);
						//System.out.println(word +"#################"+kandidate);
								if (Ersetzen){
									kandidate = kandidate.concat(endung);
									System.out.println(word +"#################"+kandidate);
								}
						
						if (word.equalsIgnoreCase(kandidate)){
							//System.out.println("Tas: " + word);
							//psTable.add(endung, kandidate, lineNumber, context);
						}
						else {
					
							if (word.length() > 2 ){
								//für die Assimilierung von Doppelkonsonanten
								//word = word.substring(0,word.length()-1) + word.charAt(word.length()-2);
								
								// für f -> w Verschiebung bei der Pluralbildung des Substantivs
								// word = word.substring(0, word.length()-1) + "w";
								
								word = word.replaceAll("o", "x");
						/*		word = word.replaceAll("ë", "x");
						        word = word.replaceAll("nd", "x");
								word = word.replaceAll("e", "x");
								word = word.replaceAll("i", "x");
								word = word.replaceAll("o", "x");
								word = word.replaceAll("u", "x");
								
								word = word.replaceAll("é", "x");
						 */
								word = word.replaceAll("é", "x");
								
								
								kandidate = kandidate.replaceAll("o", "x");
						/*		kandidate = kandidate.replaceAll("ä", "x");
								kandidate = kandidate.replaceAll("ë", "x");
								
								kandidate = kandidate.replaceAll("e", "x");
								kandidate = kandidate.replaceAll("i", "x");
								kandidate = kandidate.replaceAll("o", "x");
								kandidate = kandidate.replaceAll("u", "x");
								
								kandidate = kandidate.replaceAll("é", "x");
								
						*/		
								kandidate = kandidate.replaceAll("é", "x");
								
															
								 //System.out.println(word +" "+kandidate);
							}
							if (word.equalsIgnoreCase(kandidate)){
							System.out.println("Tas: " + wordOrig + " " + kandidateOrig);
							
						//		psTable.add(wordOrig, AffixArt, kandidateOrig, lineNumber, context);
							}
							word = wordOrig;
						}// the end of the else
					}
			
			}
	}
	
	public void writeResult(String File)throws Exception{
		sTable.saveTable(File);
	}
	
	public void whitestemmResult(String File)throws Exception{
		psTable.saveTable(File);
	}
	public void checkTheParadigm(String file) throws Exception{
		System.out.println("________________________________________________");
		TextWords st = sTable.getParadigmStemmsInformation();
		st.saveTable(file);
		// sTable = st;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
				System.out.println("Das Programm wurde gestartet");
				String Input = "E:\\Promotion\\Rodange-POS\\renertNewTagged.xml";
				String inputTest = "E:\\Promotion\\Lemmatizer\\Lux\\zumloeschen.xml";
				String Output = "E:\\Promotion\\Rodange-POS\\evalutionResult_chen.csv";
				CountSuffix cs = new CountSuffix(Input); //, Output);
				System.out.println("Die Dateien wurden gelesen");
				
				//cs.setSuffixAndCount("ei schaft éi anz ie ik in ung ett onk ioun heet ad keet ur ive", "NOUN");
				
				
			//	cs.setAffixAndCount(null, "ss", "éi i", "VERB", "infixPlusSuffix");
			//	cs.setAffixAndCount(null, "tt", "éi i", "VERB", "infixPlusSuffix");

			//	cs.setAffixAndCount(null, "t", "oo éi", "VERB", "infixPlusSuffix");
			//	cs.setAffixAndCount(null, "s", "oo éi", "VERB", "infix");
			
				cs.setAffixAndCount("ge", "t", "u a", "VERB", "zirkumfix");
				
				cs.setAffixAndCount(null, "t", "u ue", "VERB", "infixPlusSuffix");
				cs.setAffixAndCount(null, "s", "u ue", "VERB", "infixPlusSuffix");			
							
				
			
			/*										
				cs.setAffixAndCount(null, "t", "u i", "VERB", "infixPlusSuffix");
				cs.setAffixAndCount(null, "s", "u i", "VERB", "infixPlusSuffix");
								
				cs.setAffixAndCount(null, "ss", "i ë", "VERB", "infixPlusSuffix");
				cs.setAffixAndCount(null, "tt", "i ë", "VERB", "infixPlusSuffix");
				
				cs.setAffixAndCount(null, "s", "i ë", "VERB", "infixPlusSuffix");
				cs.setAffixAndCount(null, "t", "i ë", "VERB", "infixPlusSuffix");
				
				cs.setAffixAndCount(null, "s", "i ee", "VERB", "infixPlusSuffix");
				cs.setAffixAndCount(null, "t", "i ee", "VERB", "infixPlusSuffix");
				
				cs.setAffixAndCount(null, "s", "i äi", "VERB", "infixPlusSuffix");
				cs.setAffixAndCount(null, "t", "i äi", "VERB", "infixPlusSuffix");
				
				cs.setAffixAndCount(null, "t", "ë ee", "VERB", "infixPlusSuffix");
				cs.setAffixAndCount(null, "s", "ë ee", "VERB", "infixPlusSuffix");
				
				cs.setAffixAndCount(null, "s", "w f", "VERB", "infixPlusSuffix");
				cs.setAffixAndCount(null, "t", "w f", "VERB", "infixPlusSuffix"); */
				
			//	cs.setAffixAndCount(null, "ten", null, "VERB", "suffix");
			//	cs.setAffixAndCount(null, "te", null, "VERB", "suffix");
			//	cs.setAffixAndCount("ge", "t", null, "VERB", "zirkumfix");
				
		//		cs.setAffixAndCount("ge", "en", "ie a", "VERB", "infixPlusSuffix");
		//		cs.setAffixAndCount("ge", null, "ie a", "VERB", "infixPlusPrefix");
				
			//	cs.setAffixAndCount("g", "t", null, "VERB", "zirkumfix");
				//cs.setAffixAndCount("g", null, null, "VERB", "prefix");
				cs.setAffixAndCount(null, "nn", null, "VERB", "suffix");
				cs.setAffixAndCount(null, "tt", null, "VERB", "suffix");
			//	cs.setAffixAndCount(null, "nn", null, "VERB", "suffix");
		//		cs.setAffixAndCount(null, "s", null, "VERB", "suffix");
		//		cs.setAffixAndCount(null, "t", null, "VERB", "suffix");
				cs.setAffixAndCount(null, "e", null, "VERB", "suffix");
		//		cs.setAffixAndCount(null, null, null, "VERB", "nullmorphem");		
				
				
				cs.writeResult("E:\\Promotion\\Rodange-POS\\paradigmStable.csv");
			
				cs.sTable.saveTable("E:\\Promotion\\Rodange-POS\\paradigmSuffixZelle.csv");
				
				//cs.checkTheStemm("NOUN", "er", false);
				//cs.whitestemmResult("E:\\Promotion\\Rodange-POS\\evalutionResult_stemm.csv");
				cs.sTable.getParadigmStemmsInformation2("E:\\Promotion\\Rodange-POS\\paradigm2.csv");
				//cs.checkTheParadigm("E:\\Promotion\\Rodange-POS\\paradigm.csv");
				
				
				//cs.whiteResult(Output);
	}

}
