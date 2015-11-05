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
import java.sql.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpSession;
import user.user;

/**
 *
 * @author User
 */
public class registerServlet extends HttpServlet{

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
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
        String message = null, messageDetail = null;
        String email, password, passwordConfirm;
        boolean isRegistered = false;
        
        String messageUrl = "/message.jsp";
        RequestDispatcher dispatchMessage = request.getServletContext().getRequestDispatcher(messageUrl);
    
        email = request.getParameter("emailReg");
        password = request.getParameter("passwordReg");
        passwordConfirm = request.getParameter("passwordConfirm");
        
        EmailValidator validator = new EmailValidator();
        boolean isEmailValid = validator.validate(email);
        
        String ipAdd = request.getRemoteAddr();
        PrintWriter out = response.getWriter();
        HttpSession userSession = request.getSession();
        
        try{
            DB_Conn conn = new  DB_Conn();
            Connection con = conn.getConnection();
            
            if(email != null && isEmailValid){
                if(password.length() > 5 && password.equals(passwordConfirm)){
                    String newUser= "INSERT INTO 'Papeleria'.'user' " +
                             "('user_id', 'email', 'password', 'registeredOn')" +
                             "VALUES (NULL, ?, SHA1(?), NOW());";
                
                    PreparedStatement psmt = con.prepareStatement(newUser);
                    psmt.setString(1, email);
                    psmt.setString(2, password);
                    
                    int i = psmt.executeUpdate();
                    
                    if(i == 1){
                        isRegistered = true;
                        out.println("You are registered");
                        
                        user User = new user();
                        User.setUserEmail(email);
                        
                        userSession.setAttribute("user", User);
                        response.sendRedirect(request.getContextPath());
                    }
                    else{
                        out.println("Something ent wrong");
                    }
                }
                else{
                    message = "Passwords do not match or is less than 5 characters";
                    messageDetail = "Write a valid password";
                    out.println("Registration failed");
                }
            }
            else{
                message = "Email is not valid";
                messageDetail = "Enter a valid email";
                out.println("Registration failed");
            }
            
            if(!isRegistered){
                request.setAttribute("message", message);
                request.setAttribute("messageDetail", messageDetail);
                
                dispatchMessage.forward(request, response);
            }
        }
        catch(SQLIntegrityConstraintViolationException ex){
            message = "Error: ";
            messageDetail = ex.getMessage();
            out.println("Exception: " + ex.getMessage());
            
            request.setAttribute("message", message);
            request.setAttribute("messageDetail", messageDetail);
            dispatchMessage.forward(request, response);
        }
        catch(SQLException | ClassNotFoundException | IOException | ServletException ex){
            message = "Error: ";
            messageDetail = ex.getMessage();
            out.println("Exception: " + ex.getMessage());
            request.setAttribute("message", message);
            request.setAttribute("messageDetail", messageDetail);
            dispatchMessage.forward(request, response);
            response.sendError(404);
        }
    }
}
