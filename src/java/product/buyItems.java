/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import cart.cart;
import database.DB_Conn;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import user.user;

/**
 *
 * @author User
 */
@WebServlet(name = "buyItems", urlPatterns = {"/buyItems"})
public class buyItems extends HttpServlet{
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet buyItems</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet buyItems at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {
            out.close();
        }
    }
    
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
        PrintWriter out = response.getWriter();
        String name, age, address, mobile;
        int orderId;
        Connection con = null;
        HttpSession session = request.getSession();
        user User;
        cart Cart;
        
        User = (user) session.getAttribute("user");
        Cart = (cart) session.getAttribute("cart");
        
        name = request.getParameter("name");
        age = request.getParameter("age");
        address = request.getParameter("address");
        mobile = request.getParameter("mobile");
        
        if(name.trim().length() > 1 &&
            address.trim().length() >= 5 &&
            mobile.trim().length()  >= 5 &&
            mobile.trim().length() <= 12){
            
            if(!(session.getAttribute("user") == null) &&
               !(session.getAttribute("cart") == null)){
                try{
                    response.setContentType("text/html;charset=UTF-8");
                    con = new DB_Conn().getConnection();
                    
                    con.setAutoCommit(false);
                    String insertOrder = "INSERT INTO 'Papeleria'.'order ( "+
                                         "'order_id', 'user_id', 'status', "+
                                         "'shippers_name', 'address', 'mobile_number' " +
                                         "'shippers_email', 'ordered_on', total_order_prices') " +
                                         "VALUES(NULL, ?, 'pending', ?, ?, ?, ?, NOW(), ?);";
                    PreparedStatement psmt = con.prepareStatement(insertOrder);
                
                    psmt.setInt(1, User.getUserId());
                    psmt.setString(2, name);
                    psmt.setString(3, address);
                    psmt.setString(4, mobile);
                    psmt.setString(5, User.getUserEmail());
                    psmt.setDouble(6, Cart.getTotalPriceOfCart());
                
                    int i = psmt.executeUpdate();
                    
                    if(i == 1){
                        String latestOrderId = "SELECT 'order_id' " +
                                                "FROM 'order' " +
                                                "WHERE 'user_id' = " + User.getUserId() + " " +
                                                "ORDER BY 'ordered_on' DESC; ";
                        
                        psmt.close();
                    
                        Statement st = con.createStatement();
                        ResultSet execQuery = st.executeQuery(latestOrderId);
                        execQuery.next();

                        orderId = execQuery.getInt("order_id");
                        execQuery.close();

                        ArrayList<String> productCategories = Cart.getProductCategory();
                        ArrayList<String> productNames = Cart.getProductName();
                        ArrayList<Double> prices = Cart.getPrices();
                        ArrayList<Integer> qty = Cart.getQty();
                        ArrayList<Integer> id = Cart.getId();

                        String insertToSales = "INSERT INTO 'Papeleria'.'sales' ( " +
                                               "'sales_id', 'order_id', 'product_id', " +
                                               "'product_name', 'product_price', 'product_quantity', " +
                                               "'sold_on', 'user_id') " +
                                               "VALUES(NULL, ?, ?, ?, ?, ?, NOW(), ?);";

                        PreparedStatement psmt2 = con.prepareStatement(insertToSales);

                        for(int j=0; j<productNames.size(); j++){
                            psmt2.setInt(1, orderId);
                            psmt2.setInt(2, id.get(j));
                            psmt2.setString(3, productNames.get(j));
                            psmt2.setDouble(4, prices.get(j));
                            psmt2.setInt(5, qty.get(j));
                            psmt2.setInt(6, User.getUserId());
                            
                            int execUpdate = psmt.executeUpdate();
                            
                            if(execUpdate == 1){
                                out.println("Sales update success");
                            }
                            else{
                                out.println("Sales update failed");
                            }
                        }
                        
                        for(int j=0; j<productNames.size(); j++){
                            st.addBatch(
                                    "UPDATE 'products' " +
                                    "SET 'product_qty' = 'product_qty' - " + qty.get(j) + " " +
                                    "WHERE 'products'.'product_id' = " + id.get(j) + " " +
                                    "AND 'product_name' = '" + productNames.get(j) + "';"
                                );
                        }
                        
                        int [] execBatch = st.executeBatch();
                        
                        for(int j=0; j<execBatch.length; j++){
                            out.print(execBatch + " --> ");
                        }
                        
                        con.commit();
                        response.sendRedirect(request.getContextPath() + "/userinfo.jsp");
                    }
                    else{
                        response.sendRedirect(request.getContextPath());
                    }
                }
                catch (SQLException ex) {
                            try {
                            Logger.getLogger(buyItems.class.getName()).log(Level.SEVERE, null, ex);
                            con.rollback();  
                            String message, messageDetail;
                            String messageUrl = "/message.jsp";
                            RequestDispatcher dispatchMessage =
                            request.getServletContext().getRequestDispatcher(messageUrl);
                            message = "Oops, Less Product Stock...!";
                            messageDetail = "We see that your demand for the product was critically higher than what we had in our inventory, We respect your purchase but your purchase was cancelled, We are sorry, but please in a urgent requirement do order less stock right now!!";
                            request.setAttribute("message", message);
                            request.setAttribute("messageDetail", messageDetail);
                            dispatchMessage.forward(request, response);
                                            //response.sendError(500);
                            
                                //response.sendRedirect("/saikiranBookstoreApp/buyItems.jsp");
                            } catch (SQLException ex1) {
                                
                            String message, messageDetail;
                            String messageUrl = "/message.jsp";
                            RequestDispatcher dispatchMessage =
                            request.getServletContext().getRequestDispatcher(messageUrl);
                            
                            message = "Oops, Less Product Stock...!";
                            messageDetail = "We see that your demand for the product was critically higher than what we had in our inventory, We respect your purchase but your purchase was cancelled, We are sorry, but please in a urgent requirement do order less stock right now!!";
                            
                            request.setAttribute("message", message);
                            request.setAttribute("messageDetail", messageDetail);
                            
                            dispatchMessage.forward(request, response);
                            
                            }
                } 
                catch (ClassNotFoundException ex) {
                    out.println("you user " + ex);
                }
                session.removeAttribute("cart");
            }
            else{
                out.println("No items in cart");
            }
        }
        else{
            out.println("Not valid");
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
