package com.solrj.searchengine;

import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;

public class SolrjPopulator {
	public static void main(String[] args) throws IOException,
			SolrServerException {
		HttpSolrServer server = new HttpSolrServer("http://localhost:8080/solr");
		for (int i = 0; i < 1000; ++i) {
			SolrInputDocument doc = new SolrInputDocument();
			doc.addField("cat", "book");
			doc.addField("id", "book-" + i);
			doc.addField("name", "The Legend of the Hobbit part " + i);
			server.add(doc);
			if (i % 100 == 0)
				server.commit(); // periodically flush
		}
		server.commit();
	}
}