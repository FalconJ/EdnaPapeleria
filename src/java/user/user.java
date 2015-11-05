/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import database.DB_Conn;
import java.sql.*;

/**
 *
 * @author User
 */
public class user {
    private String userEmail = null;
    private String username;
    private String address;
    private String gender;
    private String userImage;
    private String mobileNum;
    private int userId = 0;
    private Connection conn;
    
    public void setUserEmail(String userEmail) throws SQLException, ClassNotFoundException{
        this.userId = findUserId(userEmail);
        boolean getAllValues = fetchAllValues(getUserId());
        this.userEmail = userEmail;
    }
    
    public String getUserEmail(){
        return userEmail;
    }
    
    public String getUsername(){
        return username;
    }
    
    public String getAdress(){
        return address;
    }
    
    public String getGender(){
        return gender;
    }
    
    public String getMobileNum(){
        return mobileNum;
    }
    
    public String getUserImage(){
        return userImage;
    }
    
    public int getUserId(){
        return userId;
    }
    
    public int findUserId(String email) throws SQLException, ClassNotFoundException{
        int usId;
        String getUserId = "Select 'user_id' FROM 'user' WHERE 'email' = ?";
        conn = new DB_Conn().getConnection();
        
        PreparedStatement psmt = conn.prepareStatement(getUserId);
        psmt.setString(1, email);
        
        ResultSet execQuery = psmt.executeQuery();
        execQuery.next();
        
        usId = execQuery.getInt("user_id");
        return usId;
    }
    
    public boolean fetchAllValues(int userId)throws SQLException, ClassNotFoundException{
        String fetchSQL = "Select * FROM 'user-details' WHERE 'user_id' = ?";
        
        conn = new DB_Conn().getConnection();
        
        PreparedStatement psmt = conn.prepareStatement(fetchSQL);
        psmt.setInt(1, userId);
        
        ResultSet execQuery = psmt.executeQuery();
        
        boolean next = execQuery.next();
        
        if(next){
            username = execQuery.getString("username");
            address = execQuery.getString("address");
            gender = execQuery.getString("gender");
            userImage = execQuery.getString("userImage");
            mobileNum = execQuery.getString("mobile_num");
            
            return true;
        }
        else{
            username = null;
            address = null;
            gender = null;
            userImage = null;
            mobileNum = null;
            return false;
        }
    }
}
