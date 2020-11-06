package com.niro.niroapp.chats.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.niro.niroapp.R
import com.niro.niroapp.chats.adapters.GroupsAdapter
import com.niro.niroapp.chats.viewmodels.repositories.GetAllGroupsForIndustryRepository
import com.niro.niroapp.models.APIResponse
import com.niro.niroapp.models.responsemodels.CommodityItem

import com.niro.niroapp.models.responsemodels.UserGroup
import com.niro.niroapp.utils.FilterResultsListener
import com.niro.niroapp.viewmodels.ListViewModel

class GroupListViewModel : ListViewModel(), FilterResultsListener<UserGroup> {

    private var groups = ArrayList<UserGroup>().toMutableList()

    private lateinit var groupsAdapter : GroupsAdapter


    init {
        groupsAdapter = GroupsAdapter(getViewType(),groups,this)
    }


    fun getGroups() = groups
    fun getGroupsAdapter() = groupsAdapter


    override fun getViewType(): Int {
        return R.layout.card_group_list_item
    }

    override fun updateList() {
        groupsAdapter.updateList(groups)

    }


    fun fetchAllGroups(industry : String,context : Context) : MutableLiveData<APIResponse> {
        return GetAllGroupsForIndustryRepository(industry = industry,context = context).getResponse()
    }

    override fun onResultsFiltered(filteredList: List<UserGroup>) {

    }
}