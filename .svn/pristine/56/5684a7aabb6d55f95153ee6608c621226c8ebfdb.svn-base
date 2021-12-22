/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Morphal;

import XMLDatabase.ExistDatabase;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;
import javax.xml.transform.OutputKeys;
import lunacorpustools.JPanelForTypes;
import lunacorpustools.zeileGUI;
import org.exist.xmldb.XQueryService;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

/**
 *
 * @author joshgun.sirajzade
 */
public class MorphSearchEngineExist {

    String regExPrefix;
    String regExBasis = ".*";
    String regExSuffix;
    String regExFlex = "(s?t)?(en?|er|em|t)?$";
    ExistDatabase database;

    public MorphSearchEngineExist(ExistDatabase database) {
        this.database = database;
    }

    public ResourceSet getElementWithSuffix(String suffix, String pos) {

        String xQuerry = "xquery version \"3.0\";"
                + " for $w in //@suffix[.='"+suffix+"']"
                + " where $w/../@pos='"+pos+"'"
                + " order by ($w/..)"
                + " return <b fileId =\"{base-uri($w/..)}\" sen=\"{$w/../@sen}\" id = \"{$w/../@id}\" pos =\"{$w/../@pos}\"> {string($w/..)} </b>";

        String pfad = "db/Korpus/fertigGetagged";

        ResourceSet result = database.executeXQuery(pfad, xQuerry);
        return result;

    }

    public ResourceSet doSearch(String prefix, String suffix, String POS) {

        regExPrefix = prefix;
        regExBasis = ".*";
        regExSuffix = suffix;
        regExFlex = "(s?t)?(en?|er|em|t)?$";

        String xQuerry = "xquery version \"3.0\";"
                + " for $w in //w "
                + " where matches($w, \"^" + regExPrefix + regExBasis + regExSuffix + regExFlex + "\") "
                + " order by ($w) "
                + " return <b fileId =\"{base-uri($w)}\" sen=\"{$w/@sen}\" id = \"{$w/@id}\" pos =\"{$w/@pos}\"> {string($w)} </b>";

        String pfad = "db/Korpus/fertigGetagged";

        regExPrefix = prefix;
        regExBasis = ".*";
        regExSuffix = suffix;
        regExFlex = "(s?t)?(en?|er|em|t)?";

        String where = "";
        if (POS.equals("Alle") || POS.equals("Wortklassen")) {
            where = " where not($w/@prefix) and not($w/@suffix)";
        } else {
            where = " where $w/@pos=\"" + POS + "\" and not($w/@prefix) and not($w/@suffix)";
        }


        String xQuerryNeu = "xquery version \"3.0\";"
                + "declare option exist:timeout\"120000\";"
                + "declare option exist:output-size-limit\"6\";"
                + "let $query :="
                + "<query>"
                + "<bool><regex>" + regExPrefix + regExBasis + regExSuffix + regExFlex + "</regex></bool>"
                + "</query>"
                + " for $w in //w[ft:query(., $query) and ./@id <2500]"
                + where
                + " order by ($w) "
                + " return <b fileId =\"{base-uri($w)}\" sen=\"{$w/@sen}\" id = \"{$w/@id}\" pos =\"{$w/@pos}\"> {string($w)} </b>";

        ResourceSet result = database.executeXQuery(pfad, xQuerryNeu);
        return result;

    }

    public ResourceSet doRegExSearch(String regex, String POS, String pfad) {

        String where = "";
        if (POS.equals("Alle") || POS.equals("Wortklassen")) {
            // X-Path für alle Wörter //w[@pos]
        } else {
            where = " where $w/@pos=\"" + POS + "\"";
        }

        String xQuerryNeu = "xquery version \"3.0\";"
          //      + "declare option exist:timeout\"15000\";"
          //      + "declare option exist:output-size-limit\"6\";"
                + " let $query :="
                + "<query>"
                + "<bool><regex>" + regex + "</regex></bool>"
                + "</query>"
                + " for $w in //w[ft:query(., $query)]"
                + where
                + " order by ($w) "
                + " return <b fileId =\"{base-uri($w)}\" sen=\"{$w/@sen}\" id = \"{$w/@id}\" pos =\"{$w/@pos}\"> {string($w)} </b>";

       

        ResourceSet result = database.executeXQuery(pfad, xQuerryNeu);
        return result;

    }
    
    public ResourceSet doRegExSearchInUnannotated(String regex, String POS) {

        String where = "";
        if (POS.equals("Alle") || POS.equals("Wortklassen")) {
            where = "\" not($w/@prefix) and not($w/@suffix)";
        } else {
            where = " where $w/@pos=\"" + POS + "\" and not($w/@prefix) and not($w/@suffix)";
        }

        String xQuerryNeu = "xquery version \"3.0\";"
                + "declare option exist:timeout\"15000\";"
                + "declare option exist:output-size-limit\"6\";"
                + " let $query :="
                + "<query>"
                + "<bool><regex>" + regex + "</regex></bool>"
                + "</query>"
                + " for $w in //w[ft:query(., $query)]"
                + where
                + " order by ($w) "
                + " return <b fileId =\"{base-uri($w)}\" sen=\"{$w/@sen}\" id = \"{$w/@id}\" pos =\"{$w/@pos}\"> {string($w)} </b>";

        String pfad = "db/Korpus/fertigGetagged";

        ResourceSet result = database.executeXQuery(pfad, xQuerryNeu);
        return result;

    }

    public void getBasisForm(XMLResource resource, JPanel panel) throws XMLDBException {
        Node node = (Node) resource.getContentAsDOM();
        Element childNode = (Element) node.getFirstChild();
        String childText = childNode.getTextContent();

        // Notiz: schauen, dann dass man die Wortbasis nicht überschreibt

        String lemmaOhnePref = childText.replaceFirst(regExPrefix, "");
        String lemmaOhneAlles = lemmaOhnePref.replaceAll(regExSuffix + regExFlex, "");

        String xQuerry2 = "xquery version \"3.0\";"
                + " for $w in //w "
                // Notiz: was soll man als Basis suchen....

                + " where matches($w, \"^(ge)?" + lemmaOhneAlles + "(s?t)?(en?|er|em|t|s)?$\", \"i\") and not (matches($w, \"" + lemmaOhneAlles + regExSuffix + "\", \"i\"))"
                + " return $w ";

        String pfad2 = "db/Korpus/fertigGetagged";


        System.out.println(" Für das Wort: " + childNode.getTextContent());

        ResourceSet result2 = database.executeXQuery(pfad2, xQuerry2);
        System.out.println(" ++++++++++++++ size of result for: <" + lemmaOhneAlles + "> " + result2.getSize());
        for (int y = 0; y < (int) result2.getSize(); y++) {
            XMLResource resource2 = (XMLResource) result2.getResource((long) y);
            Node node2 = (Node) resource2.getContentAsDOM();
            Element childNode2 = (Element) node2.getFirstChild();

            System.out.println(" Gefundene Basen: " + childNode2.getTextContent());
            JPanel basePanel = new JPanel();
            JLabel base = new JLabel(childNode2.getTextContent());
            basePanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            basePanel.add(base);
            panel.add(basePanel);
        }

    }

    public void getContext(String childText, String satzNummer, String fileID, String wortID, String pos, JPanel panel, int nummer) throws XMLDBException {
        //database.cleanup();
      
        //System.out.println("wort und die Satznummer: " + childText + " " + satzNummer + " fileId " + fileID);

        String xQuerryContext = "xquery version \"3.0\";"
                // and base-uri(.)='"+ fileID+"' 
                // and base-uri(.)='"+ fileID+"'
                + " for $w in doc('" + fileID + "')//w[@sen=\"" + satzNummer + "\" ] | doc('" + fileID + "')//c[@sen=\"" + satzNummer + "\" ]"
                + " return $w ";

        String pfad2 = "db/Korpus/fertigGetagged";
        Collection col = database.getCollection();
        XQueryService service = (XQueryService) col.getService("XQueryService", "1.0");
        // set pretty-printing on
        service.setProperty(OutputKeys.INDENT, "yes");
        service.setProperty(OutputKeys.ENCODING, "UTF-8");

        CompiledExpression compiled = service.compile(xQuerryContext);
        ResourceSet resultNeu = database.executeXQueryBySettingCollectionSeperately(pfad2, compiled, service);
        // System.out.println("Funde zu diesem Satz: " + resultNeu.getSize());
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < (int) resultNeu.getSize(); y++) {
            XMLResource resource2 = (XMLResource) resultNeu.getResource((long) y);
            Node node2 = (Node) resource2.getContentAsDOM();
            Element childNode2 = (Element) node2.getFirstChild();

            sb.append(childNode2.getTextContent() + " ");
            // System.out.print(childNode2.getTextContent() + " ");
        }
        printToGui(panel, sb.toString(), childText, nummer, fileID, wortID, pos);

    }

    public LinkedHashMap<String, ArrayList<HashMap<String, String>>> makeTypes(ResourceSet result, JProgressBar bar) {
        LinkedHashMap<String, ArrayList<HashMap<String, String>>> map = new LinkedHashMap<String, ArrayList<HashMap<String, String>>>();
        try {
            ResourceIterator it = result.getIterator();
            int all = (int)result.getSize();
            int zaehler = 0;
            while (it.hasMoreResources()) {
                int percent = (int) ((zaehler * 100) / all );
                bar.setValue(percent);
               
                XMLResource resource = (XMLResource) it.nextResource();
                try {
                Node node = (Node) resource.getContentAsDOM();
                
                Element childNode = (Element) node.getFirstChild();
                String word = childNode.getTextContent();
                HashMap<String, String> innerMap = new HashMap<String, String>();
               
                String satzNummer = childNode.getAttribute("sen");
                String fileID = childNode.getAttribute("fileId");
                String wortID = childNode.getAttribute("id");
                String pos = childNode.getAttribute("pos");

                innerMap.put("sen", satzNummer);
                innerMap.put("fileId", fileID);
                innerMap.put("id", wortID);
                innerMap.put("pos", pos);
        
                if (map.containsKey(word)) {
                    ArrayList<HashMap<String, String>> list = map.get(word);
                    list.add(innerMap);
                } else {
                    ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                    list.add(innerMap);
                    map.put(word, list);
                }
               
                zaehler++;
                } catch (XMLDBException ex) {
                    
                } finally {
                    //((org.exist.xmldb.EXistResource)resource).freeResources();
                }
            }
        } catch (XMLDBException ex) {
            Logger.getLogger(MorphSearchEngineExist.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
           
        }
        return map;

    }
    
    public void printStatisticTypeToken(JPanel panel, String typeSize, String tokenSize){
        JPanel ueberschrift = new JPanel();
        // Formatierung
        ueberschrift.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel ueberschriftText = new JLabel("Suchergebnisse Types: " + typeSize +", Tokens: " + tokenSize + " ");
        Font font = ueberschriftText.getFont();
        Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize() + 2);
        ueberschriftText.setFont(boldFont);
        ueberschrift.add(ueberschriftText);
        ueberschrift.setMaximumSize(new Dimension(Integer.MAX_VALUE, ueberschrift.getMinimumSize().height));
        ueberschrift.revalidate();
        ueberschrift.repaint();
        panel.add(ueberschrift);

  
    }

    public void printToGUIStichwort(String word, ArrayList<HashMap<String, String>> nodes, JPanel panel, JPanel second) {

        JPanelForTypes ueberschrift = new JPanelForTypes();
        // Formatierung
       
        ueberschrift.setLayout(new FlowLayout(FlowLayout.LEFT));
        JLabel ueberschriftText = new JLabel(word + " (" + nodes.size() + ")");
        ueberschrift.setElements(nodes);
        Font font = ueberschriftText.getFont();
        Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize() + 2);
        ueberschriftText.setFont(boldFont);
        ueberschriftText.addMouseListener(new StichWortMousListener(second, this, word, nodes));
        ueberschriftText.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Formatierung Ende
         
        JCheckBox boxJa = new JCheckBox();
        JCheckBox boxNo = new JCheckBox();
        ueberschrift.setJa(boxJa);
        ueberschrift.setNein(boxNo);
        // myAlleAuswahlButton.addMouseListener(new StichWortMousListener(second, this, word, nodes));
        ueberschrift.add(boxJa);
        ueberschrift.add(boxNo);
        
        ueberschrift.add(ueberschriftText);
        ueberschrift.setMaximumSize(new Dimension(Integer.MAX_VALUE, ueberschrift.getMinimumSize().height));
        ueberschrift.revalidate();
        ueberschrift.repaint();
        panel.add(ueberschrift);


    }

    private void printToGui(JPanel panel, String str, String word, int nummer, String fileID, String wortID, String pos) {

        String[] parts = str.split(word);
        if (parts.length < 2) {
            System.out.println("Sentence has only one word: " + word + " in: " + str);
        } else {
            zeileGUI zeile = new zeileGUI(pos, nummer + ") " + parts[0], word, parts[1], fileID, wortID);
            zeile.printZeile(panel);
        }

    }
}

class Listen implements ActionListener {

    private String buttonID;
    private String boxID;
    private JPanel affixGUI;

    public Listen(String buttonID, String boxID, JPanel panel) {
        this.buttonID = buttonID;
        this.affixGUI = panel;
        this.boxID = boxID;
    }

    public void actionPerformed(ActionEvent e) {
        // TODO add your handling code here:
        boolean tue = false;

        for (Component comp : affixGUI.getComponents()) {
            if (comp instanceof JPanel) {
                JPanel pan = (JPanel) comp;
                for (Component comp2 : pan.getComponents()) {
                    if (comp2 instanceof JButton) {
                        if (buttonID.equals(comp2.getName())) {
                            tue = true;
                            //   System.out.println("Soll nur ein mal passieren " + buttonID + " " + comp2.getName());  
                        } else {
                            tue = false;
                        }
                    }
                    if (comp2 instanceof JCheckBox) {

                        JCheckBox box = (JCheckBox) comp2;
                        if (tue) {
                        //    System.out.println("So mehrmals");
                            if (box.getName().equals(boxID)) {
                                if (box.isSelected()) {
                                    box.setSelected(false);
                                } else {
                                    box.setSelected(true);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


class StichWortMousListener implements MouseListener {

    private JPanel panel;
    MorphSearchEngineExist morphSearch;
    ArrayList<HashMap<String, String>> nodes;
    String word;

    public StichWortMousListener(JPanel panel, MorphSearchEngineExist morphSearch, String word, ArrayList<HashMap<String, String>> nodes) {
        this.panel = panel;
        this.morphSearch = morphSearch;
        this.word = word;
        this.nodes = nodes;
    }

    public ArrayList getNodes(){
        return nodes;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        System.out.print("Hier it works");
        SwingWorker morphalSuche;
        morphalSuche = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                try {
                    panel.removeAll();
                    panel.repaint();
                    JPanel ueberschrift = new JPanel();
                    // Formatierung
                    ueberschrift.setLayout(new FlowLayout(FlowLayout.LEFT));
                    JLabel ueberschriftText = new JLabel(word);
                    Font font = ueberschriftText.getFont();
                    Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize() + 2);
                    ueberschriftText.setFont(boldFont);
                    // Formatierung Ende
                    JButton myAlleAuswahlButton = new JButton();
                    myAlleAuswahlButton.setName(word);
                    Dimension d = new Dimension(20, 20);
                    myAlleAuswahlButton.setPreferredSize(d);
                    myAlleAuswahlButton.addActionListener(new Listen(word, word+"L", panel));
                    
                    JButton myAlleAuswahlButtonRechts = new JButton();
                    myAlleAuswahlButtonRechts.setName(word);
                    myAlleAuswahlButtonRechts.setPreferredSize(d);
                    myAlleAuswahlButtonRechts.addActionListener(new Listen(word, word+"R", panel));
                    
                    ueberschrift.add(myAlleAuswahlButton);
                    ueberschrift.add(myAlleAuswahlButtonRechts);
                    
                    ueberschrift.add(ueberschriftText);
                    ueberschrift.setMaximumSize(new Dimension(Integer.MAX_VALUE, ueberschrift.getMinimumSize().height));
                    ueberschrift.addMouseListener(new PopClickListenerUb(panel));
                    panel.add(ueberschrift);
                    ueberschrift.revalidate();
                    ueberschrift.repaint();
                    for (int y = 0; y < nodes.size(); y++) {
                        HashMap<String, String> element = nodes.get(y);
                           
                            String satzNummer = element.get("sen");
                            String fileID = element.get("fileId");
                            String wortID = element.get("id");
                            String pos = element.get("pos");
                        morphSearch.getContext(word, satzNummer, fileID, wortID, pos, panel, y);
                        panel.repaint();
                    }

                } catch (XMLDBException ex) {
                    Logger.getLogger(StichWortMousListener.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }

            protected void done() {
                try {
                    System.out.println("Done");
                    get();
                } catch (ExecutionException e) {
                    e.getCause().printStackTrace();
                    String msg = String.format("Unexpected problem: %s",
                            e.getCause().toString());
                    JOptionPane.showMessageDialog(panel, msg, "Error", JOptionPane.ERROR_MESSAGE);
                } catch (InterruptedException e) {
                    // Process e here
                }
                //   jButtonTaggen.setEnabled(true);
            }
        };
        if (morphalSuche.getState() == StateValue.STARTED) {
            morphalSuche.cancel(true);
        } else {
            morphalSuche.execute();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }
}

class PopUpUeberschrit extends JPopupMenu {

    

    public PopUpUeberschrit(JPanel panel) {
       
        JMenu popupWortart = new JMenu("Wortart überall ändern");

        JMenuItem itemN = new JMenuItem("N");
        itemN.addMouseListener(new AlleWortArtenListener(panel, "N"));
        popupWortart.add(itemN);

        JMenuItem itemV = new JMenuItem("V");
        itemV.addMouseListener(new AlleWortArtenListener(panel, "V"));
        popupWortart.add(itemV);

        JMenuItem itemADJ = new JMenuItem("ADJ");
        itemADJ.addMouseListener(new AlleWortArtenListener(panel, "ADJ"));
        popupWortart.add(itemADJ);

        JMenuItem itemD = new JMenuItem("D");
        itemD.addMouseListener(new AlleWortArtenListener(panel, "D"));
        popupWortart.add(itemD);

        JMenuItem itemP = new JMenuItem("P");
        itemP.addMouseListener(new AlleWortArtenListener(panel, "P"));
        popupWortart.add(itemP);

        JMenuItem itemNUM = new JMenuItem("NUM");
        itemNUM.addMouseListener(new AlleWortArtenListener(panel, "NUM"));
        popupWortart.add(itemNUM);

        JMenuItem itemKO = new JMenuItem("KO");
        itemKO.addMouseListener(new AlleWortArtenListener(panel, "KO"));
        popupWortart.add(itemKO);

        JMenuItem itemAPPR = new JMenuItem("APPR");
        itemAPPR.addMouseListener(new AlleWortArtenListener(panel, "APPR"));
        popupWortart.add(itemAPPR);

        JMenuItem itemAPPRAART = new JMenuItem("APPRART");
        itemAPPRAART.addMouseListener(new AlleWortArtenListener(panel, "APPRART"));
        popupWortart.add(itemAPPRAART);

        JMenuItem itemAV = new JMenuItem("AV");
        itemAV.addMouseListener(new AlleWortArtenListener(panel, "AV"));
        popupWortart.add(itemAV);

        JMenuItem itemPTK = new JMenuItem("PTK");
        itemPTK.addMouseListener(new AlleWortArtenListener(panel, "PTK"));
        popupWortart.add(itemPTK);

        JMenuItem item$ = new JMenuItem("$");
        item$.addMouseListener(new AlleWortArtenListener(panel, "$"));
        popupWortart.add(item$);

        add(popupWortart);

    }
}

class AlleWortArtenListener implements MouseListener {

    JPanel panel;
    String pos;
    public AlleWortArtenListener(JPanel panel, String pos){
        this.panel = panel;
        this.pos = pos;
    }
    @Override
    public void mouseClicked(MouseEvent e) {
       // throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (Component comp : panel.getComponents()) {
            if (comp instanceof zeileGUI) {
                zeileGUI zeile = (zeileGUI) comp;
                zeile.setPartofspecch("["+pos+"]");
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
    
}


class PopClickListenerUb extends MouseAdapter {
    JPanel panel;
    public PopClickListenerUb(JPanel panel) {
        this.panel = panel;
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            doPop(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            doPop(e);
        }
    }

    private void doPop(MouseEvent e) {
        PopUpUeberschrit menu = new PopUpUeberschrit(panel);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}




