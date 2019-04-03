package com.heramb.newsgateway;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class NewsService extends Service {

    private static final String TAG = "NewsService";
    private boolean isArticle_run = true;
    private ServiceReceiver serviceReceiver;
    private ArrayList<Article> listofarticles = new ArrayList <Article>();

    public NewsService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        serviceReceiver = new ServiceReceiver();
        IntentFilter filter1 = new IntentFilter(MainActivity.ACTION_MSG_TO_SERVICE);
        registerReceiver(serviceReceiver, filter1);

        new Thread(new Runnable() {
            @Override
            public void run() {

                while (isArticle_run) {
                    while(listofarticles.isEmpty()){
                        try {
                            Thread.sleep(250);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    Intent intent = new Intent();
                    intent.setAction(MainActivity.ACTION_NEWS_STORY);
                    intent.putExtra(MainActivity.ARTICLE_LIST, listofarticles);
                    sendBroadcast(intent);
                    listofarticles.clear();
                }
                Log.i(TAG, "NewsService was properly stopped");
            }
        }).start();

        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        isArticle_run = false;
        Toast.makeText(this, "NewsService Stopped", Toast.LENGTH_SHORT).show();
    }

    public void setArticles(ArrayList<Article> list){
        listofarticles.clear();
        listofarticles.addAll(list);

    }

    class ServiceReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            switch (intent.getAction()) {
                case MainActivity.ACTION_MSG_TO_SERVICE:
                    String sourceId ="";
                    String temp="";
                    if (intent.hasExtra(MainActivity.SOURCE_ID)) {
                        sourceId = intent.getStringExtra(MainActivity.SOURCE_ID);
                        temp=sourceId.replaceAll(" ","-");
                    }

                    new NewsArticleDownloader(NewsService.this, temp).execute();
                    break;
            }

        }
    }
}


