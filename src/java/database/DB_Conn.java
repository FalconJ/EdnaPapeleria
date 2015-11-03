/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

/**
 *
 * @author User
 */

import java.sql.*;

public class DB_Conn {
    private String database = "Papeleria";
    private String username = "admin";
    private String password = "1234";
    private final String url = "jdbc:sqlserver://localhost\\SQLEXPRESS2012:1433;databaseName=" + database + "";
    private Connection connection;
    
    /**
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public Connection getConnection() throws SQLException, ClassNotFoundException{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        
        connection = DriverManager.getConnection(url, username, password);
        
        return connection;
    }
    
    public String getDatabase(){
        return database;
    }
    
    public void setDatabase(String database){
        this.database = database;
    }
    
    public String getUsername(){
        return username;
    }
    
    public void setUsername(String username){
        this.username = username;
    }
    
    public String getPassword(){
        return password;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
}
