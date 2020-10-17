package com.niro.niroapp.pubnub_chat.groupchatAdaapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


import com.niro.niroapp.R;
import com.niro.niroapp.pubnub_chat.groupchatAdaapters.viewholders.ReceivedMessageHolder;
import com.niro.niroapp.pubnub_chat.groupchatAdaapters.viewholders.SentMessageViewHolder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class ChatAppMsgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int MESSAGE_TYPE_SENT = 1;
    private static final int MESSAGE_TYPE_RECEVIED = 2;

    private List<ChatAppMsgDTO> msgDtoList = null;
    Context context;

    public ChatAppMsgAdapter(List<ChatAppMsgDTO> msgDtoList, Context context) {
        this.msgDtoList = msgDtoList;
        this.context = context;

    }

    @Override
    public void onBindViewHolder(@NotNull RecyclerView.ViewHolder holder, final int position) {
        final ChatAppMsgDTO msgDto = msgDtoList.get(position);
        holder.setIsRecyclable(false);

        // If the message is a received message.
        if (holder instanceof SentMessageViewHolder)
        {
            SentMessageViewHolder sentMessageViewHolder = (SentMessageViewHolder) holder;
            sentMessageViewHolder.initializeView(msgDto);

        }
        else {
            ReceivedMessageHolder receivedMessageHolder = (ReceivedMessageHolder) holder;
            receivedMessageHolder.initializeView(msgDto);
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        switch (viewType)
        {
            case MESSAGE_TYPE_SENT:
                View viewSent = layoutInflater.inflate(R.layout.card_chat_sender, parent, false);
                return new SentMessageViewHolder(viewSent);
            default:
                View viewDefault = layoutInflater.inflate(R.layout.card_chat_other_user_normal, parent, false);
                return new ReceivedMessageHolder(viewDefault);
        }


    }

    @Override
    public int getItemCount() {
        if(msgDtoList==null)
        {
            msgDtoList = new ArrayList<ChatAppMsgDTO>();
        }
        return msgDtoList.size();
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {

        ChatAppMsgDTO chatAppMsg = msgDtoList.get(position);


        return chatAppMsg.getMsgType().equalsIgnoreCase(MessageType.SENT.getMessageType()) ? MESSAGE_TYPE_SENT : MESSAGE_TYPE_RECEVIED ;
    }
}
