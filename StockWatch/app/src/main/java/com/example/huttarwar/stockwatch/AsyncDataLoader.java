package com.example.huttarwar.stockwatch;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class AsyncDataLoader extends AsyncTask<String,Void,String>{

    private static final String TAG ="" ;
    private static String name;
    private MainActivity mainActivity;
    private int count;
    private static String stocksinfodownload = "https://api.iextrading.com/1.0/stock/";
    public AsyncDataLoader (MainActivity ma) {
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
            mainActivity.no_Data_Found();

        }
        else
        {
            String clean = sb.replaceAll("//", "");
            System.out.print("Sb data after clean:"+clean);
            TemporaryStocks temporaryStocksObject = parseJSON(clean);
            mainActivity.temporary_choose_Symbol(temporaryStocksObject);
        }
    }

    @Override
    protected String doInBackground(String... params)
    {
        Uri symbolUri = Uri.parse(stocksinfodownload + params[0] +"/quote");
        name = params[1];

        String urlToUse = symbolUri.toString();
        StringBuilder sb = new StringBuilder();
        try {
            URL url = new URL(urlToUse);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader((new InputStreamReader(is)));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        catch (Exception e)
        {
            return null;
        }

        Log.d(TAG, "inside doinBackground");
        return sb.toString();
    }


    private TemporaryStocks parseJSON(String s)
    {
        try
        {
            JSONObject jsonObject = new JSONObject(s);
            count = jsonObject.length();
            Log.d(TAG, String.valueOf(count));
            String symbol = jsonObject.getString("symbol");
            String value = jsonObject.getString("latestPrice");
            String updown = jsonObject.getString("change");
            String change = jsonObject.getString("change");
            String changePercent = jsonObject.getString("changePercent");
            TemporaryStocks StockObject = new TemporaryStocks(symbol, name, value, updown, change, changePercent);
            return StockObject;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}