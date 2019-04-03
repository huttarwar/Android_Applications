package com.example.heramb.know_your_govt;

import java.io.Serializable;
import java.util.HashMap;

public class Official_Person implements Serializable {

    private String nameofofficial;
    private String postofofficial;
    private String party;
    private String line1;
    private String line2;
    private String city;
    private String state;

    private String emails;
    private String websites;

    private String zipcode;
    private String phones;
    private String urls;
    private String photoUrls;
    HashMap<String,String> channels;

    private String zipcodedoinback;


    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getPhotoUrls() {
        return photoUrls;
    }

    public void setPhotoUrls(String photoUrls) {
        this.photoUrls = photoUrls;
    }

    public String getPhones() {
        return phones;
    }

    public void setPhones(String phones) {
        this.phones = phones;
    }

    public Official_Person(String city, String state, String zipcode) {
        this.city = city;
        this.state = state;
        this.zipcode = zipcode;
    }

    public Official_Person(String postofofficial) {
        this.postofofficial = postofofficial;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public void setZipcodedoinback(String zipcode) {
        this.zipcodedoinback = zipcode;
    }

    public String getZipcodeDoinback(){
        return zipcodedoinback;
    }

    private static int ctr =1;

    public Official_Person(){}


    public String getNameofofficial() {
        return nameofofficial;
    }

    public void setNameofofficial(String nameofofficial) {
        this.nameofofficial = nameofofficial;
    }

    public String getPostofofficial() {
        return postofofficial;
    }

    public void setPostofofficial(String postofofficial) {
        this.postofofficial = postofofficial;
    }

    public String getParty() {
        return party;
    }

    public void setParty(String party) {
        this.party = party;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getCity() {
        return city;
    }

    public HashMap<String, String> getChannels() {
        return channels;
    }

    public void setChannels(HashMap<String, String> channels) {
        this.channels = channels;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public String getWebsites() {
        return websites;
    }

    public void setWebsites(String websites) {
        this.websites = websites;
    }
}
