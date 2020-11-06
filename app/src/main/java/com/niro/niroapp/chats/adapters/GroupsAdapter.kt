package com.niro.niroapp.chats.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.niro.niroapp.adapters.GenericRecyclerAdapter
import com.niro.niroapp.adapters.viewholders.GenericViewHolder
import com.niro.niroapp.chats.adapters.viewholders.GroupViewHolder
import com.niro.niroapp.databinding.CardContactBinding
import com.niro.niroapp.databinding.CardGroupListItemBinding
import com.niro.niroapp.models.responsemodels.UserGroup

import com.niro.niroapp.users.adapters.viewholders.ContactViewHolder
import com.niro.niroapp.utils.FilterResultsListener

class GroupsAdapter(private val layoutRes : Int,
                    private var dataList: MutableList<UserGroup>?,
                    private val filterResultListener: FilterResultsListener<UserGroup>
) : GenericRecyclerAdapter<UserGroup>(dataList,filterResultListener) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<UserGroup> {

        val inflater = LayoutInflater.from(parent.context)
        val bindingGroupListItem = DataBindingUtil.inflate<CardGroupListItemBinding>(inflater,layoutRes,parent,false)
        return GroupViewHolder(bindingGroupListItem,getVariablesMap())

    }
}