/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.solr_web_application;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import java.util.Iterator;
import java.util.List;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.noggit.JSONUtil;

/**
 *
 * @author Imran Bijapuri
 */
public class Simple_Query_Solr {

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
            query.setQuery("india");
            query.setQuery("india");
            query.setFields(new String[]{"id","name","contents"});
            query.setHighlight(true); //set other params as needed
            query.setParam("hl.fl", "name,contents");
            query.setParam("hl.simple.pre", "<em>");
            query.setParam("hl.simple.post", "</em>");
            query.setParam("hl.fragsize", "100");

            QueryResponse queryResponse = server.query(query);
            //System.out.println(queryResponse);
            //response = server.query(solrQuery);
            Iterator<SolrDocument> iter = queryResponse.getResults().iterator();

            while (iter.hasNext()) {
                SolrDocument resultDoc = iter.next();
                String id = (String) resultDoc.getFieldValue("id");
                //System.out.println(id);
                //String id = (String) resultDoc.getFieldValue("id"); //id is the uniqueKey field

                if (queryResponse.getHighlighting().get(id) != null) {
                    List<String> highlightSnippets = queryResponse.getHighlighting().get(id).get("name");
                    List<String> highlightSnippets1 = queryResponse.getHighlighting().get(id).get("contents");
                   // System.out.println("name "+highlightSnippets);
                  //  System.out.println("content "+highlightSnippets1);
                }
            }
            //String jsonString = JSONUtil.toJSON(queryResponse);
            //ResponseWrapper data = new Gson().fromJson(jsonString, ResponseWrapper.class);
            System.out.println("Highlighting data "+queryResponse.getHighlighting());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
