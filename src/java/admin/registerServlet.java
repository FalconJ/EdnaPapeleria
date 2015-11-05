/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import database.DB_Conn;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.RequestDispatcher;
import helpers.*;
import java.sql.*;
import user.user;

/**
 *
 * @author User
 */
public class registerServlet extends HttpServlet{
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet registerServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet registerServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String message = null;
        String messageDetail = null;
        String email, password, passwordConfirm;
        boolean isRegistered;
        String messageUrl = "/message.jsp";
        EmailValidator validator = new EmailValidator();
        boolean isEmailValid;
        
        RequestDispatcher dispatchMessage = request.getServletContext().getRequestDispatcher(messageUrl);
    
        email = request.getParameter("email");
        password = request.getParameter("password");
        passwordConfirm = request.getParameter("passwordConfirm");
        isEmailValid = validator.validate(email);
        
        String ipAdd = request.getRemoteAddr();
        
        PrintWriter out = response.getWriter();
        
        HttpSession userSession = request.getSession();
        
        try{
            DB_Conn conn = new DB_Conn();
            Connection con = conn.getConnection();
            
            if(email != null){
                if(isEmailValid){
                    if(password.length() > 5 && password.equals(passwordConfirm)){
                        String newUser = "INSERT INTO 'Papeleria'.'user' " +
                                         "('user_id', 'email', 'pass', 'registeredOn') " +
                                         "VALUES(NULL, ?, SHA1(?), NOW());";
                    
                        PreparedStatement psmt = con.prepareStatement(newUser);
                        
                        psmt.setString(1, email);
                        psmt.setString(2, password);
                        
                        int i = psmt.executeUpdate();
                        
                        if(i == 1){
                            isRegistered = true;
                            out.println("Welcome, you are now registered");
                            user User = new user();
                            
                            User.setUserEmail(email);
                            userSession.setAttribute("user", User);
                            response.sendRedirect("/Papeleria/index.jsp");
                        }
                        else{
                            isRegistered = false;
                            out.println("You are not registered");
                        }
                    }
                    else{
                        isRegistered = false;
                        message = "Password not correct";
                        messageDetail = "Please provide a correct password over five characters and that they match";
                        out.println("Registration failed: password not correct");
                    }
                }
                else{
                    isRegistered = false;
                    message = "Email address not valid";
                    messageDetail = "Provide a valid email address";
                    out.println("Registration failed: Email not valid");
                }
            }
            else{
                isRegistered = false;
                message = "Enter an email address";
                messageDetail = "Please provide a valid email address";   
            }
            
            if(isRegistered == false){
                request.setAttribute("message", message);
                request.setAttribute("messageDetail", messageDetail);
                
                dispatchMessage.forward(request, response);
            }
        }
        catch(SQLIntegrityConstraintViolationException ex){
            messageDetail = ex.getMessage();
            message = "You have registered previously, use your password";
            out.print("Failed: " + ex);
            
            request.setAttribute("message", message);
            request.setAttribute("messageDetail", messageDetail);
            
            dispatchMessage.forward(request, response);
        }
        catch(SQLException | ClassNotFoundException | IOException | ServletException ex){
            messageDetail = ex.getMessage();
            message = "There was a problem with your registration, try again";
            
            request.setAttribute("message", message);
            request.setAttribute("messageDetail", messageDetail);
            
            dispatchMessage.forward(request, response);
        }
    }
    
    /**
     *  Return a short description of the servlet
     * 
     * @return
     */
    @Override
    public String getServletInfo(){
        return "Short description";
    }
}
