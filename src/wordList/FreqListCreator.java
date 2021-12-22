/*
 * FreqListCreator
 */

package wordList;

import java.io.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import Tokenizer.FileTokeniser;
import wordList.FreqComparator;
import wordList.WordFreq;

public class FreqListCreator {

	public void run (String file) throws IOException {

		FileTokeniser ft = new FileTokeniser(file);
		FreqList flist = new FreqList();
		while (ft.hasMoreTokens()){
			flist.add(ft.getNextToken().toLowerCase());
		}	
		ft.close();

		/* 
		 //noch eine Datei

		FileTokeniser tt = new FileTokeniser("E:\\Promotion\\LuxCorpus\\lux_Korpus\\Compte_rendu_des_s�ances _publiques.txt");
		while (tt.hasMoreTokens()){
			flist.add(tt.getNextToken());
		}	
		tt.close();

		 */
		//ende noch eine Datei

		//PrintWriter pw = new PrintWriter(new FileWriter(args[0]+".frq"));

		Iterator fl = flist.iterator();
		WordFreq[] wf = new WordFreq[flist.getT()]; 
		int i = 0;
		while (fl.hasNext()){
			String word = (String)fl.next();
			int freq = flist.getFreq(word);				
			wf[i] = new WordFreq(word, freq);
			i++;			
		}

		Comparator freqC = new FreqComparator();	
		Arrays.sort(wf, freqC);

		// Mittelwertberechnung


		double currFreq;
		double mittelWert= (double)flist.getN() / (double)wf.length;
		double varsumme = 0.0;
		double variance = 0.0;
		double std = 0.0;
		FreqList zahlList = new FreqList();
		// Mittelwertberechnung Ende
		for (int t = 0; t < wf.length; t++){
			currFreq = wf[t].getFreq();
			varsumme = varsumme + ((currFreq - mittelWert) * (currFreq - mittelWert));
			System.out.println(t + ". " + wf[t].getWord() + " " + (int)currFreq);
			int curr = (int)currFreq; 
			String word = ""+curr;
			zahlList.add(word);
		}
		variance = varsumme / (flist.getT() - 1);
		std = Math.sqrt(variance);
		System.out.println(flist.getT() + " " + flist.getN() + " " + wf.length);
		System.out.println("Mittelwert: " + mittelWert + " variance: " + variance + " std: " + std);
		System.out.println(zahlList.getT());

		Iterator it = zahlList.iterator();
		while (it.hasNext()){
			String word = (String)it.next();
			System.out.println(word + " " + (int)zahlList.getFreq(word));	

		}
		//	flist.save(pw);
		//	pw.close();
	}

	public static void main (String args[]) throws IOException {
		if (args.length != 1){
			System.err.println("usage: FreqListCreator filename");
			return;
		}

		new FreqListCreator().run(args[0]);
	}

} // end of the class FreqListCreator
