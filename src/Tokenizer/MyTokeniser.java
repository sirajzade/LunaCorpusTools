/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tokenizer;

import java.io.IOException;
import java.io.StringReader;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author sira2201
 */

  

public class MyTokeniser {
   
    private PreLuxTokeniser plt;
    
    
    
    public char satzzeichen [];
    
    private void convertToSatzzeichen (String zeichenString){
        satzzeichen = zeichenString.toCharArray();
    }
    
    public MyTokeniser(String spunct, String wpunct, String partikels[], String saztzeichen) {
     convertToSatzzeichen(saztzeichen);   
 //    plt = new PreLuxTokeniser(Spunct, Wpunct, Partikels);
    }

    private boolean istSatzzeichen (String zeichenU){
        for (int i = 0; i < satzzeichen.length; i++) {
            char zeichen = satzzeichen[i];
            if (zeichen == zeichenU.charAt(0)){
                return true;
            }
        }
        return false;
    }
    
    public Document tokeniseTextToXml (String text){
        Document doc = null;
                try {
            String xmldec = "<?xml version='1.0' encoding='UTF-8'?>";
            String xmlanfang = "<text>";
            String xmlende = "</text>";
            String xmltext = xmldec + xmlanfang + xmlende;
            doc = new SAXBuilder().build(new StringReader(xmltext));
//            Element root = doc.getRootElement();
//            String tisi = plt.tokenizeInWords(text);
//            
//            System.out.println("hier ist der Text: " + tisi);
//            StringTokenizer st = new StringTokenizer(tisi);
//             while (st.hasMoreTokens()){
//                        String token;
//                        token = st.nextToken();
//                        System.out.println(token);
//                        if (istSatzzeichen(token)){
//                            Element c = new Element("c");
//                            c.setText(token);
//                            root.addContent(c);
//                        } else {   
//                            Element w = new Element("w");
//                            w.setText(token);
//                            root.addContent(w);
//                        }
//                }
//            
        } catch (JDOMException ex) {
            Logger.getLogger(MyTokeniser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MyTokeniser.class.getName()).log(Level.SEVERE, null, ex);
        }
         return doc;
    }
}