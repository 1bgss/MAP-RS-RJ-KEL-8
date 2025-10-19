package com.example.antrianrumahsakit.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.antrianrumahsakit.R
import com.example.antrianrumahsakit.databinding.ActivityMainAdminBinding
import com.example.antrianrumahsakit.ui.RoleSelectionActivity
import com.google.android.material.navigation.NavigationView

class MainActivityAdmin : AppCompatActivity() {

    private lateinit var binding: ActivityMainAdminBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        setSupportActionBar(binding.topAppBar)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_admin) as NavHostFragment
        val navController = navHost.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.adminHomeFragment,
                R.id.adminDoctorFragment,
                R.id.adminPoliFragment,
                R.id.adminPatientFragment,
                R.id.adminReportsFragment,
                R.id.adminSettingsFragment
            ),
            drawerLayout
        )

        NavigationUI.setupWithNavController(binding.topAppBar, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.bottomNav, navController)

        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.menu.clear()
        navView.inflateMenu(R.menu.drawer_menu_admin)
        navView.setNavigationItemSelectedListener { menuItem ->
            handleDrawerNavigation(menuItem)
            true
        }

        val header = navView.getHeaderView(0)
        header.findViewById<TextView>(R.id.tvUserName).text = "Admin Rumah Sakit"
        header.findViewById<TextView>(R.id.tvUserRole).text = "Administrator"
        header.findViewById<ImageView>(R.id.imgAvatar).setImageResource(R.drawable.ic_admin)
    }

    private fun handleDrawerNavigation(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.drawer_reset ->
                android.widget.Toast.makeText(this, "Data antrian direset", android.widget.Toast.LENGTH_SHORT).show()
            R.id.drawer_logout -> {
                startActivity(Intent(this, RoleSelectionActivity::class.java))
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }
}
