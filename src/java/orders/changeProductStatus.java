/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package orders;

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

/**
 *
 * @author User
 */
public class changeProductStatus extends HttpServlet{
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet changeProductStatus</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet changeProductStatus at " + request.getContextPath() + "</h1>");
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
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        String changeStatus = request.getParameter("changeStatus");
        String order [] = request.getParameterValues("order");
        
        response.setContentType("text/html;charset=UTF-8");
        
        PrintWriter out = response.getWriter();
    
        if(order != null){
            for(int i=0; i<order.length; i++){
                out.println (" <br/>"+order[i]);
            }
            
            if (changeStatus.equals("approved")){
                try {
                    StringBuilder sqlBuffer;
                    sqlBuffer = new StringBuilder();
                    
                    
                    Connection c = new DB_Conn().getConnection();
                    Statement st = c.createStatement();
                    
                    for(int i=0; i<order.length; i++){
                    String sqlUpdatePending = "UPDATE  'order' SET " +
                                              "'status' = 'approved'" +
                                              "WHERE  'order'.'order_id' ='"+order[i]+"';";
                        
                        st.addBatch(sqlUpdatePending);
                    }
                    
                    out.print(" "+sqlBuffer.toString());
                   
                    int[] executeBatch = st.executeBatch();
                    out.println (executeBatch.length +" Products Approved");
                    
                    
                } catch (SQLException | ClassNotFoundException ex) {
                    out.print(" " +ex);
                    Logger.getLogger(changeProductStatus.class.getName()).log(Level.SEVERE, null, ex);
                }               
                
                response.sendRedirect(request.getContextPath()+"/admin_pendingOrders.jsp");
                
            }else if (changeStatus.equals("delivered")){
                //cancel the pending oreders
                try {
                    StringBuilder sqlBuffer = new StringBuilder();
                                        
                    Connection c = new DB_Conn().getConnection();
                    Statement st = c.createStatement();
                    
                    for(int i=0; i<order.length; i++){
                    
                        String sqlUpdatePending = "UPDATE 'order' SET " +
                                                  "'status' =  'delivered' " +
                                                  "WHERE  'order'.'order_id' = '" + order[i] + "';";
                        st.addBatch(sqlUpdatePending);
                    }
                    out.print(" "+sqlBuffer.toString());
                    
                    int[] executeBatch = st.executeBatch();
                    out.println (executeBatch.length +" Products Approved");
                    
                    
                } catch (SQLException | ClassNotFoundException ex) {
                    out.print(" " +ex);
                    Logger.getLogger(changeProductStatus.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                response.sendRedirect("/Papeleria/admin_approvedOrders.jsp");
            }
            else if (changeStatus.equals("cancel")){
                //cancel the pending oreders
                try {
                    StringBuilder sqlBuffer = new StringBuilder();
                    
                    Connection c = new DB_Conn().getConnection();
                    Statement st = c.createStatement();
                    
                    for(int i=0; i<order.length; i++){
                        String sqlDeleteOrder, sqlDeleteSales;
                        sqlDeleteOrder = " DELETE FROM 'order' WHERE 'order_id' = " + order[i] + ";";
                        sqlDeleteSales = " DELETE FROM 'sales' WHERE 'order_id' = " + order[i] + ";";

                        st.addBatch(sqlDeleteOrder);
                        st.addBatch(sqlDeleteSales);
                    }
                    
                    out.print(" "+sqlBuffer.toString());
                    
                    int[] executeBatch = st.executeBatch();
                    out.println (executeBatch.length +" Products Deleted");
                    
                    
                } catch (SQLException | ClassNotFoundException ex) {
                    out.print(" " +ex);
                    
                    Logger.getLogger(changeProductStatus.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                response.sendRedirect("/Papeleria/admin_Performance.jsp");
            
            }
       }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
