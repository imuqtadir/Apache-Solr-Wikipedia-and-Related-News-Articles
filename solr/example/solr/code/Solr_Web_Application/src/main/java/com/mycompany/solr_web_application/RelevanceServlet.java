package com.mycompany.solr_web_application;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.solr.client.solrj.SolrServerException;

/**
 * Servlet implementation class RelevanceServlet
 */
public class RelevanceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RelevanceServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
            System.out.println(request.getParameter("contents"));
            System.out.println(request.getParameter("url"));
            System.out.println(request.getParameter("name"));
            
            NewsRelevanceFinder newsRF = new NewsRelevanceFinder();
            try {
                ResultWrapper wrapper = newsRF.retrieveRelevantNews(request.getParameter("name"), request.getParameter("contents"));
                HttpSession session = request.getSession();
                
                
                session.setAttribute("retrieveRelevantNews", wrapper.news_response);
                session.setAttribute("socialmediaresponse", wrapper.socialmedia_response);
                System.out.println("ppppppppppppppp  " + wrapper.socialmedia_response);
                session.setAttribute("url", request.getParameter("url"));
                
            } catch (SolrServerException ex) {
                Logger.getLogger(RelevanceServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
            
	}

}
