/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loginRegister;

import database.DB_Conn;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import helpers.*;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import user.user;

/**
 *
 * @author User
 */
public class loginServlet extends HttpServlet{
        protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet loginServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet loginServlet at " + request.getContextPath() + "</h1>");
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
        doPost(request, response);
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
        String email, password;
        String dbEmail, dbPassword;
        String message = "", messageDetail = "";
        
        boolean isLoggedIn = false;
        HttpSession userSession = request.getSession();
        PrintWriter out = response.getWriter();
        email = request.getParameter("email");
        password = request.getParameter("password");
    
        String messageUrl = "/message.jsp";
        RequestDispatcher dispatchMessage = request.getServletContext().getRequestDispatcher(messageUrl);
    
        try{
            password = SecureSHA1.getSHA1(password);
            
            DB_Conn conn = new DB_Conn();
            Connection con = conn.getConnection();
            
            String getUsers = "SELECT 'email', 'passwords' FROM 'users';";
            PreparedStatement st = con.prepareStatement(getUsers);
            
            ResultSet rs = st.executeQuery();
            
            while(rs.next()){
                dbEmail = rs.getString("email");
                dbPassword = rs.getString("password");
                
                if(email.equals(dbEmail)){
                    if(password.equals(dbPassword)){
                        isLoggedIn = true;
                        out.println("You are logged in");
                        
                        user User = new user();
                        User.setUserEmail(email);
                        
                        userSession.setAttribute("user", User);
                        response.sendRedirect(request.getContextPath() + "/index.jsp");
                    }
                    else{
                        message = "Wrong password";
                        messageDetail = "Password entered is not correct";
                        out.println("Wrong password entered");
                        break;
                    }
                }
                else{
                    out.println("Email is not registered");
                    message = "Email is not registered";
                    messageDetail = "Email address not registered";
                }
            }
            
            if(!isLoggedIn){
                request.setAttribute("message", message);
                request.setAttribute("messageDetail", messageDetail);
                dispatchMessage.forward(request, response);
            }
        }
        catch(SQLException ex){
            out.println("Problem in the process");
            message = "An error ocurred";
            messageDetail = "Error" + ex;
            
            request.setAttribute("message", message);
            request.setAttribute("messageDetail", messageDetail);
        }
        catch(NoSuchAlgorithmException | ClassNotFoundException | IOException | ServletException ex){
            out.println("Problem in the process");
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
