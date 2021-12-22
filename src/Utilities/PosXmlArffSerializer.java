/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;
import org.jdom2.input.SAXBuilder;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

/**
 *
 * @author joshgun.sirajzade
 */
public class PosXmlArffSerializer {

    public PosXmlArffSerializer() throws JDOMException, IOException {

    }

    private String buildStringFromList(ArrayList list) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        Iterator it = list.iterator();
        while (it.hasNext()) {
            sb.append("'" + it.next() + "', ");
        }
        //sb.replace(sb.length() -2, sb.length(), "");        
        sb.append("'beginText'");
        sb.append("}");
        return sb.toString();
    }
    
    private String buildEndingStringFromList(ArrayList list) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        Iterator it = list.iterator();
        while (it.hasNext()) {
            String ending = (String) it.next();
            sb.append("'" + ending + "', ");
            
        }
        //sb.replace(sb.length() -2, sb.length(), "");        
        sb.append("'?'");
        sb.append("}");
        return sb.toString();
    }

    private String buildTheNominalKopfForBiGrammWithWords(String XmlFile) throws JDOMException, IOException {

        Document doc = new SAXBuilder().build(new StringReader(XmlFile));
        XPathExpression xpath = XPathFactory.instance().compile("//w | //c");
        List wordsBuffer = xpath.evaluate(doc);
        ArrayList<String> freqList = new <String>ArrayList();

        for (int i = 0; i < wordsBuffer.size(); i++) {
            Element el = (Element) wordsBuffer.get(i);

            String word = el.getText();
            // ckeck if the word already there
            if (!freqList.contains(word)) {
                freqList.add(word);
            }

        }
        String nominal = buildStringFromList(freqList);

        String kopf = "@relation POSdata\n"
                + "\n"
                + "@attribute pos {$, D, KO, PTK, NUM, ADJ, ADK, APPAR, N, APPR, P, ADV, PRT, APPRART, AV, APPART, V}\n"
                + "@attribute word " + nominal + " \n"
                + "@attribute wordBefore " + nominal + "\n"
                + "@attribute posBefore {$, D, KO, PTK, NUM, ADJ, ADK, APPAR, N, APPR, P, ADV, PRT, APPRART, AV, APPART, V}\n"
                + "\n"
                + "@data"
                + "\n";
        return kopf;

    }
    
    
    private ArrayList<String> buildArrayListForEndings (List wordsBuffer){
        ArrayList<String> freqList = new <String>ArrayList();

        for (int i = 0; i < wordsBuffer.size(); i++) {
            Element el = (Element) wordsBuffer.get(i);

            String word = el.getText();
            if (word.length()>4){
            String ending = word.substring(word.length()-3, word.length());
            // ckeck if the word already there
            if (!freqList.contains(ending)) {
                freqList.add(ending);
            }

            }
        }
        return freqList;
    }
    
    private String buildTheNominalKopfForThreeGrammWithWords(String XmlFile) throws JDOMException, IOException {

        Document doc = new SAXBuilder().build(new StringReader(XmlFile));
        XPathExpression xpath = XPathFactory.instance().compile("//w | //c");
        List wordsBuffer = xpath.evaluate(doc);
        ArrayList<String> freqList = new <String>ArrayList();

        for (int i = 0; i < wordsBuffer.size(); i++) {
            Element el = (Element) wordsBuffer.get(i);

            String word = el.getText();
            // ckeck if the word already there
            if (!freqList.contains(word)) {
                freqList.add(word);
            }

        }
        ArrayList freqListEnding = buildArrayListForEndings(wordsBuffer);
        String nominal = buildStringFromList(freqList);
        String endings = buildEndingStringFromList(freqListEnding);

        String kopf = "@relation POSdata\n"
                + "\n"
                + "@attribute wordBeforeBefore " + nominal + " \n"
                + "@attribute posBeforeBefore {$, D, KO, PTK, NUM, ADJ, ADK, APPAR, N, APPR, P, ADV, PRT, APPRART, AV, APPART, V}\n"
                + "@attribute wordBefore " + nominal + "\n"
                + "@attribute posBefore {$, D, KO, PTK, NUM, ADJ, ADK, APPAR, N, APPR, P, ADV, PRT, APPRART, AV, APPART, V}\n"
                + "@attribute ending " + endings + "\n"
                + "@attribute upperCase {yes, no}\n"
                + "@attribute word " + nominal + "\n"
                + "@attribute pos {$, D, KO, PTK, NUM, ADJ, ADK, APPAR, N, APPR, P, ADV, PRT, APPRART, AV, APPART, V}\n"
                + "\n"
                + "@data"
                + "\n";
        return kopf;

    }

    private String buildTheStringKopf() {
        String kopf = "@relation POSdata\n"
                + "\n"
                + "@attribute pos {$, D, KO, PTK, NUM, ADJ, ADK, APPAR, N, APPR, P, ADV, PRT, APPRART, AV, APPART, V}\n"
                + "@attribute word String\n"
                + "@attribute wordBefore String\n"
                + "@attribute posBefore String\n"
                + "\n"
                + "@data"
                + "\n";
        return kopf;
    }
    
    private String buildTheStringKopfForQuatro() {
        String kopf = "@relation POSdata\n"
                + "\n"
                + "@attribute pos3 {$, D, KO, PTK, NUM, ADJ, ADK, APPAR, N, APPR, P, ADV, PRT, APPRART, AV, APPART, V}\n"
                + "@attribute pos2 {$, D, KO, PTK, NUM, ADJ, ADK, APPAR, N, APPR, P, ADV, PRT, APPRART, AV, APPART, V}\n"
                + "@attribute pos1 {$, D, KO, PTK, NUM, ADJ, ADK, APPAR, N, APPR, P, ADV, PRT, APPRART, AV, APPART, V}\n"
                + "@attribute pos {$, D, KO, PTK, NUM, ADJ, ADK, APPAR, N, APPR, P, ADV, PRT, APPRART, AV, APPART, V}\n"
                + "\n"
                + "@data"
                + "\n";
        return kopf;
    }
    
     private String buildTheStringKopfForTri() {
        String kopf = "@relation POSdata\n"
                + "\n"
                + "@attribute pos2 {$, D, KO, PTK, NUM, ADJ, ADK, APPAR, N, APPR, P, ADV, PRT, APPRART, AV, APPART, V}\n"
                + "@attribute pos1 {$, D, KO, PTK, NUM, ADJ, ADK, APPAR, N, APPR, P, ADV, PRT, APPRART, AV, APPART, V}\n"
                + "@attribute pos {$, D, KO, PTK, NUM, ADJ, ADK, APPAR, N, APPR, P, ADV, PRT, APPRART, AV, APPART, V}\n"
                + "\n"
                + "@data"
                + "\n";
        return kopf;
    }

    
     
    private String serializeForTreeGrammWithWords(String XmlFile) throws JDOMException, IOException {
        Document doc = new SAXBuilder().build(new StringReader(XmlFile));
        XPathExpression xpath = XPathFactory.instance().compile("//w | //c");
        List wordsBuffer = xpath.evaluate(doc);

        StringBuilder sb = new StringBuilder();

        sb.append(buildTheNominalKopfForThreeGrammWithWords(XmlFile));

        for (int i = 0; i < wordsBuffer.size(); i++) {
            Element el = (Element) wordsBuffer.get(i);

            String word = el.getText();
            String pos = el.getAttributeValue("pos");
            // select el before capone
            String wordBefore;
            String posBefore;
            String wordBeforeBefore;
            String posBeforeBefore;
            if (i == 0) {
                wordBefore = "beginText";
                posBefore = "$";
                wordBeforeBefore = "beginText";
                posBeforeBefore = "$";
            } 
            
            else if (i == 1) {
                wordBeforeBefore = "beginText";
                posBeforeBefore = "$";
                
                Element elBefore = (Element) wordsBuffer.get(i - 1);
                wordBefore = elBefore.getText();
                posBefore = elBefore.getAttributeValue("pos");

            } else {

                Element elBefore = (Element) wordsBuffer.get(i - 1);
                wordBefore = elBefore.getText();
                posBefore = elBefore.getAttributeValue("pos");

                Element elBeforeBefore = (Element) wordsBuffer.get(i - 2);
                wordBeforeBefore = elBeforeBefore.getText();
                posBeforeBefore = elBeforeBefore.getAttributeValue("pos");
            }

            if (pos != "" && posBefore != "" && posBeforeBefore != "" && word != "") {
                //sb.append(pos + ", '" + word + "', '" + wordBefore + "', " + posBefore + ", '" + wordBeforeBefore + "', " + posBeforeBefore + "\n");
               // System.out.println(word);
                String ending = "?";
                String upperCase = "no";
                        if (word.length()>4){
                         ending = word.substring(word.length()-3, word.length());
                        }
                        char c = word.charAt(0); 
                        if (Character.isUpperCase(c)){
                            upperCase = "yes";
                        }
                sb.append("'"+wordBeforeBefore + "', " + posBeforeBefore + ", '" + wordBefore + "', " + posBefore + ", '"+ending+"' " + upperCase+ " '" + word + "', " + pos + "\n");
            }
        }
    
    return sb.toString ();
}

    
    private String serializeForBiGrammWithWords(String XmlFile) throws JDOMException, IOException {
        Document doc = new SAXBuilder().build(new StringReader(XmlFile));
        XPathExpression xpath = XPathFactory.instance().compile("//w | //c");
        List wordsBuffer = xpath.evaluate(doc);

        StringBuilder sb = new StringBuilder();

        sb.append(buildTheNominalKopfForBiGrammWithWords(XmlFile));

        for (int i = 0; i < wordsBuffer.size(); i++) {
            Element el = (Element) wordsBuffer.get(i);

            String word = el.getText();
            String pos = el.getAttributeValue("pos");
            // select el before capone
            String wordBefore;
            String posBefore;
            
            if (i == 0) {
                wordBefore = "beginText";
                posBefore = "$";
            } 
            
            else {

                Element elBefore = (Element) wordsBuffer.get(i - 1);
                wordBefore = elBefore.getText();
                posBefore = elBefore.getAttributeValue("pos");
               
            }

            if (pos != "" && posBefore != "") {
                sb.append(pos + ", '" + word + "', '" + wordBefore + "', " + posBefore + "\n");
            }
        }
    
    return sb.toString ();
}
    
    
     private String serializeForQuatroGramm(String XmlFile) throws JDOMException, IOException {
        Document doc = new SAXBuilder().build(new StringReader(XmlFile));
        XPathExpression xpath = XPathFactory.instance().compile("//w | //c");
        List wordsBuffer = xpath.evaluate(doc);

        StringBuilder sb = new StringBuilder();

        sb.append(buildTheStringKopfForQuatro());

        for (int i = 0; i < wordsBuffer.size(); i++) {
            Element el = (Element) wordsBuffer.get(i);

            String pos = el.getAttributeValue("pos");
            // select el before capone
            String pos1;
            String pos2;
            String pos3;
            
            if (i == 0) {
                pos1 = "$";
                pos2 = "$";
                pos3 = "$";
            } 
            else if (i == 1){
                Element el1 = (Element) wordsBuffer.get(i-1);
                pos1 = el1.getAttributeValue("pos");
                pos2 = "$";
                pos3 = "$";
            }
            else if (i == 2){
                Element el1 = (Element) wordsBuffer.get(i-1);
                pos1 = el1.getAttributeValue("pos");
                
                Element el2 = (Element) wordsBuffer.get(i-2);
                pos2 = el2.getAttributeValue("pos");
                
                pos3 = "$";
            }
            else {
                Element el1 = (Element) wordsBuffer.get(i-1);
                pos1 = el1.getAttributeValue("pos");
                
                Element el2 = (Element) wordsBuffer.get(i-2);
                pos2 = el2.getAttributeValue("pos");
                
                Element el3 = (Element) wordsBuffer.get(i-3);
                pos3 = el3.getAttributeValue("pos");
               
            }

            if (pos != "" && pos1 != "" && pos2 != "" && pos3 != "") {
                sb.append(pos3 + ", " + pos2 + ", " + pos1 + ", " + pos + "\n");
            }
        }
    
    return sb.toString ();
}
     
       private String serializeForTriGramm(String XmlFile) throws JDOMException, IOException {
        Document doc = new SAXBuilder().build(new StringReader(XmlFile));
        XPathExpression xpath = XPathFactory.instance().compile("//w | //c");
        List wordsBuffer = xpath.evaluate(doc);

        StringBuilder sb = new StringBuilder();

        sb.append(buildTheStringKopfForTri());

        for (int i = 0; i < wordsBuffer.size(); i++) {
            Element el = (Element) wordsBuffer.get(i);

            String pos = el.getAttributeValue("pos");
            // select el before capone
            String pos1;
            String pos2;
            
            
            if (i == 0) {
                pos1 = "$";
                pos2 = "$";
                
            } 
            else if (i == 1){
                Element el1 = (Element) wordsBuffer.get(i-1);
                pos1 = el1.getAttributeValue("pos");
                pos2 = "$";
             }
            else {
                Element el1 = (Element) wordsBuffer.get(i-1);
                pos1 = el1.getAttributeValue("pos");
                
                Element el2 = (Element) wordsBuffer.get(i-2);
                pos2 = el2.getAttributeValue("pos");
                
            }

            if (pos != "" && pos1 != "" && pos2 != "") {
                sb.append(pos2 + ", " + pos1 + ", " + pos + "\n");
            }
        }
    
    return sb.toString ();
}
    
private static String readLineByLineJava8(String filePath) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();
        Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8);
        stream.forEach(s -> contentBuilder.append(s).append("\n"));
        return contentBuilder.toString();
    }

    private void writeStringByBiteToFile(String path, String content) throws IOException {
        Files.write(Paths.get(path), content.getBytes());
    }

    public static void main(String[] args) throws IOException, JDOMException {
        // TODO Auto-generated method stub

        String filePath = "/Users/joshgun.sirajzade/Documents/WBLUX/databaseDaniela/db/Utilities/TrainingData/2003-2004_2_POS_Katja.xml";
        String file = readLineByLineJava8(filePath);
        PosXmlArffSerializer poser = new PosXmlArffSerializer();
        String outPut = poser.serializeForTreeGrammWithWords(file);
        String outputFilePath = "/Users/joshgun.sirajzade/Documents/LehreUniLux/2018_Summer/SurpervisingSoumyaRamakrishna/Luxembourgish resources for sentiment analysis/LunaData/TrainingPosKatja.arff";
        poser.writeStringByBiteToFile(outputFilePath, outPut);
    }

}
