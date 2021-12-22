package Tokenizer;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import javax.swing.JProgressBar;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.filter.Filters;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPath;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

public class SentenceTokeniserXML {

	private Document doc;
	private XMLOutputter out;
        private String[] satzZeichen;
        private int lineZ;

	
        public String getOutPut(){
            out = new XMLOutputter(Format.getPrettyFormat()); 
            String put = out.outputString(doc);
            return put;
        }
        
       
	public SentenceTokeniserXML(String xmlfile, String textBodyElement, String XMLElemente, String satzzeichen, JProgressBar bar)
			throws IOException , JDOMException{
                lineZ = 0;
		doc = new SAXBuilder().build(new StringReader(xmlfile));
                satzZeichen = stringToArray(satzzeichen);
                bar.setValue(40);
		processTheDocument(doc, textBodyElement, XMLElemente, bar);
		
	}
        
        public String[] stringToArray(String input){
            String zeichen[] = new String[input.length()];
            for (int i = 0; i < input.length(); i++) {
                 zeichen[i] = input.charAt(i)+"";
            }
            return zeichen;
        }

     public void processTheDocument(Document XMLdoc, String textBodyElemente, String Elemente, JProgressBar bar)
            throws IOException, JDOMException {

      

        List textStuecke = XPath.selectNodes(XMLdoc, textBodyElemente);
        bar.setValue(60);
        //Iterator iti = textStuecke.iterator();
        //while (iti.hasNext()) {
        for (int i = 0; i < textStuecke.size(); i++){
            Element textstueck = (Element) textStuecke.get(i);
            System.out.println("für den Text" + textstueck.getValue());
            tagSentence(textstueck, Elemente, bar);
            lineZ++;
            System.out.println("LineZ wurde inkrementiert wegen dem ende des Textstuckes");
        } // the end of textstucke iteration

     } //the end of the method process the Document

     public void tagSentence(Element textstueck, String Elemente, JProgressBar bar) throws JDOMException{
                    
         System.out.println(textstueck.getName() + " " + lineZ);
         
         List wANDc = textstueck.getChildren();
         
        // Iterator wcit = wANDc.iterator();
         bar.setValue(80);
        // while (wcit.hasNext()) {
        for (int j = 0; j < wANDc.size(); j++){
             Element stein = (Element) wANDc.get(j);
             System.out.println("Für das Wort " + stein.getText());
             if (stein.getName().equals("c") && istEinSatzZeichen(stein.getText())) {
                 stein.setAttribute("sen", lineZ + "");
                 lineZ++;
                 System.out.println("LineZ wurde inkrementiert wegen Satzzeichen");
             } else {
                 stein.setAttribute("sen", lineZ + "");

             }
             
         } // the end of the wcit while
         //lineZ++;
         //System.out.println(lineZ);
     }
     
     
        public boolean istEinSatzZeichen(String zeichen){
            boolean retval = false;
            for (int i = 0; i < satzZeichen.length; i++) {
                String string = satzZeichen[i];
                if (string.equals(zeichen)){
                    retval = true;
                }
            } 
            return retval;
        }
        
	/**
	 * @param args
	 * @throws JDOMException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException, JDOMException {
		// TODO Auto-generated method stub


		String InPut = "E:\\Promotion\\Lemmatizer\\Lux\\loeschen.xml";
		String nInPut = "E:\\tustep_daten\\lexicolux\\renfrak\\renertZusammen.xml";
		String OutPut = "E:\\Promotion\\Rodange-POS\\loeschen_sentenciert.xml";
                String satzzeichen = ".!";
//		SentenceTokeniserXML st = new SentenceTokeniserXML(InPut, OutPut, "reg", satzzeichen); // reg to l changed
		System.out.println("Satz Tokenisierung ist fertig");
	}

}
