 package com.mycompany.solr_web_application;

 import java.io.IOException;
 import java.util.ArrayList;
 import java.util.Collection;
 import org.apache.solr.client.solrj.SolrServerException;
 import org.apache.solr.client.solrj.impl.HttpSolrServer;
 import org.apache.solr.client.solrj.impl.XMLResponseParser;
 import org.apache.solr.common.SolrInputDocument;

 public class Solr_Initialize
 {
   public static void main(String[] args)
     throws IOException, SolrServerException
   {
     String url = "http://localhost:8080/solr";
     HttpSolrServer server = new HttpSolrServer(url);
     server.setMaxRetries(1);
     server.setConnectionTimeout(5000);



     server.setParser(new XMLResponseParser());



     server.setSoTimeout(10000);
     server.setDefaultMaxConnectionsPerHost(100);
     server.setMaxTotalConnections(100);
     server.setFollowRedirects(false);


     server.setAllowCompression(true);
     Collection<SolrInputDocument> docs = new ArrayList();
     for (int i = 11; i < 20; i++)
     {
       SolrInputDocument doc = new SolrInputDocument();
       doc.addField("cat", "book");
       doc.addField("id", "book-" + i);
       doc.addField("name", "The Legend of the Hobbit part " + i);
       doc.addField("content", "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.");
       server.add(doc);
       if (i % 100 == 0) {
         server.commit();
       }
     }
     server.commit();
   }

   public static String generateRandomString(int length)
   {
     try
     {
       StringBuffer buffer = new StringBuffer();
       String characters = "";
       characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
       int charactersLength = characters.length();
       for (int i = 0; i < length; i++)
       {
         double index = Math.random() * charactersLength;
         buffer.append(characters.charAt((int)index));
       }
       return buffer.toString();
     }
     catch (Exception e) {}
     return null;
   }
 }