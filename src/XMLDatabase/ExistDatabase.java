/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLDatabase;

import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import javax.swing.JOptionPane;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.exist.xmldb.EXistResource;
import org.exist.xmldb.XQueryService;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.CompiledExpression;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XMLResource;

/**
 *
 * @author sirajzad
 */
public class ExistDatabase {

    private String URI;
    final String driver = "org.exist.xmldb.DatabaseImpl";
    Collection col = null;
    XMLResource resource = null;
    ResourceSet resourceSet = null;

    public ExistDatabase(String URI) throws ClassNotFoundException {
        // Prepare the database
        try {
            this.URI = URI;
            Class cl = Class.forName(driver);
            Database database = (Database) cl.newInstance();
            database.setProperty("create-database", "true");
            DatabaseManager.registerDatabase(database);
        } catch (XMLDBException ex) {
            JOptionPane.showMessageDialog(null, "Luna konnte keine Verbindung zur Datenbank herstellen. "
                    + "Bitte überprüfen Sie Ihre Internetverbindung und verwenden Sie "
                    + "die Datenbankoption nur innerhalb des Uni-Netzwerks!"
                    + "\n"
                    + ex.getMessage());
        } catch (IllegalAccessException illEx){
            System.out.println(illEx.getMessage());
        } catch (InstantiationException inEx){
             System.out.println(inEx.getMessage());
        }
    }

    public Collection getCollection(){
        return col;
    }
    public void setCollection(String name) {
        try {
        col = DatabaseManager.getCollection(URI + name, "admin", "Wortbildung");
        if (col == null) {
            JOptionPane.showMessageDialog(null, "Luna konnte keine Verbindung zur Datenbank herstellen. \n"
                    + "Bitte überprüfen Sie Ihre Internetverbindung und verwenden Sie \n"
                    + "die Datenbankoption nur innerhalb des Uni-Netzwerks! \n"
                    + "Collection was not found!"         
                    + "\n");
           
        } else {
            col.setProperty(OutputKeys.INDENT, "no");
        }
        } catch (XMLDBException ex){
               System.err.println("Hier");
        }
    }

    private String nodeToString(Node node) throws TransformerConfigurationException, TransformerException {

        StringWriter writer = new StringWriter();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");

        transformer.transform(new DOMSource(node), new StreamResult(writer));

        String xml = writer.toString();
        return xml;
    }

    public String getXmlFile(String docID) throws XMLDBException, TransformerConfigurationException, TransformerException {
        String datei = null;
        try {
            XMLResource resourceLocal = (XMLResource) col.getResource(docID);
            datei = nodeToString(resourceLocal.getContentAsDOM());
        } catch (XMLDBException ex) {
            JOptionPane.showMessageDialog(null, "Luna konnte keine Verbindung zur Datenbank herstellen. "
                    + "Bitte überprüfen Sie Ihre Internetverbindung und verwenden Sie "
                    + "die Datenbankoption nur innerhalb des Uni-Netzwerks!"
                    + "\n"
                    + ex.getMessage());
        }
        return datei;
    }

    public ResourceSet executeXQuery(String collection, String xQuery) {
            setCollection(collection);    
        try {
            XQueryService service = (XQueryService) col.getService("XQueryService", "1.0");
            // set pretty-printing on
            service.setProperty(OutputKeys.INDENT, "yes");
            service.setProperty(OutputKeys.ENCODING, "UTF-8");

            CompiledExpression compiled = service.compile(xQuery);
            // execute query and get results in ResourceSet
            resourceSet = service.execute(compiled);
            
        } catch (XMLDBException ex) {
        /*    JOptionPane.showMessageDialog(null, "Entweder konnte Luna keine Verbindung zur Datenbank herstellen, oder "
                    + "es gibt einen Syntaxfehler in XQuerry! Die Fehlermeldung kommt von der Methode executeXquerry\n"
                    + ex.getMessage()); */
        }
        return resourceSet;
    }
    
    public ResourceSet executeXQueryBySettingCollectionSeperately(String collection, CompiledExpression compiled, XQueryService service) {    
        try {
          
            // execute query and get results in ResourceSet
            resourceSet = service.execute(compiled);
        } catch (XMLDBException ex) {
            JOptionPane.showMessageDialog(null, "Entweder konnte Luna keine Verbindung zur Datenbank herstellen, oder "
                    + "es gibt einen Syntaxfehler in XQuerry! Die Fehlermeldung kommt von der Methode executeXquerry\n"
                    + ex.getMessage());
        }
        return resourceSet;

    }

    public String[] getResourses(String collection) throws XMLDBException {
        setCollection(collection);
        String[] result = col.listResources();
        return result;
    }

    public void putData(String collection, String name, String data) throws XMLDBException, URISyntaxException {
        setCollection(collection);
        URI uri = new URI(name);
        XMLResource document = (XMLResource) col.createResource(uri.toString(), "XMLResource");
        document.setContent(data);
        col.storeResource(document);
        col.close();
    }

    public void cleanup() throws XMLDBException {

        if (col != null) {
            try {
                col.close();
            } catch (XMLDBException xe) {
                xe.printStackTrace();
            }
        }
    }

    // do not use it, it is not ready yet
    public void cleanupResources() throws XMLDBException {
        ResourceIterator i = resourceSet.getIterator();
        EXistResource res = null;
        while (i.hasMoreResources()) {
            //dont forget to cleanup resources
            try {
                if (res != null) {
                    res = (EXistResource) i.nextResource();
                    //  res.freeResources();
                }
            } catch (XMLDBException xe) {
            }

        }

    }

    public static void main(String args[]) throws ClassNotFoundException, InstantiationException, IllegalAccessException, XMLDBException {
        String URI = "xmldb:exist://cw-mullercl-1.uni.lux:8080/exist/xmlrpc";
        String engelmann = "xmldb:exist://engelmann.uni.lu:8899/exist/xmlrpc";
        ExistDatabase database = new ExistDatabase(engelmann);

        String xquerry = " xquery version \"3.0\"; "
                + "  for $w in //title "
                + "return $w";

        ResourceSet result = database.executeXQuery("/db/korpus/", xquerry);
        System.out.println(result.getSize() + " Elemente wurden gefunden!");

        XMLResource res = (XMLResource) result.getResource(0);
        Node n = res.getContentAsDOM();
        Node child = n.getFirstChild();

        System.out.println(n.getNodeName() + " " + n.hasChildNodes() + " " + child.getNodeName() + " " + child.getTextContent());


        for (int i = 0; i < (int) result.getSize(); i++) {
            XMLResource resource = (XMLResource) result.getResource((long) i);
            Node node = resource.getContentAsDOM();
            Element childNode = (Element) node.getFirstChild();
            childNode.setAttribute("wow", "mow");
            System.out.println(node.getNodeName() + " " + node.hasChildNodes() + " " + childNode.getNodeName() + " " + childNode.getTextContent());
        }


    }
}
