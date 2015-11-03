/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin;

import database.DB_Conn;
import helpers.EmailValidator;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLIntegrityConstraintViolationException;
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
public class addAnAdministrator extends HttpServlet{
    /*
        Processes requests for both HTTP
        GET and POST methods
    */
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException{
        response.setContentType("text/html;charset=UTF-8");
        try(PrintWriter out = response.getWriter()) {
            out.println("<html>");
            out.println("<head>");
            out.println("<title> Servlet addAnAdministrator</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet addAnAdministrator at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }   
    }
    
    /**
     *  Handles POST method
     * 
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String message = null;
        String messageDetail = null;
        String email, password, passwordConfirm;
        boolean isRegistered = false;
        boolean isEmailValid = false;
        String messageUrl = "/message.jsp";
        EmailValidator validator = new EmailValidator();
        
        RequestDispatcher dispatchMessage = request.getServletContext().getRequestDispatcher(messageUrl);
        
        email = request.getParameter("email");
        password = request.getParameter("password");
        passwordConfirm = request.getParameter("passwordConfirm");
        isEmailValid = validator.validate(email);
    
        PrintWriter out = response.getWriter();
        HttpSession userSession = request.getSession();
    
        try{
            DB_Conn connection = new DB_Conn();
            Connection con = connection.getConnection();
        
            if(email != null){
                if(isEmailValid){
                    if(password.length() > 5 && password.equals(passwordConfirm)){
                        String newUser = "INSERT INTO 'Papeleria'.'Administradores'" +
                                         "('admin_id', 'email', 'password')" +
                                         "VALUES(NULL, ?, SHA1(?));";
                        
                        PreparedStatement psmt = con.prepareStatement(newUser);
                        psmt.setString(1, email);
                        psmt.setString(2, password);
                        
                        int i = psmt.executeUpdate();
                        
                        if(i == 1){
                            isRegistered = true;
                            out.println("Welcome, you are registered");
                            user User = new user();
                            
                            User.setUserEmail(email);
                            userSession.setAttribute("user", User);
                            response.sendRedirect("/Papeleria/admin_settings.jsp");
                        }
                        else{
                            isRegistered = false;
                            out.println("Something went wrong, try again.");
                        }
                    }
                    else{
                        isRegistered = false;
                        message = "Password length is than 5 characters or doesnt match";
                        messageDetail = "Please provide a correct password";
                        out.println("Registration failed: Password not valid");
                    }
                }
                else{
                    isRegistered = false;
                    message = "Please provide a valid email address";
                    messageDetail = "Not a valid email";
                    out.println("Registration failed: Email not valid");
                }
                
                if (isRegistered == false) {
                    request.setAttribute("message", message);
                    request.setAttribute("messageDetail", messageDetail);
                    dispatchMessage.forward(request, response);
                }
            }
        }
        catch(SQLIntegrityConstraintViolationException ex){
        // user exists but wrong passwotd ask to CHANGE THE PASSWORD
            messageDetail = ex.getMessage();
            message = "User registered previously, try again with your password";
            out.println("Failed: " + ex);
            
            request.setAttribute("message", message);
            request.setAttribute("messageDetail", messageDetail);
            
            dispatchMessage.forward(request, response);
        }
        catch(Exception ex){
            messageDetail = ex.getMessage();
            message = "There was a problem registering";
            out.println("Failed: " + ex);
            
            request.setAttribute("message", message);
            request.setAttribute("messageDetail", messageDetail);
            dispatchMessage.forward(request, response);
            response.sendError(404);
        }
    }

    /**
     * Handles the HTTP GET method
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
    
    @Override
    public String getServletInfo(){
        return "Short Description";
    }
}
