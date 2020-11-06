package com.niro.niroapp.chats.adapters.viewholders

import androidx.databinding.library.baseAdapters.BR
import com.niro.niroapp.adapters.viewholders.GenericViewHolder
import com.niro.niroapp.databinding.CardGroupListItemBinding
import com.niro.niroapp.models.responsemodels.UserGroup


class GroupViewHolder(private val viewBinding : CardGroupListItemBinding, private val  variables : HashMap<Int,Any?>)  : GenericViewHolder<UserGroup>(viewBinding){


    override fun bind(item: UserGroup, position: Int) {

        variables[BR.userGroup] = item
        viewBinding.cardGroupItem.setOnClickListener { viewBinding.itemClickListener?.onItemClick(item) }
    }

}