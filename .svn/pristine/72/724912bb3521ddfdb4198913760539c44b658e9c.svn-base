/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Utilities;

import XMLDatabase.Benutzer;
import XMLDatabase.ExistDatabase;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import lunacorpustools.JPanelForTypes;
import lunacorpustools.NewMDIApplication;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

/**
 *
 * @author sirajzad
 */
public class DataModels {
     
     public static HashMap TableToHashMap(DefaultTableModel tm) {
        HashMap retval = new HashMap();
        int reihenAnzahl = tm.getRowCount();
        if (reihenAnzahl == 0) {
            retval.put("d", "d");
        } else {
            for (int i = 0; i < reihenAnzahl; i++) {
                String value1 = (String) tm.getValueAt(i, 0);
                String value2 = (String) tm.getValueAt(i, 1);

                if (value1 != "" && !value1.matches(" *")) {
                    retval.put(value1, value2);
                    System.out.println("Folgendes wurde gelesen: " + value1 + " " + value2);
                }
            }
        }
        return retval;
    }
     
     public static void putInTheDatabaseAskName (Benutzer benutzer, String pfad, String text){
          if (benutzer.isLogged()){
            try {
                ExistDatabase database = benutzer.getDatabase();
                String name = JOptionPane.showInputDialog("Geben Sie einen Namen");
                database.putData(pfad, name, text);
  
            } catch (URISyntaxException ex) {
                 Logger.getLogger(DataModels.class.getName()).log(Level.SEVERE, null, ex);
             } catch (XMLDBException ex) {
                Logger.getLogger(NewMDIApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
            } else {
               JOptionPane.showMessageDialog(null , "Bitte loggen Sie sich ein, um eine Datei aus der Datenbank öffnen zu können!");
        }
     }
     
     public static void putInTheDatabaseWithName(Benutzer benutzer, String pfad, String text, String name) {
        if (benutzer.isLogged()) {
            try {
                ExistDatabase database = benutzer.getDatabase();
                database.putData(pfad, name, text);

            } catch (URISyntaxException ex) {
                Logger.getLogger(DataModels.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("URI is going wrong!");
            } catch (XMLDBException ex) {
                Logger.getLogger(NewMDIApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            JOptionPane.showMessageDialog(null, "Bitte loggen Sie sich ein, um eine Datei aus der Datenbank öffnen zu können!");
        }
    }
     
     public static String HashMapToXMLStandardisation(HashMap hashy){
         String beginn = "<regEx>";
         String end = "</regEx>";
         StringBuilder sb = new StringBuilder();
         sb.append(beginn);
         Iterator it = hashy.keySet().iterator();
        //  System.out.println("Austausch vorbereitet!");
          while (it.hasNext()){
               String key = (String)it.next();
               String value = (String) hashy.get(key);
               sb.append("<zelle><suchen>" +  key + "</suchen><austauschen>" + value + "</austauschen></zelle>");
               
               System.out.println("In der Schleife mit " + key + " " +value);
          }
         sb.append(end);
         return sb.toString();
     }
     
     public static ResourceSet getResourseSetFromTheDatabaseX(ExistDatabase database, String pfad) throws XMLDBException{
           
             String xquerry = " xquery version \"3.0\"; "
                     + "declare namespace xsd = \"http://www.tei-c.org/ns/1.0\";"
                     + "  for $w in //xsd:title[@type=\"short\"] "
                     + "return $w";

             ResourceSet result = database.executeXQuery(pfad, xquerry);
             return result;
     }
     
     
     
     public static String[] resourceSettoArray(ResourceSet result) throws XMLDBException{
            
             String werke[] = new String[(int) result.getSize()];
             for (int i = 0; i < (int) result.getSize(); i++) {
                 XMLResource resource = (XMLResource) result.getResource((long) i);
                 Node node = resource.getContentAsDOM();
                 Element childNode = (Element) node.getFirstChild();
                 // System.out.println(node.getNodeName() + " " + node.hasChildNodes() + " " + childNode.getNodeName() + " " + childNode.getTextContent());
                 werke[i] = childNode.getTextContent();
             }
             return werke;

     }
     
     public static String getSelectedXMLResourse(int index, ExistDatabase database, ResourceSet result) throws XMLDBException, TransformerConfigurationException, TransformerException{
         String datei = null;
         if (index != -1) {
             XMLResource xml = (XMLResource) result.getResource(index);
             String id = xml.getDocumentId();
             // System.out.println(id);
             datei = database.getXmlFile(id);

         }
         return datei;

     }
     
    
     
     public static DefaultTableModel XMLtoTableModel(String Input) throws ParserConfigurationException, SAXException, IOException {
         Document doc = loadXMLFromString(Input);
         DefaultTableModel tm = new javax.swing.table.DefaultTableModel(
            
            new String [] {
                "Reguläre Ausdrücke zum Suchen", "Reguläre Ausdrücke zum Austauschen"
            }, 0
        );
         
         Element root = doc.getDocumentElement();

         NodeList list = root.getElementsByTagName("zelle");

         for (int i = 0; i < list.getLength(); i++) {
             Node zelle =  list.item(i);
             Element zell = (Element)zelle;
             Element suchen =  (Element)zell.getElementsByTagName("suchen").item(0);
             Element tauschen =  (Element)zell.getElementsByTagName("austauschen").item(0);;
             String such = suchen.getTextContent();
             String tau = tauschen.getTextContent();
             
             
             tm.addRow( new String [] {such , tau });
             System.out.println("Added " + such + " " + tau + " " + suchen.getNodeName());
         }
         return tm;
     }
     
     public static Document loadXMLFromString(String xml) throws ParserConfigurationException, SAXException, IOException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();

        return builder.parse(new ByteArrayInputStream(xml.getBytes()));
    }
     
     public static String getDocumentName(String Text, String xPathSureName, String xPathShortName) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException{
         Document doc = loadXMLFromString(Text);
         XPath xPath = XPathFactory.newInstance().newXPath();
         NodeList nodes = (NodeList)xPath.evaluate(xPathSureName, doc.getDocumentElement(), XPathConstants.NODESET);
         Element title = (Element) nodes.item(0);
         String surname = title.getTextContent();
         
         NodeList nodes2 = (NodeList)xPath.evaluate(xPathShortName, doc.getDocumentElement(), XPathConstants.NODESET);
         Element title2 = (Element) nodes2.item(0);
         String shortName = title2.getTextContent(); 
         String retval = null;
         
         if (surname != null){
             retval = shortName.replaceAll(" ", "_");
         } else {
             retval = surname.replaceAll(" ", "_") + "_" + shortName.replaceAll(" ", "_");
         }
         return retval;
     }
     
        public static int levenshteinDistance(CharSequence lhs, CharSequence rhs) {
        int len0 = lhs.length() + 1;
        int len1 = rhs.length() + 1;

        // the array of distances                                                       
        int[] cost = new int[len0];
        int[] newcost = new int[len0];

        // initial cost of skipping prefix in String s0                                 
        for (int i = 0; i < len0; i++) {
            cost[i] = i;
        }

        // dynamically computing the array of distances                                  

        // transformation cost for each letter in s1                                    
        for (int j = 1; j < len1; j++) {
            // initial cost of skipping prefix in String s1                             
            newcost[0] = j;

            // transformation cost for each letter in s0                                
            for (int i = 1; i < len0; i++) {
                // matching current letters in both strings                             
                int match = (lhs.charAt(i - 1) == rhs.charAt(j - 1)) ? 0 : 1;

                // computing cost for each transformation                               
                int cost_replace = cost[i - 1] + match;
                int cost_insert = cost[i] + 1;
                int cost_delete = newcost[i - 1] + 1;

                // keep minimum cost                                                    
                newcost[i] = Math.min(Math.min(cost_insert, cost_delete), cost_replace);
            }

            // swap cost/newcost arrays                                                 
            int[] swap = cost;
            cost = newcost;
            newcost = swap;
        }

        // the distance is the cost for transforming all letters in both strings        
        return cost[len0 - 1];
    }
        
       
}
