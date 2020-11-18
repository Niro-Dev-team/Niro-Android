package com.niro.niroapp.chats.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.niro.niroapp.adapters.GenericRecyclerAdapter
import com.niro.niroapp.adapters.viewholders.GenericViewHolder
import com.niro.niroapp.chats.adapters.viewholders.ChatOrderViewHolder
import com.niro.niroapp.databinding.CardChatOrderItemBinding
import com.niro.niroapp.models.responsemodels.ChatOrderItem
import com.niro.niroapp.utils.FilterResultsListener

class ChatOrderAdapter(private val layoutRes : Int,
                       private var dataList: MutableList<ChatOrderItem>?,
                       private val filterResultListener: FilterResultsListener<ChatOrderItem>
) : GenericRecyclerAdapter<ChatOrderItem>(dataList,filterResultListener) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<ChatOrderItem> {

        val inflater = LayoutInflater.from(parent.context)
        val bindingChatOrderItem = DataBindingUtil.inflate<CardChatOrderItemBinding>(inflater,layoutRes,parent,false)
        return ChatOrderViewHolder(bindingChatOrderItem,getVariablesMap())

    }


    fun notifyNewItemInserted(position : Int) {
        notifyItemInserted(position)
    }


    fun notifyItemRemovedFromList(position: Int) {
        notifyItemRemoved(position)
    }
}