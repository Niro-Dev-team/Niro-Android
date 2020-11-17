package com.niro.niroapp.fragments

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.niro.niroapp.R
import com.niro.niroapp.databinding.BottomsheetFragmentBinding
import com.niro.niroapp.models.responsemodels.User
import com.niro.niroapp.utils.NiroAppConstants
import com.niro.niroapp.utils.NiroAppUtils
import kotlinx.android.synthetic.main.bottomsheet_fragment.view.*
import androidx.fragment.app.FragmentManager as FragmentManager1

class BottomSheetFragment() : BottomSheetDialogFragment() {

    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    private lateinit var bindingBottomSheetFragment: BottomsheetFragmentBinding
    private var mCurrentUser : User? = null


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            mCurrentUser = it.getParcelable(NiroAppConstants.ARG_CURRENT_USER) as? User
        }

        Log.e("BottomsheetBundle",mCurrentUser.toString())
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bindingBottomSheetFragment = DataBindingUtil.inflate(inflater,
            R.layout.bottomsheet_fragment, container, false)
        bindingBottomSheetFragment.lifecycleOwner = viewLifecycleOwner
        setUserDataInNavHeader(mCurrentUser )

        return bindingBottomSheetFragment.root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View).setBackgroundColor(Color.TRANSPARENT)
        initializeListeners()
    }

    private fun initializeListeners() {
       // bindingBottomSheetFragment.tvMyProfile.setOnClickListener { openProfileDetails(mCurrentUser) }
        bindingBottomSheetFragment.navSupport.setOnClickListener { openSupportFragment() }
        bindingBottomSheetFragment.navAbout.setOnClickListener { openAboutFrag() }
        bindingBottomSheetFragment.navLogout.setOnClickListener { showLogoutDialog() }
        }

    private fun showLogoutDialog() {
        LogoutDialog().show(childFragmentManager, NiroAppConstants.TAG_DIALOG_LOGOUT)
    }


    private fun openAboutFrag() {
        findNavController().navigate(R.id.action_bottomsheet_to_navigation_contactUs)

    }

    private fun openSupportFragment() {
        findNavController().navigate(R.id.action_bottomsheet_to_navigation_supoort)
    }

//    private fun openProfileDetails(user: User?) {
//            findNavController().navigate(R.id.action_bottomsheet_to_navigation_user_profile,
//            bundleOf(NiroAppConstants.ARG_CURRENT_USER to user))
//    }
        private fun setUserDataInNavHeader(user: User?) {
        val headerLayout = bindingBottomSheetFragment.bottomSheets.headerLayouts
            if (user != null) {
                headerLayout.findViewById<TextView>(R.id.tvUserName1).text =
                    user.fullName ?: NiroAppUtils.getCurrentUserType(user.userType)
            }
            if (user != null) {
                headerLayout.findViewById<TextView>(R.id.tvUserNumber1).text = user.phoneNumber ?: ""
            }
    }
}