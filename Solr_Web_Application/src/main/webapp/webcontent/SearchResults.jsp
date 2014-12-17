<%-- 
    Document   : SearchResults
    Created on : Nov 29, 2014, 3:56:39 PM
    Author     : Imran Bijapuri
--%>

<%@page contentType="text/html" pageEncoding="windows-1252"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	request.setAttribute("jsonList1",
			session.getAttribute("retrieveRelevantNews"));
	request.setAttribute("socialmedia_response",
			session.getAttribute("socialmediaresponse"));
%>

<html>
<head>
<meta http-equiv="Content-Type"
	content="text/html; charset=windows-1252">
<title>WiKi Article Search!!</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta name="description" content="" />
<meta name="author" content="" />
<link href="css/bootstrap.min.css" rel="stylesheet" />
<link href="css/style.css" rel="stylesheet" />
<link rel="browser-tab-icon" sizes="144x144" href="./img/wikisearch.png" />
<link rel="browser-tab-icon" sizes="114x114" href="./img/wikisearch.png" />
<link rel="browser-tab-icon" sizes="72x72" href="./img/wikisearch.png" />
<link rel="browser-tab-icon" href="./img/wikisearch.png" />
<link rel="shortcut icon" href="./img/wikisearch.png" />
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/scripts.js"></script>

<script type="text/javascript">
	function autoResize(id) {
		var newheight;
		var newwidth;

		if (document.getElementById) {
			newheight = document.getElementById(id).contentWindow.document.body.scrollHeight;
			newwidth = document.getElementById(id).contentWindow.document.body.scrollWidth;
		}

		document.getElementById(id).height = (newheight) + "px";
		document.getElementById(id).width = (newwidth) + "px";
	}

	function tolink(name) {
		var arr = name.split(" ");
		var temp = "";
		for (var i = 0; i < arr.length; i++) {
			temp = temp + "_" + arr[i];
		}
		temp = temp.substring(1, temp.length);
		var win = window.open('http://en.wikinews.org/wiki/' + temp, '_blank');
		win.focus();
	}
//-->
</script>

</head>

<body>
	<table style="width: 100%">
		<tr>
			<td>
				<div class="container">
					<div style="margin-top: 3%;" class="row clearfix">
						<div class="col-md-12 column">
							<form class="form-horizontal" role="form">

								<div id="stage">
									<div id="response_region" style="float: left;">
										<iframe height="1000" id="frame"
											src="http://en.wikipedia.org/wiki/<%=session.getAttribute("url")%>"
											width="800"> </iframe>
									</div>
									<div class="row clearfix" style="position: absolute; left:72.5%">
										<div class="col-md-12 column">
											<div class="panel-group" id="panel-921256">
												<div class="panel panel-default">
													<div class="panel-heading">
														<a style="font-weight: bold;"
															class="panel-title collapsed" data-toggle="collapse"
															data-parent="#panel-921256" href="#panel-element-82063">News
															Stories</a>
													</div>
													<div id="panel-element-82063" class="panel-collapse">
														<c:forEach var="jsonitem1" items="${jsonList1}" begin="0"
															end="4">

															<div class="panel-body">
																<a onclick="tolink('${jsonitem1.name}')" href="#"> <c:out
																		value="${jsonitem1.name}" />
																</a>
															</div>
														</c:forEach>
													</div>
												</div>
												<!-- Social Media Posts start here-->
												<div class="panel panel-default">
													<div class="panel-heading">
														<a style="font-weight: bold;"
															class="panel-title collapsed" data-toggle="collapse"
															data-parent="#panel-921256" href="#panel-element-663303">Social
															Media Posts</a>
													</div>
													<div id="panel-element-663303"
														class="panel-collapse">

														<c:forEach var="jsonitem" items="${socialmedia_response}">
															<div class="panel-body">
																<c:choose>
																	<c:when test="${empty jsonitem.name}">
																		<a href="<c:out value="${jsonitem.source}" />"><c:out
																				value="${jsonitem.caption}" /></a>
																	</c:when>
																	<c:otherwise>
																		<a href="<c:out value="${jsonitem.source}" />"><c:out
																				value="${jsonitem.name}" /></a>
																	</c:otherwise>
																</c:choose>
															</div>
														</c:forEach>
													</div>
												</div>
											</div>
										</div>
									</div>
								</div>
							</form>
						</div>
					</div>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>