 package com.mycompany.solr_web_application;

 import java.io.IOException;
 import java.io.PrintStream;
 import java.io.PrintWriter;
 import javax.servlet.ServletException;
 import javax.servlet.http.HttpServlet;
 import javax.servlet.http.HttpServletRequest;
 import javax.servlet.http.HttpServletResponse;

 public class autocomplete
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
       out.println("<title>Servlet autocomplete</title>");
       out.println("</head>");
       out.println("<body>");
       out.println("<h1>Servlet autocomplete at " + request.getContextPath() + "</h1>");
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
     Solr_Query obj = new Solr_Query();
     ResultWrapper wrapper = new ResultWrapper();
     String result = obj.auto_Spellchecker(request.getParameter("data"));
     System.out.println("Query " + result);
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response)
     throws ServletException, IOException
   {
     processRequest(request, response);
   }

   public String getServletInfo()
   {
     return "Short description";
   }
 }