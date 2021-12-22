package lunacorpustools.POSTrainer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.xpath.XPath;
import wordList.FreqList;

public class TrainSentencePositions {

    FreqList anzahlStellenMitPOS;
    FreqList anzahlStellen;
    
    FreqList anzahlStellenMitPOSBackwards;
    FreqList anzahlStellenBackwards;
    
    String toShowAnfangsPositionen;
    String toUseAnfangePositionen;
    
    String toShowEndPositionen;
    String toUseEndPositionen;
    
    
    public TrainSentencePositions() throws JDOMException, IOException {

        anzahlStellenMitPOS = new FreqList();
        anzahlStellen = new FreqList();

        anzahlStellenMitPOSBackwards = new FreqList();
        anzahlStellenBackwards = new FreqList();
        
        
    }

    public void setFrequencies(List tokens, String attForPOS) throws JDOMException, FileNotFoundException {
        //List sentences = Satzmodul(xPathAusdruck, doc, attForSentenceNumber, wortElement, punctElement);
        List sentences = neuesSatzmodul(tokens, "sen");
        Iterator senIt = sentences.iterator();
        while (senIt.hasNext()) {
            List words = (List) senIt.next();
            for (int i = 0; i < words.size(); i++) {
                Element word = (Element) words.get(i);
                String att = word.getAttributeValue(attForPOS);
                if (att != null) {
                    anzahlStellenMitPOS.add(att + "@@@" + (i + 1));
                    anzahlStellen.add("" + (i + 1));
                     
                }
               // System.out.println(att + " " + (i + 1));
            }
        } // the end of the while loop
        String [] anfangPositionen = writePositionProbabilitiesToUseToShow(anzahlStellenMitPOS, anzahlStellen);
        toUseAnfangePositionen = anfangPositionen[0];
        toShowAnfangsPositionen = anfangPositionen[1];
    }

    public void setFrequenciesBackwards(List tokens, String attForSentenceNumber, String attForPOS, String wortElement, String punctElement) throws JDOMException, FileNotFoundException {
        // List sentences = Satzmodul(xPathAusdruck, doc, attForSentenceNumber, wortElement, punctElement);
        List sentences = neuesSatzmodul(tokens, "sen");
        
        Iterator senIt = sentences.iterator();
        while (senIt.hasNext()) {
            List words = (List) senIt.next();
            int y = 1;
            for (int i = words.size() - 1; i >= 0; i--) {
                Element word = (Element) words.get(i);
                String att = word.getAttributeValue(attForPOS);
                anzahlStellenMitPOSBackwards.add(att + "@@@" + y);

                // we don't need FreqList anzahlStellenBackwards really, 
                // because anzahlStellen is the same, but for the separate Functionality of both
                // methods setFrequences and setFrequenciesBackwards we implement it
                anzahlStellenBackwards.add("" + y);
                y++;
            }
        } // the end of the while loop
        String [] endPositionen = writePositionProbabilitiesToUseToShow(anzahlStellenMitPOSBackwards, anzahlStellenBackwards);
        toUseEndPositionen = endPositionen[0];
        toShowEndPositionen = endPositionen[1];
    }

    public String getAnfangPositionenToUse() {
        return toUseAnfangePositionen;              
    }

    public String getEndPositionenToUse() {
        return toUseEndPositionen;
    }
    
    public String getAnfangPositionenToShow() {
        return toShowAnfangsPositionen;
    }

    public String getEndPositionenToShow() {
        return toShowEndPositionen;
        
    }
    

    private String[] writePositionProbabilitiesToUseToShow(FreqList stellenMitPOS, FreqList alleStellen) {
        String [] retval = new String[2];
        
        StringBuffer toShow = new StringBuffer();
        StringBuffer toUse = new StringBuffer();
        
        toShow.append("Tag_Position Wahrscheinlichkeit FrequentzDesTagsInDerPosition AlleTagsInDerPosition \n");
        Iterator it = stellenMitPOS.iterator();
        while (it.hasNext()) {
            String word = (String) it.next();
            int freq = stellenMitPOS.getFreq(word);
            //String pos = word.substring(0, word.indexOf('_'));
            int position = Integer.parseInt(word.substring(word.indexOf("@@@") + 3, word.length()));
            int alleInDerPostiton = alleStellen.getFreq(position + "");
            double p = 0.0;
            p = (double) freq / (double) alleInDerPostiton;
            toShow.append(word + " " + p + " " + freq + " " + alleInDerPostiton + "\n");
            
            String ohneKlammerAffe = word.replaceAll("@@@", "_");
            toUse.append(ohneKlammerAffe + " " + p + "\n");
        }
        retval[0] = toUse.toString();
        retval[1] = toShow.toString();
        return retval;
    }
    
    

    private List<List> Satzmodul(String xPathAusdruck, Document dok, String attForSentenceNumber, String wortElement, String punctElelement) throws JDOMException {

        List listOfSentences = new ArrayList<List>();
        List listOfWords = new ArrayList<Element>();

        List sentences = XPath.selectNodes(dok, xPathAusdruck);
        Iterator senIt = sentences.iterator();
        String attBefore = null;
        while (senIt.hasNext()) {
            Element sentence = (Element) senIt.next();
            String attAfter = sentence.getAttributeValue(attForSentenceNumber);
            // if 2 sentences have same number, that means they are one sentence 
            if (attAfter.equals(attBefore)) {
                List words = getWordsAndPunc(sentence, wortElement, punctElelement);
//				List newWords = new ArrayList();
//					for (int i = 0; i < words.size(); i++) {
//						Element word = (Element)words.get(i);
//						Element newWord = word;
//						newWords.add(newWord);
//					}
                listOfWords.addAll(words);
            } // otherwise it is only one sentence
            else {
                if (!listOfWords.isEmpty()) {
                    listOfSentences.add(listOfWords);
                    listOfWords = new ArrayList<Element>();
                }
                listOfWords = getWordsAndPunc(sentence, wortElement, punctElelement);
            }
            attBefore = attAfter;
        }

        // for the last sentence
        listOfSentences.add(listOfWords);
        return listOfSentences;
    }

    
    private List<List> neuesSatzmodul(List tokens, String attForSen) throws JDOMException{
        List listOfSentences = new ArrayList<List>();
        List listOfWords = new ArrayList<Element>();

        Iterator tokensIt = tokens.iterator();
        String attAlt = "0";
        while (tokensIt.hasNext()){
            Element token = (Element)tokensIt.next();
            String att = token.getAttributeValue(attForSen);
            if (att!=null){
                if (att.equals(attAlt)){
                    listOfWords.add(token);    
                } else {
                    listOfSentences.add(listOfWords);
                    listOfWords = new ArrayList<Element>();
                }
  
                attAlt = att;
            }
            else {
       
                System.out.println("Das Token <"+ token.getText() + "> hat keine Satzinformation auf der Seite: ");
            }
            
        }
        
        return listOfSentences;
    }
    
    private List getWordsAndPunc(Element sentence, String wortElement, String punctElement) {
        List list = new ArrayList();
        List test = sentence.getContent();
        Iterator tata = test.iterator();
        while (tata.hasNext()) {
            Object o = tata.next();
            if (o instanceof Element) {
                Element el = (Element) o;
          //       System.out.println(el.getName() + " " + wortElement);
                if (el.getName().equals(wortElement) || el.getName().equals(punctElement)) {
                    String att = el.getAttributeValue("pos");
                    if (!att.equals("")){
           //             System.out.println(el.getText() + " " +att);
                        list.add(el);
                    }
                }
            }
        }
        return list;
    }

    public static void main(String[] args) throws JDOMException, IOException {
        // TODO Auto-generated method stub

        String xmlFile = "E:\\Promotion\\Rodange-POS\\renert_train_sentenciert.xml";
        String Output = "E:\\Promotion\\Rodange-POS\\Satzpositionen1.txt";
        String OutputBack = "E:\\Promotion\\Rodange-POS\\SatzpositionenBack1.txt";
        //	TrainSentencePositions tsp = new TrainSentencePositions(xmlFile);
        //	tsp.setFrequencies("//s", "n", "pos", "w", "c");
        //	tsp.setFrequenciesBackwards("//s", "n", "pos", "w", "c");
    }
}
