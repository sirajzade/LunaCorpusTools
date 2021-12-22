package indexedConcordancer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPath;

public class IndexedConcordancerXML {

	private FileIndex theIndex = null;
	private Document doc = null;
	private XMLOutputter out = null;
	private OutputStream os = null;
	
	private Document doc2 = null;
	private Document doc3 = null;
	private XMLOutputter out2 = null;
	private OutputStream os2 = null;
	Element root = null;
	private String lineDummy = null;
	
	private HashMap hash = null;
	private HashMap hashPb = null;
	
	public IndexedConcordancerXML (String filename) throws IOException, JDOMException{
		theIndex = new FileIndex(filename);

		doc = new SAXBuilder().build(filename);
		hash = new HashMap<String, Element>();
		hashPb = new HashMap<String, String>();
		Format format = Format.getRawFormat();  //getPrettyFormat();
		out = new XMLOutputter(format);
		out2 = new XMLOutputter(format);
		root = new Element("div");
		doc2 = new Document(root);
		setHash();
		}
	
	public void setHash() throws JDOMException{
		List l = XPath.selectNodes(doc, "//pb | //w");
		Iterator it = l.iterator();
		String pb = "";
		while (it.hasNext()){
			Element w = (Element)it.next();
			if ("pb".equals(w.getName())){
			pb = w.getAttributeValue("n");
			}
			else{	
			String att = w.getAttributeValue("n");
			hash.put(att, w);
			hashPb.put(att, pb);
			}
				
		}
		
	}
	
	// die gefundenen Phraseologismen laden
	public void loadTheFile(String filename, String partikel) throws IOException, NumberFormatException, JDOMException{
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = br.readLine();
		while (line != null){
			if (line.length() > 50){
			String teil = line.substring(line.indexOf("'") + 1, line.length());
			String bword = teil.substring(0, teil.indexOf("'"));
			String words[] = bword.split(" ");
			String number = line.substring(0,line.indexOf("."));
			String score = line.substring(line.lastIndexOf(":")+2, line.length());
			
			System.out.println("Progress: " + number + " score: " + score);
			putMultiWordUnits(partikel, words[0], words[1], 5, 5, Double.parseDouble(score));
			}
			line = br.readLine();
		}
		
	}
	
	
	public void putMultiWordUnits(String partikel, String theWord, String Collocate, int left, int right, double score ) throws JDOMException{
	
		int positionsW[] = theIndex.lookup(theWord);
		int positionsC[] = theIndex.lookup(Collocate);
		Element td = new Element("td");
		Element tr = new Element("tr");
		Element table = new Element("table");
		table.addContent(tr);
		tr.addContent(td);
		
		if (positionsW != null && positionsC != null){
			
			td.addContent(putHead(positionsW[0], positionsC[0], score));
			
			for (int a = 0; a < positionsW.length; a++){
				for (int b = 0; b < positionsC.length; b++){
					if (positionsW[a] - left < positionsC[b] && positionsW[a] + right > positionsC[b]){
					
						Element w = (Element)hash.get(positionsW[a]+"");
						w.setAttribute("mwid" ,"k"+positionsW[a]+"w" + hashPb.get(positionsW[a]+""));
						w.setAttribute("collScore", score+"");
					
						 
						Element c = (Element)hash.get(positionsC[b]+"");
						c.setAttribute("mwid" , "k"+positionsC[b]+"w" + hashPb.get(positionsC[b]+""));
						c.setAttribute("collScore", score+"");
						
						Element zeile = createZeile(partikel, positionsW[a], positionsC[b], 6, 6);
						td.addContent(zeile);
						
					} // the end of the IF
					
				} // the end of the inner FOR
					
			} // end of the for / positionsW iterating
		    	root.addContent(table);
		} else {
			System.err.println("No index positions were found for the words: '"+ theWord+"'" + " '"+Collocate+"'");
		}
	}
	
	public Element putHead(int positionForWordOne, int positionForWordTwo, double score){
		
		int first = 0;
		int second = 0;
		
		if (positionForWordOne > positionForWordTwo){
			 first = positionForWordOne;
			 second = positionForWordTwo;
		} else {
			 first = positionForWordTwo;
			 second = positionForWordOne;
		}
		
		Element head = new Element("div");
		head.setAttribute("class", "head");
		
		Element w1 = (Element)hash.get(first+"");	Element ww1 = (Element)w1.clone();
		Text t = new Text("'");
		ww1.addContent(0, t); ww1.setName("font");
		Element w2 = (Element)hash.get(second+""); Element ww2 = (Element)w2.clone();
		ww2.addContent("'");  ww2.setName("font");
		Element w3 = new Element("font");
		w3.addContent("  Signifikanzwert: "+score+"");
		
		Element bold = new Element("font");
		bold.setAttribute("class", "bold");
		bold.addContent(ww1);
		bold.addContent(ww2);
		head.addContent(bold);
		head.addContent(w3);
		return head;
	}
	
	public Element createZeile(String partikel, int positionForWordOne, int positionForWordTwo, int left, int right){
		int beginn = 0;
		int end = 0;
		
		if (positionForWordOne > positionForWordTwo){
			 beginn = positionForWordOne - left;
			 end = positionForWordOne + right;
		} else {
			 beginn = positionForWordOne - left;
			 end = positionForWordOne + right;
		}
		
		if(beginn < 0){
			beginn = 0;
		}
		
		if (end > hash.size()){
			end = hash.size() - 1;
		}
		
		Element zeile = new Element("div");
		zeile.setAttribute("class", "zeile");
		while (beginn < end){
			Element n = (Element)hash.get(beginn+"");
			Element nn = (Element)n.clone();
			nn.setName("font");
			if (nn.getAttribute("mwid")!=null){
				nn.setAttribute("class", "mw");
				String ids[] = nn.getAttributeValue("mwid").split("w");
				String id = nn.getAttributeValue("mwid");
				nn.setAttribute("onclick", "openPagePhras('"+partikel+"','"+ids[1]+"','"+id+"','fenster1')");
			}
			
			Element l = n.getParentElement();
			while (!l.getName().equals("l")){
				l = l.getParentElement();
			}
			
			String number = l.getAttributeValue("n");
			System.out.println(l.getName()+" "+ number + " " + lineDummy + " " + n.getAttributeValue("n"));
			
			if (!number.equalsIgnoreCase(lineDummy)){
				Element font = new Element("font");
				font.addContent("(" + number + ")");
				zeile.addContent(font);
			}
			lineDummy = number;
			zeile.addContent(nn);
			beginn++;
		}
		
		return zeile;
	}
	
	/** Zum S�ubern der Tabelle mit Phraseologismen
	 	die leeren Tabellen bzw. Funde, die keine Beispielzeile
	 	enthalten l�schen 
	 * @throws JDOMException */
	
	public void cleanPhrasTable () throws JDOMException{
		List l = XPath.selectNodes(doc2, "//table[tr/td/div/@class='zeile']");
		Iterator iti = l.iterator();
		Element div2 = new Element("div");
		doc3 = new Document(div2);
		while (iti.hasNext()){
			Element table = (Element)iti.next();
			div2.addContent(table.detach());
		}
	}
	
	public void writeTheResult(String filename, String MWUnits) throws IOException{
		os = new FileOutputStream(filename);
		out.output(doc,os);
		
		os2 = new FileOutputStream(MWUnits);
		out2.output(doc3, os2);
		System.out.println("Results are in the file: " + filename);
	}
	/**
	 * @param args
	 * @throws IOException 
	 * @throws JDOMException 
	 */
	public static void main(String[] args) throws IOException, JDOMException {
		
		// die Input-Datei
		String file = "E:\\Promotion\\Lemmatizer\\Lux\\loeschen3.xml";
		
		// die Output-Datei - das Korpus
		String OutPut = "E:\\Promotion\\Lemmatizer\\Lux\\mrcorpus.xml";
		
		// Die gefundenen Phraseologismen werden im Korpus markiert, sie kommen aus
		// einer separaten Datei
		String filename = "E:\\tustep_daten\\lexicolux\\luxtexte\\BiGramms1.txt";
		
		// Damit der Text schneller durchsucht wird, wird auch eine Index-Datei 
		// eingebunden
		String index = "E:\\Promotion\\Lemmatizer\\Lux\\loeschen3.xml";
		
		// Es wird zuletzt auch eine Tabelle, die die Phraseologismen darstellt, 
		// exportiert. Diese Tabelle enth�lt die Verweise auf die Fundstellen
		// im Korpus
		String mWUnits = "E:\\programme\\xampp\\htdocs\\studenten\\web\\daten\\zeilen.xml";
		
		IndexedConcordancerXML icxml = new IndexedConcordancerXML(file);
		icxml.loadTheFile(filename, "Renert/Renert");
		icxml.writeTheResult(OutPut, mWUnits);
	}

}
