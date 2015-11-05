/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cart;

import java.sql.*;
import java.util.*;
import database.DB_Conn;

/**
 *
 * @author User
 */
public class cart {

    public ArrayList<Integer> id;
    public ArrayList<String> productCategory;
    public ArrayList<String> productName;
    public ArrayList<Double> prices;
    public ArrayList<Integer> qty;
    public ArrayList<String> res;
    
    Connection conn;

    public cart() {
        this.id = new ArrayList();
        this.productCategory = new ArrayList();
        this.productName = new ArrayList();
        this.prices = new ArrayList();
        this.qty = new ArrayList();
        this.res = new ArrayList();
    }
    
    public ArrayList<String> showProducts(){
        for(int i=0; i<this.id.size(); i++){
            System.out.println(qty.get(i) + " " + id.get(i));
            res.add(qty.get(i) + ":" + id.get(i));
        }
        return res;
    }
    
    public void listItemsOfCart() throws SQLException, ClassNotFoundException{
        conn = new DB_Conn().getConnection();
        Statement st = conn.createStatement();
        
        for(int i=0; i<id.size(); i++){
            Integer getItemId = id.get(i);
            String getItemName = "SELECT 'product-name', 'category-name', 'price' FROM 'products' WHERE 'products_id' =" + getItemId + "";
        
            ResultSet rs = st.executeQuery(getItemName);
            rs.next();
            
            String pName = rs.getString("product-name");
            Double pPrice = rs.getDouble("price");
            String pCategory = rs.getString("category-name");
        
            productName.add(pName);
            productCategory.add(pCategory);
            prices.add(pPrice);
        }
    }
    
    public ArrayList<String> getProductCategory(){
        return productCategory;
    }
    
    public ArrayList<String> getProductName(){
        return productName;
    }
    
    public ArrayList<Double> getPrices() throws SQLException, ClassNotFoundException{
        listItemsOfCart();
        
        return prices;
    }
    
    public String getProductName(int id) throws SQLException, ClassNotFoundException{
        conn = new DB_Conn().getConnection();
        
        String getName = "SELECT 'product-name' FROM 'products' WHERE 'product-id' =" + id + "";
        Statement st = conn.createStatement();
        
        ResultSet rs = st.executeQuery(getName);
        rs.next();
        
        String name = rs.getString("product-name");
        return name;
    }
    
    public String getProductCategory(int id) throws SQLException, ClassNotFoundException{
        conn = new DB_Conn().getConnection();
        
        String getCategory = "SELECT 'category-name' FROM 'products' WHERE 'product-id' =" + id + "";
        Statement st = conn.createStatement();
        
        ResultSet rs = st.executeQuery(getCategory);
        rs.next();
        
        String category = rs.getString("category-name");
        return category;
    }
    
    public double getProductPrice(int id) throws SQLException, ClassNotFoundException{
        conn = new DB_Conn().getConnection();
        
        String getPrice = "SELECT 'price' FROM 'products' WHERE 'products-id' =" + id + "";
        Statement st = conn.createStatement();
        
        ResultSet rs = st.executeQuery(getPrice);
        rs.next();
        
        double price = rs.getDouble("price");
        return price;
    }
    
    public double getTotalPriceOfCart() throws SQLException, ClassNotFoundException{
        //Step 1: GET THE "ids" inside the arraylist, 
        //Step 2:  find the corresponding matching price
        //Step 3:  multiply with the "qty"
        // ids (Product name) => retrieve price (Store it) => Multiply by *qty

        ArrayList<Double> tPrices = new ArrayList();
        
        conn = new DB_Conn().getConnection();
        Statement st = conn.createStatement();
        
        for(int i=0; i<id.size(); i++){
            String getPrices = "SELECT 'price' FROM 'products' WHERE 'product_id'=" + id.get(i).toString() + ";";
            
            ResultSet rs = st.executeQuery(getPrices);
            rs.next();
            
            double f = rs.getFloat("price");
            f *= qty.get(i);
            tPrices.add(f);
        }
        
        double sum = 0;
        for(int i=0; i<tPrices.size(); i++){
            sum +=  tPrices.get(i);
        }
        
        return sum;
    }
    
    public ArrayList<Integer> getQty(){
        return qty;
    }
    
    public ArrayList<Integer> getId(){
        return id;
    }
    
    public boolean addProduct(int id) throws SQLException, ClassNotFoundException{
    //Step 1 : check for ids in DATABASE
    //Step 2 : Match the given id with database
    //          Matched -> break and mtches = 1
    //STEP 3 : if arraylist contains the same id Do not insert
    //          increment the qty, else insert into id & qty
  
        boolean added  = false; 
        boolean matches = false;
        
        conn = new DB_Conn().getConnection();
        Statement st = conn.createStatement();
        
        String getValidIds = "SELECT 'product_id' FROM 'products'";
        ResultSet rs = st.executeQuery(getValidIds);
        
        while(rs.next()){
            int idProd = rs.getInt("product_id");
            if(idProd == id){
                matches = true;
                break;
            }  
        }
        
        if(matches == true){
            if(this.id.contains(id)){
                int index = this.id.indexOf(id);
                this.qty.set(index, this.qty.get(index) + 1);
            }
            else{
                this.qty.add(1);
                this.id.add(id);
            }
            added = true;
        }
        
        return added;
    }
    
    public boolean removedProduct(int productId){
        boolean removed = false;
        
        if(this.id.contains(productId)){
            int index = this.id.indexOf(productId);
            int quantity = this.qty.get(index);
            
            //remove
            this.productCategory.remove(index);
            this.productName.remove(index);
            this.prices.remove(index);
            this.qty.remove(index);
            this.id.remove(index);
            
            removed = true;
        }
        
        return removed;
    }
}
