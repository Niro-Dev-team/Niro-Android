package com.niro.niroapp.pubnub_chat.groupchatAdaapters.viewholders;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.niro.niroapp.R;
import com.niro.niroapp.pubnub_chat.Login;
import com.niro.niroapp.pubnub_chat.groupchat.GroupChatActivity;
import com.niro.niroapp.pubnub_chat.groupchatAdaapters.ChatAppMsgDTO;
import com.niro.niroapp.pubnub_chat.groupchatAdaapters.FullScreenImage;


public class SentMessageViewHolder extends RecyclerView.ViewHolder {




    ImageView sent;
    TextView rightMsgTextView;
    Context context;
    TextView SenderName;
    TextView you;
    TextView sentTime;
    TextView ReceivedTime;
    public SentMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        if(itemView!=null) {



            rightMsgTextView =  itemView.findViewById(R.id.chat_left_msg_text_view);
            sentTime= itemView.findViewById(R.id.sentTime);
            sent = itemView.findViewById(R.id.sentImage);

        }
    }
    public void initializeView(ChatAppMsgDTO msgDto)
    {
        if (msgDto.getTypeOfMsg().equals("media"))
        {
            //   Log.e("opened","opend2");


            sent.setVisibility(View.VISIBLE);


            sentTime.setText(msgDto.getTime());
            final String url =msgDto.getUrl();
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .error(R.mipmap.ic_launcher_round);
            Glide.with(GroupChatActivity.context)
                    .load(url)
                    .apply(options).into(sent);
          //  sent.setTag();
            sent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    Intent intent = new Intent(view.getContext(), FullScreenImage.class);

                    intent.putExtra("url", msgDto.getUrl());
                    view.getContext().startActivity(intent);

                }
            });

        }
        else {
            Log.e("opened","opend3");

            rightMsgTextView.setText(msgDto.getMsgContent());

            sentTime.setText(msgDto.getTime());
            // Remove left linear layout.The value should be GONE, can not be INVISIBLE
            // Otherwise each iteview's distance is too big.


            rightMsgTextView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    //pass the 'context' here
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                    alertDialog.setTitle("Do You want to delete this message");
                    alertDialog.setMessage("?");
                    alertDialog.setPositiveButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                          //  msgDto.remove(position);
                        }
                    });

                    AlertDialog dialog = alertDialog.create();
                    dialog.show();
                    return false;
                }
            });

        }


    }
}
