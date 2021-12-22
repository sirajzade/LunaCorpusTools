/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Morphal;

/**
 *
 * @author joshgun.sirajzade
 */

import java.text.Collator;
import java.util.Comparator;

public class SuffixZellenComparator implements Comparator {
	
	public int compare(Object o1, Object o2) {
		Word s1 = (Word)o1;
		Word s2 = (Word)o2;
		int i = prepairForCompare( s1.getSuffix() ).compareTo( prepairForCompare( s2.getSuffix() ) );
	    return ( 0 != i ) ? i : ((String)s1.getSuffix()).compareTo( (String)s2.getSuffix() );
	  }

	  private String prepairForCompare( Object o )
	  {
	    return ((String)o).toLowerCase().replace( 'ä', 'a' )
	                                    .replace( 'ö', 'o' )
	                                    .replace( 'ü', 'u' )
	                                    .replace( 'ß', 's' );
	  }

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
