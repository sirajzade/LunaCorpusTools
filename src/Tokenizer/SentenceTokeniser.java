package Tokenizer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

import wordList.FreqList;

import Tokenizer.PreTokeniser;



public class SentenceTokeniser {
	public Vector sentences = new Vector();
	String inputfile;


	public SentenceTokeniser (String infile)
			throws IOException {
		inputfile = infile;
		BufferedReader input = null;
		input = new BufferedReader (new FileReader(infile));

		String sentence = "";
		String line = input.readLine();
		int z = 0;

		while (line!=null){

			for (int a = 0; a < line.length(); a++){
				char ch = line.charAt(a);
				sentence = sentence + ch;

				if (ch=='.' || ch==':' || ch=='?' || ch == ';' || ch == '!' || ch == ','){
					// System.out.println(sentence);
					sentences.add(z, sentence);
					sentence = "";
					z++;
				}

			}

			sentence = sentence + " ";

			line = input.readLine();
		}
		input.close();

	} // end of the constructor


	public Vector getSentences (){
		return sentences;
	}



	public int countTokens (String line){
		int b = 0;
		StringTokenizer tokens = new StringTokenizer(PreTokeniser.tokenise(line));
		while (tokens.hasMoreTokens()){
			tokens.nextToken();
			b++;
		}
		return b;
	} //end of the method countTokens


	public void writeSentencesWithFreq(String outputfile)throws IOException{

		PrintWriter output = new PrintWriter(outputfile);

		for (int i = 0; i <sentences.size(); i ++){
			int b = countTokens(sentences.elementAt(i).toString());
			System.out.println(i + ". "+ sentences.elementAt(i).toString() + " ~ " + b);
			int a = i + 1;
			output.println(sentences.elementAt(i).toString() + " ~ " + b + " ~ " + a);
		}
		output.close();
	}// end of the class whriteSentencesWithFreq


	public void writeSentences (String outputfile) throws IOException{

		PrintWriter output = new PrintWriter(outputfile);

		for (int i = 0; i <sentences.size(); i ++){
			output.println(sentences.elementAt(i).toString());
			output.println("                                     ");
		}
		output.close();

	}


	public void sentenceMeanVariance () throws IOException{
		double currFreq = 0.0;
		double varsumme = 0.0;
		double variance = 0.0;
		double std = 0.0;

		FreqList hlist = new FreqList();
		hlist.creator(inputfile);
		// instead of counting of tokens for every sentence, we just compute the number
		// of all words in the text corpus, therefore getN();

		double MittelWert= (double)hlist.getN() / (double)sentences.size();

		for (int i = 0; i < sentences.size(); i ++){
			currFreq = countTokens(sentences.elementAt(i).toString()); 
			varsumme = varsumme + ((currFreq - MittelWert) * (currFreq - MittelWert));
		}

		System.out.println("varsumme: " + varsumme + "hlistN: " + (int)hlist.getN()+ "sentence size: " + (double)sentences.size());
		variance = varsumme / (hlist.getN() - 1);
		std = Math.sqrt(variance);
		System.out.println("Mittelwert: " + MittelWert + " variance: " + variance + " std: " + std);		

	} // end of the class sentenceMeanVariance


	public FreqList getSentencesLengthFreq(){
		FreqList flist = new FreqList();
		for (int i = 0; i < sentences.size(); i ++){
			String ab = ""+countTokens(sentences.elementAt(i).toString());
			flist.add(ab);
		}
		return flist;
	}


	public void sentenceLengthMeanVariance(){
		FreqList flist = new FreqList();
		flist = getSentencesLengthFreq();

		double currFreqH;
		double mittelWertH= (double)flist.getN() / (double)flist.getT();
		double varsummeH = 0.0;
		double varianceH = 0.0;
		double stdH = 0.0;

		Iterator it = flist.iterator();
		while (it.hasNext()){
			String word = (String)it.next();
			currFreqH = (double)flist.getFreq(word);
			System.out.println(word + " " + (int)flist.getFreq(word));
			varsummeH = varsummeH + ((currFreqH - mittelWertH) * (currFreqH - mittelWertH));
		}
		varianceH = varsummeH / (flist.getN() - 1);
		stdH = Math.sqrt(varianceH);
		System.out.println("Mittelwert: " + mittelWertH + " variance: " + varianceH + " std: " + stdH);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		SentenceTokeniser st = new SentenceTokeniser("E:\\Promotion\\workspace\\renert.txt");
		st.writeSentences("E:\\Promotion\\Rodange-Kollokationen\\tokenisedSentences.txt");
		st.sentenceMeanVariance();
		st.sentenceLengthMeanVariance();
	}

} //end of the class SentenceTokeniser