package lunacorpustools.POSTagger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JProgressBar;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPath;

public class TaggerXML {

    private Lexicon lex = null;
    private Matrix matTriLeft = null;
    private Matrix matTriMiddle = null;
    private Matrix matBiLeft = null;
    private Matrix matBiRight = null;
    private Suffix suf = null;
    private CaseControl upp = null;
    private CaseControl low = null;
    private WordLength wL = null;
    private SentencePosition senLeftP = null;
    private SentencePosition senRightP = null;
    private String firstTag = null;
    private String secondTag = null;
    private String currTag = null;
    private Document doc = null;
    private XMLOutputter out = null;
    private Element firstElement = null;
    private Element currElement = null;
    private String posTags = null;
    private Vector ergebnisTabelle = null;
    private int correct = 0;

    public TaggerXML(String Input, String lexicon, String matrixLeft, String matrixMiddle, String suffixMat, String uppMat,
            String lengthFile, String leftPositionDat, String rightPositionDat, String biLeft, String biRight, String lowMat)
            throws JDOMException, IOException {

        System.out.println("Tagger wird inizialisiert");
        lex = new Lexicon(stringToReader(lexicon));
        System.out.println("Lexikon wurde geladen");
        matTriLeft = new Matrix(stringToReader(matrixLeft));
        System.out.println("Trigramm left wurde geladen");
        matTriMiddle = new Matrix(stringToReader(matrixMiddle));
        System.out.println("Trigramm middle wurde geladen");
        matBiLeft = new Matrix(stringToReader(biLeft));
        System.out.println("Bigramm left wurde geladen");
        matBiRight = new Matrix(stringToReader(biRight));
        System.out.println("Bigramm right middle wurde geladen");
        suf = new Suffix(stringToReader(suffixMat));
        System.out.println("Suffixtable wurde geladen");
        upp = new CaseControl(stringToReader(uppMat));
        System.out.println("Upper case wurde geladen");
        low = new CaseControl(stringToReader(lowMat));
        System.out.println("Lower case wurde geladen");
        wL = new WordLength(stringToReader(lengthFile));
        System.out.println("Word legth wurde geladen");
        senLeftP = new SentencePosition(stringToReader(leftPositionDat));
        System.out.println("Sentence positions from left wurde geladen");
        senRightP = new SentencePosition(stringToReader(rightPositionDat));
        System.out.println("Sentence positions from right wurde geladen");

        doc = new SAXBuilder().build(new StringReader(Input));
        System.out.println("Die zu taggende XML-Datei wurde geladen");
        out = new XMLOutputter(Format.getPrettyFormat());
        ergebnisTabelle = new Vector();
        System.out.println("Die Ergebniss-Tabelle wurde inizialisiert");

    }

    public TaggerXML(String lexicon, String matrixLeft, String matrixMiddle, String suffixMat, String uppMat,
            String lengthFile, String leftPositionDat, String rightPositionDat, String biLeft, String biRight, String lowMat)
            throws JDOMException, IOException {

        System.out.println("Tagger wird inizialisiert");
        lex = new Lexicon(stringToReader(lexicon));
        System.out.println("Lexikon wurde geladen");
        matTriLeft = new Matrix(stringToReader(matrixLeft));
        System.out.println("Trigramm left wurde geladen");
        matTriMiddle = new Matrix(stringToReader(matrixMiddle));
        System.out.println("Trigramm middle wurde geladen");
        matBiLeft = new Matrix(stringToReader(biLeft));
        System.out.println("Bigramm left wurde geladen");
        matBiRight = new Matrix(stringToReader(biRight));
        System.out.println("Bigramm right middle wurde geladen");
        suf = new Suffix(stringToReader(suffixMat));
        System.out.println("Suffixtable wurde geladen");
        upp = new CaseControl(stringToReader(uppMat));
        System.out.println("Upper case wurde geladen");
        low = new CaseControl(stringToReader(lowMat));
        System.out.println("Lower case wurde geladen");
        wL = new WordLength(stringToReader(lengthFile));
        System.out.println("Word legth wurde geladen");
        senLeftP = new SentencePosition(stringToReader(leftPositionDat));
        System.out.println("Sentence positions from left wurde geladen");
        senRightP = new SentencePosition(stringToReader(rightPositionDat));
        System.out.println("Sentence positions from right wurde geladen");
        ergebnisTabelle = new Vector();
        System.out.println("Die Ergebniss-Tabelle wurde inizialisiert");

    }

    public Vector getTabelle() {
        return ergebnisTabelle;
    }

    private BufferedReader stringToReader(String File) {
        BufferedReader br = new BufferedReader(new StringReader(File));
        return br;
    }

    public static List getWordsAndPunc(Element sentence, String wortElement, String punctElement) {
        List list = new ArrayList();
        List test = sentence.getContent();
        Iterator tata = test.iterator();
        while (tata.hasNext()) {
            Object o = tata.next();
            if (o instanceof Element) {
                Element el = (Element) o;
                if (el.getName().equals(wortElement) || el.getName().equals(punctElement)) {
                    list.add(el);
                }
            }
        }
        return list;
    }

    private List<List> neuesSatzmodul(List tokens, String attForSen) throws JDOMException {
        List listOfSentences = new ArrayList<List>();
        List listOfWords = new ArrayList<Element>();

        Iterator tokensIt = tokens.iterator();
        String attAlt = "0";
        while (tokensIt.hasNext()) {
            Element token = (Element) tokensIt.next();
            String att = token.getAttributeValue(attForSen);
            if (att != null) {
                if (att.equals(attAlt)) {
                    listOfWords.add(token);
                } else {
                    listOfSentences.add(listOfWords);
                    listOfWords = new ArrayList<Element>();
                    listOfWords.add(token);
                }
                attAlt = att;
            } else {
                System.out.println("Das Token <" + token.getText() + "> hat keine Satzinformation auf der Seite. ID: " + token.getAttributeValue("id"));
            }
        }
        // this is for the last sentence
        listOfSentences.add(listOfWords);
        return listOfSentences;
    }

    public static List<List> Satzmodul(String xPathAusdruck, Document dok, String attForSentenceNumber, String wortElement, String punctElelement) throws JDOMException {

        List<List> listOfSentences = new ArrayList<List>();
        List<Element> listOfWords = new ArrayList<Element>();

        List sentences = XPath.selectNodes(dok, xPathAusdruck);
        Iterator senIt = sentences.iterator();
        String attBefore = null;
        while (senIt.hasNext()) {
            Element sentence = (Element) senIt.next();
            String attAfter = sentence.getAttributeValue(attForSentenceNumber);
            // if 2 sentences have same number, that means they are one sentence 
            if (attAfter.equals(attBefore)) {
                List words = getWordsAndPunc(sentence, wortElement, punctElelement);
                listOfWords.addAll(words);
            } // otherwise it is only one sentence
            else {
                if (!listOfWords.isEmpty()) {
                    listOfSentences.add(listOfWords);
                    listOfWords = new ArrayList<Element>();
                }
                listOfWords = getWordsAndPunc(sentence, wortElement, punctElelement);
            }
            attBefore = attAfter;
        }

        // for the last sentence
        listOfSentences.add(listOfWords);
        return listOfSentences;
    }

    /**
     * Mit dieser Methode wird der ganze Text getaggt
     *
     * @param xPathAusdruck
     * @param booly
     * @param ergebnisseDokumentieren
     * @param posPOS
     * @param attForSen
     * @param bar
     * @throws IOException
     * @throws JDOMException
     */
    public void fullTagging(String xPathAusdruck, boolean booly[], boolean ergebnisseDokumentieren, String posPOS,
            String attForSen, JProgressBar bar) throws IOException, JDOMException, FileNotFoundException, URISyntaxException {

        posTags = posPOS;

        List tokens = XPath.selectNodes(doc, xPathAusdruck);
        // Here we get all sentences of the text  
        // List sentences = Satzmodul(xPathAusdruck, doc, attForSentenceNumber, wortElement, punctElelement);
        System.out.println("Die zu taggenden Tokens wurden selektiert");
        List sentences = neuesSatzmodul(tokens, attForSen);
        System.out.println("Sätze wurden aus der XML-Datein gezogen");
        //		List sentences = XPath.selectNodes(doc, xPathAusdruck);

        int leftPosition = 0;
        int rightPosition = 0;
        // first iteration through text using sentences

        for (int j = 0; j < sentences.size(); j++) {
            List words = (List) sentences.get(j);
            int percent = (j * 100) / sentences.size();
            bar.setValue(percent);
            // second iteration on the words
            for (int i = 0; i < words.size(); i++) {
                Element word = (Element) words.get(i);
                leftPosition = i + 1;
                rightPosition = words.size() - i;
                // here we process the element in order to tag
                tagging(word, leftPosition, rightPosition, booly, ergebnisseDokumentieren);
                //System.out.println("Das Wort " + word.getText() + " position left: " + leftPosition + " position Right " + rightPosition);
            }

        }

        //	tag(currElement.getText(), null, booly);	
    }

    /**
     * Mit dieser Methode wird der ganze Text getaggt
     *
     * @param xPathAusdruck
     * @param booly
     * @param ergebnisseDokumentieren
     * @param posPOS
     * @param attForSen
     * @param bar
     * @throws IOException
     * @throws JDOMException
     */
    public int fullTaggingEval(List tokens, boolean booly[], boolean ergebnisseDokumentieren, String posPOS,
            String attForSen) throws IOException, JDOMException, FileNotFoundException, URISyntaxException {

        posTags = posPOS;

        // Here we get all sentences of the text  
        // List sentences = Satzmodul(xPathAusdruck, doc, attForSentenceNumber, wortElement, punctElelement);
        System.out.println("Die zu taggenden Tokens wurden selektiert");
        List sentences = neuesSatzmodul(tokens, attForSen);
        System.out.println("Sätze wurden aus der XML-Datein gezogen für Evaluation");
        //		List sentences = XPath.selectNodes(doc, xPathAusdruck);

        int leftPosition = 0;
        int rightPosition = 0;
        // first iteration through text using sentences

        for (int j = 0; j < sentences.size(); j++) {
            List words = (List) sentences.get(j);
            // second iteration on the words
            for (int i = 0; i < words.size(); i++) {
                Element word = (Element) words.get(i);
                leftPosition = i + 1;
                rightPosition = words.size() - i;
                // here we process the element in order to tag

                tagging(word, leftPosition, rightPosition, booly, ergebnisseDokumentieren);

                
            }

        }

        //	tag(currElement.getText(), null, booly);	
        return correct;
    }

    public String getXmlOutput() {
        String XMLoutput = out.outputString(doc);
        return XMLoutput;
    }

    /**
     * Diese Methode ist die Vorbereitung zum eigentlichen Taggen, in der man
     * die Umgebung der Wörter speichert und verwaltet
     *
     * @param dasElement
     * @param leftPosition
     * @param rightPosition
     * @param booly
     * @param ergebnisseDokumentieren
     * @throws JDOMException
     * @throws IOException
     */
    public void tagging(Element dasElement, int leftPosition, int rightPosition, boolean booly[], boolean ergebnisseDokumentieren) throws JDOMException, IOException, FileNotFoundException, URISyntaxException {

        // why to use 2 wordElements?
        firstElement = currElement;
        currElement = dasElement;

        // Here would it be possible to get the name of XML-Element
        // to decide whether it is punctuation or not, but now tagger decides 
        // it himself  
        String nameFirstElement = "";
        String word1 = "";
        if (firstElement == null) {
            word1 = null;
            nameFirstElement = null;
        } else {
            word1 = firstElement.getText();
            nameFirstElement = firstElement.getName();
        }
        String word2 = currElement.getText();
        // here we tag each word 
        tag(word1, word2, leftPosition - 1, rightPosition + 1, leftPosition, rightPosition, booly, ergebnisseDokumentieren, nameFirstElement);
        // for the end of the stream - the last currElement  
    }

    public void put(String ctag, double tagger) throws URISyntaxException {
        DecimalFormat df2 = new DecimalFormat("#.##");
        String vorhanden = firstElement.getAttributeValue("pos");
        if (vorhanden!=null || !vorhanden.equals("")){
            if (vorhanden.equals(ctag)){
                correct++;
            }
            else {
                System.out.println("Fehler bei dem Wort:  " + firstElement.getText() 
                        + " richtig: " + vorhanden + " falsch: " + ctag);
            }
            
        }
        firstElement.setAttribute("pos", ctag);
        firstElement.setAttribute("tagger", df2.format(tagger));

    }

    /**
     * wird momentan nicht verwendet
     *
     * @param werte
     * @return
     */
    public long markov(double werte[]) {
        long p = (long) 0.0;
        boolean booly = false;
        for (int i = 0; i < werte.length; i++) {
            if (werte[i] != 0.0) {
                if (booly) {
                    p = p * Double.doubleToLongBits(werte[i]);
                } else {
                    p = Double.doubleToLongBits(werte[i]);
                    booly = true;
                }
            }
        }
        return p;
    }

    private double abrunden(double zahl) {
        return Math.round(zahl * 10000) / 10000.0;
    }

    public double addAllPs(Vector ps) {
        double retval = 0;
        double sumOfPs = 0;
        for (int i = 0; i < ps.size(); i++) {
            double d = (Double) ps.elementAt(i);
            sumOfPs = sumOfPs + d;
        }
        // Normalise the p with the size of the vector array
        retval = sumOfPs / ps.size();
        return retval;
    }

    public void tag(String word1, String word2, int leftPositionWord1, int rightPositionWord1, int leftPositionWord2, int rightPositionWord2, boolean booly[], boolean ergebnisseDokumentieren, String currentElementName) throws FileNotFoundException, URISyntaxException {

        Vector dummy = new Vector();
        if (word1 != null && word2 != null) {

            firstTag = secondTag;
            secondTag = currTag;

            double score = 0.0;

            // here we iterate through all tag and evalute probabilities for each and every tag
            StringTokenizer st = new StringTokenizer(posTags);
            while (st.hasMoreTokens()) {

                String tmpTag = st.nextToken();

                // Ausschließregeln anwenden
                if (checkRulesExclude(tmpTag, word1, currentElementName)) {
                    //System.out.println(" Ausgeschloßen!:    " + tmpTag +" "+  word1+ " " +currentElementName);
                } else {

                    StringTokenizer stn = new StringTokenizer(tagSecondWord(word2, dummy, leftPositionWord2, rightPositionWord2));

                    while (stn.hasMoreTokens()) {
                        String rightTag = stn.nextToken();
                        Vector zeile = new Vector();
                        zeile.add("\n Das Wort: " + word1);

                        Vector Werte = tagModules(booly, word1, rightTag, tmpTag, leftPositionWord1, rightPositionWord1, zeile);

                        double tmpScore = addAllPs(Werte);

                        zeile.add("Wortklasse: " + tmpTag);
                        zeile.add("Gesamtwert: " + tmpScore);

                        if (tmpScore >= score) {
                            score = tmpScore;
                            currTag = tmpTag;
                        }

                        zeile.add("SatzpositionAnfang: " + leftPositionWord1);
                        zeile.add("SatzpositionEnde: " + rightPositionWord1);
                        if (ergebnisseDokumentieren) {
                            ergebnisTabelle.add(zeile);
                        }
                    }
                }
            }
            //System.out.println("getaggt: " + word1 + " mit " + currTag);
            put(currTag, score);
        }

    }

    public String checkRulesPositive(String currentTag, String word, String currentElementName) {
        String retval = currentTag;
        if ("c".equals(currentElementName)) {
            retval = "$";
        }
        return retval;
    }

    public boolean checkRulesExclude(String currentTag, String word, String currentElementName) {
        boolean retval = false;
        // System.out.println(currentTag +" "+ word +" "+ currentElementName);
        if ("w".equals(currentElementName) && "$".equals(currentTag)) {
            retval = true;
            //          System.out.println("Hier muss es feuern!... ");
        } else {
            retval = false;
        }
        return retval;
    }

    public String tagSecondWord(String word, Vector zeile, int leftPosition, int rightPosition) {
        String tag = "";
        double score = 0.0;
        String secondTag2 = "";
        double secondScore = 0.0;

        StringTokenizer st = new StringTokenizer(posTags);
        while (st.hasMoreTokens()) {
            String tmpTag = st.nextToken();

            boolean booly[] = {true, true, true, true, true, false, false, false, false, true, true};

            Vector werte = tagModules(booly, word, firstTag, tmpTag, leftPosition, rightPosition, zeile);
            double tmpScore = addAllPs(werte);

            if (tmpScore > score) {
                secondScore = score;
                score = tmpScore;

                secondTag2 = tag;
                tag = tmpTag;

            } else if (secondScore == score) {
                tag = tag + " " + secondTag2;
            }

        }
        return tag;
    }

    private Vector<Double> tagModules(boolean booly[], String word, String rightTag, String tmpTag, int firstLePosition, int firstRiPosition, Vector<String> zeile) {
        Vector<Double> werte = new Vector<Double>();

        if (booly[0]) {
            double lexScore = abrunden(lex.look(word, tmpTag));
            werte.add(lexScore);
            zeile.add("lexScore: " + lexScore);
        }

        if (booly[1]) {
            double sufScore = abrunden(suf.look(word, tmpTag));
            werte.add(sufScore);
            zeile.add("suffixScore: " + sufScore);
        }

        if (booly[2]) {
            double uppScore = abrunden(upp.lookUpperCase(word, tmpTag));
            werte.add(uppScore);
            zeile.add("upperScore: " + uppScore);
        }

        if (booly[3]) {
            double lowScore = abrunden(low.lookLowerCase(word, tmpTag));
            werte.add(lowScore);
            zeile.add("lowerScore: " + lowScore);
        }

        if (booly[4]) {
            double lengthScore = abrunden(wL.look(word, tmpTag));
            werte.add(lengthScore);
            zeile.add("wordLengthScore: " + lengthScore);
        }

        if (booly[5]) {
            double matScore = abrunden(matTriLeft.lookup(firstTag, secondTag, tmpTag));
            werte.add(matScore);
            zeile.add("triLeftScore: " + matScore);
        }

        if (booly[6]) {
            double matMiddleScore = abrunden(matTriMiddle.lookup(secondTag, tmpTag, rightTag));
            werte.add(matMiddleScore);
            zeile.add("triMiddleScore: " + matMiddleScore);
        }

        if (booly[7]) {
            double BiLeftScore = abrunden(matBiLeft.lookup(secondTag, tmpTag, ""));
            werte.add(BiLeftScore);
            zeile.add("biLeftScore: " + BiLeftScore);
        }

        if (booly[8]) {
            double BiRightScore = abrunden(matBiRight.lookup(tmpTag, rightTag, ""));
            werte.add(BiRightScore);
            zeile.add("biRigthScore: " + BiRightScore);
        }

        if (booly[9]) {
            double leftPositionScore = abrunden(senLeftP.lookup(tmpTag, firstLePosition + ""));
            werte.add(leftPositionScore);
            zeile.add("satzpositionAnfang: " + leftPositionScore);
        }

        if (booly[10]) {
            double rightPositionScore = abrunden(senRightP.lookup(tmpTag, firstRiPosition + ""));
            werte.add(rightPositionScore);
            zeile.add("satzpositionEnde: " + rightPositionScore);
        }
        return werte;
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException, JDOMException {
        // TODO Auto-generated method stub

        String LexFile = "E:\\Promotion\\workspace\\Grundlage\\src\\POSTagger\\lex.prop";
        String MatrixFile = "E:\\Promotion\\Rodange-POS\\posleft.txt";
        String middleMatrixFile = "E:\\Promotion\\Rodange-POS\\posmiddle.txt";
        String SuffixFile = "E:\\Promotion\\Rodange-POS\\suf.txt";
        String UpperFile = "E:\\Promotion\\Rodange-POS\\upp.txt";
        String lengthFile = "E:\\Promotion\\Rodange-POS\\length.txt";

        String Input = "E:\\Promotion\\Rodange-POS\\loeschen_sentenciert.xml";

        String luInput = "E:\\Promotion\\Rodange-POS\\evaluation_rtl_lu.xml";

        String Output = "E:\\Promotion\\Rodange-POS\\renertNewTagged1.xml";

        String leftPositionFile = "E:\\Promotion\\Rodange-POS\\Satzpositionen1.txt";

        String rightPositionFile = "E:\\Promotion\\Rodange-POS\\SatzpositionenBack1.txt";

        String biLeft = "E:\\Promotion\\Rodange-POS\\posbiLeft.txt";
        String biRight = "E:\\Promotion\\Rodange-POS\\posbiRight.txt";
        String lowMat = "E:\\Promotion\\Rodange-POS\\low.txt";

        TaggerXML engine = new TaggerXML(LexFile, MatrixFile, middleMatrixFile, SuffixFile,
                UpperFile, Input, lengthFile, leftPositionFile, rightPositionFile, biLeft, biRight, lowMat);
        //	System.out.println(engine.tagSecondWord("More") + " --" );

        String possTags = "DET NOUN ADJ CONJ NUM PREP PRON ADV VERB NEG INFTO PREPA VERBZ PUNC";
        boolean booly[] = new boolean[12];
        //                engine.fullTagging("//s",booly,possTags);

        System.out.println("Erfolgreich getagged in der Datei: " + Output);
    }
}
