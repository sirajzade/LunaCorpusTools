/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lunacorpustools;

/**
 *
 * @author joshgun.sirajzade
 */
public class ListItemDatabase {
    String value;
    String id;

    public ListItemDatabase (String fileName, String fileID) {
        value = fileName;
        id = fileID;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public String toString(){
        return value;
    }
    
    
}
