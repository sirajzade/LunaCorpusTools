/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package XMLDatabase;

import java.util.HashMap;

/**
 *
 * @author sirajzad
 */
public class Benutzer {

    private HashMap<String, String> users;
    private String currentUser; 
    private boolean eingelogged;
    private ExistDatabase database;
    private String URI;
    private String ordner;

    public Benutzer() {
        users = new HashMap<String, String>();
        eingelogged = false;
        users.put("amaru", "amaru");
        users.put("britta", "britta");
        users.put("josh", "josh");
        users.put("peter", "peter");
        users.put("katja", "rothe");
        users.put("tania", "tania");
        users.put("vicky", "vicky");
        users.put("daniela", "daniela");
    }

    public String getCurrentUser(){
        return currentUser;
    }
    
    private void initializeDatabase(String URI) throws ClassNotFoundException {
        database = new ExistDatabase(URI);
        
    }

    public void ausloggen() {
        database = null;
        eingelogged = false;
    }

    public ExistDatabase getDatabase() {
        if (eingelogged) {
            return database;
        } else {
            return null;
        }
    }

    /**
     * Diese Methode ist für das einloggen zuständig
     * @param name
     * @param pass
     * @return
     * @throws ClassNotFoundException
     * @throws InstantiationException 
     */
    
     public String einloggen(String name, String pass) throws ClassNotFoundException, InstantiationException {
          String retvall = "Der Benutzername existiert nicht!";
          String URI = "xmldb:exist://cw-mullercl-1.uni.lux:8080/exist/xmlrpc";
          String engelmann = "xmldb:exist://engelmann.uni.lu:8899/exist/xmlrpc";
          String engelmannIP = "xmldb:exist://10.244.2.18:8899/exist/xmlrpc";
          String passWord = users.get(name);
          // Wenn der Name des Benutzers in der Liste ist
          if (passWord != null) {
               // Wenn das Passwortstimmt
               if (passWord.equals(pass)) {
                    eingelogged = true;
                    currentUser = name;
                    initializeDatabase(engelmann);
                    retvall = "Sie sind erfolgreich eingeloggt!";
               }    else {
                    retvall = "Das Passwort ist falsch!";
               }
          } else if (name.equals("test") && pass.equals("test")) {
                    eingelogged = true;
                    currentUser = name;
                    initializeDatabase(engelmannIP);
                    retvall = "Sie sind auf einer Testdatenbank eingeloggt!";
          }
          return retvall;
     }

    public boolean isLogged() {
        return eingelogged;
    }
}
