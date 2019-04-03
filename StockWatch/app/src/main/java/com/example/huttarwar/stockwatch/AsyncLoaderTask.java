package com.example.huttarwar.stockwatch;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class AsyncLoaderTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "";
    private MainActivity mainActivity;
    private int count;
    private String symbol;

    private static String stocksearch = "https://api.iextrading.com/1.0/ref-data/symbols";


    public AsyncLoaderTask(MainActivity ma)
    {
        mainActivity = ma;
    }

    @Override
    protected void onPreExecute()
    {
    }

    @Override
    protected void onPostExecute(String sb)
    {
        if (sb == null)
        {
            mainActivity.no_Symbol_Found();

        }
        else
        {
            //ArrayList<Stocks> stocksArrayList = parseJSON(sb);
            HashMap<String,String> stocksStringHashMap = parseJSON(sb);
            mainActivity.choose_Symbol(stocksStringHashMap);
        }
    }

    @Override
    protected String doInBackground(String... params)
    {
        symbol = params[0];
        Uri symbolUri = Uri.parse(stocksearch);
        //Log.d(TAG, String.valueOf(Uri.parse(stocksearch + params[0])));
        String urlToUse = symbolUri.toString();
        StringBuilder sb = new StringBuilder();
        try
        {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();

            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null)
            {
                sb.append(line).append('\n');

            }
        }
        catch (Exception e)
        {
            return null;
        }
        Log.d(TAG, "Do in Background");

        return sb.toString();
    }


    //private ArrayList<Stocks> parseJSON(String s)
    private HashMap<String,String> parseJSON(String s)
    {
        ArrayList<Stocks> stocksArrayList = new ArrayList<>();
        HashMap<String,String> stringHashMap = new HashMap<>();
        try
        {
            //JSONObject jsonObject = new JSONObject(s);
            JSONArray jsonObject = new JSONArray(s);
            for (int i = 0; i < jsonObject.length(); i++)
            {
                JSONObject jStock = (JSONObject) jsonObject.get(i);
                if(jStock.getString("symbol").startsWith(symbol))
                {
                    String name = jStock.getString("name");
                    String symbol = jStock.getString("symbol");
                    Log.d(TAG, "Inside parseJASON" + i);
                    stocksArrayList.add(new Stocks(symbol, name));
                    stringHashMap.put(symbol,name);
                }
            }
            System.out.println(stringHashMap);
            return stringHashMap;

            //return stocksArrayList;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
