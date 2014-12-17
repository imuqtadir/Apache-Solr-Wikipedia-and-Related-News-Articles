/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.solr_web_application;

import java.util.Iterator;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;

/**
 *
 * @author Imran Bijapuri
 */
public class Wildcard {

    public static void main(String[] args) {
        try {
            String url = null;
            HttpSolrServer server = null;
            url = "http://localhost:8983/solr/wiki";
            server = new HttpSolrServer(url);
            server.setMaxRetries(1);
            server.setConnectionTimeout(20000);
            server.setParser(new XMLResponseParser());
            server.setSoTimeout(10000);
            server.setDefaultMaxConnectionsPerHost(100);
            server.setMaxTotalConnections(100);
            server.setFollowRedirects(false);
            server.setAllowCompression(true);

            SolrQuery query = new SolrQuery();
            query.setQuery("indi*");
            query.setRequestHandler("/wildcard");
            query.setFields(new String[]{"id", "name", "contents"});
            query.setHighlight(true); //set other params as needed
           

            QueryResponse queryResponse = server.query(query);
            System.out.println(queryResponse);
            //response = server.query(solrQuery);
            

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
