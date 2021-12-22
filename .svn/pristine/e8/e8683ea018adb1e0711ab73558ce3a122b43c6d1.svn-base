/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lunacorpustools;

import java.awt.Dimension;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.xpath.XPath;

/**
 *
 * @author joshgun.sirajzade
 */
public class CorpusModel {
    
    public CorpusModel (String Data, JPanel PanelForTable, JScrollPane ScrollPaneForPanel){
        try {
                SAXBuilder sb = new SAXBuilder();
                Document doc = sb.build(new StringReader(Data));
                List<?> saetze = XPath.selectNodes(doc, "//s"); 
		Iterator saetzeIt = saetze.iterator();
		while (saetzeIt.hasNext()){
                        Element satz = (Element)saetzeIt.next();
                        List<?> woerter = satz.getChildren("w"); 
                        Iterator woerterIt = woerter.iterator();
                        String [][] array = new String[getCountAttributes(woerter)][woerter.size()+1];
                        String [] arrayC = new String[woerter.size()+1];
                        
                        setWordsTable(array, arrayC, woerterIt);
                        System.out.println("______________________________________");
                        
                       addTableToScrollPane(array, arrayC, PanelForTable, ScrollPaneForPanel); 
                        
                        
		}
                      
            } catch (JDOMException ex) {
            Logger.getLogger(NewMDIApplication.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex){
                
            }
       }
    
        
    
    
    
        public int getCountAttributes(List woerter){
            Element w = (Element)woerter.get(0);
            int retval = w.getAttributes().size();
            return retval;
        }
        
        
        public void setWordsTable(String[][] array, String [] arrayC, Iterator woerterIt){
                        arrayC[0] = "TOKEN:";
                        
                        int x = 1;
                                              
                        while (woerterIt.hasNext()){
                            Element w = (Element)woerterIt.next();
                            arrayC[x] = w.getText();
                            
                            System.out.println(w.getText());
                            List attributes = w.getAttributes();
                                                    
                            for (int y = 0; y < attributes.size(); y++) {
                                Attribute attribute = (Attribute)attributes.get(y);
                                String ausgabe = attribute.getValue(); 
                                array[y][0] = attribute.getName() + ":";
                                array[y][x] = ausgabe;
                                System.out.println("++++ " + ausgabe);
                            }
                            x++;
                        }
        }
    
        public void addTableToScrollPane(String[][] array, String [] arrayC, JPanel PanelForTable, JScrollPane ScrollPaneForPanel){
            
                        JTable jt = new JTable(array, arrayC);
                                                                 
                        jt.setFillsViewportHeight(false);
                        
                        jt.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
                                              
                        JScrollPane pane = new JScrollPane();
                        pane.setViewportView(jt);
                        pane.setPreferredSize(new Dimension(300, 100));
                        
                        PanelForTable.add(pane);
                        ScrollPaneForPanel.setViewportView(PanelForTable);
            
        }
    
    
}
