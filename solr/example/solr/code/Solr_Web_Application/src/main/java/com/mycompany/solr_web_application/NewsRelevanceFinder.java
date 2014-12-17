package com.mycompany.solr_web_application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.miscellaneous.PatternAnalyzer;
import org.apache.lucene.index.memory.MemoryIndex;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.solr.client.solrj.SolrServerException;
/*
// Step-1: We extract the "links text" from the "contents", and then
			// form a string that acts as the corpus of one doc that is to be
			// indexed in the memory.

			// Step-2: We index the "links text" into memory.. Kind of like an
			// on-the-fly index, with a single document.

			// Step-3: We maintain a list of 'links text' in an arraylist

			// Step-4: we run each term in the list as a query against the
			// 'in-memory index of contents', and generate scores for each of
			// the terms in the arraylist

			// step-5: The terms (links) with the top 5 scores will be part of
			// our query to solr. These terms are the most used links from the
			// contents
			// data.

			// Step-6: We then add a weightages of 1) 30% to initial user query,
			// 2) 40% to title user clicked and 3) 30% to the links generated

			// STEP-1 -> START
*/
public class NewsRelevanceFinder extends DefaultSimilarity {

	public NewsRelevanceFinder() {
	}

	@SuppressWarnings("deprecation")
	public ResultWrapper retrieveRelevantNews(String title, String contents) throws SolrServerException {
            List<NewsWrapper> objmedia = new ArrayList();
            ResultWrapper wrapper = new ResultWrapper();
            Socialmedia objsocial = new Socialmedia();
		try {
                   	Map<String, Double> linksScoreMap = new LinkedHashMap<String, Double>();
			Matcher matcher = Pattern.compile("\\[([^\\]]+)").matcher(
					contents);
			while (matcher.find())
				linksScoreMap
						.put(matcher.group(1).replaceAll("\\W", " "), 0.00);
			Analyzer analyzer = PatternAnalyzer.DEFAULT_ANALYZER;
			// Analyzer analyzer = new SimpleAnalyzer();
			MemoryIndex index = new MemoryIndex();
			index.addField("contents", contents, analyzer);
			// index.addField("title", title, analyzer);
			QueryParser parser = new QueryParser("contents", analyzer);
			for (Map.Entry<String, Double> e : linksScoreMap.entrySet()) {
				String link = e.getKey();
				double score = index.search(parser.parse(link));
				e.setValue(score);
			}
			// reverse sort map by values
			Map<String, Double> reverseSortedMap = reverseSortMap(linksScoreMap);
                        String query = title;
			for (Map.Entry<String, Double> e : reverseSortedMap.entrySet())
				query = query + " " + e.getValue();
                        
                        Solr_Query sQ = new Solr_Query();
                        objmedia =  sQ.fetch_News_Results(query, 1);
                        wrapper.news_response = objmedia;
                try {
                  
                    wrapper.socialmedia_response =  objsocial.sendGet(title);
                    System.out.println("Facebook response "+wrapper.socialmedia_response);
                  
                } catch (Exception ex) {
                    Logger.getLogger(NewsRelevanceFinder.class.getName()).log(Level.SEVERE, null, ex);
                }
                        
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
                return wrapper;
	}

	public Map<String, Double> reverseSortMap(Map<String, Double> map) {
		List<Map.Entry<String, Double>> mapEntryList = new ArrayList<Map.Entry<String, Double>>(
				map.entrySet());
		Collections.sort(mapEntryList,
				new Comparator<Map.Entry<String, Double>>() {
					@Override
					public int compare(Map.Entry<String, Double> e1,
							Map.Entry<String, Double> e2) {
						double v1 = (double) e1.getValue();
						double v2 = (double) e2.getValue();
						if (v1 > v2)
							return -1;
						else if (v1 < v2)
							return 1;
						else
							return 0;
					}
				});
		Map<String, Double> reverseSortedMap = new LinkedHashMap<String, Double>();
		for (Map.Entry<String, Double> e : mapEntryList) {
			reverseSortedMap.put(e.getKey(), e.getValue());
		}
		return reverseSortedMap;
	}
}
