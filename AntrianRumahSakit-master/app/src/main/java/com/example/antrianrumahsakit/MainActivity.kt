package com.example.antrianrumahsakit

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.antrianrumahsakit.databinding.ActivityMainBinding
import com.example.antrianrumahsakit.ui.RoleSelectionActivity
import com.example.antrianrumahsakit.viewmodel.SharedQueueViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val sharedViewModel: SharedQueueViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        setSupportActionBar(binding.topAppBar)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHost.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.queueListFragment, R.id.poliFragment, R.id.historyFragment, R.id.profileFragment),
            drawerLayout
        )
        NavigationUI.setupWithNavController(binding.topAppBar, navController, appBarConfiguration)

        // bottom navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav?.let { NavigationUI.setupWithNavController(it, navController) }

        // drawer view setup
        val navView = findViewById<NavigationView>(R.id.nav_view)

        sharedViewModel.currentRole.observe(this) { role ->
            when (role) {
                "patient" -> navView.menu.clear().also { navView.inflateMenu(R.menu.drawer_menu_pasien) }
                "doctor" -> navView.menu.clear().also { navView.inflateMenu(R.menu.drawer_menu_dokter) }
                "admin" -> navView.menu.clear().also { navView.inflateMenu(R.menu.drawer_menu_admin) }
            }
        }

        navView.setNavigationItemSelectedListener { menuItem ->
            handleDrawerNavigation(menuItem)
            true
        }

        // FAB -> tambah antrian (khusus pasien)
        binding.fabCenter.setOnClickListener {
            if (sharedViewModel.currentRole.value == "patient") {
                navController.navigate(R.id.addQueueFragment)
            }
        }

        // animate logo
        try {
            val logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
            val logoView = binding.topAppBar.findViewById<View>(R.id.logo)
            logoView?.startAnimation(logoAnim)
        } catch (_: Exception) {}
    }

    private fun handleDrawerNavigation(menuItem: MenuItem) {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val controller = navHost.navController

        when (menuItem.itemId) {
            R.id.drawer_master_data -> controller.navigate(R.id.masterDataFragment)
            R.id.drawer_reports -> controller.navigate(R.id.reportsFragment)
            R.id.drawer_settings -> controller.navigate(R.id.settingsFragment)
            R.id.drawer_history -> controller.navigate(R.id.historyFragment)
            R.id.drawer_profile -> controller.navigate(R.id.profileFragment)
            R.id.drawer_logout -> {
                val intent = Intent(this, RoleSelectionActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        return NavigationUI.navigateUp(navHost.navController, appBarConfiguration)
    }
}
