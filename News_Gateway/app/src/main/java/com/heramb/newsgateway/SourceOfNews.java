package com.heramb.newsgateway;

import java.io.Serializable;

public class SourceOfNews implements Serializable {

    String srcId;
    String srcUrl;
    String srcName;
    String srcCategory;

    public String getSrcId() {
        return srcId;
    }

    public void setSrcId(String srcId) {
        this.srcId = srcId;
    }

    public String getSrcUrl() {
        return srcUrl;
    }

    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }

    public String getSrcName() {
        return srcName;
    }

    public void setSrcName(String srcName) {
        this.srcName = srcName;
    }

    public String getSrcCategory() {
        return srcCategory;
    }

    public void setSrcCategory(String srcCategory) {
        this.srcCategory = srcCategory;
    }
}
