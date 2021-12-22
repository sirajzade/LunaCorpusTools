/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lunacorpustools;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;


/**
 *
 * @author joshgun.sirajzade
 */
public class zeileGUI extends JPanel{

   
    JLabel firstNormalPart = new JLabel();
    JLabel secondNormalPart = new JLabel();
    JLabel fileName = new JLabel();
    JLabel wordBold = new JLabel();
    JLabel partofspecch = new JLabel();
    JCheckBox myCheckBoxJa = new JCheckBox();
    JCheckBox myCheckBoxNull = new JCheckBox();
    
    String wordID;
    String fileID;

    public zeileGUI(String pos, String first, String word, String second, String fileID, String wortID)  {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        firstNormalPart.setText(first);
        secondNormalPart.setText(second);
        fileName.setText(" (" + fileID +") ");
        wordBold.setText(word);
        wordBold.setName(wortID);
        Font font = wordBold.getFont();
        Font boldFont = new Font(font.getFontName(), Font.BOLD, font.getSize() + 2);
        Font normalFont = new Font(font.getFontName(), Font.PLAIN, font.getSize());

        firstNormalPart.setFont(normalFont);
        secondNormalPart.setFont(normalFont);
        partofspecch.setFont(normalFont);
        fileName.setFont(normalFont);
        wordBold.setFont(boldFont);

        JLabel dummyLeer = new JLabel("           ");
        this.add(dummyLeer);
        this.add(myCheckBoxJa);
        this.add(myCheckBoxNull);

        /*
         JComboBox<String> comboBox = new JComboBox<String>();
         comboBox.addItem("D");
         comboBox.addItem("N");
         comboBox.addItem("V");
         comboBox.addItem("KO");
         comboBox.addItem("ADV");
         comboBox.addItem("$");
         comboBox.addItem("ADJ");
         comboBox.addItem("PTK");
         comboBox.addItem("APPR");
         comboBox.addItem("APPRART");
         comboBox.addItem("P");
         comboBox.addItem("NUM");
         zeile.add(comboBox);
         */
        partofspecch.setText("[" + pos + "]");
        this.add(partofspecch);
        this.add(firstNormalPart);
        this.add(wordBold);
        this.add(secondNormalPart);
        this.add(fileName);
        //   zeile.setBorder(new WindowsBorders.DashedBorder(Color.DARK_GRAY));
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, this.getMinimumSize().height));

        this.addMouseListener(new PopClickListener(partofspecch));
        myCheckBoxJa.setName(word+"L");
        myCheckBoxNull.setName(word+"R");
        this.setName(fileID);
        this.fileID = fileID;
        this.wordID = wortID;
    }

    public void printZeile(JPanel panel) {
        panel.add(this);
        panel.revalidate();
        panel.repaint();
    }
    
    public JLabel getWordBold() {
        return wordBold;
    }

    public void setWordBold(JLabel wordBold) {
        this.wordBold = wordBold;
    }

    public String getPartofspecch() {
        String pos = partofspecch.getText();
        String retvalBefore = pos.replaceAll("\\[", "");
        String retval = retvalBefore.replaceAll("\\]", "");
        return retval;
    }

    public void setPartofspecch(String pos) {
        this.partofspecch.setText(pos);
        this.repaint();
    }

    public JCheckBox getMyCheckBoxJa() {
        return myCheckBoxJa;
    }

    public void setMyCheckBoxJa(JCheckBox myCheckBoxJa) {
        this.myCheckBoxJa = myCheckBoxJa;
    }

    public JCheckBox getMyCheckBoxNull() {
        return myCheckBoxNull;
    }

    public void setMyCheckBoxNull(JCheckBox myCheckBoxNull) {
        this.myCheckBoxNull = myCheckBoxNull;
    }

    public String getWordID() {
        return wordID;
    }

    public void setWordID(String wordID) {
        this.wordID = wordID;
    }

    public String getFileID() {
        return fileID;
    }

    public void setFileID(String fileID) {
        this.fileID = fileID;
    }
   
    
    
    
}
class PopClickListener extends MouseAdapter {

    JLabel label;

    public PopClickListener(JLabel label) {
        this.label = label;
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger()) {
            doPop(e);
        }
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger()) {
            doPop(e);
        }
    }

    private void doPop(MouseEvent e) {
        PopUpDemo menu = new PopUpDemo(label);
        menu.show(e.getComponent(), e.getX(), e.getY());
    }
}

class WortArtMousListener implements MouseListener {

    JLabel label;
    String text;

    public WortArtMousListener(JLabel label, String text) {
        this.label = label;
        this.text = text;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("Hier passiert Clicked");
    }

    @Override
    public void mousePressed(MouseEvent e) {
        label.setText(text);
        label.repaint();
        label.getParent().getParent().repaint();
        label.revalidate();

        System.out.println("Hier passiert");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //    throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //  throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet.");
    }
}

class PopUpDemo extends JPopupMenu {

    JMenuItem anItem1;
    JMenuItem anItem2;
    JMenuItem anItem3;
    JMenuItem anItem4;
    JLabel wortart;

    public PopUpDemo(JLabel wortart) {
        this.anItem1 = new JMenuItem("Basisanalyse");
        this.wortart = wortart;
        this.anItem1.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("do somebody something Clicked!");
                // throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void mousePressed(MouseEvent e) {
                // throw new UnsupportedOperationException("Not supported yet.");
                System.out.println("do somebody something Pressed!");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                // throw new UnsupportedOperationException("Not supported yet.");
                System.out.println("do somebody something Released!");
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // throw new UnsupportedOperationException("Not supported yet.");
                System.out.println("do somebody something Entered!");
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // throw new UnsupportedOperationException("Not supported yet.");
                System.out.println("do somebody something Exited!");
            }
        });
        add(anItem1);
        anItem2 = new JMenuItem("Sortieren");
        add(anItem2);
        anItem2 = new JMenuItem("Drucken");
        add(anItem2);

        JMenu popupWortart = new JMenu("Wortart ändern");

        JMenuItem itemN = new JMenuItem("N");
        itemN.addMouseListener(new WortArtMousListener(wortart, "[N]"));
        popupWortart.add(itemN);

        JMenuItem itemV = new JMenuItem("V");
        itemV.addMouseListener(new WortArtMousListener(wortart, "[V]"));
        popupWortart.add(itemV);

        JMenuItem itemADJ = new JMenuItem("ADJ");
        itemADJ.addMouseListener(new WortArtMousListener(wortart, "[ADJ]"));
        popupWortart.add(itemADJ);

        JMenuItem itemD = new JMenuItem("D");
        itemD.addMouseListener(new WortArtMousListener(wortart, "[D]"));
        popupWortart.add(itemD);

        JMenuItem itemP = new JMenuItem("P");
        itemP.addMouseListener(new WortArtMousListener(wortart, "[P]"));
        popupWortart.add(itemP);

        JMenuItem itemNUM = new JMenuItem("NUM");
        itemNUM.addMouseListener(new WortArtMousListener(wortart, "[NUM]"));
        popupWortart.add(itemNUM);

        JMenuItem itemKO = new JMenuItem("KO");
        itemKO.addMouseListener(new WortArtMousListener(wortart, "[KO]"));
        popupWortart.add(itemKO);

        JMenuItem itemAPPR = new JMenuItem("APPR");
        itemAPPR.addMouseListener(new WortArtMousListener(wortart, "[APPR]"));
        popupWortart.add(itemAPPR);

        JMenuItem itemAPPRAART = new JMenuItem("APPRART");
        itemAPPRAART.addMouseListener(new WortArtMousListener(wortart, "[APPRART]"));
        popupWortart.add(itemAPPRAART);

        JMenuItem itemAV = new JMenuItem("AV");
        itemAV.addMouseListener(new WortArtMousListener(wortart, "[AV]"));
        popupWortart.add(itemAV);

        JMenuItem itemPTK = new JMenuItem("PTK");
        itemPTK.addMouseListener(new WortArtMousListener(wortart, "[PTK]"));
        popupWortart.add(itemPTK);

        JMenuItem item$ = new JMenuItem("$");
        item$.addMouseListener(new WortArtMousListener(wortart, "[$]"));
        popupWortart.add(item$);

        add(popupWortart);

    }
}
