package com.pd.beertimer

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import com.pd.beertimer.feature.welcome.WelcomeFragment
import com.pd.beertimer.util.CHANNEL_ID
import com.pd.beertimer.util.SHARED_PREF_FIRST_TIME_LAUNCH
import com.tlapp.beertimemm.drinking.DrinkCoordinator
import nl.joery.animatedbottombar.AnimatedBottomBar
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity(), NavController.OnDestinationChangedListener {

    private var menu: Menu? = null
    private val sharedPreferences: SharedPreferences by inject()
    private val drinkCoordinator: DrinkCoordinator by inject()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar))
        setUpBottomBar()
        createNotificationChannel()
        setupToolbar()
        checkForFirstTimeLaunch()
        if (savedInstanceState == null) {
            if (drinkCoordinator.isDrinking()) {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_countDownFragment)
            }
        }
    }

    private fun checkForFirstTimeLaunch() {
        if (sharedPreferences.getBoolean(SHARED_PREF_FIRST_TIME_LAUNCH, true) && !BuildConfig.DEBUG) {
            sharedPreferences.edit().putBoolean(SHARED_PREF_FIRST_TIME_LAUNCH, false).apply()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.activityFragmentContainer, WelcomeFragment())
                addToBackStack(null)
            }.commit()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            menu.findItem(R.id.action_info).iconTintList = this.getColorStateList(R.color.selector)
        }
        this.menu = menu
        if (supportFragmentManager.fragments.isNotEmpty()) menu.findItem(R.id.action_info)?.isVisible = false
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_info -> {
                findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_infoFragment)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onStart() {
        super.onStart()
        findNavController(R.id.nav_host_fragment).addOnDestinationChangedListener(this)
    }

    override fun onStop() {
        super.onStop()
        findNavController(R.id.nav_host_fragment).removeOnDestinationChangedListener(this)

    }

    fun onWelcomeScreenDismissed() {
        menu?.findItem(R.id.action_info)?.isVisible = true
    }

    private fun setupToolbar() {
        supportActionBar?.setDisplayShowTitleEnabled(false)
        findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            findNavController(R.id.nav_host_fragment).popBackStack()
        }
    }

    private fun setUpBottomBar() {
        findViewById<AnimatedBottomBar>(R.id.bottom_bar).setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                val currentDestinationId = findNavController(R.id.nav_host_fragment).currentDestination?.id
                when (newTab.id) {
                    R.id.tab_home -> {
                        if (currentDestinationId != R.id.startDrinkingFragment) {
                            findNavController(R.id.nav_host_fragment).popBackStack(R.id.startDrinkingFragment, false)
                        }
                    }
                    R.id.tab_timer -> {
                        if (currentDestinationId != R.id.countDownFragment) {
                            findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_countDownFragment)
                        }
                    }
                    R.id.tab_profile -> {
                        if (currentDestinationId != R.id.profileFragment) {
                            findNavController(R.id.nav_host_fragment).navigate(R.id.action_global_meFragment)
                        }
                    }
                    else -> throw ClassNotFoundException()
                }
            }

        })
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setDisplayShowHomeEnabled(false)
        val bottomBar = findViewById<AnimatedBottomBar>(R.id.bottom_bar)
        bottomBar.visibility = View.VISIBLE

        this.menu?.findItem(R.id.action_info)?.isVisible = true
        when (destination.id) {
            R.id.countDownFragment -> bottomBar.selectTabById(R.id.tab_timer)
            R.id.startDrinkingFragment -> bottomBar.selectTabById(R.id.tab_home)
            R.id.meFragment -> bottomBar.selectTabById(R.id.tab_profile)
            R.id.infoFragment, R.id.profileFragment, R.id.myDrinksFragment, R.id.addDrinkFragment -> {
                this.menu?.findItem(R.id.action_info)?.isVisible = false
                bottomBar.visibility = View.GONE
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
                supportActionBar?.setDisplayShowHomeEnabled(true)
            }
        }
    }
}
