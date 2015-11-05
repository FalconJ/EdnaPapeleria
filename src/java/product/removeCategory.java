/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package product;

import database.DB_Conn;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author User
 */
public class removeCategory extends HttpServlet{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet removeCategory</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet removeCategory at " + request.getContextPath() + "</h1>");
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
        String message, messageDetail;
        String messageUrl = "/message.jsp";
        RequestDispatcher dispatchMessage;
        dispatchMessage = request.getServletContext().getRequestDispatcher(messageUrl);
        
        try{
            ArrayList<Integer> subcategory = new ArrayList<>();
            subcategory.clear();
            
            Connection c = new DB_Conn().getConnection();
            Statement st = c.createStatement();
            
            PrintWriter out = response.getWriter();
            Enumeration<String> parameterNames = request.getParameterNames();
            
            while(parameterNames.hasMoreElements()){
                String [] parameterValues = request.getParameterValues(parameterNames.nextElement());
                
                if(parameterValues.length > 1){
                    for(int i=0; i<parameterValues.length; i++){
                        out.println(" " + parameterValues[i]);
                        subcategory.add(Integer.parseInt(parameterValues[i]));
                    }
                }
                else{
                    out.println (" " + parameterValues[0]);
                    subcategory.add(Integer.parseInt(parameterValues[0]));
                }
            }
            
            for(int i=0; i<subcategory.size(); i++){
                out.println("<br><br>" + subcategory.get(i));
                
                String category = "DELETE FROM category " +
                                  "WHERE category_id = '" + subcategory.get(i) + "';";
                st.addBatch(category);
            }
            
            st.executeBatch();
        }
        catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(removeCategory.class.getName()).log(Level.SEVERE, null, ex);
            message = "An Error occoured during the process of Deletion";
            messageDetail = "There was an error during the deletion of the process, Please try after some time";
                   
            request.setAttribute("message", message);
            request.setAttribute("messageDetail", messageDetail);
            dispatchMessage.forward(request, response);
        
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
