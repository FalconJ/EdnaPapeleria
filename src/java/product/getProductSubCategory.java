/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import database.DB_Conn;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author User
 */
public class getProductSubCategory extends HttpServlet{

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet getProductSubCategory</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet getProductSubCategory at " + request.getContextPath() + "</h1>");
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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
    
        try{
            String category = request.getParameter("category");
            
            Connection con = new DB_Conn().getConnection();
            Statement st = con.createStatement();
            
            String getSubCategory = "SELECT * " +
                                    "FROM sub_category " +
                                    "WHERE category_name = '" + category + "' ;"; 
            
            ResultSet execQuery = st.executeQuery(getSubCategory);
            
            while(execQuery.next()){
                String subCategory = execQuery.getString("sub_category_name");
                out.print("<option value='" + subCategory + "'>" + subCategory + "</option>");
            }
        }
        catch(SQLException ex){
            out.println("SQL Exception: " + ex);
            Logger.getLogger(getProductSubCategory.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(ClassNotFoundException ex){
            out.println("Class not found");
            Logger.getLogger(getProductSubCategory.class.getName()).log(Level.SEVERE, null, ex);
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
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        processRequest(request, response);
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
