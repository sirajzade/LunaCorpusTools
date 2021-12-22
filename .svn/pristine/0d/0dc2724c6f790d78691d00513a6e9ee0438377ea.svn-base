/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lunacorpustools;

import XMLDatabase.ExistDatabase;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComboBox;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

/**
 *
 * @author joshgun.sirajzade
 */
public class DatabaseGUI {

    public static void getSuffixSort(ExistDatabase database, JComboBox box) {
        try {
            box.addItem("alle");
            String xQuerry = " xquery version \"3.0\"; "
                    + " declare namespace functx = \"http://www.functx.com\"; "
                    + " declare function functx:value-union "
                    + " ( $arg1 as xs:anyAtomicType* , "
                    + " $arg2 as xs:anyAtomicType* )  as xs:anyAtomicType* { "
                    + " distinct-values(($arg1, $arg2)) }; "
                    + " for $w in functx:value-union(collection(\"/db/Korpus/fertigGetagged\")//@suffix, ()) "
                    + " return <b>{$w}</b> ";

            String pfad = "db/Korpus/fertigGetagged";
            ResourceSet result = database.executeXQuery(pfad, xQuerry);

            for (int i = 0; i < (int) result.getSize(); i++) {
                XMLResource resource = (XMLResource) result.getResource((long) i);
                Node node2 = (Node) resource.getContentAsDOM();
                Element childNode2 = (Element) node2.getFirstChild();
                String text = childNode2.getTextContent();
                box.addItem(text);
            }

        } catch (XMLDBException ex) {
            Logger.getLogger(DatabaseGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void getUserSort(ExistDatabase database, JComboBox box) {
        try {
            box.addItem("alle");
            String xQuerry = " xquery version \"3.0\"; "
                    + " declare namespace functx = \"http://www.functx.com\"; "
                    + " declare function functx:value-union "
                    + " ( $arg1 as xs:anyAtomicType* , "
                    + " $arg2 as xs:anyAtomicType* )  as xs:anyAtomicType* { "
                    + " distinct-values(($arg1, $arg2)) }; "
                    + " for $w in functx:value-union(collection(\"/db/Korpus/fertigGetagged\")//@user, ()) "
                    + " return <b>{$w}</b> ";

            String pfad = "db/Korpus/fertigGetagged";
            ResourceSet result = database.executeXQuery(pfad, xQuerry);

            for (int i = 0; i < (int) result.getSize(); i++) {
                XMLResource resource = (XMLResource) result.getResource((long) i);
                Node node2 = (Node) resource.getContentAsDOM();
                Element childNode2 = (Element) node2.getFirstChild();
                String text = childNode2.getTextContent();
                box.addItem(text);
            }

        } catch (XMLDBException ex) {
            Logger.getLogger(DatabaseGUI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void getPOSSort(ExistDatabase database, JComboBox box) {
        try {
            box.addItem("Alle");
            String xQuerry = " xquery version \"3.0\"; "
                    + " declare namespace functx = \"http://www.functx.com\"; "
                    + " declare function functx:value-union "
                    + " ( $arg1 as xs:anyAtomicType* , "
                    + " $arg2 as xs:anyAtomicType* )  as xs:anyAtomicType* { "
                    + " distinct-values(($arg1, $arg2)) }; "
                    + " for $w in functx:value-union(collection(\"/db/Korpus/fertigGetagged\")//w/@pos, ()) "
                    + " return <b>{$w}</b> ";

            String pfad = "db/Korpus/fertigGetagged";
            ResourceSet result = database.executeXQuery(pfad, xQuerry);

            for (int i = 0; i < (int) result.getSize(); i++) {
                XMLResource resource = (XMLResource) result.getResource((long) i);
                Node node2 = (Node) resource.getContentAsDOM();
                Element childNode2 = (Element) node2.getFirstChild();
                String text = childNode2.getTextContent();
                box.addItem(text);
            }

        } catch (XMLDBException ex) {
            Logger.getLogger(DatabaseGUI.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
