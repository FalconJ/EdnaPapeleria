<%-- 
    Document   : index
    Created on : Nov 3, 2015, 9:31:45 AM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Statement"%>
<%@page import="java.sql.Connection"%>
<%@page import="database.DB_Conn"%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Papeleria Isaac</title>
        
        <%@include file="includesPage/_stylesheets.jsp" %>
        <link rel="stylesheet" href="css/slider.css"  />
        <script type="text/javascript" src="js/jquery.js"></script>
        <script type="text/javascript" src="js/slider.js"></script>
        
    <script type="text/javascript" >
    // This is the script for the banner slider
    $(document).ready(function() {
        $('#slider').s3Slider({
            timeOut: 7000
        });
    });
    </script>


    <script type="text/javascript" src="js/myScript.js"></script>    
    </head>
    
    <body>
        <%
        if (session.getAttribute("user") == null ){// THen new user, show join now
        %>
            <jsp:include page="includesPage/_joinNow.jsp"></jsp:include>
        <%
        }else {
        %>
            <jsp:include page="includesPage/_logout.jsp"></jsp:include>
        <%
        }
        %>
        
        <%@include file="includesPage/_search_navigationbar.jsp" %>

        <%@include file="includesPage/_facebookJoin.jsp" %>
        
        <div id="banner">
            <div class="container_16">
                <ul id="sliderContent">
                    <li class="sliderImage" style="display: none;">
                        <img src="img/banner/b1.png" width="900" height="300" />
                        <span class="top" style="display: none;">
                            <strong>Papeleria</strong>
                            
                            <br> <p>lorem ipsum</p>
                        </span>
                    </li>
                    
                    <li class="sliderImage" style="display: none;">
                        <img src="img/banner/b2.png" width="900" height="300" />
                        
                        <span class="top" style="display: none;">
                            <strong>Libros, diarios ...</strong>
                            
                            <br> <p>Lorem Ipsum2</p>
                        </span>
                    </li>
                    
                    <li class="sliderImage" style="display: none;">
                        <img src="img/banner/b3.png" width="900" height="300" />
                        
                        <span class="top" style="display: none;">
                            <strong>Colores para los ninos</strong>
                            
                            <br><p>Lorem Ipsum3</p>
                        </span>
                    </li>
                    
                    <li>
                        <img src="img/banner/b4.png" width="900" height="300" />
                        
                        <span class="top" style="display: none;">
                            <strong>Mochilas</strong>
                            
                            <br><p>Lorem Ipsum4</p>
                        </span>
                    </li>
                    
                    <div class="clear sliderImage"></div>
                </ul>
            </div>
        </div>
        
        <div class="container_16">
            <div id="contents">
                
            </div>
        </div>
    </body>
</html>
