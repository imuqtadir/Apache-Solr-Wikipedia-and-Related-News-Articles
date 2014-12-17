<%@page import="com.mycompany.solr_web_application.Solr_Query"%>
<%@page contentType="text/html" pageEncoding="windows-1252"%>
<%@page import="com.mycompany.solr_web_application.ResultWrapper"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.solr_web_application.MediaWrapper"%>
<%@page import="com.mycompany.solr_web_application.Socialmedia"%>
<%@page import="com.google.gson.JsonObject"%>
<%@page import="com.google.gson.JsonElement"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.google.gson.JsonArray"%>
<%@page import="com.google.gson.JsonParser"%>
<%@page import="com.google.gson.stream.JsonReader"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.ArrayList"%>
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
<script>
            function reload() {
                var q = document.getElementById("query").value;
                window.location.href = "http://localhost:8081/wikisearch2/SearchHome.jsp?query=" + q;
            }

            function autocom() {
                if (document.getElementById("query").value.length > 5) {
                    //alert("hey");
                    $.get('http://localhost:8081/wikisearch2/autocomplete?data=' + document.getElementById("query").value, function(data) {
                        //alert(data);
                    });
                }
            }

            function Convert_to_link(name, divid) {
                //alert(name);
                var arr = name.split(" ");
                var temp = "";
                for (var i = 0; i < arr.length; i++) {
                    temp = temp + "_" + arr[i];
                }
                temp = temp.substring(1, temp.length);
                //alert(temp);
                document.getElementById(divid).innerHTML = '<iframe src="http://en.wikipedia.org/wiki/' + temp + '" width="800" height="400"></iframe>';
                //window.location.href = "http://en.wikipedia.org/wiki/"+temp;
                // return name;
            }
        </script>
</head>

<body>
	<table style="width: 100%">
		<tr>
			<td>
				<div class="container">
					<div class="row clearfix">
						<div class="col-md-12 column">
							<form class="form-horizontal" role="form">
								<input type="hidden" id="hidquery" name="hidquery"
									value="<%=request.getParameter("query")%>">


								<div class="form-group">
									<div class="col-sm-offset-2 col-sm-10"
										style="margin-top: 2%; margin-bottom: 2%; left: -17%;">
										<h3 style="display: none">WiKi Search</h3>
										<img width="110" id="wiki_search" src="./img/wikisearch.png"
											alt="Wiki Search" style="float: left;">
										<div style="position: relative; margin-top: 7.2%">
											<input type="text" oninput="autocom()"
												style="float: left; margin-left: 3%; width: 65%;"
												placeholder="Enter your query.." id="query"
												class="form-control">
											<!-- <button  id="driver" type="submit" class="btn btn-default"
                                                        onclick="javascript:Redirect()">Search</button> -->
											<input style="margin-left: 2%;" type="button"
												class="btn btn-default" id="driver" value="Search"
												onclick="reload()">
										</div>
									</div>
								</div>
								<div id="stage">
									<div id="response_region" style="float: left;">
										<%
											Solr_Query solrQuery = new Solr_Query();
											ResultWrapper objwrapper = null;
											if (request.getParameter("query") != null) {
												// fetching news stories
												try {
													solrQuery = new Solr_Query();
													objwrapper = solrQuery.fetchQueryResults(
															request.getParameter("query"), 1);

													request.setAttribute("jsonList1", objwrapper.list);

													solrQuery = new Solr_Query();
													objwrapper = solrQuery.fetchQueryResults(
															request.getParameter("query"), 0);
													JsonParser jsonParser = new JsonParser();
													request.setAttribute("jsonList", objwrapper.list);

													request.setAttribute("socialmedia_response",
															objwrapper.socialmedia_response);
												} catch (Exception ex) {
													ex.printStackTrace();
												}
										%>
										<script>
                                                document.getElementById("query").value = document.getElementById("hidquery").value
                                                hidquery
                                            </script>
									</div>


									<table style="width: 100%">

										<%
											if (objwrapper.spellchecker_active) {
										%>
										<tr>
											<td colspan="2">No results found for <b>"<%=request.getParameter("query")%>"
											</b>.Instead showing results for <b> "<%=objwrapper.spellcheck_query%>"
											</b>.
											</td>
										</tr>
										<%
											} else if ((objwrapper.did_you_mean)
														&& (objwrapper.spellcheck_query != "")) {
										%>
										<tr>
											<td colspan="2">Did you mean <u><b><a
														href="http://localhost:8081/wikisearch2/SearchHome.jsp?query=<%=objwrapper.spellcheck_query%>"
														id="exampleid" style="cursor: pointer"><%=objwrapper.spellcheck_query%></a></b></u>?
											</td>
										</tr>
										<%
											} else {
										%>
										<tr>
											<td colspan="2">Your query <b>"<%=request.getParameter("query")%>"
											</b> fetched <%=objwrapper.totalresult%> results.
											</td>
										</tr>
										<%
											}
										%>

										<tr>
											<td><br> <br></td>
										</tr>

										<tr>
											<td style="width: 70%;">
												<div id="search_results"
													style="display: block; float: left; width: 100%">
													<c:forEach var="jsonitem" items="${jsonList}">
														<div class="panel panel-info">
															<div class="panel-heading">
																<h3 class="panel-title">
																	<a style="cursor: pointer; font-weight: bold;"
																		onclick="Convert_to_link('${jsonitem.get('name').getAsString()}', '${jsonitem.get('id').getAsString()}')"><c:out
																			value="${jsonitem.get('name').getAsString()}" /></a>
																</h3>
															</div>

															<div id="${jsonitem.get('id').getAsString()}"></div>

															<div class="panel-body">
																<c:out value="${jsonitem.get('id').getAsString()}" />
															</div>
															<div class="panel-footer">

																<c:choose>
																	<c:when
																		test="${fn:length(jsonitem.get('contents').getAsString()) > 400}">
																		<c:out
																			value="${fn:substring(jsonitem.get('contents').getAsString(),0,300)}" />....
                                                                        </c:when>

																	<c:otherwise>.
                                                                            <c:out
																			value="${jsonitem.get('contents').getAsString()}" />
																	</c:otherwise>
																</c:choose>


															</div>
														</div>
														<div style="display: none;">
															<dl>
															</dl>
														</div>
													</c:forEach>

												</div>
											</td>
											<td style="width: 1%"></td>
											<td style="width: 30%; vertical-align: top">
												<div style="width: 100%">
													<div class="row clearfix">
														<div class="col-md-12 column">
															<div class="panel-group" id="panel-921256">
																<div class="panel panel-default">
																	<div class="panel-heading">
																		<a style="font-weight: bold;"
																			class="panel-title collapsed" data-toggle="collapse"
																			data-parent="#panel-921256"
																			href="#panel-element-82063">News Stories</a>
																	</div>
																	<div id="panel-element-82063" class="panel-collapse">
																		class="panel-collapse collapse">
																		<c:forEach var="jsonitem1" items="${jsonList1}"
																			begin="0" end="4">
																			<div class="panel-body">
																				<a
																					href="<c:out value='${jsonitem1.get(\"url\").getAsString()}'/>">
																					<c:out
																						value="${jsonitem1.get(\"name\").getAsString()}" />
																				</a>
																			</div>
																		</c:forEach>
																	</div>
																</div>
															</div>


																<!-- Social Media Posts start here-->
																<div class="panel panel-default">
																	<div class="panel-heading">
																		<a style="font-weight: bold;"
																			class="panel-title collapsed" data-toggle="collapse"
																			data-parent="#panel-921256"
																			href="#panel-element-663303">Social Media Posts</a>
																	</div>
																	<div id="panel-element-663303"
																		class="panel-collapse collapse">

																	<c:forEach var="jsonitem"
																		items="${socialmedia_response}">
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
											</td>
										</tr>
									</table>
									<%
										}
									%>
								</div>

								<img id="loading_image" src="./img/loading.gif"
									alt="Loading page.." style="display: none;">
								<%-- <div id="dynamic_load" style="display: none;">
                                            <jsp:include page="./SearchResultsSection.jsp" />
                                    </div> --%>
							</form>
						</div>
					</div>
				</div>
			</td>
		</tr>
	</table>
</body>
</html>