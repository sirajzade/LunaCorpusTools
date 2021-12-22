/*
 * PositionLister.java
 */

package indexedConcordancer;

import java.io.*;


public class PositionLister {
	
	private WordPositionReceiver target = null;
	private Reader source = null;
	
public PositionLister(Reader input, WordPositionReceiver output)
throws IOException {
	source = input;
	target = output;
	boolean inWord = false;
	int currentPosition = 0;
	int wordPosition = 0;
	StringBuffer currentToken = new StringBuffer();
	int c = source.read();
	while (c > -1){
		char ch = (char)c;
		if (inWord){
			if (Character.isLetterOrDigit(ch)){
				currentToken.append(ch);
			} else {
				target.receiveWordPosition(currentToken.toString(), wordPosition); 
				currentToken.setLength(0);
				inWord = false;
			}
		} else {
			if (Character.isLetterOrDigit(ch)) {
				wordPosition = currentPosition;
				currentToken.append(ch);
				inWord = true;
			} else {
				// nothing
			}
		}
		currentPosition++;
		c = source.read();
	}
	
}
	
	
} // end of the class PositionLister
