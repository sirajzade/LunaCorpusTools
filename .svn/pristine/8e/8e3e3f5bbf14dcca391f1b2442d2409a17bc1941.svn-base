/*
 * Matrix.java
 */
package lunacorpustools.POSTagger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * This class implements a transition matrix for tag probabilities.
 */
public class Matrix {

    private Map storage;

    public Matrix(BufferedReader matrixfile) throws IOException {
        storage = new HashMap();
        load(matrixfile);
    }

    /**
     * Look up a tag sequence in the matrix. If the tag labels are invalid, an
     * ArrayIndexOutOfBounds exception will be thrown.
     *
     * @param t1 the first tag to look at.
     * @param t2 the tag following t1.
     * @param t3 the third tag
     * @return the transition probability of t3 followed by t1 and t2.
     */
    public double lookup(String t1, String t2, String t3) {
        String tags = t1 + t2 + t3;
        //System.out.println(tags);
        //double p = (Double)storage.get(tags) * 100;
        double p = 0.0;
        if (t1 != null && t2 != null && t3 != null) {
            if (storage.containsKey(tags)) {
          //      System.out.println("matrix wird verwendet" + p);
                p = (Double) storage.get(tags);
            }
        }
        return p;
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

            } else {
                StringBuffer word = new StringBuffer(st.nextToken());
                while (st.countTokens() > 2) {
                    word.append(' ');
                    word.append(st.nextToken());
                }
                String retString = word.toString();
                double retDouble = Double.parseDouble(st.nextToken());
                storage.put(retString, retDouble);
                System.out.println(retString + " _ " + retDouble);
            }
            line = br.readLine();

        }
    }

    // Main method for testing
    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new FileReader("C:\\Arbeit\\hallo.txt"));

        Matrix mat = new Matrix(br);
        String t1 = "PREP";
        String t2 = "NOUN";
        //String t3 = "VERB";
        System.out.println(mat.lookup(t1, t2, ""));
    }
}	// end of class Matrix

