/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import database.DB_Conn;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author User
 */
public class administrator {
    private Connection conn;
    private String adminEmail = null;
    private ArrayList<String> listOfAdmin = new ArrayList<>();
    
    public ArrayList<String> getListOfAdmin() throws SQLException, ClassNotFoundException{
        conn = new DB_Conn().getConnection();
        
        String getAdminEmail = "SELECT * FROM 'user'";
        Statement st = conn.createStatement();
        
        ResultSet execQuery = st.executeQuery(getAdminEmail);
        listOfAdmin.clear();
        
        while(execQuery.next()){
            listOfAdmin.add(execQuery.getString("email"));
        }
        
        return listOfAdmin;
    }
    
    public String getAdminEmail(){
        return adminEmail;
    }
    
    public void setAdminEmail(String adminEmail){
        this.adminEmail = adminEmail;
    }
}
