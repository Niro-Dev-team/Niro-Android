package com.niro.niroapp.chats.viewmodels

import androidx.lifecycle.ViewModel
import com.niro.niroapp.R
import com.niro.niroapp.chats.adapters.ChatOrderAdapter
import com.niro.niroapp.models.responsemodels.ChatOrderItem
import com.niro.niroapp.utils.FilterResultsListener
import com.niro.niroapp.viewmodels.ListViewModel

class ChatOrdersViewModel : ListViewModel(),FilterResultsListener<ChatOrderItem> {

    private var ordersList = ArrayList<ChatOrderItem>().toMutableList()

    private var adapter : ChatOrderAdapter

    init {
        adapter = ChatOrderAdapter(getViewType(),ordersList,this)
    }

    override fun getViewType(): Int  = R.layout.card_chat_order_item

    fun setOrdersList(ordersList : MutableList<ChatOrderItem>) {
        this.ordersList = ordersList
    }

    fun insertNewItem(item : ChatOrderItem) {
        this.ordersList.add(item)
        adapter.notifyNewItemInserted(this.ordersList.size - 1)
    }

    fun removeItemFromList(item : ChatOrderItem) {
        val index = ordersList.indexOfFirst { it.orderId == item.orderId }
        if(index >= 0) adapter.notifyItemRemovedFromList(index)
    }

    override fun updateList() = adapter.updateList(ordersList)

    fun getAdapter() = adapter

    override fun onResultsFiltered(filteredList: List<ChatOrderItem>) {

    }

}