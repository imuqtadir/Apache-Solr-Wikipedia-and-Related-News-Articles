package com.mycompany.solr_web_application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpURLConnectionExample
{
  private final String USER_AGENT = "Mozilla/5.0";

  public static void main(String[] args)
    throws Exception
  {
    HttpURLConnectionExample http = new HttpURLConnectionExample();

    System.out.println("Testing 1 - Send Http GET request");
    http.sendGet();
  }

  private void sendGet()
    throws Exception
  {
    String url = "https://graph.facebook.com/search?q=india%20pakistan&type=post&locale=en_US&access_token=775413162487046|SsJJQCRnIh-gM9R7FeBIsEFJhc8";

    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection)obj.openConnection();
    con.setRequestMethod("GET");
    con.setRequestProperty("User-Agent", "Mozilla/5.0");
    int responseCode = con.getResponseCode();
    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));

    StringBuffer response = new StringBuffer();
    String inputLine;
    while ((inputLine = in.readLine()) != null)
    {
      System.out.println(inputLine);
      response.append(inputLine);
    }
    in.close();
  }
}