/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Morphal;

/**
 *
 * @author joshgun.sirajzade
 */

public class Word implements Comparable{

	private String token = null;
	private String prefix = null;
	private String suffix = null;
	private String infix = null;
	private String vorkommen = " ";
	private String context = null;
	private String stemm = null;
	private String affixArt = null; 
	
	
	public Word(String token, String Prefix, String Suffix, String Infix, String stemm, String vorkommen, String context, String AffixArt) {
	
		this.token = token;
		this.prefix = Prefix;
		this.suffix = Suffix;
		this.infix = Infix;
		this.vorkommen = vorkommen;
		this.context = context;
		this.stemm = stemm;
		this.affixArt = AffixArt;
		
		
	}
	
	public String getStemm() {
		return stemm;
	}

	public void setStemm(String stemm) {
		this.stemm = stemm;
	}

	public String getZelle(){
		return token + " " + vorkommen + " " + context; 
	}
	
	
	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public String getInfix() {
		return infix;
	}

	public String getWord() {
		return token;
	}


	public void setWord(String word) {
		this.token = word;
	}


	public String getAffixArt() {
		return affixArt;
	}


	public String getVorkommen() {
		return vorkommen;
	}


	public void setVorkommen(String vorkommen) {
		this.vorkommen = vorkommen;
	}

        public void addVorkommen(String newVorkommen){
             System.out.println("hier Vorkommen: " + vorkommen);
            if(vorkommen.indexOf(newVorkommen)==-1){
               vorkommen = vorkommen+ "; " + newVorkommen; 
            }
        }

	public String getContext() {
		return context;
	}


	public void setContext(String context) {
		this.context = context;
	}
	
	public int compareTo(Object other) throws ClassCastException{
		if (other instanceof Word){
			Word sz = (Word)other;
			return (token.compareTo(sz.token));
		} else {
			throw new ClassCastException("SuffixZelle !=" + other.getClass().getName());
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
