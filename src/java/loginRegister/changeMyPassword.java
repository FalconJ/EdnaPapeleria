/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loginRegister;

import database.DB_Conn;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
public class changeMyPassword extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet changeMyPassword</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet changeMyPassword at " + request.getContextPath() + "</h1>");
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
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        try{
            String email = request.getParameter("email");
            String password = request.getParameter("passowrd");
            
            String changePassword = "UPDATE 'Papeleria'.'user' SET 'password' = SHA1(?) WHERE 'user'.'email' = ?";
            
            DB_Conn conn = new DB_Conn();
            Connection con = conn.getConnection();
            
            PreparedStatement psmt = con.prepareStatement(changePassword);
            psmt.setString(1, password);
            psmt.setString(2, email);
            
            int i = psmt.executeUpdate();
            PrintWriter out = response.getWriter();
            
            if(i==1){
                out.println("Password Updated, login again");
            }
            else{
                out.println("Update failed, try again");
            }
        }
        catch(SQLException | ClassNotFoundException ex){
            Logger.getLogger(changeMyPassword.class.getName()).log(Level.SEVERE, null, ex);
            
            PrintWriter out = response.getWriter();
            out.println("Eror: " + ex);
            
            response.sendError(404);
        }
    }
}
