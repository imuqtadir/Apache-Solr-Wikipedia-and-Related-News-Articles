 package com.mycompany.solr_web_application;

 import com.google.gson.Gson;
 import com.google.gson.JsonArray;
 import com.google.gson.JsonElement;
 import com.google.gson.JsonObject;
 import com.google.gson.JsonParser;
 import java.io.BufferedReader;
 import java.io.InputStreamReader;
 import java.io.PrintStream;
 import java.net.HttpURLConnection;
 import java.net.URL;
 import java.net.URLEncoder;
 import java.util.ArrayList;
 import java.util.Iterator;
 import java.util.List;

 public class Socialmedia
 {
   private final String USER_AGENT = "Mozilla/5.0";

   public static void main(String[] args)
     throws Exception
   {
     Socialmedia http = new Socialmedia();
     http.sendGet("india");
   }

   public List<MediaWrapper> sendGet(String q)
     throws Exception
   {
     String eq = URLEncoder.encode(q, "UTF-8");
     String url = "https://graph.facebook.com/search?q=" + eq + "&type=post&limit=5&access_token=775413162487046|SsJJQCRnIh-gM9R7FeBIsEFJhc8";

     URL obj = new URL(url);
     HttpURLConnection con = (HttpURLConnection)obj.openConnection();
     con.setRequestMethod("GET");
     con.setRequestProperty("User-Agent", "Mozilla/5.0");
     int responseCode = con.getResponseCode();
     BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

     StringBuffer response = new StringBuffer();
     String inputLine;
     while ((inputLine = in.readLine()) != null) {
       response.append(inputLine);
     }
     in.close();
     System.out.println(response.toString());


     JsonElement jelement = new JsonParser().parse(response.toString());
     JsonObject jobject = jelement.getAsJsonObject();
     JsonArray jsonarray = jobject.getAsJsonArray("data");

     Iterator iterator = jsonarray.iterator();

		List<MediaWrapper> objmedia = new ArrayList<MediaWrapper>();
		while (iterator.hasNext()) {
			JsonElement json2 = (JsonElement) iterator.next();
			if (json2 != null && json2.getAsJsonObject()!= null && json2.getAsJsonObject().get("name") != null
					&& !json2.getAsJsonObject().get("name").equals((""))) {
				Gson gson = new Gson();
				MediaWrapper media = (MediaWrapper) gson.fromJson(json2,
						MediaWrapper.class);
				objmedia.add(media);
			}
		}
     return objmedia;
   }
 }