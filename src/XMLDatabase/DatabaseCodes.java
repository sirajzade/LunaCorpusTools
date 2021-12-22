/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLDatabase;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import lunacorpustools.KorpusTreeNode;
import lunacorpustools.ListItemDatabase;
import lunacorpustools.NewMDIApplication;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

/**
 *
 * @author joshgun.sirajzade
 */
public class DatabaseCodes {
    public static void openKorpusTreeModel(Benutzer benutzer, String pfad, String xQuerry, DefaultMutableTreeNode root){
                         
            try {
                ExistDatabase database = benutzer.getDatabase();
                ResourceSet result = database.executeXQuery(pfad, xQuerry);
            
                ListItemDatabase werke[] = getFilesArrayFromDatabaseWithXQuerry(result);
       //         Arrays.sort(werke);
             
                for (int i = 0; i < werke.length; i++) {
                     ListItemDatabase werk = werke[i];
                     KorpusTreeNode werkNode = new KorpusTreeNode(werk.getValue());
                     werkNode.setID(werk.getId());
                     root.add(werkNode);
                     System.out.println(werk.getValue() + " wurde geaddet! ");
                }
                System.out.println(" Korpus geladen: "+ root.getUserObject());
                database.cleanup();    
           
            } catch (XMLDBException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
                Logger.getLogger(NewMDIApplication.class.getName()).log(Level.SEVERE, null, ex);
            }

        
    }
    
    public static void saveData(Benutzer benutzer, String fileID, String wortID, String att, String value, String pos, String user) {

        ExistDatabase database = benutzer.getDatabase();

        String xQuerry = " xquery version \"3.0\"; "
                + " for $w in doc(\"" + fileID + "\")//w[ft:query(./@id, \"" + wortID + "\")]"
                + " return update insert attribute " + att + " {'" + value + "'} into $w ";

        String pfad2 = "db/Korpus/fertigGetagged";

        ResourceSet resultNeu = database.executeXQuery(pfad2, xQuerry);

        String xQuerry2 = " xquery version \"3.0\"; "
                + " for $w in doc(\"" + fileID + "\")//w[ft:query(./@id, \"" + wortID + "\")]"
                + " return update insert attribute user {'" + user + "'} into $w ";

        ResourceSet resultNeu2 = database.executeXQuery(pfad2, xQuerry2);

        String xQuerry3 = " xquery version \"3.0\"; "
                + " for $w in doc(\"" + fileID + "\")//w[ft:query(./@id, \"" + wortID + "\")]"
                + " return update insert attribute pos {'" + pos + "'} into $w ";

        ResourceSet resultNeu3 = database.executeXQuery(pfad2, xQuerry3);

    }
    
    public static void saveDataQuicker(Benutzer benutzer, String fileID, String wortID, String att, String value, String pos, String user) {

        ExistDatabase database = benutzer.getDatabase();

        String xQuerry = " xquery version \"3.0\"; "
                + " for $w in doc(\"" + fileID + "\")//w[ft:query(./@id, \"" + wortID + "\")]"
                + " let $w1 := update insert attribute " + att + " {'" + value + "'} into $w "
                + " let $w2 := update insert attribute user {'" + user + "'} into $w "
                + " let $w2 := update insert attribute pos {'" + pos + "'} into $w " 
                + " return $w";

        String pfad2 = "db/Korpus/fertigGetagged";
        ResourceSet resultNeu = database.executeXQuery(pfad2, xQuerry);
        
    }
    
    public static void saveDataNoQuicker(Benutzer benutzer, String fileID, String wortID, String att, String value, String pos, String user) {

        ExistDatabase database = benutzer.getDatabase();
        String xQuerry = " xquery version \"3.0\"; "
                + " for $w in doc(\"" + fileID + "\")//w[ft:query(./@id, \"" + wortID + "\")] "
                + " let $w1 := update insert attribute " + att + " {'null'} into $w "
                + " let $w2 := update insert attribute user {'" + user + "'} into $w "
                + " let $w3 := update insert attribute pos {'" + pos + "'} into $w "
                + " let $w4 := update insert attribute no" + att + " {'" + value + "'} into $w"
                + " return $w ";

        String pfad2 = "db/Korpus/fertigGetagged";

        ResourceSet resultNeu = database.executeXQuery(pfad2, xQuerry);

    }
    
    public static void saveAnAttribute(Benutzer benutzer, String fileID, String wortID, String att, String value){
        ExistDatabase database = benutzer.getDatabase();
        String xQuerry = " xquery version \"3.0\"; "
                + " for $w in doc(\"" + fileID + "\")//w[ft:query(./@id, \"" + wortID + "\")]"
                + " return update insert attribute " + att + " {'" + value + "'} into $w ";

        String pfad2 = "db/Korpus/fertigGetagged";
       
        ResourceSet resultNeu = database.executeXQuery(pfad2, xQuerry);
    }
    
    public static void saveDataNo(Benutzer benutzer, String fileID, String wortID, String att, String value, String pos, String user) {

        ExistDatabase database = benutzer.getDatabase();
        String xQuerry = " xquery version \"3.0\"; "
                + " for $w in doc(\"" + fileID + "\")//w[ft:query(./@id, \"" + wortID + "\")]"
                + " return update insert attribute " + att + " {'null'} into $w ";

        String pfad2 = "db/Korpus/fertigGetagged";

        ResourceSet resultNeu = database.executeXQuery(pfad2, xQuerry);

        String xQuerry2 = " xquery version \"3.0\"; "
                + " for $w in doc(\"" + fileID + "\")//w[ft:query(./@id, \"" + wortID + "\")]"
                + " return update insert attribute user {'" + user + "'} into $w ";

        ResourceSet resultNeu2 = database.executeXQuery(pfad2, xQuerry2);

        String xQuerry3 = " xquery version \"3.0\"; "
                + " for $w in doc(\"" + fileID + "\")//w[ft:query(./@id, \"" + wortID + "\")]"
                + " return update insert attribute pos {'" + pos + "'} into $w ";

        ResourceSet resultNeu3 = database.executeXQuery(pfad2, xQuerry3);

        String xQuerry4 = " xquery version \"3.0\"; "
                + " for $w in doc(\"" + fileID + "\")//w[ft:query(./@id, \"" + wortID + "\")]"
                + " return update insert attribute no" + att + " {'" + value + "'} into $w ";

        ResourceSet resultNeu4 = database.executeXQuery(pfad2, xQuerry4);
    }
    
    public static ListItemDatabase [] getFilesArrayFromDatabaseWithXQuerry(ResourceSet result) throws XMLDBException{
       ListItemDatabase retval[] = null;
            ListItemDatabase werke[] = new ListItemDatabase [(int) result.getSize()];
            for (int i = 0; i < (int) result.getSize(); i++) {
                XMLResource resource = (XMLResource) result.getResource((long) i);
                Node node = (Node) resource.getContentAsDOM();
                Element childNode = (Element) node.getFirstChild();
                // System.out.println(node.getNodeName() + " " + node.hasChildNodes() + " " + childNode.getNodeName() + " " + childNode.getTextContent());
                ListItemDatabase item = new ListItemDatabase(childNode.getTextContent(), childNode.getAttribute("id"));
                werke[i] = item;
            }
            retval = werke;
        return retval;
    }
    
    public static void getStatistics(String pfad, StringBuilder buffi, Benutzer benutzer){
         String xquerryGesamtToken = " xquery version \"3.0\"; "
                    + " let $var := count(//w | //c) "
                    + " return <b> {$var} </b> ";
            
            String xquerryDokumentenAnzahl = " xquery version \"3.0\"; "
                    + " let $var := count(//title[@type=\"short\"]) "
                    + " return <b> {$var} </b> ";

            ExistDatabase database = benutzer.getDatabase();
            
            ResourceSet resultGesamtToken = database.executeXQuery(pfad, xquerryGesamtToken);
            
            try {
                XMLResource resource = (XMLResource) resultGesamtToken.getResource((long) 0);
                Node node = (Node) resource.getContentAsDOM();
                Element childNode = (Element) node.getFirstChild();
                buffi.append(childNode.getTextContent());
                buffi.append(" Tokens in ");
            } catch (XMLDBException ex) {
                Logger.getLogger(NewMDIApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
            ResourceSet result = database.executeXQuery(pfad, xquerryDokumentenAnzahl);
            try {
                XMLResource resource2 = (XMLResource) result.getResource((long) 0);
                Node node = (Node) resource2.getContentAsDOM();
                Element childNode = (Element) node.getFirstChild();
                buffi.append(childNode.getTextContent());
                buffi.append(" Dokumenten \n");

            } catch (XMLDBException ex) {
                Logger.getLogger(NewMDIApplication.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public static void korpusLaden(Benutzer benutzer, JTree Gesamtkorpus, JTree jTreeTrainingsdata, JTree JTreeZuTagendeTexte, JTree JTreeTopics ){
       
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Gesamtkorpus");
        DefaultTreeModel model = new DefaultTreeModel(root);
        Gesamtkorpus.removeAll();
        Gesamtkorpus.setModel(model);
              
        DefaultMutableTreeNode root2 = new DefaultMutableTreeNode("Trainingsdata");
        DefaultTreeModel model2 = new DefaultTreeModel(root2);
                
        DefaultMutableTreeNode texte = new DefaultMutableTreeNode("Texte");
        root2.add(texte);
        
        DefaultMutableTreeNode lexika = new DefaultMutableTreeNode("Lexika");
        root2.add(lexika);
        
        jTreeTrainingsdata.removeAll();
        jTreeTrainingsdata.setModel(model2);
        
        DefaultMutableTreeNode rootTagendeTexte = new DefaultMutableTreeNode("Standardisierte Texte");
        DefaultTreeModel modelZuTagenndeText = new DefaultTreeModel(rootTagendeTexte);
        JTreeZuTagendeTexte.removeAll();
        JTreeZuTagendeTexte.setModel(modelZuTagenndeText);
        
          String xQuerry = " xquery version \"3.0\";"
                 + " for $w in //titleStmt"
                 + " order by $w/title[@type=\"short\"]"
                 + " return "
                 + " if ($w/author/surname!='') "
                 + " then <b id =\"{base-uri($w)}\"> { concat ( concat (string($w/author/surname), ', '),  string($w/title[@type=\"short\"]) )} </b> "
                 + " else <b id =\"{base-uri($w)}\"> { string ($w/title[@type=\"short\"]) } </b> ";
         
         String pfad = "db/Korpus/fertigGetagged";
         String pfad2 = "db/Utilities/TrainingData"; 
         String pfad3 = "db/Korpus/tokenSentenceStandard";
         
         String xQuerryLexikon = " xquery version \"3.0\";"
                 + " for $w in //lexikonTitle"
                 + " return "
                 + " <b id =\"{base-uri($w)}\"> {string($w)} </b> ";
                 
         
        DefaultMutableTreeNode rootTopicTexte = new DefaultMutableTreeNode("texts for topic modeling");
        DefaultTreeModel modelTopicText = new DefaultTreeModel(rootTopicTexte);
        JTreeTopics.removeAll();
        JTreeTopics.setModel(modelTopicText);
        
         
            if (benutzer.isLogged()) {
                  
            openKorpusTreeModel(benutzer, pfad, xQuerry, root);
            openKorpusTreeModel(benutzer, pfad2, xQuerry, texte);
            openKorpusTreeModel(benutzer, pfad2, xQuerryLexikon, lexika);
            openKorpusTreeModel(benutzer, pfad3, xQuerry, rootTagendeTexte);
            openKorpusTreeModel(benutzer, pfad, xQuerry, rootTopicTexte);

        } else {
            JOptionPane.showMessageDialog(null, "Bitte loggen Sie sich ein, um eine Datei aus der Datenbank öffnen zu können!");
        }
        
    }
    
    public static boolean lodNachGucken(String word, Benutzer benutzer) throws XMLDBException{
        boolean booly = false;
        
         String xQuerry = "xquery version \"3.0\";" +
                 " for $w in doc('/db/Projektmanagement/lodUTF8.xml')//option/@value[.=\""+ word +"\"] " + 
                 " return string($w) " ;
        
           ExistDatabase database = benutzer.getDatabase();
            
           ResourceSet resultGesamtToken = database.executeXQuery("/db/Projektmanagement", xQuerry);
         
         int size = (int) resultGesamtToken.getSize();
         if (size > 0){
             booly = true;
         }
         System.out.println(size);
        return booly;
        
    }
}
