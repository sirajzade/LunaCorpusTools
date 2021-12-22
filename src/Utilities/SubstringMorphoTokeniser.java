/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author joshgun.sirajzade
 */
public class SubstringMorphoTokeniser {

    private ArrayList readSubStrigs(String file) {
        BufferedReader reader;
        ArrayList<String> list = new ArrayList();
        try {
            reader = new BufferedReader(new FileReader(
                    file));
            String line = reader.readLine();
            while (line != null) {
                String freqS = line.substring(line.indexOf("> ") + 1, line.length());
                int freq = Integer.parseInt(freqS.trim());
                String substr = line.substring(2, line.indexOf("> "));
                if (freq > 500 && !substr.contains(" ")) {
                    // System.out.println(line + " --> " + substr + " and " + freq);
                    list.add(substr);

                }
                // read next line
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private void showTheSortetList(ArrayList<String> list) {
        Collections.sort(list, new MyComparator());
        for (int i = 0; i < list.size(); i++) {
            String get = list.get(i);
            System.out.println(get);

        }
    }
    
    private void morphosize(ArrayList<String> morphs, String filePath) throws ParserConfigurationException, SAXException, IOException{
        Collections.sort(morphs, new MyComparator());
        File fXmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile);
        doc.getDocumentElement().normalize();
        NodeList nList = doc.getElementsByTagName("w");
        for (int y = 0; y < nList.getLength(); y++) {
            Element wordE = (Element)nList.item(y);
            String word = wordE.getTextContent();
            for (int i = 0; i < morphs.size(); i++) {
                String morph = morphs.get(i);
                if (word.contains(morph)){
                    System.out.println(word + " and " + morph);
                }
            }
        }
        
    }

    public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
        SubstringMorphoTokeniser smt = new SubstringMorphoTokeniser();
        ArrayList list = smt.readSubStrigs("/Users/joshgun.sirajzade/Documents/LehreUniLux/2018_Summer/news_articlesSubstringFrequency432.txt");
        
        smt.morphosize(list, "/Users/joshgun.sirajzade/Dropbox/RTL_comments_adjectives/RTL_Kommentarer_2008-2012-55.xml");

    }

    public class MyComparator implements Comparator<String> {

        @Override
        public int compare(String o1, String o2) {
            if (o1.length() > o2.length()) {
                return -1;
            } else if (o1.length() < o2.length()) {
                return 1;
            }
            return o1.compareTo(o2);
        }
    }

}
