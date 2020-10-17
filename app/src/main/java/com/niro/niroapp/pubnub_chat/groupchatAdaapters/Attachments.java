package com.niro.niroapp.pubnub_chat.groupchatAdaapters;

public class Attachments {
    private String fileName;
    private String url;

    private String senderName;
    private String who;

    public Attachments(String fileName, String url, String senderName, String who) {
        this.fileName = fileName;
        this.url = url;
        this.senderName = senderName;
        this.who = who;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }
}
