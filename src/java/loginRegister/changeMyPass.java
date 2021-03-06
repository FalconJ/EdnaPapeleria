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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import user.user;

/**
 *
 * @author User
 */
public class changeMyPass extends HttpServlet{
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet changeMyPass</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet changeMyPass at " + request.getContextPath() + "</h1>");
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
            throws ServletException, IOException {
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
        
        try{
            String email = request.getParameter("email");
            String password = request.getParameter("password");

            if(email == null){
                HttpSession session = request.getSession();
                user User = (user)session.getAttribute("user");
                email = User.getUserEmail();
            }

            String changePassword = "UPDATE 'Papeleria'.'user'" +
                                    "SET 'password' = SHA1('" + password + "')" +
                                     "WHERE 'user'.'email' = '" + email + "');";
            DB_Conn conn = new DB_Conn();
            Connection con = conn.getConnection();

            Statement psmt =  con.createStatement();
            int i = psmt.executeUpdate(changePassword);
            
            PrintWriter out = response.getWriter();
            
            if(i == 1){
                out.println("Password Updated, login again");
            }
            else{
                out.println("Error in updating password, try again");
            }
            response.sendRedirect(request.getContextPath() + "/userinfo.jsp");
        }
        catch(SQLException | ClassNotFoundException ex){
            Logger.getLogger(changeMyPassword.class.getName()).log(Level.SEVERE, null, ex);
            PrintWriter out = response.getWriter();
            out.println("Error: " + ex);
            response.sendError(404);
        }
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
