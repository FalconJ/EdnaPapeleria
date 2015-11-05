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
public class addCategory extends HttpServlet{
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet addCategory</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet addCategory at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        processRequest(request, response);
    }
    
    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String categoryName = request.getParameter("categoryName");
        PrintWriter out = response.getWriter();
        
        String message = addCategory(categoryName);
        out.println(message);
    }
    
    private String addCategory(String categoryName){
        String message;
        
        if(categoryName.equals("")){
            message = "Please enter a category name";
        }
        else{
            try{
                DB_Conn conn = new DB_Conn();
                Connection con = conn.getConnection();
                
                String newCategory = "INSERT INTO 'Papeleria'.'category'" +
                                     "('category_id', 'category_name') VALUES " +
                                     "(NULL, '" + categoryName + "');";
                Statement st = con.createStatement();
                
                int rows = st.executeUpdate(newCategory);
                
                if(rows == 1){
                    message = categoryName + " category inserted";
                }
                else{
                    message = "category insertion failed";
                }
                
                st.close();
                con.close();
            }
            catch(SQLSyntaxErrorException ex){
                message = "Error" + ex.getMessage();
            }
            catch(SQLException | ClassNotFoundException ex){
                message = "Error" + ex.getMessage();
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
