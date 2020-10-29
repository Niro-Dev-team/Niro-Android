package com.niro.niroapp.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import com.niro.niroapp.R
import com.niro.niroapp.databinding.ActivityMainBinding
import com.niro.niroapp.databinding.BottomsheetFragmentBinding
import com.niro.niroapp.models.responsemodels.User
import com.niro.niroapp.utils.NiroAppConstants
import com.niro.niroapp.utils.NiroAppUtils
import kotlinx.android.synthetic.main.bottomsheet_fragment.*
import kotlinx.android.synthetic.main.bottomsheet_fragment.view.*

class BottomSheetFragment() : BottomSheetDialogFragment() {
    override fun getTheme(): Int = R.style.BottomSheetDialogTheme
    private lateinit var bindingBottomSheetFragment: BottomsheetFragmentBinding

    private lateinit var mCurrentUser: User
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog = BottomSheetDialog(requireContext(), theme)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.bottomsheet_fragment,container,false)

       // bindingBottomSheetFragment = DataBindingUtil.setContentView( R.layout.bottomsheet_fragment)
       // initializeCurrentUser()
        //setUserDataInNavHeader(mCurrentUser)



        return  view;
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (view?.parent as View).setBackgroundColor(Color.TRANSPARENT)
    }
    private fun initializeCurrentUser() {
        if (!(this::mCurrentUser.isInitialized)) mCurrentUser = NiroAppUtils.getCurrentUser(requireContext())
    }
//        private fun setUserDataInNavHeader(user: User) {
//        val headerLayout = bindingBottomSheetFragment.bottomSheets.header.header_inside
//        headerLayout.findViewById<TextView>(R.id.tvUserName1).text =
//            user.fullName ?: NiroAppUtils.getCurrentUserType(user.userType)
//        headerLayout.findViewById<TextView>(R.id.tvUserNumber1).text = user.phoneNumber ?: ""
//
//        headerLayout.findViewById<LinearLayout>(R.id.layoutMyProfileBottom)
//            .setOnClickListener { openProfileDetails(user) }
//
//
//    }
        private fun openProfileDetails(user: User) {
            startActivity(Intent(
                requireContext(),
                ProfileDetailsFragment::class.java
            ))

           // var bundel  = bundleOf(NiroAppConstants.ARG_CURRENT_USER to user)


    }
}