package com.heramb.newsgateway;

import java.io.Serializable;


public class Article implements Serializable {
String atclAuthor;
String atclTitle;
String atclDescription;
String atclUrlToImage;
String atclPublishedAt;
String atclUrl;

    public String getAtclUrl() {
        return atclUrl;
    }

    public void setAtclUrl(String atclUrl) {
        this.atclUrl = atclUrl;
    }

    public String getAtclAuthor() {
        return atclAuthor;
    }

    public void setAtclAuthor(String atclAuthor) {
        this.atclAuthor = atclAuthor;
    }

    public String getAtclTitle() {
        return atclTitle;
    }

    public void setAtclTitle(String atclTitle) {
        this.atclTitle = atclTitle;
    }

    public String getAtclDescription() {
        return atclDescription;
    }

    public void setAtclDescription(String atclDescription) {
        this.atclDescription = atclDescription;
    }

    public String getAtclUrlToImage() {
        return atclUrlToImage;
    }

    public void setAtclUrlToImage(String atclUrlToImage) {
        this.atclUrlToImage = atclUrlToImage;
    }

    public String getAtclPublishedAt() {
        return atclPublishedAt;
    }

    public void setAtclPublishedAt(String atclPublishedAt) {
        this.atclPublishedAt = atclPublishedAt;
    }
}
