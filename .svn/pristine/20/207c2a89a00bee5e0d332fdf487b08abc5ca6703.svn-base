/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tokenizer;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import javax.swing.JProgressBar;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPath;

/**
 *
 * @author sirajzad
 */
public class Standardisation {

    private Document doc;
    private XMLOutputter out;
    private HashMap austausch;

    public Standardisation(String xmlFile, HashMap austausch, JProgressBar bar) throws JDOMException, IOException {
        //  System.out.println("Constructor has been set1!");
        doc = new SAXBuilder().build(new StringReader(xmlFile));
        this.austausch = austausch;

        processTheDocument("//w | //c", bar);


    }

    private void putAttributeIfChange(Element word, String oldW, String newW) {
        // now, let us see, if there is a change in the word
        // if no 
        if (oldW.equals(newW)) {
        } // if yes
        else {
        //    System.out.println("Neuer Wert wurde hinzugefügt: " + newW);
            word.setAttribute("stan", newW);
        }

    }

    public void processTheDocument(String Elemente, JProgressBar bar)
            throws IOException, JDOMException {

        List wANDc = XPath.selectNodes(doc, Elemente);
        int gesamt = wANDc.size();

        for (int i = 0; i < gesamt; i++) {
            //   System.out.println("Bearbeitung der Elemente!"); 

            doAllRegEx((Element)wANDc.get(i));


            int barWert = i * 100 / gesamt;
            bar.setValue(barWert);
        } // the end of the wcit for
    }

    private void doAllRegEx(Element stein) {    
        Iterator it = austausch.keySet().iterator();
        //  System.out.println("Austausch vorbereitet!");
        while (it.hasNext()) {
            String word = stein.getText();
            String standValue = stein.getAttributeValue("stan");
        
            String key = (String) it.next();
            String value = (String) austausch.get(key);

            // let us see, if the word has allready got an attribute stand
            // if yes
            if (standValue != null) {
                String wordNormalised = standValue.replaceAll(key, value);
             //   System.out.println("stand ist vorhanden: " + standValue+ " " + wordNormalised);
                putAttributeIfChange(stein, standValue, wordNormalised);
            } // if no
            else {
                String wordNormalised = word.replaceAll(key, value);
                stein.setAttribute("stan", wordNormalised);
            //    System.out.println("Erster Durchgang: " + wordNormalised);
            }

        }
    }

    
    public String getOutPut() {
        out = new XMLOutputter(Format.getPrettyFormat());
        String put = out.outputString(doc);
        System.out.println("Output wurde betätigt!");

        return put;
    }
}
