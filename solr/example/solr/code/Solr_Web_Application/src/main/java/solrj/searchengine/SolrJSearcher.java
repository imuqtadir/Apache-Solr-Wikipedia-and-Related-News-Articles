package com.solrj.searchengine;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocumentList;

import java.net.MalformedURLException;

public class SolrJSearcher {
	public static void main(String[] args) throws MalformedURLException,
			SolrServerException {
		HttpSolrServer solr = new HttpSolrServer("http://localhost:8080/solr");

		SolrQuery query = new SolrQuery();
		query.setQuery("sony digital camera");
		query.addFilterQuery("cat:electronics", "store:amazon.com");
		query.setFields("id", "price", "merchant", "cat", "store");
		query.setStart(0);
		query.set("defType", "edismax");

		QueryResponse response = solr.query(query);
		SolrDocumentList results = response.getResults();
		for (int i = 0; i < results.size(); ++i) {
			System.out.println(results.get(i));
		}
	}
}