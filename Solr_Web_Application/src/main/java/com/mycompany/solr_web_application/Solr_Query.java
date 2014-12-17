package com.mycompany.solr_web_application;

import com.google.gson.Gson;
import info.bliki.wiki.model.WikiModel;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.impl.XMLResponseParser;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SpellCheckResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.params.ModifiableSolrParams;
import org.jsoup.Jsoup;
import org.noggit.JSONUtil;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Solr_Query {

    public String url = null;
    public HttpSolrServer server = null;
    public ResultWrapper objwrapper = new ResultWrapper();
    Socialmedia objsocialmedia = new Socialmedia();

    public static void main(String[] args) {
        Solr_Query obj = new Solr_Query();
        try {
            obj.fetch_News_Results("india", 1);
        } catch (SolrServerException ex) {
            Logger.getLogger(Solr_Query.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ResultWrapper fetchQueryResults(String inputQuery, int type) {
        configure_Solr_Server(type);
        SolrQuery solrQuery = new SolrQuery();
        //solrQuery.setQuery(inputQuery);
        
        if(inputQuery.contains("*")){
        solrQuery.setRequestHandler("/wildcard");
        solrQuery.set("q", inputQuery);
            
        }else{
        solrQuery.setRequestHandler("/better");
        solrQuery.set("mainQuery", inputQuery);
        }
        
        solrQuery.setHighlight(true);
        solrQuery.setRows(Integer.valueOf(10));
        solrQuery.set("wt", new String[]{"application/json"});
        solrQuery.set("indent", true);
        solrQuery.setIncludeScore(true);
        QueryResponse response = null;
        String jsonString = "";
        String alternative = "";
        boolean status = false;
        try {
            response = server.query(solrQuery);
            if (response != null) {
                SolrDocumentList solrDocList = response.getResults();
                //JSONUtil.toJSON(solrDocList);

                int totalMatches = (int) solrDocList.getNumFound();

                if (totalMatches > 40) {
                    this.objwrapper.totalresult = totalMatches;
                    jsonString = JSONUtil.toJSON(solrDocList);
                    JsonParser jsonParser = new JsonParser();
                    try {
                        ArrayList<JsonObject> jsonList = new ArrayList<JsonObject>();
                        if (totalMatches > 0) {
                            JsonArray userArr = jsonParser
                                    .parse(jsonString).getAsJsonArray();
                            if (userArr != null) {
								for (JsonElement jElem : userArr) {
									// refining title not listing out any article with initial
									// wiki-markup in front of it

									String wikiJSONTitle = jElem.getAsJsonObject().get("name").getAsString();
									
									int indexOfColon = wikiJSONTitle.indexOf(':');
									String markUpText = "";
									if (indexOfColon != -1)
										markUpText = wikiJSONTitle.substring(0, indexOfColon);
									if (!markUpText.equals("") && markUpText.length() < 15) {
										continue;
									} else {
										// removing any possible html mark-up
										jElem.getAsJsonObject().addProperty("name", wikiJSONTitle.replaceAll(
												"\\<.*?>", " "));
										String solrContentsResp = jElem.getAsJsonObject().get("contents").getAsString();
										String wikiTextToHTML = WikiModel.toHtml(solrContentsResp);
										String htmlLessString = Jsoup.parse(wikiTextToHTML).text();
										
										htmlLessString = htmlLessString.replaceAll("\\{.*?\\} ?", "");
										htmlLessString = htmlLessString.replaceAll("\\(.*?\\) ?", "");
										htmlLessString = htmlLessString.replaceAll("==.*?== ?", "");
										htmlLessString = htmlLessString.replaceAll("[():{}]+", " ");
										htmlLessString = htmlLessString.replace("?????", " ");
										htmlLessString = htmlLessString.replaceAll("\\s+", " ").trim();
										jElem.getAsJsonObject().addProperty("contents", htmlLessString);

										jsonList.add(jElem.getAsJsonObject());
									}
								}
							}
						}
						this.objwrapper.list = jsonList;

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    this.objwrapper.socialmedia_response = this.objsocialmedia.sendGet(inputQuery);

                    this.fetch_News_Results(inputQuery, 1);

                } else if (totalMatches == 0) {
                    System.out.println("totalMatches are 0");
                    alternative = Spellchecker(inputQuery, type);
                    fetch_Spellchecker_Result(alternative, type);
                } else {
                    System.out.println("totalMatches are less than 40");
                    alternative = Spellchecker(inputQuery, 0);
                    this.objwrapper.did_you_mean = true;
                    this.objwrapper.spellcheck_query = alternative;
                    System.out.println("this is alt " + alternative);
                    this.objwrapper.totalresult = totalMatches;
                    jsonString = JSONUtil.toJSON(solrDocList);
                    JsonParser jsonParser = new JsonParser();
                    try {
                        ArrayList<JsonObject> jsonList = new ArrayList<JsonObject>();
                        if (totalMatches > 0) {
                            JsonArray userArr = jsonParser
                                    .parse(jsonString).getAsJsonArray();
                            if (userArr != null) {
								for (JsonElement jElem : userArr) {
									// refining title not listing out any article with initial
									// wiki-markup in front of it
									String wikiJSONTitle = jElem.getAsJsonObject().get("name").getAsString();
									int indexOfColon = wikiJSONTitle.indexOf(':');
									String markUpText = "";
									if (indexOfColon != -1)
										markUpText = wikiJSONTitle.substring(0, indexOfColon);
									if (!markUpText.equals("") && markUpText.length() < 15) {
										continue;
									} else {
										String solrContentsResp = jElem.getAsJsonObject().get("contents").getAsString();
										String wikiTextToHTML = WikiModel.toHtml(solrContentsResp);
										String htmlLessString = Jsoup.parse(wikiTextToHTML).text();
										jElem.getAsJsonObject().addProperty("contents", htmlLessString);

										jsonList.add(jElem.getAsJsonObject());
									}
								}
							}
						}
						this.objwrapper.list = jsonList;
                        this.fetch_News_Results(inputQuery, 1);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    this.objwrapper.socialmedia_response = this.objsocialmedia.sendGet(inputQuery);
                    System.out.println("alternative us " + alternative);
                }

            }
        } catch (SolrServerException ex) {
            ex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this.objwrapper;
    }

    public List<NewsWrapper> fetch_News_Results(String inputQuery, int type) throws SolrServerException {
        configure_Solr_Server(1);
        SolrQuery solrQuery = new SolrQuery();
        //solrQuery.setQuery(inputQuery);
        solrQuery.set("mainQuery", inputQuery);
        solrQuery.setRequestHandler("/better");
        solrQuery.setHighlight(true);
        solrQuery.setRows(Integer.valueOf(10));
        solrQuery.set("wt", new String[]{"application/json"});
        solrQuery.set("indent", true);
        solrQuery.setIncludeScore(true);
        QueryResponse response = null;
        String jsonString = "";

        List<NewsWrapper> objmedia = new ArrayList();

        response = server.query(solrQuery);
        if (response != null) {
            SolrDocumentList solrDocList = response.getResults();
            JSONUtil.toJSON(solrDocList);

            jsonString = JSONUtil.toJSON(solrDocList);
            JsonParser jsonParser = new JsonParser();
            try {

                JsonArray userArr = jsonParser.parse(jsonString).getAsJsonArray();

				if (userArr != null) {
					for (JsonElement jElem : userArr) {
						// refining title not listing out any article with initial
						// wiki-markup in front of it
						String wikiJSONTitle = jElem.getAsJsonObject().get("name").getAsString();
						int indexOfColon = wikiJSONTitle.indexOf(':');
						String markUpText = "";
						if (indexOfColon != -1)
							markUpText = wikiJSONTitle.substring(0, indexOfColon);
						if (!markUpText.equals("") && markUpText.length() < 15) {
							continue;
						} else {
							Gson gson = new Gson();
							NewsWrapper media = (NewsWrapper) gson.fromJson(
									jElem.getAsJsonObject(), NewsWrapper.class);
							objmedia.add(media);
						
						}
					}
				}

                this.objwrapper.news_response = objmedia;
                System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxxx" + this.objwrapper.news_response);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return objmedia;

    }

    public void fetch_Spellchecker_Result(String alternative, int type) {
        System.out.println("fetch_Spellchecker_Result");
        configure_Solr_Server(type);
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.setQuery(alternative);
        solrQuery.setHighlight(true);
        solrQuery.setRows(Integer.valueOf(10));
        solrQuery.set("wt", new String[]{"application/json"});
        solrQuery.set("indent", true);
        solrQuery.setIncludeScore(true);

        QueryResponse response = null;
        this.objwrapper.spellchecker_active = true;
        this.objwrapper.spellcheck_query = alternative;
        try {
            response = this.server.query(solrQuery);
            if (response != null) {
                SolrDocumentList solrDocList = response.getResults();
                JSONUtil.toJSON(solrDocList);

                int totalMatches = (int) solrDocList.getNumFound();
                this.objwrapper.totalresult = totalMatches;
                String jsonString = JSONUtil.toJSON(solrDocList);
                JsonParser jsonParser = new JsonParser();
                try {
                    ArrayList<JsonObject> jsonList = new ArrayList();
                    if (totalMatches > 0) {
                        JsonArray userArr = jsonParser.parse(jsonString).getAsJsonArray();
                        if (userArr != null) {
                            for (JsonElement jElem : userArr) {
                                jsonList.add(jElem.getAsJsonObject());
                            }
                        }
                    }
                    this.objwrapper.list = jsonList;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                this.objwrapper.socialmedia_response = this.objsocialmedia.sendGet(alternative);

                this.objwrapper.news_response = this.fetch_News_Results(alternative, 1);
            }
        } catch (Exception e) {
        }
    }

    public void configure_Solr_Server(int type) {
        try {
            if (type == 0) {
                this.url = "http://localhost:8983/solr/wiki";
            } else if (type == 1) {
                this.url = "http://localhost:8983/solr/wikinews";
            }
            this.server = new HttpSolrServer(this.url);
            this.server.setMaxRetries(1);
            this.server.setConnectionTimeout(20000);
            this.server.setParser(new XMLResponseParser());
            this.server.setSoTimeout(10000);
            this.server.setDefaultMaxConnectionsPerHost(100);
            this.server.setMaxTotalConnections(100);
            this.server.setFollowRedirects(false);
            this.server.setAllowCompression(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String Spellchecker(String inputQuery, int type) {
        System.out.println("I am in spellchekcer");
        String alternative = "";
        try {
            String url = "";

            url = "http://localhost:8983/solr/wiki";

            SolrServer solr = new HttpSolrServer(url);
            ModifiableSolrParams params = new ModifiableSolrParams();

            params.set("qt", new String[]{"/spell"});
            params.set("q", new String[]{inputQuery});
            params.set("spellcheck", new String[]{"on"});
            params.set("spellcheck.build", new String[]{"true"});
            params.set("spellcheck.onlyMorePopular", new String[]{"true"});
            QueryResponse response = solr.query(params);

            
            
            
            SpellCheckResponse spellCheckResponse = response.getSpellCheckResponse();
            System.out.println("spellCheckResponse " + spellCheckResponse.isCorrectlySpelled());
            
            
            if (!spellCheckResponse.isCorrectlySpelled()) {
                List<SpellCheckResponse.Suggestion> suggestion = response
                        .getSpellCheckResponse().getSuggestions();
                ((SpellCheckResponse.Suggestion) suggestion.get(0))
                        .getAlternatives().get(0);
                alternative = (String) ((SpellCheckResponse.Suggestion) suggestion
                        .get(0)).getAlternatives().get(0);
                System.out.println(" First Alternative " + alternative);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alternative;
    }

    public String auto_Spellchecker(String inputQuery) {
        String alternative = "";
        try {
            SolrServer solr = new HttpSolrServer(
                    "http://localhost:8983/solr/#/wiki");
            ModifiableSolrParams params = new ModifiableSolrParams();
            params.set("qt", new String[]{"/spell"});
            params.set("q", new String[]{inputQuery});
            params.set("spellcheck", new String[]{"on"});
            params.set("spellcheck.build", new String[]{"true"});
            params.set("spellcheck.onlyMorePopular", new String[]{"true"});
            QueryResponse response = solr.query(params);

            SpellCheckResponse spellCheckResponse = response
                    .getSpellCheckResponse();
            if (!spellCheckResponse.isCorrectlySpelled()) {
                for (SpellCheckResponse.Suggestion suggestion : response
                        .getSpellCheckResponse().getSuggestions()) {
                    System.out.println(suggestion.getAlternatives());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return alternative;
    }
}
