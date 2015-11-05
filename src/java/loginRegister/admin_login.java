/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loginRegister;

import admin.administrator;
import database.DB_Conn;
import helpers.SecureSHA1;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.RequestDispatcher;
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
public class admin_login extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet admin_login</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet admin_login at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    /**
     *Handles HTTP GET method
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
     *Handles HTTP Post method
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String email, password;
        String dbEmail, dbPassword;
        String message = "", messageDetail = "";
        String messageUrl = "/message.jsp";
        boolean isLoggedIn = false;
        HttpSession userSession = request.getSession();
        PrintWriter out = response.getWriter();
        RequestDispatcher dispatchMessage;
        
        email = request.getParameter("email");
        password = request.getParameter("password");
        
        dispatchMessage = request.getServletContext().getRequestDispatcher(messageUrl);
        try{
            password = SecureSHA1.getSHA1(password);
            
            DB_Conn conn = new DB_Conn();
            Connection con = conn.getConnection();
            
            String getUsers = "SELECT * FROM 'administrators'";
            PreparedStatement st = con.prepareStatement(getUsers);
            
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                dbEmail = rs.getString("email");
                dbPassword = rs.getString("password");
            
                if(email.equals(dbEmail)){
                    message = "Email exists in admin account";
                    
                    if(password.equals(dbPassword)){
                        isLoggedIn = true;
                        user User = new user();
                        administrator Administrator = new administrator();
                        
                        User.setUserEmail(email);
                        userSession.setAttribute("user", User);
                        userSession.setAttribute("admin", Administrator);
                        response.sendRedirect(request.getContextPath());
                    }
                    else{
                        message = "Wrong password";
                        messageDetail = "Password does not match";
                        break;
                    }
                }
                else{
                    message = "Not an admin account";
                    messageDetail = "You are not currently logged in as an admin";
                }
            }
            
            if(!isLoggedIn){
                request.setAttribute("message", message);
                request.setAttribute("messageDetail", messageDetail);
                dispatchMessage.forward(request, response);
            }
        }
        catch(NoSuchAlgorithmException | SQLException | ClassNotFoundException | IOException | ServletException e){
            message = "Error in login";
            messageDetail = "Error: " + e.getMessage();
            
            request.setAttribute("message", message);
            request.setAttribute("messageDetail", messageDetail);
            //dispatchMessage.forward(request, response);
        }
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getServletInfo(){
        return "Short Description";
    }
}
