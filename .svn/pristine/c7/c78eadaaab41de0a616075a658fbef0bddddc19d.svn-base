package indexedConcordancer;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPath;

public class IndexCreatorXML {

	private HashMap wordPositions;
	
	private Document doc = null;
	private XMLOutputter out = null;
	private OutputStream os = null;
	private List <Element>allWords = null;
	public IndexCreatorXML (String filename, String outPut, String xPathForAllWords) throws JDOMException, IOException {
		
		doc = new SAXBuilder().build(filename);
		
		out = new XMLOutputter(); 
		
		wordPositions = new HashMap<String, Vector>();
		
		allWords =  (List<Element>) XPath.selectNodes(doc, xPathForAllWords);
		if (allWords == null){
			System.err.println("No words found!");
		}
		setWordNumbers();
		writeDoc(outPut);
		PrintWriter pw = new PrintWriter(outPut+".idx", "UTF8");
		writeIndex(pw);
		pw.close();

		System.out.println("Ende________________________");
	}
	

	public void writeIndex (PrintWriter output) {
		Iterator it = wordPositions.keySet().iterator();
		while (it.hasNext()){
			String key = (String)it.next();
			Vector positions = (Vector)wordPositions.get(key);
			output.print(key+"=");
			for (int i = 0; i < positions.size(); i++){
				output.print(positions.elementAt(i)+" ");
			}
			output.println(" ");
		}
	}
	
	public void setWordNumbers(){
		for (int a = 0; a < allWords.size(); a++){
			Element word = allWords.get(a);
			word.setAttribute("n", a+"");
			setHash(word, a);
		}
	}
	
	public void setHash(Element e, int n){
		String word = e.getValue().toLowerCase();
		Vector v = (Vector) wordPositions.get(word);
		if (v == null){
			v = new Vector();
			v.add(n);
			wordPositions.put(word, v);
		}
		else{
			v = (Vector)wordPositions.get(word);
			v.add(n);
			wordPositions.put(word, v);
		}
	}
	
	
	public void writeDoc(String filename)throws IOException{
		OutputStream os = new FileOutputStream(filename);
		out.output(doc,os);
		System.out.println("Document was written in the file:'"+filename+"'");
	}
	
	
	/**
	 * @param args
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public static void main(String[] args) throws JDOMException, IOException {
		// TODO Auto-generated method stub
		String file = "E:\\Promotion\\Lemmatizer\\Lux\\loeschen2.xml";
		String filename = "E:\\Promotion\\Lemmatizer\\Lux\\loeschen3.xml";
		
		IndexCreatorXML ixml = new IndexCreatorXML(file, filename, "//w");
		
		
	}

}
