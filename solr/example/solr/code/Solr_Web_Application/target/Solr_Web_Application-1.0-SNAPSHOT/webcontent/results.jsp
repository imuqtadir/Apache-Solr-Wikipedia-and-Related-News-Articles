<%-- 
    Document   : results
    Created on : Nov 18, 2014, 11:11:38 AM
    Author     : Imran Bijapuri
--%>


<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.google.gson.JsonArray"%>
<%@page import="com.google.gson.JsonParser"%>
<%@page import="com.google.gson.stream.JsonReader"%>
<%@page import="java.io.PrintWriter"%>

<%@page import="com.mycompany.solr_web_application.Solr_Query"%>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<!DOCTYPE html>
<%
	Solr_Query objSolr_Query = new Solr_Query();
	String jsonResponseString = objSolr_Query.fetchqueryresults(request
			.getParameter("q"));

	JsonParser jsonParser = new JsonParser();
	JsonArray userarray = jsonParser.parse(jsonResponseString)
			.getAsJsonArray();
%>

<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title>JSP Page</title>
</head>
<body>
	<table style="width: 80%" align="center">
		<tr>
			<td><br></td>
		</tr>
		<tr>
			<td>Results for your Query : " <%=request.getParameter("q")%> "
			</td>
		</tr>
		<tr>
			<td><br></td>
		</tr>
		<%
			for (int i = 0; i < userarray.size(); i++) {
				out.println("<tr><td>"
						+ userarray.get(i).getAsJsonObject().get("id")
								.getAsString() + "</td></tr>");
				out.println("<tr><td>"
						+ userarray.get(i).getAsJsonObject().get("contents")
								.getAsString() + "</td></tr>");
				/*JsonArray farray = userarray.get(i).getAsJsonObject().get("content").getAsJsonArray();
				for (int s = 0; s < farray.size(); s++) {
				    out.println("<tr><td>"+farray.get(s).getAsString().toString()+"</td></tr>");
				}*/

				out.println("<tr><td height=\"40\"></td></tr>");
			}
		%>
	</table>
	<h1></h1>
</body>
</html>
