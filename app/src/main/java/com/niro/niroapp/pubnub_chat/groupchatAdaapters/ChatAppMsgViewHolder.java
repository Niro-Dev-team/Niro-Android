package com.niro.niroapp.pubnub_chat.groupchatAdaapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.niro.niroapp.R;


enum MessageType{
    SENT("Sent"), RECEIVED("Received");
    private String messageType;
    MessageType(String type)
    {
        messageType=type;
    }

    public String getMessageType() {
        return messageType;
    }
}

public class ChatAppMsgViewHolder extends RecyclerView.ViewHolder {

    LinearLayout leftMsgLayout;

    LinearLayout rightMsgLayout;
    LinearLayout textLaytout;

    TextView leftMsgTextView;
    ImageView sent, receive;
    TextView rightMsgTextView;
    TextView SenderName;
    TextView you;
    TextView sentTime,senttime2;
    TextView ReceivedTime;


    public ChatAppMsgViewHolder(View itemView) {
        super(itemView);

        if(itemView!=null) {

            leftMsgTextView =  itemView.findViewById(R.id.chat_left_msg_text_view);
            rightMsgTextView =  itemView.findViewById(R.id.chat_left_msg_text_view);

            SenderName = itemView.findViewById(R.id.senderName);
            sentTime =  itemView.findViewById(R.id.sentTime);

            ReceivedTime= itemView.findViewById(R.id.sentTime);
            sent = itemView.findViewById(R.id.sentImage);
            receive = itemView.findViewById(R.id.sentImage);

        }
    }
}