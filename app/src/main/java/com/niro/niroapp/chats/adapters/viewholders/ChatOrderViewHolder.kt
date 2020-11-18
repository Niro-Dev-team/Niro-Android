package com.niro.niroapp.chats.adapters.viewholders

import android.view.View
import com.niro.niroapp.adapters.viewholders.GenericViewHolder
import com.niro.niroapp.databinding.CardChatOrderItemBinding
import com.niro.niroapp.models.responsemodels.ChatOrderItem

class ChatOrderViewHolder(private val viewBinding : CardChatOrderItemBinding,private val variables : HashMap<Int,Any?>) : GenericViewHolder<ChatOrderItem>(viewBinding) {

    override fun bind(item: ChatOrderItem, position: Int) {
        viewBinding.chatOrderItem = item
        setVariables(variables)
        initializeUpdateItemListeners(item)
    }

    private fun initializeUpdateItemListeners(item: ChatOrderItem) {

        viewBinding.btnAddQuantity.setOnClickListener { addProductQuantity(item) }
        viewBinding.btnRemoveQuantity.setOnClickListener { decreaseQuantity(item) }
    }

    private fun decreaseQuantity(item: ChatOrderItem) {
        if(getCurrentQuantity() > 0) {
            val currentQuantity = getCurrentQuantity() - 1

            if(currentQuantity == 0) viewBinding.removeItemListener?.removeOrderItem(item)
            else updateQuantityInView(currentQuantity)
        }
    }


    private fun addProductQuantity(item: ChatOrderItem) {
        val currentQuantity = getCurrentQuantity() + 1
        updateQuantityInView(currentQuantity)
    }


    private fun getCurrentQuantity() : Int = viewBinding.tvProductQuantity.text.toString().toInt()

    private fun updateQuantityInView(quantity: Int) {
        viewBinding.tvProductQuantity.text = "$quantity"

    }


}