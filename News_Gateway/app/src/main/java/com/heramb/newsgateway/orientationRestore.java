package com.heramb.newsgateway;

import java.io.Serializable;
import java.util.ArrayList;

public class orientationRestore implements Serializable {
    private ArrayList<SourceOfNews> source_OfNews_List = new ArrayList<SourceOfNews>();
    private ArrayList<Article> article_List = new ArrayList <Article>();
    private ArrayList<String> categories = new ArrayList <String>();
    private int curSource;
    private int curArticle;

    public ArrayList <SourceOfNews> getSource_OfNews_List() {
        return source_OfNews_List;
    }

    public void setSource_OfNews_List(ArrayList <SourceOfNews> source_OfNews_List) {
        this.source_OfNews_List = source_OfNews_List;
    }

    public ArrayList <Article> getArticle_List() {
        return article_List;
    }

    public void setArticle_List(ArrayList <Article> article_List) {
        this.article_List = article_List;
    }

    public ArrayList <String> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList <String> categories) {
        this.categories = categories;
    }

    public int getCurSource() {
        return curSource;
    }

    public void setCurSource(int curSource) {
        this.curSource = curSource;
    }

    public int getCurArticle() {
        return curArticle;
    }

    public void setCurArticle(int curArticle) {
        this.curArticle = curArticle;
    }
}
