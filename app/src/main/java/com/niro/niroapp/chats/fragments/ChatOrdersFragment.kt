package com.niro.niroapp.chats.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.niro.niroapp.Niro
import com.niro.niroapp.R
import com.niro.niroapp.chats.viewmodels.ChatOrdersViewModel
import com.niro.niroapp.chats.viewmodels.factories.ChatOrderViewModelFactory
import com.niro.niroapp.databinding.ChatOrdersFragmentBinding
import com.niro.niroapp.models.responsemodels.ChatOrderItem
import com.niro.niroapp.utils.FragmentUtils
import com.niro.niroapp.utils.NiroAppUtils
import com.niro.niroapp.utils.RemoveOrderItemFromListListener
import androidx.databinding.library.baseAdapters.BR

class ChatOrdersFragment : Fragment(),RemoveOrderItemFromListListener {

    private lateinit var bindingChatOrdersFragment : ChatOrdersFragmentBinding

    private lateinit var viewModel: ChatOrdersViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingChatOrdersFragment = DataBindingUtil.inflate(inflater,R.layout.chat_orders_fragment, container, false)
        bindingChatOrdersFragment.lifecycleOwner = viewLifecycleOwner
        NiroAppUtils.clearViewModel(requireActivity())
        return bindingChatOrdersFragment.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ChatOrderViewModelFactory().getViewModel(null,requireActivity())

        initializeChatOrders()
        initializeListeners()

    }

    private fun initializeListeners() {
        bindingChatOrdersFragment.btnSubmit.setOnClickListener { createChatOrders() }
        bindingChatOrdersFragment.fbAddOrder.setOnClickListener { showAddOrderLayout() }
    }

    private fun showAddOrderLayout() {

    }

    private fun createChatOrders() {

    }

    private fun initializeChatOrders() {
        val layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
       FragmentUtils.initializeRecyclerView(bindingChatOrdersFragment.rvOrders,layoutManager,viewModel.getAdapter(),
           hashMapOf(BR.removeItemListener to this))
    }

    override fun removeOrderItem(item: ChatOrderItem) {
        viewModel.removeItemFromList(item)
    }

}