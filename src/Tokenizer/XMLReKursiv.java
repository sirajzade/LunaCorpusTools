/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Tokenizer;

import java.util.List;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 *
 * @author joshgun.sirajzade
 */
public class XMLReKursiv {
    public static int durchgang = 0;
   
    
    
    public static void tagElement(Element el){
        durchgang++;
        
        List content = el.getContent();
       // Iterator it = content.iterator();
        for (int i = 0; i < content.size(); i++) {
            Object o = content.get(i);
           
            if (o instanceof Element){
                Element oe = (Element)o;
          //      System.out.println("Element taggen: " + oe.getName() + " duchrgang " + durchgang);
                tagElement(oe);
            }
            else if (o instanceof Text){
                Text t = (Text)o;
                String text = t.getText();
        
                Element tokenEle = new Element("tokenEle");
                tokenEle.setText(text);
                
                int u = el.indexOf(t);
                t.detach();
                el.addContent(u, tokenEle);
                
          //      System.out.println("Den Text taggen: " + t.getText() + " duchrgang " + durchgang);
            }
            else {
                
            }
        }
        
    }
    
   
    
    
    
     public static void main(String[] args) throws Exception {
         Element el1 = new Element("el1");
         el1.addContent("   ");
         Element el1zu2 = new Element("el1zu2");
         el1.addContent(el1zu2);
         el1.addContent("     text text text");
         
         Element el2 = new Element("el2");
         el2.addContent("text2 text2 text2");
         
         Element el3 = new Element("el3");
         el3.addContent("text3 text3 text3");
         
         Element el4 = new Element("el4");
         el4.addContent("text4 text4 text4");
         
         el1.addContent(el2);
         el2.addContent(el3);
         el3.addContent(el4);
         
         XMLOutputter out = new XMLOutputter();
           out.setFormat(Format.getPrettyFormat());
            out.output(el1, System.out);
            
            System.out.println("____________________");
            
            XMLReKursiv.tagElement(el1);
         
            System.out.println("____________________");
            
            out.output(el1, System.out);
            
         
     }

    
    
}
