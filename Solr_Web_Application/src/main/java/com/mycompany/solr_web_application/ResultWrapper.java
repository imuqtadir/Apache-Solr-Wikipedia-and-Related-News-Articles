 package com.mycompany.solr_web_application;

 import java.util.List;

 public class ResultWrapper
 {
   public List list;
   public int totalresult;
   public List<MediaWrapper> socialmedia_response;
   public List<NewsWrapper> news_response;
   public boolean spellchecker_active = false;
   public String spellcheck_query;
   public boolean did_you_mean = false;
 }
