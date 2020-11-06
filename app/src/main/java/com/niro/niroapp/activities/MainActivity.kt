package com.niro.niroapp.activities


import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import com.niro.niroapp.R
import com.niro.niroapp.database.SharedPreferenceManager
import com.niro.niroapp.databinding.ActivityMainBinding
import com.niro.niroapp.fragments.CreateOrderFragment
import com.niro.niroapp.fragments.LogoutDialog
import com.niro.niroapp.models.responsemodels.User
import com.niro.niroapp.models.responsemodels.UserType
import com.niro.niroapp.users.fragments.ContactsFragment
import com.niro.niroapp.utils.NiroAppConstants
import com.niro.niroapp.utils.NiroAppUtils
import com.pubnub.api.PNConfiguration
import com.pubnub.api.PubNub
import com.niro.niroapp.viewmodels.LocaleHelper.onAttach
import kotlinx.android.synthetic.main.app_bar_home.view.*

class MainActivity : AppCompatActivity() {

    private lateinit var bindingActivityMain: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var mCurrentUser: User
    private lateinit var mNavController: NavController
    private var mDialog: Dialog? = null

    private lateinit var mPubnub : PubNub


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bindingActivityMain = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

//        val drawerLayout: DrawerLayout = findViewById(R.id.nav_drawer)
        //val navView: NavigationView = findViewById(R.id.nav_view)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_nav_view)



        mNavController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_support,
                R.id.nav_about,
                R.id.nav_logout,
                R.id.navigation_home,
                R.id.navigation_orders,
                R.id.action_chat,
                R.id.navigation_loaders,
                R.id.action_profile,
                R.id.profile_settings


            )
        )
        setupActionBarWithNavController(mNavController, appBarConfiguration)
        //navView.setupWithNavController(mNavController)

        bottomNavigationView.setupWithNavController(mNavController)
        bottomNavigationView.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED

//        navView.setNavigationItemSelectedListener { item ->
//            launchSelectedNavigationDrawerFragment(
//                item
//            )
//        }
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            launchSelectedFragment(
                item
            )
        }
        checkForAppUpdates()

        initializePubnubChat()
        initializeUserProfile()
    }


    private fun initializePubnubChat()  {
        val pnConfiguration = PNConfiguration()
        pnConfiguration.subscribeKey = "SubscribeKey"
        pnConfiguration.publishKey = "PublishKey"
        pnConfiguration.isSecure = false
        pnConfiguration.uuid = NiroAppUtils.getCurrentUserId(this)
        mPubnub =  PubNub(pnConfiguration)
    }


    fun getPubnub() = mPubnub



    private fun launchSelectedNavigationDrawerFragment(item: MenuItem): Boolean {

        if (!::mNavController.isInitialized) mNavController =
            findNavController(R.id.nav_host_fragment)
        when (item.itemId) {
            R.id.nav_support -> {
                setToolbarTitleAndImage(getString(R.string.menu_support), R.drawable.ic_phone_32)
                mNavController.navigate(R.id.nav_support)
               // closeDrawer()
            }
            R.id.nav_about -> {
                setToolbarTitleAndImage(
                    getString(R.string.menu_about),
                    R.drawable.ic_app_icon_black
                )
                mNavController.navigate(R.id.nav_about)
               // closeDrawer()
            }
            R.id.nav_logout -> {

                showLogoutDialog()
              //  closeDrawer()
            }
        }
        return true

    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(onAttach(base))
    }
//    private fun launchSelectedNavigationDrawerFragment(item: MenuItem): Boolean {
////
////        if (!::mNavController.isInitialized) mNavController =
////            findNavController(R.id.nav_host_fragment)
////        when (item.itemId) {
////            R.id.nav_support -> {
////                setToolbarTitleAndImage(getString(R.string.menu_support), R.drawable.ic_phone_32)
////                mNavController.navigate(R.id.nav_support)
////                closeDrawer()
////            }
////            R.id.nav_about -> {
////                setToolbarTitleAndImage(
////                    getString(R.string.menu_about),
////                    R.drawable.ic_app_icon_black
////                )
////                mNavController.navigate(R.id.nav_about)
////                closeDrawer()
////            }
////            R.id.nav_logout -> {
////
////                showLogoutDialog()
////                closeDrawer()
////            }
////        }
////        return true
////    }

    private fun showLogoutDialog() {
        LogoutDialog().show(supportFragmentManager, NiroAppConstants.TAG_DIALOG_LOGOUT)
    }

    private fun launchHomeFragment(user: User) {
        setToolbarTitleAndImage(getString(R.string.title_chats), R.drawable.ic_chat)
        mNavController.navigate(R.id.action_chat)
    }


    private fun initializeUserProfile() {
        initializeCurrentUser()
       // setUserDataInNavHeader(mCurrentUser)
        bindingActivityMain.appBarHome.contentHome.bottomNavView.menu.findItem(R.id.navigation_loaders).title =
            if ((mCurrentUser.userType ?: "").equals(
                    UserType.COMMISSION_AGENT.name,
                    true
                )
            ) getString(R.string.title_users) else getString(R.string.title_buyers)

        launchHomeFragment(mCurrentUser)
    }

//    private fun openProfileDetails(user: User) {
//        findNavController(R.id.nav_host_fragment).navigate(
//            R.id.navigation_profile_details,
//            bundleOf(NiroAppConstants.ARG_CURRENT_USER to user)
//        )
//        closeDrawer()
//    }


    private fun initializeCurrentUser() {
        if (!(this::mCurrentUser.isInitialized)) mCurrentUser = NiroAppUtils.getCurrentUser(this)
    }

//    private fun setUserDataInNavHeader(user: User) {
//        val headerLayout = bindingActivityMain.navView.getHeaderView(0)
//        headerLayout.findViewById<TextView>(R.id.tvUserName).text =
//            user.fullName ?: NiroAppUtils.getCurrentUserType(user.userType)
//        headerLayout.findViewById<TextView>(R.id.tvUserNumber).text = user.phoneNumber ?: ""
//
//        headerLayout.findViewById<LinearLayout>(R.id.layoutMyProfile)
//            .setOnClickListener { openProfileDetails(user) }
//
//
//    }


    override fun onSupportNavigateUp(): Boolean {
        return mNavController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    private fun launchSelectedFragment(item: MenuItem): Boolean {

        initializeCurrentUser()
        when (item.itemId) {
//            R.id.navigation_home -> {
//                launchHomeFragment(mCurrentUser)
//            }

            R.id.navigation_orders -> {
                setToolbarTitleAndImage(getString(R.string.title_orders), R.drawable.ic_orders)
                mNavController.navigate(
                    R.id.navigation_orders,
                    bundleOf(NiroAppConstants.ARG_CURRENT_USER to mCurrentUser)
                )
            }

            R.id.action_chat -> {
                setToolbarTitleAndImage(getString(R.string.title_chats), R.drawable.ic_payments)
                mNavController.navigate(
                    R.id.action_chat,
                    bundleOf(NiroAppConstants.ARG_CURRENT_USER to mCurrentUser)
                )
            }
            R.id.navigation_loaders -> {
                setToolbarTitleAndImage(
                    if ((mCurrentUser.userType ?: "").equals(
                            UserType.COMMISSION_AGENT.name,
                            true
                        )
                    ) getString(R.string.title_users) else getString(R.string.title_buyers),
                    R.drawable.ic_user_type
                )
                mNavController.navigate(
                    R.id.navigation_loaders,
                    bundleOf(NiroAppConstants.ARG_CURRENT_USER to mCurrentUser)
                )
            }

            R.id.action_profile -> {
                setToolbarTitleAndImage(getString(R.string.title_loans), R.drawable.ic_loans_24)
                mNavController.navigate(
                    R.id.action_profile,
                    bundleOf(NiroAppConstants.ARG_CURRENT_USER to mCurrentUser)
                )
            }
            R.id.profile_settings -> {
                setToolbarTitleAndImage(getString(R.string.profile), R.drawable.ic_profile_24)

                mNavController.navigate(
                    R.id.action_bottomsheet,
                    bundleOf(NiroAppConstants.ARG_CURRENT_USER to mCurrentUser)
                )
            }
        }

        return true

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        val contactsFragment =
            supportFragmentManager.findFragmentByTag(NiroAppConstants.TAG_CONTACTS)

        if (contactsFragment != null && contactsFragment.isVisible) {
            (contactsFragment as ContactsFragment).onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
        }

        val createOrderFragment =
            supportFragmentManager.findFragmentById(R.id.navigation_create_order)

        if (createOrderFragment != null && createOrderFragment.isVisible) {
            (createOrderFragment as CreateOrderFragment).onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
            )
        }

    }

//    private fun closeDrawer() {
//        bindingActivityMain.navDrawer.closeDrawer(Gravity.LEFT)
//    }


    fun setToolbarTitleAndImage(title: String, imageIcon: Int) {
        bindingActivityMain.appBarHome.toolbar.tvHeaderName.text = title
        if (imageIcon > -1) {
            bindingActivityMain.appBarHome.toolbar.ivMenuImage.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                bindingActivityMain.appBarHome.toolbar.ivMenuImage.setImageDrawable(
                    resources.getDrawable(
                        imageIcon,
                        null
                    )
                )
            } else bindingActivityMain.appBarHome.toolbar.ivMenuImage.setImageDrawable(
                resources.getDrawable(
                    imageIcon
                )
            )
        } else bindingActivityMain.appBarHome.toolbar.ivMenuImage.visibility = View.INVISIBLE

    }

    fun updateUser(updatedUser: User?) {
        if (updatedUser != null) {
            mCurrentUser = updatedUser
            initializeUserProfile()
        }
    }

    private fun checkForAppUpdates() {

        val numberOfFailedOrCancelledAttempts = getNumberOfUpdatedFailedOrCancelled()

        val appUpdateManager = AppUpdateManagerFactory.create(this)

        val appUpdateInfoTask = appUpdateManager.appUpdateInfo

        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)

            ) {
                updateApp(
                    appUpdateManager, appUpdateInfo,
                    if (numberOfFailedOrCancelledAttempts >= NiroAppConstants.MAX_ALLOWED_ATTEMPTS) AppUpdateType.IMMEDIATE else AppUpdateType.FLEXIBLE)
            }
        }
    }


    private fun updateApp(
        appUpdateManager: AppUpdateManager,
        appUpdateInfo: AppUpdateInfo,
        appUpdateType: Int
    ) {

        appUpdateManager.startUpdateFlowForResult(
            appUpdateInfo,
            appUpdateType,
            this,
            NiroAppConstants.APP_UPDATE_REQUEST_CODE
        )
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == NiroAppConstants.APP_UPDATE_REQUEST_CODE && (resultCode != Activity.RESULT_OK || resultCode == Activity.RESULT_CANCELED)) {
            updateNumberOfFailedOrCancelledUpdated(getNumberOfUpdatedFailedOrCancelled() + 1)

        }
    }

    private fun getNumberOfUpdatedFailedOrCancelled() : Int {
        return SharedPreferenceManager(this, NiroAppConstants.LOGIN_SP).getIntegerPreference(
            NiroAppConstants.KEY_NO_FAILED_UPDATES,
            0
        )
    }

    private fun updateNumberOfFailedOrCancelledUpdated(count : Int) {
        SharedPreferenceManager(this,NiroAppConstants.LOGIN_SP).storeIntegerPreference(NiroAppConstants.KEY_NO_FAILED_UPDATES,count)
    }



}


