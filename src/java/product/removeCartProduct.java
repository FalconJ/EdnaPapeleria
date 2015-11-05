/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import cart.cart;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


/**
 *
 * @author User
 */
public class removeCartProduct extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet removeCartProduct</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet removeCartProduct at " + request.getContextPath() + "</h1>");
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
        try{
            String id =  request.getParameter("id");
            int intId = Integer.parseInt(id);
            PrintWriter out = response.getWriter();
            out.println ("Id of the product " + id );
            HttpSession session = request.getSession();
            
            cart Cart;
            Cart = (cart) session.getAttribute("cart");
        
            response.setContentType("text/html;charset=UTF-8");
            out.println("<br>Total value price of the cart " + Cart.getTotalPriceOfCart());
        
            ArrayList<String> productName = new ArrayList<>();
            ArrayList<Double> productPrices = new ArrayList<>();
            ArrayList<Integer> qty = new ArrayList<>();
            ArrayList<Integer> ids = new ArrayList<>();
            
            Cart.removedProduct(intId);
            out.println("<a href='/MyCartApplication/addToCart.jsp'>Goto Cart</a>");
        }
        catch(SQLException | ClassNotFoundException ex){
            Logger.getLogger(removeCartProduct.class.getName()).log(Level.SEVERE, null, ex);
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
            throws ServletException, IOException {
        processRequest(request, response);
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
