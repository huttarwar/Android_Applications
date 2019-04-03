package com.heramb.newsgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private boolean newsService_Run = false;
    static final String ACTION_MSG_TO_SERVICE = "ACTION_MSG_TO_SERVICE";
    static final String ACTION_NEWS_STORY = "ACTION_NEWS_STORY";
    static final String ARTICLE_LIST = "ARTICLE_LIST";
    static final String SOURCE_ID = "SOURCE_ID";
    private ArrayList<String> source_list = new ArrayList <String>();
    private ArrayList<String> category_list = new ArrayList <String>();
    private ArrayList<SourceOfNews> source_OfNews_InfoList = new ArrayList <SourceOfNews>();
    private ArrayList<Article> article_InfoList = new ArrayList <Article>();
    private HashMap<String, SourceOfNews> source_Map = new HashMap<>();
    private Menu menu;
    private NewsReceiver newsReceiver;
    private String curNewsSource;
    private ArrayAdapter adapter;
    private PagerAdapter pageAdapter;
    private List<Fragment> fragment;
    private ViewPager pager;
    private boolean Flag;
    private int curSourcePointer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        if(!newsService_Run &&  savedInstanceState == null) {
            Intent intent = new Intent(MainActivity.this, NewsService.class);
            startService(intent);
            newsService_Run = true;
        }

        newsReceiver = new NewsReceiver();
        IntentFilter filter = new IntentFilter(MainActivity.ACTION_NEWS_STORY);
        registerReceiver(newsReceiver, filter);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);


        mDrawerList.setOnItemClickListener(
                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        pager.setBackgroundResource(0);
                        curSourcePointer = position;
                        selectItem(position);
                    }
                }
        );

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        );

        adapter = new ArrayAdapter<>(this, R.layout.list_element, source_list);
        mDrawerList.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        fragment = new ArrayList<>();

        pageAdapter = new PagerAdapter(getSupportFragmentManager());
        pager = findViewById(R.id.viewPager);
        pager.setAdapter(pageAdapter);

        if (source_Map.isEmpty() && savedInstanceState == null )
            new NewsSourceDownloader(this, "").execute();

    }


    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(configuration);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {

        if (mDrawerToggle.onOptionsItemSelected(menuItem)) {
            return true;
        }

        new NewsSourceDownloader(this, menuItem.getTitle().toString()).execute();
        mDrawerLayout.openDrawer(mDrawerList);
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    private void selectItem(int i) {
        curNewsSource = source_list.get(i);
        Intent intent = new Intent(MainActivity.ACTION_MSG_TO_SERVICE);
        intent.putExtra(SOURCE_ID, curNewsSource);
        sendBroadcast(intent);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.action_menu, menu);
        this.menu =menu;
        if(Flag){
            this.menu.add("All");
            for (String s : category_list)
                this.menu.add(s);
        }
        return true;
    }

    public void setSources(ArrayList<SourceOfNews> sourceOfNewsList, ArrayList<String> categoryList)
    {
        source_Map.clear();
        source_list.clear();
        source_OfNews_InfoList.clear();

        //category_list.addAll(categoryList);
        source_OfNews_InfoList.addAll(sourceOfNewsList);

        for(int i = 0; i< sourceOfNewsList.size(); i++){
            source_list.add(sourceOfNewsList.get(i).getSrcName());
            source_Map.put(sourceOfNewsList.get(i).getSrcName(), (SourceOfNews) sourceOfNewsList.get(i));
        }

        if(!menu.hasVisibleItems()) {
            category_list.clear();
            category_list =categoryList;
            menu.add("All");
            Collections.sort(categoryList);
            for (String s : categoryList)
                menu.add(s);
        }

        adapter.notifyDataSetChanged();

    }


    private void reInfo(ArrayList<Article> articles) {

        setTitle(curNewsSource);
        for (int i = 0; i < pageAdapter.getCount(); i++)
            pageAdapter.notifyChangeInPosition(i);

        fragment.clear();

        for (int i = 0; i < articles.size(); i++) {
            Article a = articles.get(i);

            fragment.add(ArticleInfo.newInstance(articles.get(i), i, articles.size()));
        }

        pageAdapter.notifyDataSetChanged();
        pager.setCurrentItem(0);
        article_InfoList = articles;
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(newsReceiver);
        Intent intent = new Intent(MainActivity.this, NewsReceiver.class);
        stopService(intent);
        super.onDestroy();
    }




        @Override
    protected void onSaveInstanceState(Bundle outState) {
        orientationRestore layoutRestore = new orientationRestore();
        layoutRestore.setCategories(category_list);
        layoutRestore.setSource_OfNews_List(source_OfNews_InfoList);
        layoutRestore.setCurArticle(pager.getCurrentItem());
        layoutRestore.setCurSource(curSourcePointer);
        layoutRestore.setArticle_List(article_InfoList);
        outState.putSerializable("state", layoutRestore);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        orientationRestore layoutRestore1 = (orientationRestore)savedInstanceState.getSerializable("state");
        Flag = true;
        article_InfoList = layoutRestore1.getArticle_List();
        category_list = layoutRestore1.getCategories();
        source_OfNews_InfoList = layoutRestore1.getSource_OfNews_List();
        for(int i = 0; i< source_OfNews_InfoList.size(); i++){
            source_list.add(source_OfNews_InfoList.get(i).getSrcName());
            source_Map.put(source_OfNews_InfoList.get(i).getSrcName(), (SourceOfNews) source_OfNews_InfoList.get(i));
        }
        mDrawerList.clearChoices();
        adapter.notifyDataSetChanged();
        mDrawerList.setOnItemClickListener(

                new ListView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        pager.setBackgroundResource(0);
                        curSourcePointer = position;
                        selectItem(position);

                    }
                }
        );
        setTitle("News Gateway");

    }

    class NewsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case ACTION_NEWS_STORY:
                    ArrayList<Article> artList;
                    if (intent.hasExtra(ARTICLE_LIST)) {
                        artList = (ArrayList <Article>) intent.getSerializableExtra(ARTICLE_LIST);
                        reInfo(artList);
                    }
                    break;
            }
        }
    }

    private class PagerAdapter extends FragmentPagerAdapter {
        private long baseId = 0;


        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public Fragment getItem(int position) {
            return fragment.get(position);
        }

        @Override
        public int getCount() {
            return fragment.size();
        }

        @Override
        public long getItemId(int position) {
            // give an ID different from position when position has been changed
            return baseId + position;
        }

        /**
         * Notify that the position of article fragment has been changed.
         * Create article new ID for each position to force recreation of the fragment
         * @param n number of items which have been changed
         */
        public void notifyChangeInPosition(int n) {
            // shift the ID returned by getItemId outside the range of all previous fragment
            baseId += getCount() + n;
        }


    }
}

