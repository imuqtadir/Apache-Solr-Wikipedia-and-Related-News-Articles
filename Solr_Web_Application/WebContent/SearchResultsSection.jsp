<%-- <%@page import="com.mycompany.solr_web_application.ResultWrapper"%>
<%@page import="java.util.List"%>
<%@page import="com.mycompany.solr_web_application.MediaWrapper"%>
<%@page import="com.mycompany.solr_web_application.Socialmedia"%>
<%@page import="com.google.gson.JsonObject"%>
<%@page import="com.google.gson.JsonElement"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<%@page import="com.google.gson.Gson"%>
<%@page import="java.util.Iterator"%>
<%@page import="com.google.gson.JsonArray"%>
<%@page import="com.google.gson.JsonParser"%>
<%@page import="com.google.gson.stream.JsonReader"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.mycompany.solr_web_application.NewsRelevanceFinder"%>
<%@page import="java.lang.Integer"%>

<%@page import="com.mycompany.solr_web_application.Solr_Query"%>
<head>
<script>
	function Convert_to_link() {
		alert("name");
		//    return name;
	}
</script>
</head>
<div id="response_region" style="float: left;">
	<%
		Solr_Query solrQuery = new Solr_Query();
		ResultWrapper objwrapper = solrQuery.fetchQueryResults(
				request.getParameter("query"), 0);

		try {
			request.setAttribute("jsonList", objwrapper.list);
			request.setAttribute("query", request.getParameter("query"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	%>
</div>
<div id="news_stories_scriplet">
	<%
		// fetching news stories
		solrQuery = new Solr_Query();
		objwrapper = solrQuery.fetchQueryResults(
				request.getParameter("query"), 1);
		try {
			request.setAttribute("jsonList1", objwrapper.list);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	%>
	<%
		request.setAttribute("socialmedia_response",
				objwrapper.socialmedia_response);
	%>
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
		<td colspan="2">Did you mean <u><b><a id="exampleid"
					style="cursor: pointer"><%=objwrapper.spellcheck_query%></a></b></u>?
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
								<a
									href="/wikisearch/RelevanceServlet?query=${query}&title=${jsonitem.get('name').getAsString()}&content=${jsonitem.get('contents').getAsString()}">
									<c:out value="${jsonitem.get('name').getAsString()}" />
								</a>
							</h3>
						</div>
						<div class="panel-body">
							<c:out value="${jsonitem.get('id').getAsString()}" />
						</div>
						<div class="panel-body">
							<c:choose>
								<c:when
									test="${jsonitem.get('contents').getAsString().length() > 500}">
									<c:set var="sub_content"
										value="${fn:substring(jsonitem.get('contents').getAsString(), 0, 500)}" />
									<c:out value="${sub_content}" />
									<a href="#"> <c:out value="..(more)" />
									</a>
								</c:when>
								<c:otherwise>
									<c:out value="${jsonitem.get('contents').getAsString()}" />
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
									<a class="panel-title collapsed" data-toggle="collapse"
										data-parent="#panel-921256" href="#panel-element-82063">News
										Stories</a>
								</div>
								<div id="panel-element-82063" class="panel-collapse collapse">
									<c:forEach var="jsonitem1" items="${jsonList1}" begin="0"
										end="4">
										<div class="panel-body">
											<a
												href="<c:out value='${jsonitem1.get(\"url\").getAsString()}'/>">
												<c:out value="${jsonitem1.get(\"name\").getAsString()}" />
											</a>
										</div>
									</c:forEach>
								</div>
							</div>


							<!-- Social Media Posts start here-->
							<div class="panel panel-default">
								<div class="panel-heading">
									<a class="panel-title collapsed" data-toggle="collapse"
										data-parent="#panel-921256" href="#panel-element-663303">Social
										Media Posts</a>
								</div>
								<div id="panel-element-663303">

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
		</td>
	</tr>
</table>
 --%>