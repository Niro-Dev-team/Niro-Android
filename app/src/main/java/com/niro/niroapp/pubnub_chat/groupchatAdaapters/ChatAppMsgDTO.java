package com.niro.niroapp.pubnub_chat.groupchatAdaapters;

public class ChatAppMsgDTO {

    public final static String MSG_TYPE_SENT = "Sent";

    public final static String MSG_TYPE_RECEIVED = "Received";
    public final  static String TXT_MSG= "Text";
    public final static String MEDIA_MSG="media";

    // Message content.
    private String msgContent;

    // Message type.
    private String msgType;
    private String Sender;
    private String time;
    private String typeOfMsg="";

    private String fileName;
    private String url;

    private String senderName;

    private String contentType;


    public ChatAppMsgDTO(String msgType, String msgContent, String Sender, String time) {

        this.msgType = msgType;
        this.msgContent = msgContent;
        this.Sender = Sender;
        this.time =time;

    }
    public ChatAppMsgDTO(String url, String senderName, String msgType, String media, String time) {

        this.url = url;
        this.senderName = senderName;
        this.msgType = msgType;
        this.typeOfMsg=media;
        this.time =time;


    }

    public static String getMsgTypeSent() {
        return MSG_TYPE_SENT;
    }

    public static String getMsgTypeReceived() {
        return MSG_TYPE_RECEIVED;
    }

    public static String getTxtMsg() {
        return TXT_MSG;
    }

    public static String getMediaMsg() {
        return MEDIA_MSG;
    }

    public String getTypeOfMsg() {
        return typeOfMsg;
    }

    public void setTypeOfMsg(String typeOfMsg) {
        this.typeOfMsg = typeOfMsg;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
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



    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }
    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }
}
