package com.niro.niroapp.chats.viewmodels.factories

import androidx.lifecycle.ViewModel
import com.niro.niroapp.chats.viewmodels.ChatOrdersViewModel
import com.niro.niroapp.viewmodels.factories.AbstractViewModelFactory

class ChatOrderViewModelFactory : AbstractViewModelFactory<ChatOrdersViewModel>() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = ChatOrdersViewModel() as T
}