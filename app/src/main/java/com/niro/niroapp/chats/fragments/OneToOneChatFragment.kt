package com.niro.niroapp.chats.fragments

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.niro.niroapp.R

class OneToOneChatFragment : Fragment() {

    companion object {
        fun newInstance() = OneToOneChatFragment()
    }

    private lateinit var viewModel: OneToOneChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.one_to_one_chat_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OneToOneChatViewModel::class.java)
        // TODO: Use the ViewModel
    }

}