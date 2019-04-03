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


public class NewsSourceDownloader extends AsyncTask<String, Integer, String> {

    private static final String TAG = "NewsSourceDownloader";
    private StringBuilder stringBuilder;
    private boolean Blank =false;
    boolean isBlank =true;
    private MainActivity mainActivity;
    private String category;
    private Uri.Builder buildURL = null;
    private ArrayList<SourceOfNews> sourceOfNewsArrayList = new ArrayList <SourceOfNews>();
    private ArrayList<String> categoryArrayList = new ArrayList <String>();
    private String API_KEY ="ab27203eb1264564a2bbd3de08299f2d";
    private String NewsorgAPI;

    public NewsSourceDownloader(MainActivity ma, String category){
        mainActivity = ma;
        if(category.equalsIgnoreCase("all") || category.equalsIgnoreCase("")) {
            this.category = "";
            NewsorgAPI ="https://newsapi.org/v2/sources?language=en&country=us&apiKey="+API_KEY;
        }
        else
        {
            String api1= "https://newsapi.org/v2/sources?country=us&category=";
            String api2 ="&apiKey="+API_KEY;
            NewsorgAPI = api1+category+api2;
            this.category = category;
        }

    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        for(int j = 0; j< sourceOfNewsArrayList.size(); j++)
        {
            String temp = sourceOfNewsArrayList.get(j).getSrcCategory();
            if(!categoryArrayList.contains(temp))
                categoryArrayList.add(temp);
        }
        mainActivity.setSources(sourceOfNewsArrayList, categoryArrayList);
    }

    @Override
    protected String doInBackground(String... strings) {

        buildURL = Uri.parse(NewsorgAPI).buildUpon();
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
                Blank =true;
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
        if(!Blank){
            JSONObject jsonObject = new JSONObject(s);
            JSONArray sourcesArray = jsonObject.getJSONArray("sources");
            for(int i=0;i<sourcesArray.length();i++){
                JSONObject src = (JSONObject) sourcesArray.get(i);
                SourceOfNews srcObj = new SourceOfNews();
                srcObj.setSrcId(src.getString("id"));
                srcObj.setSrcCategory(src.getString("category"));
                srcObj.setSrcName(src.getString("name"));
                srcObj.setSrcUrl(src.getString("url"));
                sourceOfNewsArrayList.add(srcObj);
            }
        }
    }
    catch (Exception e){
        e.printStackTrace();
    }
    }
}
