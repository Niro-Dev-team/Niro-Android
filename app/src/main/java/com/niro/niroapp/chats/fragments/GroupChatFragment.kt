package com.niro.niroapp.chats.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.niro.niroapp.R
import com.niro.niroapp.chats.viewmodels.GroupChatViewModel
import com.niro.niroapp.databinding.GroupChatFragmentBinding

class GroupChatFragment : Fragment() {

    private lateinit var bindingGroupChatFragment : GroupChatFragmentBinding
    private var groupChatViewModel : GroupChatViewModel? = null


    companion object {
        fun newInstance() = GroupChatFragment()
    }

    private lateinit var viewModel: GroupChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.group_chat_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(GroupChatViewModel::class.java)
        // TODO: Use the ViewModel
    }

}