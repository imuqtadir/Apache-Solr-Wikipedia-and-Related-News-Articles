package com.mycompany.solr_web_application;

import java.io.PrintStream;
import java.net.MalformedURLException;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.ModifiableSolrParams;

public class SolrJExample
{
  public static void main(String[] args)
    throws MalformedURLException, SolrServerException
  {
    SolrServer solr = new HttpSolrServer("http://localhost:8983/solr/wiki");


    ModifiableSolrParams params = new ModifiableSolrParams();
    params.set("qt", new String[] { "/spell" });
    params.set("q", new String[] { "shahrekh" });
    params.set("spellcheck", new String[] { "on" });
    params.set("spellcheck.build", new String[] { "true" });
    params.set("spellcheck.onlyMorePopular", new String[] { "true" });


    QueryResponse response = solr.query(params);
    System.out.println("response = " + response);
  }
}