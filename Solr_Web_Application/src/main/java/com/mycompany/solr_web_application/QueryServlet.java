 package com.mycompany.solr_web_application;

 import java.io.IOException;
 import java.io.PrintWriter;
 import javax.servlet.RequestDispatcher;
 import javax.servlet.ServletException;
 import javax.servlet.http.HttpServlet;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;

 public class QueryServlet
   extends HttpServlet
 {
   protected void processRequest(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException
   {
     response.setContentType("text/html;charset=UTF-8");
     PrintWriter out = response.getWriter();
     try
     {
       out.println("<!DOCTYPE html>");
       out.println("<html>");
       out.println("<head>");
       out.println("<title>Servlet QueryServlet</title>");
       out.println("</head>");
       out.println("<body>");
       out.println("<h1>Servlet QueryServlet at " + request.getContextPath() + "</h1>");
       out.println("</body>");
       out.println("</html>");
     }
     finally
     {
       out.close();
     }
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException
   {
     processRequest(request, response);
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException
   {
     PrintWriter out = response.getWriter();
     out.println(request.getParameter("query"));
     Solr_Query objSolr_Query = new Solr_Query();


     RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
     rd.forward(request, response);
   }

   public String getServletInfo()
   {
     return "Short description";
   }
 }