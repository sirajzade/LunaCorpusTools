package Tokenizer;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import org.jdom2.Attribute;
import org.jdom2.Content;


import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.EntityRef;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPath;

public class TokeniserXML {

    private Document doc;
    private XMLOutputter out;
    private PreLuxTokeniser tokeniser;
    private String [] exeptionElements;
    private long tokenID;
    private String uniqueID;
    private String namespace;
    private boolean makeUniqueID;

    public TokeniserXML(String xmlfile, String XMLElement, String Spunct, String bedingtesZusammensetzen[], String bedingtesTrennen[], String exeptionElems[], boolean uniqueID)
            throws IOException, JDOMException {

        tokenID = 0;
        tokeniser = new PreLuxTokeniser(Spunct, bedingtesZusammensetzen, bedingtesTrennen);
        doc = new SAXBuilder().build(new StringReader(xmlfile));
        exeptionElements = exeptionElems;
        makeUniqueID = uniqueID;
        processTheDocument(doc, XMLElement);

    }

    public void writeOutput(String outputfile) throws FileNotFoundException, IOException {
        out = new XMLOutputter();
        OutputStream os = new FileOutputStream(outputfile);
        out.output(doc, os);
    }

    public String getOutPut() {
        out = new XMLOutputter(Format.getPrettyFormat());
        String file = out.outputString(doc);
        return file;
    }

    public Document getXMLasDom() {
        return doc;
    }

    private void processTheDocument(Document XMLdoc, String elementToTokenise)
            throws IOException, JDOMException {

        List<?> lines = XPath.selectNodes(XMLdoc, elementToTokenise);

        Iterator linesIt = lines.iterator();

        // Here we iterate over all lines selected
        while (linesIt.hasNext()) {
            
            Element element1 = (Element) linesIt.next();
        //    System.out.println("Das Programm bearbeitet das Element -> " + element1.getName());
            tagElement(element1);
        }
    } // The end of the method prossesTheDocument

    
    private boolean isNotExeptionElement(String elemName, String [] exeptionElements){
        for (int i = 0; i < exeptionElements.length; i++) {
            String string = exeptionElements[i];
            if (string.equalsIgnoreCase(elemName)){
                return false;
            }
        }
        return true;
    }
    
    public void tagElement(Element el) {
        int durchgang = 0;
        durchgang++;
        List content = el.getContent();
       
        for (int i = 0; i < content.size(); i++) {
            Object o = content.get(i);
            Content c = (Content)o;
           // System.out.println("ForIndex: " + i + " " + c.getCType().name() + " <<<<" + c.getValue() + ">>>>");
            
            if (o instanceof Element) {
                Element oe = (Element) o;
          //      System.out.println("Element taggen: " + oe.getName() + " duchrgang " + durchgang + " index " + i);
                tagElement(oe);
                
            } else if (o instanceof Text) {
                if (isNotExeptionElement(el.getName(), exeptionElements)){
                    Text t = (Text) o;
                    int u = el.indexOf(t);
                    t.detach();
                    List retval = textToXML(t);
                    el.addContent(u, retval);
                    i = i + (retval.size()-1);
                    //System.out.println("Den Text token: '" + t.toString() + "' duchrgang " + durchgang + " index " + i + " retvalsize " + retval.size());
                }
            } else if (o instanceof EntityRef){
                System.out.println("Es ist weder Element, noch der Text!");
            }
        }

    }

    public List textToXML(Text tt) {
        List retval = new ArrayList();

        StringTokenizer st = new StringTokenizer(tokeniser.tokenise(tt.getText()));
        	//System.out.println("The Text -> "+tt.getText() + " -> " + tokeniser.tokenise(tt.getText()));
        
        while (st.hasMoreTokens()) {
            String wordtoken = st.nextToken();

            //	System.out.print(" '" +wordtoken + "' ")
            // Look, if the word is 1 character
            if (wordtoken.length() == 1) {
                char ch = wordtoken.charAt(0);
                if (!Character.isLetterOrDigit(ch)) {
                    Element c = makeCharacterToken(wordtoken);
                    retval.add(c);
                } else {
                    Element w = makeWordToken(wordtoken);
                    retval.add(w);
                }
            } else {
                Element w = makeWordToken(wordtoken);
                retval.add(w);
            }
        }
        return retval;
    }

     private Element makeWordToken(String wordtoken) {
          Element w = new Element("w");
          Attribute posW = new Attribute("pos", "");
          w.setAttribute(posW);

          Attribute idW = new Attribute("id", makeID());
          w.setAttribute(idW);

          w.addContent(wordtoken);
         
          return w;
     }
     
     private String makeID(){
          if (makeUniqueID){
               uniqueID =  UUID.randomUUID().toString();
               return uniqueID;
          } else {
               tokenID++;
               return tokenID+"";
          } 
     }
     private Element makeCharacterToken(String wordtoken) {
          Element c = new Element("c");
          Attribute posC = new Attribute("pos", "");
          c.setAttribute(posC);
          
          Attribute idC = new Attribute("id", makeID());
          c.setAttribute(idC);

          c.addContent(wordtoken);
         
          return c;
     }
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        String XMLFile = "C:\\Users\\joshgun.sirajzade\\Documents\\Wordbildung\\Korpus_Alt\\Korpus2_korr\\Berens_Kerfegsblom_1.xml";

        String OutPut = "C:\\Users\\joshgun.sirajzade\\Documents\\Wordbildung\\Korpus_Alt\\Korpus2_korr\\Berens_Kerfegsblom_1_tok.xml";


        String PARTICLES[] = {"Dr (\\\\.)->Dr$1"};
        String MARTICLES[] = {"d???([A-Z])->d??? $1"};

        // A list of characters, that may separate for word and sentences 

        String SPUNCT = "!$\"\u00a3%^&*()_+=#{}[];:???`/?,. \t\n'";

        // A list of characters, that can be a part of a word
        String WPUNCT = "'-";

        FileReader fr = new FileReader(XMLFile);
        byte[] encoded = Files.readAllBytes(Paths.get(XMLFile));
        String file = new String(encoded);

         String exeptElems[] = {""};

        TokeniserXML tokXML = new TokeniserXML(file, "//text", SPUNCT, PARTICLES, MARTICLES, exeptElems, false);
        tokXML.writeOutput(OutPut);
        System.out.println("Tokenisierung erfolgreich durchgef??hrt! ____________");



    }
}
