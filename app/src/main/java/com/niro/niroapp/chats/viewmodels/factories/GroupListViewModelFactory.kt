package com.niro.niroapp.chats.viewmodels.factories

import androidx.lifecycle.ViewModel
import com.niro.niroapp.chats.viewmodels.GroupListViewModel
import com.niro.niroapp.viewmodels.CommoditiesViewModel
import com.niro.niroapp.viewmodels.factories.AbstractViewModelFactory

class GroupListViewModelFactory : AbstractViewModelFactory<GroupListViewModel>() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T = GroupListViewModel() as T
}