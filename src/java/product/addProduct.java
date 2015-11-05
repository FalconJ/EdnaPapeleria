/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import database.DB_Conn;
import java.sql.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author User
 */
public class addProduct extends HttpServlet{
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet addCompany</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet addCompany at " + request.getContextPath() + "</h1>");
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
        
        String message, messageDetail, url;
        String productName, productQty, company, category, subCategory, price, summary, tags;
        RequestDispatcher dispatcher;
        PrintWriter out = response.getWriter();
        
        productName = request.getParameter("productName");
        productQty  = request.getParameter("productQty");
        company     = request.getParameter("company");
        category    = request.getParameter("category");
        subCategory = request.getParameter("subCategory");
        price       = request.getParameter("price");
        summary     = request.getParameter("summary");
        tags        = request.getParameter("tags");
    
        out.println("productName " + productName
                    + " <br>company " + company
                    + " <br>category " + category
                    + " <br>subCategory " + subCategory
                    + " <br>price " + price
                    + " <br>summary <br>" + summary
                    + " <br>summary <br>" + tags);
    
        if(!productName.equals("") && !price.equals("")){
            try{
                DB_Conn conn = new DB_Conn();
                try (Connection con = conn.getConnection(); Statement st = con.createStatement()) {
                    
                    String newProduct = "INSERT INTO 'Papeleria'.'products' " +
                            "('product_id', 'product_name', 'sub_category_name', " +
                            "'category_name', 'company_name', 'price', " +
                            "'summary', 'tags', 'product_qty') " +
                            "VALUES (NULL, ?, ?, ?, ?, ?, ?, ?, ?);";
                    
                    PreparedStatement psmt = con.prepareStatement(newProduct);
                    psmt.setString(1, productName);
                    psmt.setString(2, subCategory);
                    psmt.setString(3, category);
                    psmt.setString(4, company);
                    psmt.setString(5, price);
                    psmt.setString(6, summary);
                    psmt.setString(7, tags);
                    psmt.setString(8, productQty);
                    
                    int i = psmt.executeUpdate();
                    
                    if(i == 1){
                        String getProductName = "SELECT 'product_id'" +
                                "FROM 'products' " +
                                "WHERE 'product_name' = '" + productName + "' ;";
                        
                        ResultSet execQuery = st.executeQuery(getProductName);
                        String productId = "";
                        
                        while(execQuery.next()){
                            productId = execQuery.getString("product_id");
                        }
                        
                        HttpSession session = request.getSession();
                        session.setAttribute("productName", productName);
                        session.setAttribute("productId", productId);
                        
                        out.println("Product inserted");
                        message = "Product inserted";
                        messageDetail = company + " " + productName + " inserted";
                        
                        url = "/productInsertImages.jsp";
                        
                        request.setAttribute("productName", productName);
                        request.setAttribute("message", message);
                        request.setAttribute("messageDetail", messageDetail);
                        
                        dispatcher = getServletContext().getRequestDispatcher(url);
                        dispatcher.forward(request, response);
                    }
                }
            }
            catch(SQLException ex){
                message = "Product not inserted";
                messageDetail = "Error";
                
                out.println("Failed: " + ex);
            
                request.setAttribute("message", message);
                request.setAttribute("messageDetail", messageDetail);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(addProduct.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
    
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
