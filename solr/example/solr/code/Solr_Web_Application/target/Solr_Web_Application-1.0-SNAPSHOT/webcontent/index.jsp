<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>JSP Page</title>
<script>
	function Redirect() {
		var q = document.getElementById("query").value;
		var url = "http://localhost:8081/wikisearch/results.jsp?q="
				+ q;
		window.location = url;
	}
</script>

</head>
<body style="font-family: verdana;">
	<form action="results.jsp" method="POST">
		<table align="center"
			style="margin-top: 200px; font-size: 100%; font-weight: bold;">
			<tr style="text-align: center">
				<td><label> Please enter a term to search :</label></td>
			</tr>
			<tr>
				<td><input style="display: block;" type="text" size="50"
					name="query" id="query" value=""></td>
				<td><input type="button" value="Search" onclick="Redirect()">
				</td>
			</tr>
		</table>
	</form>
</body>
</html>
