/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import database.DB_Conn;
import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author User
 */
public class addCompany extends HttpServlet{
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet addCompany</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet addCompany at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        processRequest(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String companyName = request.getParameter("companyName");
        PrintWriter out = response.getWriter();
        
        String message = addCompany(companyName);
        out.println(message);
    }
    
    private String addCompany(String companyName){
        String message;
        
        if(companyName.equals("")){
            message = "Please enter a category name";
        }
        else{
            try{
                DB_Conn conn = new DB_Conn();
                Connection con;
                con = conn.getConnection();
                
                String newCategory = "INSERT INTO 'Papeleria'.'product-company'" +
                                     "('company_id', 'company_name') VALUES " +
                                     "(NULL, '" + companyName + "');";
                Statement st = con.createStatement();
                
                int rows = st.executeUpdate(newCategory);
                
                if(rows == 1){
                    message = companyName + " company inserted";
                }
                else{
                    message = "company insertion failed";
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
}
