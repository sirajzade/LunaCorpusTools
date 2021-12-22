/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author joshgun.sirajzade
 */
public class SentXmlArffSerializer {

    public SentXmlArffSerializer() {
    }

    public static final String UTF8_BOM = "\uFEFF";

    private static String removeUTF8BOM(String s) {
        if (s.startsWith(UTF8_BOM)) {
            s = s.substring(1);
            System.out.println("Bom mom");
        }
        return s;
    }

    private String buildTheStringKopf() {
        String kopf = "@relation Sentiment\n"
                + "\n"
                + "@attribute sentences String\n"
                + "@attribute sentiment {positive, negative, neutral}\n"
                + "\n"
                + "@data"
                + "\n";
        return kopf;
    }

    private ArrayList buildFreqList(Document doc, String xpathExpr) {
        ArrayList<String> freqList = new <String>ArrayList();
        try {
            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xpath = xpathfactory.newXPath();

            XPathExpression expr = xpath.compile(xpathExpr);
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList wordsBuffer = (NodeList) result;

            for (int i = 0; i < wordsBuffer.getLength(); i++) {
                Element el = (Element) wordsBuffer.item(i);

                String word = el.getTextContent();
                // ckeck if the word already there
                if (!freqList.contains(word)) {
                    freqList.add(word);
                }

            }

        } catch (XPathExpressionException ex) {
            Logger.getLogger(SentXmlArffSerializer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return freqList;
    }

    private String buildStringFromList(ArrayList list) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String word = (String) it.next();
            if (!word.equals("\"") && !word.equals("\".")) {
                sb.append("\"" + word + "\", ");
            }
        }
        sb.replace(sb.length() - 2, sb.length(), "");
        //sb.append("'beginText'");
        sb.append("}");
        return sb.toString();
    }

    private String buildThePosKopf(int times, ArrayList list) {

        StringBuilder sb = new StringBuilder();

        sb.append("@relation Sentiment\n");
        String nominal = buildStringFromList(list);
        for (int i = 0; i < times; i++) {
            sb.append("@attribute word" + i + " " + nominal + "\n\n");
            sb.append("@attribute pos" + i + " {$ INTER AUX MV D KO PTK NUM ADJ ADK APPAR N APPR P ADV PRT APPRART AV APPART V ?}\n\n");
        }

        sb.append("@attribute sentiment {positive, negative, neutral}\n");
        sb.append("@data" + "\n");
        return sb.toString();
    }

    private String buildTheADJKopf(ArrayList list) {

        StringBuilder sb = new StringBuilder();

        sb.append("@relation SentimentADJbased\n");
        String nominal = buildStringFromList(list);

        sb.append("@attribute adjective " + nominal + "\n\n");
        sb.append("@attribute ending {esch, lech, bar, eg, los, ent, iv, el, al, ?}\n");
        sb.append("@attribute endingNoun {heet, keet, ioun, echt, ung, age, ?}\n");
        sb.append("@attribute negation {net, keen, ?}\n");
        sb.append("@attribute sentiment {positive, negative, neutral}\n");
        sb.append("@attribute sentimentSentence {positive, negative, neutral}\n");
        sb.append("@data" + "\n");
        return sb.toString();
    }

    public String seralizeByString(ArrayList<String> XmlFiles) {

        StringBuilder sb = new StringBuilder();

        sb.append(buildTheStringKopf());

        for (int i = 0; i < XmlFiles.size(); i++) {
            try {
                String filePath = XmlFiles.get(i);
                System.out.println("Prosessing " + filePath);
                File fXmlFile = new File(filePath);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();

                NodeList nList = doc.getElementsByTagName("sentence");

                goToWords(nList, sb);
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                Logger.getLogger(SentXmlArffSerializer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sb.toString();
    }

    private void addFreqlistElement(ArrayList container, ArrayList dummy) {
        for (int i = 0; i < dummy.size(); i++) {
            String word = (String) dummy.get(i);
            if (!container.contains(word)) {
                container.add(word);
            }
        }
    }

    public String seralizeByPOS(ArrayList<String> XmlFiles) throws ParserConfigurationException {
        ArrayList words = new ArrayList();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < XmlFiles.size(); i++) {
            try {
                String filePath = XmlFiles.get(i);
                System.out.println("Prosessing " + filePath);
                File fXmlFile = new File(filePath);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                ArrayList dummy = buildFreqList(doc, "//sentence/w | //sentence//c");
                addFreqlistElement(words, dummy);
            } catch (SAXException ex) {
                Logger.getLogger(SentXmlArffSerializer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SentXmlArffSerializer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        sb.append(buildThePosKopf(40, words));

        for (int i = 0; i < XmlFiles.size(); i++) {
            try {
                String filePath = XmlFiles.get(i);

                NodeList nList = getNodeListOfSentences(filePath);
                goToPos(nList, sb);
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                Logger.getLogger(SentXmlArffSerializer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sb.toString();
    }

    public String seralizeByADJ(ArrayList<String> XmlFiles) throws ParserConfigurationException {
        ArrayList words = new ArrayList();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < XmlFiles.size(); i++) {
            try {
                String filePath = XmlFiles.get(i);
                System.out.println("Prosessing " + filePath);
                File fXmlFile = new File(filePath);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();
                ArrayList dummy = buildFreqList(doc, "//w[@value]");
                addFreqlistElement(words, dummy);
            } catch (SAXException ex) {
                Logger.getLogger(SentXmlArffSerializer.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(SentXmlArffSerializer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        sb.append(buildTheADJKopf(words));

        for (int i = 0; i < XmlFiles.size(); i++) {
            try {
                String filePath = XmlFiles.get(i);

                getADJs(filePath, sb);
            } catch (SAXException | IOException | XPathExpressionException ex) {
                Logger.getLogger(SentXmlArffSerializer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return sb.toString();
    }

    public String getNegation(Element sentence) {
        String retval = "?";
        NodeList ws = sentence.getElementsByTagName("w");
        for (int i = 0; i < ws.getLength(); i++) {
            Element w = (Element) ws.item(i);
            String word = w.getTextContent();
            if (word.equalsIgnoreCase("net")) {
                retval = "net";
            }
            if (word.equalsIgnoreCase("kee")) {
                retval = "keen";
            }
            if (word.equalsIgnoreCase("keen")) {
                retval = "keen";
            }

        }
        return retval;
    }

    public Element findNounBrother(Element parent) {
        Element brother = null;
        NodeList kinder = parent.getElementsByTagName("w");
        for (int i = 0; i < kinder.getLength(); i++) {
            Element get = (Element) kinder.item(i);
            String pos = get.getAttribute("pos");
            if (pos.equals("N")) {
                brother = get;
            }
        }
        return brother;
    }

    public void getADJs(String filePath, StringBuilder sb) throws SAXException, IOException, XPathExpressionException {
        try {
            System.out.println("Prosessing for ADJs for Token level" + filePath);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(filePath);
            doc.getDocumentElement().normalize();

            XPathFactory xpathfactory = XPathFactory.newInstance();
            XPath xpath = xpathfactory.newXPath();

            XPathExpression expr = xpath.compile("//w[@value]");
            Object result = expr.evaluate(doc, XPathConstants.NODESET);
            NodeList wordsBuffer = (NodeList) result;
            for (int i = 0; i < wordsBuffer.getLength(); i++) {
                Element get = (Element) wordsBuffer.item(i);
                Element parent = (Element) get.getParentNode();
                Element nounBrother = findNounBrother(parent);
                String noun = null;

                if (nounBrother != null) {
                    noun = nounBrother.getTextContent();
                }

                String adjective = get.getTextContent();
                String sentiment = get.getAttribute("value");
                String sentenceSentiment = parent.getAttribute("value");
                String negation = getNegation(parent);
                if (adjective != "" && sentiment != "" && sentenceSentiment != "") {
                    String ending = "?";
                    String endingNoun = "?";
                    if (adjective.endsWith("lech")) {
                        ending = "lech";
                    } else if (adjective.endsWith("esch")) {
                        ending = "esch";
                    } else if (adjective.endsWith("eg")) {
                        ending = "eg";
                    } else if (adjective.endsWith("lech")) {
                        ending = "lech";
                    } else if (adjective.endsWith("los")) {
                        ending = "los";
                    } else if (adjective.endsWith("iv")) {
                        ending = "iv";
                    } else if (adjective.endsWith("ent")) {
                        ending = "ent";
                    } else if (adjective.endsWith("bar")) {
                        ending = "bar";
                    } else if (adjective.endsWith("el")) {
                        ending = "el";
                    } else if (adjective.endsWith("al")) {
                        ending = "al";
                    }
                    // Ab hier kommen die Endungen für Substantive

                    if (noun != null) {

                        if (noun.endsWith("heet")) {
                            endingNoun = "heet";
                        } else if (noun.endsWith("keet")) {
                            endingNoun = "keet";
                        } else if (noun.endsWith("ung")) {
                            endingNoun = "ung";
                        } else if (noun.endsWith("ioun")) {
                            endingNoun = "ioun";
                        } else if (noun.endsWith("age")) {
                            endingNoun = "age";
                        } else if (noun.endsWith("nes")) {
                            endingNoun = "nes";
                        }
                    }

                    sb.append("\"" + adjective + "\", " + ending + ", " + endingNoun + ", "  + negation + ", " + sentiment + ", " + sentenceSentiment + "\n");
                }
            }

        } catch (ParserConfigurationException ex) {
            Logger.getLogger(SentXmlArffSerializer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public NodeList getNodeListOfSentences(String filePath) throws ParserConfigurationException, SAXException, IOException {

        System.out.println("Prosessing " + filePath);
        File fXmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("sentence");

        return nList;
    }

    private void goToWords(NodeList nList, StringBuilder sb) {
        for (int temp = 0; temp < nList.getLength(); temp++) {

            Element sentence = (Element) nList.item(temp);
            // System.out.println("\nCurrent Element :" + sentence.getNodeName() + " " +sentence.getTextContent().trim());
            NodeList wList = sentence.getChildNodes();
            sb.append("\"");
            for (int j = 0; j < wList.getLength(); j++) {
                Node child = wList.item(j);
                // erstmal lb elemente ausschließen, aber das muss noch verbessert werden.
                if (child.getNodeType() == Node.ELEMENT_NODE && !child.getNodeName().equals("lb")) {
                    Element word = (Element) wList.item(j);
                    String text = word.getTextContent();
                    if (!text.equals("\"")) {
                        sb.append(word.getTextContent() + " " + word.getAttribute("pos") + " ");
                    }
                }
            }
            sb.append("\", " + sentence.getAttribute("value") + "\n");
        }
    }

    private int getTheLongestSentence(ArrayList<String> XmlFiles) throws XPathExpressionException {
        int retval = 0;

        for (int i = 0; i < XmlFiles.size(); i++) {
            try {
                String filePath = XmlFiles.get(i);
                System.out.println("Prosessing " + filePath);
                File fXmlFile = new File(filePath);
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(fXmlFile);
                doc.getDocumentElement().normalize();

                XPathFactory xpathFactory = XPathFactory.newInstance();
                XPath xpath = xpathFactory.newXPath();

                //XPath count() example
                XPathExpression expr = xpath.compile("count(//cricketers/cricketer)");
                Number result = (Number) expr.evaluate(doc, XPathConstants.NUMBER);
            } catch (ParserConfigurationException | SAXException | IOException ex) {
                Logger.getLogger(SentXmlArffSerializer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return retval;
    }

    private void goToPos(NodeList nList, StringBuilder sb) {
        for (int temp = 0; temp < nList.getLength(); temp++) {

            Element sentence = (Element) nList.item(temp);
            // System.out.println("\nCurrent Element :" + sentence.getNodeName() + " " +sentence.getTextContent().trim());
            NodeList wList = sentence.getChildNodes();
            String label = sentence.getAttribute("value");
            int zahler = 0;
            if (!label.equals("")) {
                for (int j = 0; j < wList.getLength(); j++) {
                    if (zahler < 40) {
                        System.out.println(j);
                        Node child = wList.item(j);
                        // erstmal lb elemente ausschließen, aber das muss noch verbessert werden.
                        if (child.getNodeType() == Node.ELEMENT_NODE && !child.getNodeName().equals("lb")) {
                            Element word = (Element) wList.item(j);
                            String pos = word.getAttribute("pos");
                            String wordText = word.getTextContent();
                            if (pos != "" && wordText != "" && !wordText.equals("\"") && !wordText.equals("\".")) {
                                zahler++;
                                sb.append("\"" + wordText + "\", " + pos + ", ");
                                System.out.println("word and pos added " + j);
                            }
                        }
                    }
                }
                int rest = 40 - zahler;
                for (int i = 0; i < rest; i++) {
                    sb.append("?, ?, ");
                }

            }
            sb.append(" " + label + "\n");
        }

    }

    public ArrayList<String> getAllXmlFileNamesFromFolder(String FolderPath) {
        final File folder = new File(FolderPath);
        ArrayList<String> fileNames = new ArrayList<>();

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {

            } else {
                String fileName = fileEntry.getAbsolutePath();
                if (fileName.endsWith(".xml")) {
                    fileNames.add(fileName);
                    // System.out.println(fileName);
                }

            }
        }
        return fileNames;
    }

    private void writeStringByBiteToFile(String path, String content) throws IOException {
        Files.write(Paths.get(path), content.getBytes());
    }

    public static void main(String[] args) throws IOException, ParserConfigurationException {

        String forlderPath = "/Users/joshgun.sirajzade/Dropbox/RTL_comments_adjectives";
        SentXmlArffSerializer senti = new SentXmlArffSerializer();
        ArrayList<String> files = senti.getAllXmlFileNamesFromFolder(forlderPath);
        // String file = senti.seralizeByPOS(files);
        String outputFilePath = "/Users/joshgun.sirajzade/Documents/LehreUniLux/2018_Summer/SurpervisingSoumyaRamakrishna/Luxembourgish resources for sentiment analysis/LunaData/TrainingSentDanielaADJ_NOUN_Ending.arff";
        String file = senti.seralizeByADJ(files);
        senti.writeStringByBiteToFile(outputFilePath, file);
    }

}
