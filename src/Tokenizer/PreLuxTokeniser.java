/*
 * PreTokeniser.java
 */
package Tokenizer;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

public class PreLuxTokeniser {

    // particles which should be separated form the word
    //public static final String PARTICLES[] = {"d", "z" , "s"};
    public String BEDINGTES_TRENNEN[];
    public String BEDINGTES_ZUSAMMENSETZEN[];
    // A list of characters, that may separate for word and sentences 
    // public static final String SPUNCT = "!$\"\u00a3%^&*()_+=#{}[];:�`/?,. \t\n'";
    public String SPUNCT;

    public PreLuxTokeniser(String Spunct, String Trennen[], String Zusammensetzen[]) {
        SPUNCT = Spunct;
        BEDINGTES_TRENNEN = Trennen;
        BEDINGTES_ZUSAMMENSETZEN = Zusammensetzen;
    }

    
    private String tokenizeInWords(String line) {
        StringBuffer retval = new StringBuffer();
        String tok1 = " ";
        String tok2 = " ";
        String tok3 = " ";

        StringTokenizer st = new StringTokenizer(line, SPUNCT, true);

        while (st.hasMoreTokens()) {

            tok1 = tok2;
            tok2 = tok3;
            tok3 = st.nextToken();

          // System.out.println("tok1: (" + tok1 + ") tok2: (" + tok2 + ") tok3: (" + tok3 + ")");

            tok1= " "+tok1;
            retval.append(tok1);
     
        } // the end of the while loop after a while of working

        // adding the last 2 tokens at the end 
        retval.append(" " + tok2);
        retval.append(" " + tok3);

     //   System.out.println(retval);
        return retval.toString();
    }

    private String transformierenMitRegEx(String gesamtToken, String[] RegExLine) {
        String retval = null;

        for (int i = 0; i < RegExLine.length; i++) {

            String regexLine = RegExLine[i];
            String[] regexLineParts = regexLine.split("->");
            String regExToMatch = regexLineParts[0];
            String regExToReplace = "";
            if (regexLineParts.length == 2) {
                regExToReplace = regexLineParts[1];
            }
            try {
                Pattern p = Pattern.compile(regExToMatch);
                Matcher m = p.matcher(gesamtToken);
                //jLabel3.setText(zahl+ " Strings wurden ersetzt");
                retval = m.replaceAll(regExToReplace);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                break;
            }
            gesamtToken = retval;
        }
        return retval;
    }

    public String tokenise (String line){
        String erst = tokenizeInWords(line);
        String zwo = transformierenMitRegEx(erst, BEDINGTES_ZUSAMMENSETZEN);
        String drei = transformierenMitRegEx(zwo, BEDINGTES_TRENNEN);
        return drei;
    }
    
    
    // this method defines whether the token WPUCT is or not
    public static void main(String args[]) {
        String hallo = "kê Gêstlechen huet Dr. de Libera ugestemmt – d’Franso’sen ha’ten se. verdrîwen; d’tena’ten bla d’Fra wor do bal blub";
        String line = "een ze paken,  deen dr�ckt en a hol blo.  a wann en d' schof gefriess\u1f44d huet,";
        
        String YESPARTICLES[] = {"Dr (\\.)->Dr$1",};
        String NOPARTICLES[] = {"d’([A-Z])->d’ $1",};

        // Trennzeichen alle
        // davon die potenziellen Wortzeichen


        PreLuxTokeniser plt = new PreLuxTokeniser("!$\"\u00a3%^&*()_+=#{}[];:�`/?,.\t\n' ", NOPARTICLES, YESPARTICLES);
        StringTokenizer st = new StringTokenizer(plt.tokenise(line));
        while (st.hasMoreTokens()) {
            System.out.println("Token: " + st.nextToken());
        }

    }
} // end of the class PreTokeniser
