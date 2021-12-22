/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Morphal;

/**
 *
 * @author joshgun.sirajzade
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JPanel;
import lunacorpustools.zeileGUI;
import wordList.FreqList;
import wordList.WordFreq;

public class TextWords {
	
	private Map <String, Word> storage;
	
	String rememberTheLine;
	
	public TextWords(){
		storage = new HashMap();		
		rememberTheLine = null;
	}
	
	public void add (String Prefix, String Suffix, String Infix, String AffixArt, String Wort, String Vorkommen, String line){
	
                
                String stemm = stemmBuilder(Wort, Prefix, Suffix, Infix, AffixArt);
		Word zelle = new Word(Wort, Prefix, Suffix, Infix, stemm, Vorkommen, line, AffixArt);
                // SuffixZelle generatet
                System.out.println("SuffixZelle geaddet: " + Wort);
		if (!storage.containsKey(Wort)){
                    
                        
		//	Array [0] = Prefix;
		//	Array [1] = Suffix;
		//	Array [2] = Infix;
		//	Array [3] = AffixArt;
		//	Array [4] = Vorkommen;
		//	Array [5] = line;
			
			storage.put(Wort, zelle);
		}
		else {
			                   
                        Word dummyZelle = (Word)storage.get(Wort);
                        dummyZelle.addVorkommen(Vorkommen);
                        storage.put(Wort, dummyZelle);
		}
			//System.out.println(buffy[3]);
		
		
	}
	
        public String getPrefix (String word){
		Word zelle = (Word)storage.get(word);
		System.out.println("bei get: " + word);
		return zelle.getPrefix(); 
	}
	
	public String getSuffix (String word){
		Word zelle = (Word)storage.get(word);
		System.out.println("bei get: " + word);
		return zelle.getSuffix(); 
	}
	
	public String getInfix (String word){
		Word zelle = (Word)storage.get(word);
		System.out.println("bei get: " + word);
		return zelle.getInfix(); 
	}
	
	public String getAffixArt (String word){
		Word zelle = (Word)storage.get(word);
		System.out.println("bei get: " + word);
		return zelle.getAffixArt(); 
	}
	
	public String getVorkommen (String word){
		Word zelle = (Word)storage.get(word);
                String vorkommmen = zelle.getVorkommen();
		if (vorkommmen == null){
			return null;
		}
		return vorkommmen; 
	}
	public String getContextLine (String word){
		Word zelle = (Word)storage.get(word);
		String line = zelle.getContext();
                if (line == null){
			return null;
		}
		return line; 
	}
	
	public List getStemmsOfWords (){
		
		List words = new ArrayList();
		Iterator iti = storage.keySet().iterator();
		
		while (iti.hasNext()){
			String key = iti.next().toString();
			Word zelle = storage.get(key);
                        String affixArt = zelle.getAffixArt();
                        
			words.add(stemmBuilder(key, zelle.getPrefix(), zelle.getSuffix(), zelle.getInfix(), affixArt));
			}
		return words;
	}
	
	public String stemmBuilder (String Wort, String Prefix, String Suffix, String Infix, String AffixArt) {
		String stemm = null;
		if (AffixArt.equals("suffix")){
			stemm = Wort.substring(0, Wort.length() - Suffix.length());
		}
		if (AffixArt.equals("prefix")){
			stemm = Wort.substring(Prefix.length(), Wort.length());
			//System.out.println(Wort + " "+ stemm);
		}
		if (AffixArt.equals("zirkumfix")){
			stemm = Wort.substring(0, Wort.length() - Suffix.length());
			stemm = stemm.substring(Prefix.length(), stemm.length());
		}
		if (AffixArt.equals("infixPlusSuffix")){
			String stemmBuffer = Wort.substring(0, Wort.length() - Suffix.length());
			String Infix1[]= Infix.split(" ");
			stemm = stemmBuffer.replaceAll(Infix1[1], Infix1[0]);
		}
		
		if (AffixArt.equals("infixPlusPrefix")){
			String stemmBuffer = Wort.substring(Prefix.length(), Wort.length());
			String Infix1[]= Infix.split(" ");
			stemm = stemmBuffer.replaceAll(Infix1[1], Infix1[0]);
		}
		if (AffixArt.equals("infixPlusZirkumfix")){
			String stemmBuffer = Wort.substring(0, Wort.length() - Suffix.length());
			String stemmBuffer1 = stemmBuffer.substring(Prefix.length(), stemmBuffer.length());
			String Infix1[]= Infix.split(" ");
			stemm = stemmBuffer1.replaceAll(Infix1[1], Infix1[0]);
		}
		if (AffixArt.equals("infix")){
			String Infix1[]= Infix.split(" ");
			stemm = Wort.replaceAll(Infix1[1], Infix1[0]);
		}
		
		if (AffixArt.equals("nullmorphem")){
			stemm = Wort;
		}
		
		return stemm;
	}

	public void showTable(){
		System.out.println("--------- Show Table ----------");
		Iterator iti = storage.keySet().iterator();
		while (iti.hasNext()){
			String key = iti.next().toString();
			Word zelle = storage.get(key);
                        
			System.out.println(key + " $ " + zelle.getAffixArt()+ " $ ("+ zelle.getVorkommen() + ") $ "+ zelle.getContext());
		}
		System.out.println("--------- the end of the Table ----------");
	}
	
	
	public void saveTable (String FileName)throws IOException{
		PrintWriter pw = new PrintWriter(FileName);
		
		Iterator ity = storage.keySet().iterator();
		while (ity.hasNext()){
			String key = ity.next().toString();
                        Word zelle = storage.get(key);
			pw.println(zelle.getAffixArt() + " $ " + key + " ("+ zelle.getVorkommen() + ") $ "+ zelle.getContext());
		
                 //       pw.println(storage. getAffixArt() + " $ " + sz[i].getWord()+ " $ " + sz[i].getStemm()+ " $ (" + sz[i].getVorkommen()+ ") $ " + sz[i].getContext());
			
                }
		
		pw.close();
	}
        
        private String [] teileNormalundFett(String word, String context){
            String [] retval = new String[2];
            if (context.contains(word)){
            int beginIndex = context.indexOf(word);
            int endIndex = beginIndex + word.length();
            
            retval[0] = context.substring(0, beginIndex);
            retval[1] = context.substring(endIndex);
            } else {
                retval[0] = "";
                retval[1] = "";
            }
            
            return retval;
        }
        
        public void printToGUI (JPanel panel){
          //      panel.removeAll();
		Iterator ity = storage.keySet().iterator();         
                int nummer = 0;
		while (ity.hasNext()){
                        nummer++;
			String key = ity.next().toString();
                        Word zelle = storage.get(key);
                        
                        String context = zelle.getContext();
                        String word = zelle.getWord();
                        String parts[] = teileNormalundFett(word, context);
                        
                        System.out.println(context);     
                        System.out.println("print to GUI: " + zelle.getContext());
                        // Provisorisch, um diese Fehlermeldung zu vermeiden 
                        zeileGUI zeile = new zeileGUI(" ", nummer + ") " + parts[0], word, parts[1], word, word);
                        zeile.printZeile(panel);
                 //       pw.println(storage. getAffixArt() + " $ " + sz[i].getWord()+ " $ " + sz[i].getStemm()+ " $ (" + sz[i].getVorkommen()+ ") $ " + sz[i].getContext());
			
                }
		
        }
        
         public String printTextExport (){
          //      panel.removeAll();
               StringBuffer sb = new StringBuffer();
               sb.append("<html>");
               
		Iterator ity = storage.keySet().iterator();         
                int nummer = 0;
		while (ity.hasNext()){
                        nummer++;
			String key = ity.next().toString();
                        Word zelle = storage.get(key);
                        
                        String context = zelle.getContext();
                        String word = zelle.getWord();
                        String parts[] = teileNormalundFett(word, context);
                        
                        System.out.println("Export the text: " + zelle.getContext());
                        sb.append(nummer + ") " + parts[0] +" <b>"+ word +"</b> "+ parts[1] + " <br/>");
                  
                 //       pw.println(storage. getAffixArt() + " $ " + sz[i].getWord()+ " $ " + sz[i].getStemm()+ " $ (" + sz[i].getVorkommen()+ ") $ " + sz[i].getContext());
			
                }
                sb.append("</html>");
		return sb.toString();
        }

	
	public List getStemmGroupsByAffix (Word sz[]){
		List retval = new ArrayList();
		//is that a good idea to put list in a list, why not?
		FreqList AffixFl = new FreqList();
		for (int i = 0; i < sz.length; i++){
			String AffixArt = sz[i].getAffixArt();
			String particle = null;
				
			if (AffixArt.equals("prefix")) { particle = sz[i].getPrefix();}
			if (AffixArt.equals("suffix")) { particle = sz[i].getSuffix();}
			if (AffixArt.equals("infix")) { particle = sz[i].getInfix();}
			if (AffixArt.equals("zirkumfix")) { particle = sz[i].getPrefix() + "-" + sz[i].getSuffix();}
			if (AffixArt.equals("infixPlusSuffix")) { particle = sz[i].getInfix()+ "-" + sz[i].getSuffix();}
			if (AffixArt.equals("infixPlusPrefix")) { particle = sz[i].getInfix()+ "-" + sz[i].getPrefix();}
			if (AffixArt.equals("infixPlusZirkumfix")) { particle = sz[i].getPrefix() +"-" + sz[i].getInfix()+ "-" + sz[i].getSuffix();}
			// es ist möglich, hier das Partikel nur einmal zu setzen, weil es in der Sprache nur ein
			// Nullmorphem gibt.
			if (AffixArt.equals("nullmorphem")) { particle = "lol";}
			
			AffixFl.add(particle);
		}
		
		Iterator itFl = AffixFl.iterator();
		while (itFl.hasNext()){
			List innerList = new ArrayList();
			String particle = (String)itFl.next();
			for (int i = 0; i < sz.length; i++){
				String AffixArt = sz[i].getAffixArt();
				String particle1 = null;
				
				if (AffixArt.equals("prefix")) { particle1 = sz[i].getPrefix();}
				if (AffixArt.equals("suffix")) { particle1 = sz[i].getSuffix();}
				if (AffixArt.equals("infix")) { particle1 = sz[i].getInfix();}
				if (AffixArt.equals("zirkumfix")) { particle1 = sz[i].getPrefix() + "-" + sz[i].getSuffix();}
				if (AffixArt.equals("infixPlusSuffix")) { particle1 = sz[i].getInfix()+ "-" + sz[i].getSuffix();}
				if (AffixArt.equals("infixPlusPrefix")) { particle1 = sz[i].getInfix()+ "-" + sz[i].getPrefix();}
				if (AffixArt.equals("infixPlusZirkumfix")) { particle1 = sz[i].getPrefix() +"-" + sz[i].getInfix()+ "-" + sz[i].getSuffix();}
			    if (AffixArt.equals("nullmorphem")) { particle1 = "lol";}
				//System.out.println(particle + " " + sz[i].getWord() + " " + particle1);
				
				if (particle.equals(particle1)){
					innerList.add(sz[i]);
				}
				
			}
			retval.add(innerList);
		}
		
		return retval;
	} // the end of the method
	
	public List CompareSzLists (List l1, List l2){
		List retval = new ArrayList();
		for (int i = 0; i < l1.size(); i++) {
			
			Word sz1 = (Word)l1.get(i);
			String stemm1 = sz1.getStemm();
		    
			
			for (int j = 0; j < l2.size(); j++) {
				Word sz2 = (Word)l2.get(j);
				String stemm2 = sz2.getStemm();
				
				if (stemm1.equalsIgnoreCase(stemm2)){
					
					retval.add(sz1);
					retval.add(sz2);
				}
			}
		}
		return retval;
	}
        
        public Word [] getSuffixTableArray (){
		Vector zells = new Vector();
		Iterator iti = storage.keySet().iterator();
		while (iti.hasNext()){
			String word = iti.next().toString();					
			Word sz = storage.get(word);
			zells.add(sz);
		}
		Word szs [] = new Word[zells.size()];
		zells.copyInto(szs);
		return szs;
        }
        
	
	public List getParadigmStemms2 (){
		List l = new ArrayList();
		
		List stemGroups = getStemmGroupsByAffix(getSuffixTableArray());
		
		for (int i = 0; i < stemGroups.size(); i++) {
			List innerList1 = null;
			System.out.println("das Group: " + i +" Und Ls size: " + l.size());
			
			innerList1 = (List)stemGroups.get(i);
			
			System.out.println("Tas size: " + innerList1.size());
			
			List aNewMetaList = stemGroups.subList(i + 1, stemGroups.size());
		
			
			for (int j = 0; j < aNewMetaList.size(); j++) {
				System.out.println("das innerGroup: " + j);
				List innerList2 = (List)aNewMetaList.get(j);
				List sz = CompareSzLists(innerList1, innerList2);
								
				if (sz != null){
					l.addAll(sz);
				}
			} 
		
		}
		return l;
		}
	
	public void getParadigmStemmsInformation2(String FileName) throws IOException{
		
		TextWords st = new TextWords();
		List l = getParadigmStemms2();
		
		// lets get rid of redundancy
		HashMap hmWort = new HashMap();
				
		for (int i = 0; i < l.size(); i++) {
			Word szBuffer = (Word)l.get(i);
			String word = szBuffer.getWord();
			if (!hmWort.containsKey(word)){
				hmWort.put(word, szBuffer);
			}
		}
		
		//then we should count the stems
		FreqList fl = new FreqList();
		Iterator iti = hmWort.keySet().iterator();
		while (iti.hasNext()){
			String word = (String) iti.next();
			Word szN = (Word) hmWort.get(word);
			String stemm = szN.getStemm();
			fl.add(stemm);
		}
		WordFreq wf [] = fl.sortFrequencies();
		
		Word[] sz= new Word[hmWort.size()];
		int u = 0;
		
		for (int n = 0; n < wf.length; n++) {
			String token = wf[n].getWord();
			
			Iterator itit = hmWort.keySet().iterator();
			while (itit.hasNext()){
				String word = (String) itit.next();
				Word szN = (Word) hmWort.get(word);
				String stemmi = szN.getStemm();
				if (stemmi.equals(token)){
					sz[u] = szN;
					u++;	
				}
			}
			
		}
		// Put the hashmap to SuffixZelle Array
		
		
		
		saveTable(FileName);
	}
	
	public List getParadigmStemms (){
		// das ist die Rückgabevariable
		List l = new ArrayList();
		
		Word sz [] = getSuffixTableArray();
		
		// firwat ass de Vergleich?
		Comparator compa = new SuffixZellenComparator();
	     Arrays.sort(sz, compa);	
	     
	     
	     // das ist für die Erhebung der Redundanz
	     // #####################################
	    FreqList fl = new FreqList();
		List words = getStemmsOfWords();
			Iterator wordsIt = words.iterator();
				while (wordsIt.hasNext()){
					String word = (String)wordsIt.next();
					fl.add(word);
				}
		// Rückgabe ist die fl Variable
		//#########################################
				
				Iterator wit = fl.iterator();
				// Jetzt gehen wir alle Wörter duch
				while (wit.hasNext()){
					String word = (String)wit.next();
					int booly = 0;
					
					for (int i = 0; i < sz.length; i++){
						String stemm = sz[i].getStemm();
	    	
						
	    	   
		 	 		//	System.out.println(word + " "+ stemm);
		 	 			if (stemm.equalsIgnoreCase(word)){
		 	 				booly++;
		 	 			//	System.out.println(word + " "+ stemm);
		 	 			}
		 	 		
		 	 		if (booly >= 5){
		 	 			System.out.println(stemm);
		 	 			l.add(stemm);
		 	 			
		 	 			
		 	 		}
				} // the end of the for loop
				
			} // the end of the while loop
	return l;
	
	} // the end of the method getParadigms
	  
		
	public TextWords getParadigmStemmsInformation(){
		TextWords st = new TextWords();

		Word sz [] = getSuffixTableArray();
		List l = getParadigmStemms();
		Iterator it = l.iterator();
		while (it.hasNext()){
			String stemm = (String)it.next();
			 for (int i = 0; i < sz.length; i++){
				 if (sz[i].getStemm().equalsIgnoreCase(stemm)){
					 System.out.println("Hier ist das Stem: "+ stemm +" " + sz[i].getZelle());
					 st.add(sz[i].getPrefix(), sz[i].getSuffix(), sz[i].getInfix(), sz[i].getAffixArt(), sz[i].getWord(), sz[i].getVorkommen(), sz[i].getContext());
				 }
			 }
		}
		
		return st;
	}

	
	
	
	
	/**
	 * @param args
	 */

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TextWords st = new TextWords();
		
		st.add(null, "ung", null, "suffix", "ungWort", "n44", "tuztsd s uastu sast");
		st.add(null, "ung", null, "suffix", "WungWortung", "5,- 9", "shgsadhk s dkajhsd s djh");
		st.add(null, "ung", null, "suffix", "Dortung", "7,- 4", "shgsadhk s dkajhsd s djhdfsdf dsfsdfsf");
		st.add(null, "ung", null, "suffix", "ungWort", "4,- 4", "tuztsd s uastu sast");
		st.add(null, "ung", null, "suffix", "ungWort", "8, - 3", "shgsadhk s dkajhsd s djhdfsdf dsfsdfsf");
	
		st.showTable();
		
		System.out.println("Bura bax ha: " + st.stemmBuilder("Mütter", "ge", "t", "ü u", "infix"));
		
		List test = st.getStemmsOfWords();
		Iterator titi = test.iterator();
		while (titi.hasNext()){
			String titiNext = (String)titi.next(); 
			System.out.println(titiNext);
			
		}

	}

}
