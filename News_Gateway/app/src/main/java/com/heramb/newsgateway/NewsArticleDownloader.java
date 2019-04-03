package com.heramb.newsgateway;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class NewsArticleDownloader extends AsyncTask<String, Integer, String> {
    private static final String TAG = "NewsArticleDownloader";
    private String sourceId;
    private NewsService service;
    private String API_KEY ="ab27203eb1264564a2bbd3de08299f2d";
    private String ARTICLE_QUERY_1 = "https://newsapi.org/v2/top-headlines?sources=";
    private String ARTICLE_QUERY_2 = "&apiKey="+API_KEY;
    private Uri.Builder buildURL = null;
    private StringBuilder stringBuilder;
    private boolean blank =false;
    boolean isBlank =true;
    private ArrayList<Article> articleArrayList = new ArrayList <Article>();

    public NewsArticleDownloader(NewsService service, String sourceId){
        this.sourceId = sourceId;
        this.service= service;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        service.setArticles(articleArrayList);
    }

    @Override
    protected String doInBackground(String... strings) {
        String query ="";

        query = ARTICLE_QUERY_1+sourceId+ARTICLE_QUERY_2;

        buildURL = Uri.parse(query).buildUpon();
        connectToAPI();
        if(!isBlank) {
            parseJSON1(stringBuilder.toString());
        }
        return null;
    }


    public void connectToAPI() {

        String urlToUse = buildURL.build().toString();
        stringBuilder = new StringBuilder();
        try {
            URL url = new URL(urlToUse);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            if(httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_NOT_FOUND)
            {
                blank =true;
            }
            else {
                httpURLConnection.setRequestMethod("GET");
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader reader = new BufferedReader((new InputStreamReader(inputStream)));

                String line=null;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append('\n');
                }
                isBlank =false;

            }
        }
        catch(FileNotFoundException fe){
            Log.d(TAG, "FileNotFoundException ");
        }
        catch (Exception e) {
            //e.printStackTrace();
            Log.d(TAG, "Exception doInBackground: " + e.getMessage());
        }
    }


    private void parseJSON1(String s) {
        try{
            if(!blank){
                JSONObject jObjMain = new JSONObject(s);
                JSONArray articleArray = jObjMain.getJSONArray("articles");
                for(int i=0;i<articleArray.length();i++){
                    JSONObject article = (JSONObject) articleArray.get(i);
                    Article artObj = new Article();
                    artObj.setAtclAuthor(article.getString("author"));
                    artObj.setAtclDescription(article.getString("description"));
                    artObj.setAtclPublishedAt(article.getString("publishedAt"));
                    System.out.println(article.getString("publishedAt"));
                    artObj.setAtclTitle(article.getString("title"));
                    artObj.setAtclUrlToImage(article.getString("urlToImage"));
                    artObj.setAtclUrl(article.getString("url"));
                    articleArrayList.add(artObj);
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
