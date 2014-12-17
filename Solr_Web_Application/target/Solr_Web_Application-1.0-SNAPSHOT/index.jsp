<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script>
            function Redirect(){
                var q = document.getElementById("query").value;
                var url = "http://localhost:8080/Solr_Web_Application/results.jsp?q="+q;
                window.location = url;
            }
        </script>
        
    </head>
    <body>
        <form action="results.jsp" method="POST">
            <table align="center">
                <tr>
                    <br><br><br><br><br><br><br><br><br><br><br><br><br>
                    <td>
                        <label>Please enter a term to search :</label>
                    </td>
                    
                    <td>
                        <input type="text" size="50" name="query" id="query" value="">
                    </td>
                    
                    <td>
                        <input type="button" value="Okay" onclick="Redirect()">
                    </td>
                </tr>
            </table>
        </form>
    </body>
</html>
