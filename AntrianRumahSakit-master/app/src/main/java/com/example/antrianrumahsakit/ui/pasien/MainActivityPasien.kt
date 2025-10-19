package com.example.antrianrumahsakit.ui.pasien

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.antrianrumahsakit.R
import com.example.antrianrumahsakit.databinding.ActivityMainPasienBinding
import com.example.antrianrumahsakit.ui.RoleSelectionActivity
import com.google.android.material.navigation.NavigationView

class MainActivityPasien : AppCompatActivity() {

    private lateinit var binding: ActivityMainPasienBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPasienBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        setSupportActionBar(binding.topAppBar)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_pasien) as NavHostFragment
        val navController = navHost.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.queueListFragment,
                R.id.poliFragment,
                R.id.historyFragment,
                R.id.profileFragment,
                R.id.newsHealthFragment
            ),
            drawerLayout
        )

        NavigationUI.setupWithNavController(binding.topAppBar, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.bottomNav, navController)

        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.menu.clear()
        navView.inflateMenu(R.menu.drawer_menu_pasien)
        navView.setNavigationItemSelectedListener { menuItem ->
            handleDrawerNavigation(menuItem)
            true
        }

        val headerView = navView.getHeaderView(0)
        headerView.findViewById<TextView>(R.id.tvUserName).text = "Bagas Pratama"
        headerView.findViewById<TextView>(R.id.tvUserRole).text = "Pasien"
        headerView.findViewById<ImageView>(R.id.imgAvatar).setImageResource(R.drawable.ic_patient_avatar)

        binding.fabCenter.setOnClickListener {
            navController.navigate(R.id.addQueueFragment)
        }

        try {
            val logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
            val logoView = binding.topAppBar.findViewById<View>(R.id.logo)
            logoView?.startAnimation(logoAnim)
        } catch (_: Exception) {}
    }

    private fun handleDrawerNavigation(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.drawer_tentang -> android.widget.Toast.makeText(this, "Versi Demo", android.widget.Toast.LENGTH_SHORT).show()
            R.id.drawer_logout -> {
                startActivity(Intent(this, RoleSelectionActivity::class.java))
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }
}
