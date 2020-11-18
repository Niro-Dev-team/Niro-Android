package com.niro.niroapp.chats.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import carbon.dialog.ProgressDialog
import com.google.gson.Gson
import com.niro.niroapp.R
import com.niro.niroapp.chats.viewmodels.GroupListViewModel
import com.niro.niroapp.chats.viewmodels.factories.GroupListViewModelFactory
import com.niro.niroapp.databinding.ListOfGroupsBinding
import com.niro.niroapp.models.APIError
import com.niro.niroapp.models.APILoader
import com.niro.niroapp.models.APIResponse
import com.niro.niroapp.models.Success
import com.niro.niroapp.models.responsemodels.ChatMessage      


b
import com.niro.niroapp.models.responsemodels.UserGroup
import com.niro.niroapp.utils.ItemClickListener
import com.niro.niroapp.utils.NiroAppUtils
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.pubnub.api.callbacks.PNCallback
import java.util.*
import kotlin.collections.HashMap


class GroupListFragment : Fragment(),ItemClickListener {

    private lateinit var bindingGroupListFragment : ListOfGroupsBinding

    private var groupsListViewModel : GroupListViewModel? = null

    private var mProgressDialog : ProgressDialog? = null
    private lateinit var mPubNub : PubNub;


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        bindingGroupListFragment = DataBindingUtil.inflate(inflater,R.layout.list_of_groups,container,false)

        bindingGroupListFragment.lifecycleOwner = this

        return bindingGroupListFragment.root

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        groupsListViewModel = GroupListViewModelFactory().getViewModel(null,requireActivity())

        initializeRecyclerView()
        fetchGroupsList()
    }

    private fun fetchGroupsList() {
        val currentUserIndustry = NiroAppUtils.getCurrentUser(requireContext()).industry
        groupsListViewModel?.fetchAllGroups(currentUserIndustry ?: "Agriculture",requireContext())?.observe(viewLifecycleOwner, Observer<APIResponse> { response ->
            when (response) {
                is APILoader -> mProgressDialog = context?.let {
                    NiroAppUtils.showLoaderProgress(
                        message = getString(R.string.fetching_groups),
                        context = it
                    )
                }

                is APIError -> {
                    if(mProgressDialog != null && mProgressDialog?.isShowing == true) mProgressDialog?.dismiss()
                    NiroAppUtils.showSnackbar(
                        message = response.errorMessage,
                        root = bindingGroupListFragment.root
                    )
                }

                is Success<*> -> {

                    context?.let {
                       subscribeUserToGroupsAndFetchGroupMessageCounts(response.data as? List<UserGroup>)
                    }
                    findNavController().popBackStack()

                }

            }
        })
    }

    private fun subscribeUserToGroupsAndFetchGroupMessageCounts(groups: List<UserGroup>?) {
        mPubNub = NiroAppUtils.getPubNubObject(requireActivity()) ?: getPubNub()
        subscribeToChannels(groups)
        fetchLastMessage(groups)

    }

    private fun fetchLastMessage(groups: List<UserGroup>?) {
        mPubNub.fetchMessages()
            .channels(groups?.toMutableList()?.map { userGroup -> userGroup.groupName })
            .end(System.currentTimeMillis())
            .includeMeta(true)
            .includeMessageType(true)
            .includeUUID(true)
            .maximumPerChannel(50)
            .async(PNCallback { result, status ->
                if(!status.isError) {
                    val channels = result?.channels
                    val iterator = channels?.keys?.iterator()
                    while(iterator != null &&  iterator.hasNext()) {
                        val groupName = iterator.next()
                        val index = groups?.toMutableList()?.indexOfFirst { userGroup -> userGroup.groupName == groupName }
                        val group = groups?.toMutableList()?.get(index ?: 0)
                        val messageItems = channels[groupName]
                       group?.recentMessages?.addAll((messageItems?.map { pnFetchMessageItem -> Gson().fromJson(pnFetchMessageItem.message,
                           ChatMessage::class.java) } ?: LinkedList()))
                    }
                    if(mProgressDialog != null && mProgressDialog?.isShowing == true)  mProgressDialog?.dismiss()
                    updateList(groups)

                }
            })

    }

    private fun updateList(groups: List<UserGroup>?) {
        groupsListViewModel?.getGroups()?.clear()
        groupsListViewModel?.getGroups()?.addAll(groups?.toMutableList() ?: LinkedList())
        groupsListViewModel?.updateList()
    }

    private fun subscribeToChannels(groups: List<UserGroup>?) {
       if(groups.isNullOrEmpty()) return
        mPubNub.subscribe().channels(groups.toMutableList().map { userGroup -> userGroup.groupName })
            .execute()

    }

    private fun getPubNub(): PubNub {
        val pnConfiguration = PNConfiguration()
        pnConfiguration.subscribeKey = "SubscribeKey"
        pnConfiguration.publishKey = "PublishKey"
        pnConfiguration.isSecure = false
        pnConfiguration.uuid = NiroAppUtils.getCurrentUserId(requireContext())
        return PubNub(pnConfiguration)
    }


    private fun initializeRecyclerView() {
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        bindingGroupListFragment.groupListView.layoutManager = layoutManager

        val groupsAdapter = groupsListViewModel?.getGroupsAdapter()
        groupsAdapter?.setVariablesMap(getVariablesMap())
        bindingGroupListFragment.groupListView.adapter = groupsAdapter
    }

    private fun getVariablesMap(): HashMap<Int, Any?> {
        return hashMapOf(BR.itemClickListener to this)
    }

    override fun onItemClick(item: Any?) {

    }
}