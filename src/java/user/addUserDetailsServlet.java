/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package user;

import database.DB_Conn;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author User
 */
@WebServlet(name = "addUserDetalsServlet", urlPatterns = {"/addUserDetalsServlet"})
public class addUserDetailsServlet extends HttpServlet{
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet addUserDetalsServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet addUserDetalsServlet at " + request.getContextPath() + "</h1>");
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
        //processRequest(request, response);
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
        Connection con;
        String username, age, gender, address, mobileNum;
        
        username    = request.getParameter("username");
        age         = request.getParameter("age");
        gender      = request.getParameter("gender");
        address     = request.getParameter("address");
        mobileNum   = request.getParameter("mobileNum");
        
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        user User;
        HttpSession session = request.getSession();
        User = (user) session.getAttribute("user");
    
        if(User != null){
            try{
                String addresses = User.getAdress();
                con = new DB_Conn().getConnection();

                if(username.trim().length() > 1){
                    if(mobileNum.trim().length() <= 12 && mobileNum.trim().length() >= 3){
                        if(address.trim().length() > 5){
                            if(addresses == null){
                                String userDetails = "INSERT INTO Papeleria.user_details ( " +
                                                    "userDetail_id, " +
                                                    "user_id, " +
                                                    "username, " +
                                                    "address, " +
                                                    "gender, " +
                                                    "mobile_no) " +
                                                    "VALUES( " +
                                                    "NULL ,  ?,  ?,  ?,  ?,  '', ? );";

                                PreparedStatement psmt = con.prepareStatement(userDetails);
                                psmt.setInt(1, User.getUserId());
                                psmt.setString(2, username);
                                psmt.setString(3, address);
                                psmt.setString(4, gender);
                                psmt.setString(5, mobileNum);
                                psmt.executeUpdate();

                                out.println("Details inserted");
                                User.setUserEmail(User.getUserEmail());
                            }
                            else{
                                String newDetails = "UPDATE user_details " +
                                                    "SET " +
                                                    "username = ? " +
                                                    "address = ? " +
                                                    "gender = ? " +
                                                    "mobile_no = ? " +
                                                    "WHERE user_details.user_id = ?; ";

                                PreparedStatement psmt = con.prepareStatement(newDetails);
                                psmt.setInt(5, User.getUserId());
                                psmt.setString(1, username);
                                psmt.setString(2, address);
                                psmt.setString(3, gender);
                                psmt.setString(4, mobileNum);
                                psmt.executeUpdate();

                                out.println("Detail updated");
                                User.setUserEmail(User.getUserEmail());
                            }
                            out.println ("<a href='userinfo.jsp'>User Info</a>");
                        }
                        else{
                            response.sendRedirect(request.getContextPath()+"/userinfo.jsp");
                        }
                    }
                    else{
                        response.sendRedirect(request.getContextPath()+"/userinfo.jsp");
                    }
                }
                response.sendRedirect(request.getContextPath()+"/userinfo.jsp");
            }
            catch(NumberFormatException | SQLException | ClassNotFoundException ex){
                Logger.getLogger(addUserDetailsServlet.class.getName()).log(Level.SEVERE, null, ex);
                out.println(ex); 
                response.sendRedirect(request.getContextPath()+"/userinfo.jsp");
            }
        }
        else{
            response.sendRedirect(request.getContextPath());
        }
    }
    
    /**
     *
     * @return
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
