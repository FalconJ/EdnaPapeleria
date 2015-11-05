/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import database.DB_Conn;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author User
 */
public class addSubCategory extends HttpServlet{
 
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet addSubCategory</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet addSubCategory at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException{
        PrintWriter out = response.getWriter();
        
        String subCategoryName = request.getParameter("subCategoryName");
        String categoryName = request.getParameter("categoryName");
    
        String message = addSubCategory(subCategoryName, categoryName);
        out.print(message);
    }
    
    private String addSubCategory(String subCategoryName, String categoryName){
        String message;
        
        if(subCategoryName.equals("")){
            message = "Please enter a subcategory";
        }
        else{
            try{
                DB_Conn conn = new DB_Conn();
                Connection con = conn.getConnection();
                
                String newSubCategory = "INSERT INTO 'Papeleria'.'sub_category' " +
                                        "('subcategory_id', 'subcategory_name', 'category_name') " +
                                        "VALUES(NULL, '" + subCategoryName + "', '" + categoryName + "');";
            
                Statement st = con.createStatement();
                int i = st.executeUpdate(newSubCategory);
                
                if(i == 1){
                    message = subCategoryName + " subcategory added";
                }
                else{
                    message = "new subcategory failed";
                }
                
                st.close();
                con.close();
            }
            catch(SQLIntegrityConstraintViolationException ex){
                message = "subcategory already exists";
            }
            catch(SQLSyntaxErrorException ex){
                message = "Error in the query";
            }
            catch(SQLException ex){
                message = "there was a problem in the connection";
            }
            catch(ClassNotFoundException ex){
                message = "System cant find the class";
            }
        }
        return message;
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getServletInfo(){
        return "Short description";
    }
}
