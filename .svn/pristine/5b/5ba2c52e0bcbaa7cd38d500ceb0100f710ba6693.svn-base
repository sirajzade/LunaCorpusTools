package lunacorpustools.POSTagger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class SentencePosition {

    private Map<String, Double> storage;

    public SentencePosition(BufferedReader input) throws IOException {
        storage = new HashMap<String, Double>();
        load(input);
    }

    public void load(BufferedReader br) throws IOException {

        String line = br.readLine();

        while (line != null) {
            StringTokenizer st = new StringTokenizer(line);
            if (st.countTokens() < 2) {
                System.err.println("Insufficient line: `" + line + "�");
            } else if (st.countTokens() == 2) {

                String retString = st.nextToken();
                double retDouble = Double.parseDouble(st.nextToken());
                storage.put(retString, retDouble);
                //System.out.println(retString + " _ " + retDouble);
            } else {
                StringBuffer word = new StringBuffer(st.nextToken());
                while (st.countTokens() > 2) {
                    word.append(' ');
                    word.append(st.nextToken());
                }
                String retString = word.toString();
                double retDouble = Double.parseDouble(st.nextToken());
                storage.put(retString, retDouble);
                //System.out.println(retString + " _ " + retDouble);
            }
            line = br.readLine();

        }
    }

    public double lookup(String tag, String position) {
        String word = tag + "_" + position;
        //System.out.println(storage.size());
        double p = 0.0;
       // System.out.println("Sentence positions wird verwendet" + p);
        if (tag != null && position != null) {
            if (storage.containsKey(word)) {
                p = storage.get(word);
            }
        }
        return p;
    }

    public double abrunden(double zahl) {
        return Math.round(zahl * 10000) / 10000.0;
    }

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // TODO Auto-generated method stub
        BufferedReader br = new BufferedReader(new FileReader("E:\\Promotion\\Rodange-POS\\SatzpositionenBack1.txt"));
        SentencePosition sp = new SentencePosition(br);
        System.out.println(sp.lookup("PUNC", "3"));
        System.out.println(sp.abrunden(sp.lookup("PUNC", "3")));

    }
}
