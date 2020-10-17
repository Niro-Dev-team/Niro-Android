package com.niro.niroapp.pubnub_chat.groupchatAdaapters.viewholders;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.niro.niroapp.R;
import com.niro.niroapp.pubnub_chat.groupchat.GroupChatActivity;
import com.niro.niroapp.pubnub_chat.groupchatAdaapters.ChatAppMsgDTO;
import com.niro.niroapp.pubnub_chat.groupchatAdaapters.FullScreenImage;


public class ReceivedMessageHolder extends RecyclerView.ViewHolder {



    TextView leftMsgTextView;
    ImageView sent, receive;
    TextView rightMsgTextView;
    Context context;
    TextView SenderName;
    TextView you;
    TextView sentTime,senttime2;
    TextView ReceivedTime;
    public ReceivedMessageHolder(@NonNull View itemView) {
        super(itemView);
        if(itemView!=null) {


            leftMsgTextView =  itemView.findViewById(R.id.chat_left_msg_text_view);
            SenderName = itemView.findViewById(R.id.senderName);
            ReceivedTime= itemView.findViewById(R.id.reciveTime);
            receive = itemView.findViewById(R.id.receiveImage);

        }
    }

    public void initializeView(ChatAppMsgDTO msgDto)
    {
        if (msgDto.getTypeOfMsg().equals("media")) {
            //   Log.e("opened","opend2");

            leftMsgTextView.setVisibility(View.GONE);
            SenderName.setText(msgDto.getSenderName());
            receive.setVisibility(View.VISIBLE);
            ReceivedTime.setText(msgDto.getTime());
            final String url = msgDto.getUrl();
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);

            Glide.with(GroupChatActivity.context)
                    .load(url)
                    .apply(options).into(receive);
            receive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), FullScreenImage.class);

                    intent.putExtra("url", msgDto.getUrl());
                    view.getContext().startActivity(intent);
                }
            });
        } else {

            leftMsgTextView.setText(msgDto.getMsgContent());
            SenderName.setText(msgDto.getSender());
            ReceivedTime.setText(msgDto.getTime());


        }


    }
}
