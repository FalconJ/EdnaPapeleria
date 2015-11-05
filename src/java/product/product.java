/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import database.DB_Conn;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author User
 */
public class product {
    private int productId;
    private ArrayList<String> category;
    private ArrayList<String> subCategory;
    private ArrayList<String> company;
    private Connection con;
    
    public product(){
        category = new ArrayList<>();
        subCategory = new ArrayList<>();
        company = new ArrayList<>();
    }
    
    public int getId(String productName)
            throws SQLException, ClassNotFoundException{
        DB_Conn conn = new DB_Conn();
        con = conn.getConnection();
        
        int id;
        String idSQL = "SELECT product_id " +
                       "FROM products " +
                       "WHERE product_name = '" + productName + "' ;";
    
        Statement st = con.createStatement();
        ResultSet execQuery = st.executeQuery(idSQL);
        execQuery.next();
        
        id = execQuery.getInt("product_id");
        return id;
    }
    
    public ArrayList<String> getCategory()
            throws SQLException,ClassNotFoundException{
        DB_Conn conn = new DB_Conn();
        con = conn.getConnection();
        
        String getCategory = "SELECT category_name " +
                             "FROM category; ";
        
        PreparedStatement psmt = con.prepareStatement(getCategory);
        ResultSet execQuery = psmt.executeQuery();
        
        while(execQuery.next()){
            String cat = execQuery.getString("category_name");
            this.category.add(cat);
        }
        
        return category;
    }
    
    public ArrayList<String> getSubCategory()
            throws SQLException, ClassNotFoundException{
        DB_Conn conn = new DB_Conn();
        con = conn.getConnection();
        
        String getSubCategory = "Select sub_category_name " +
                               "FROM sub_category; ";
       
        PreparedStatement psmt = con.prepareStatement(getSubCategory);
        ResultSet execQuery = psmt.executeQuery();
        
        while(execQuery.next()){
            String subCat = execQuery.getString("sub_category_name");
            this.subCategory.add(subCat);
        }
        
        return subCategory;
    }
    
    public ArrayList<String> getCompany()
            throws SQLException, ClassNotFoundException{
        DB_Conn conn = new DB_Conn();
        con = conn.getConnection();
        
        String getCategory = "SELECT company_name " +
                             "FROM products " +
                             " GROUP BY company_name ;";
        
        PreparedStatement psmt = con.prepareStatement(getCategory);
        ResultSet execQuery = psmt.executeQuery();
        
        while(execQuery.next()){
            String company_name = execQuery.getString("company_name");
            this.company.add(company_name);
        }
        
        return company;
    }
}
