package com.example.antrianrumahsakit.ui.dokter

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
import com.example.antrianrumahsakit.databinding.ActivityMainDokterBinding
import com.example.antrianrumahsakit.ui.RoleSelectionActivity
import com.google.android.material.navigation.NavigationView

class MainActivityDokter : AppCompatActivity() {

    private lateinit var binding: ActivityMainDokterBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainDokterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        setSupportActionBar(binding.topAppBar)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.nav_host_dokter) as NavHostFragment
        val navController = navHost.navController

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.doctorQueueFragment,
                R.id.doctorHistoryFragment
            ),
            drawerLayout
        )

        NavigationUI.setupWithNavController(binding.topAppBar, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(binding.bottomNav, navController)

        val navView = findViewById<NavigationView>(R.id.nav_view)
        navView.menu.clear()
        navView.inflateMenu(R.menu.drawer_menu_dokter)
        navView.setNavigationItemSelectedListener { menuItem ->
            handleDrawerNavigation(menuItem)
            true
        }

        // ==== DAFTAR DOKTER (MANUAL DUMMY DARI SHAREDQUEUEMODEL) ====
        val dummyDoctors = listOf(
            Pair("dr. Andi Santoso", "Dokter Umum"),
            Pair("drg. Rina Puspita", "Dokter Gigi"),
            Pair("dr. Ahmad Yusuf", "Dokter Mata"),
            Pair("dr. Lestari Dewi", "Dokter Anak"),
            Pair("dr. Bambang Wijaya", "Dokter THT"),
            Pair("dr. Sri Mulyani", "Dokter Kandungan"),
            Pair("dr. Fajar Rahman", "Dokter Penyakit Dalam"),
            Pair("dr. Wulan Citra", "Dokter Kulit"),
            Pair("dr. Bima Prasetyo", "Dokter Saraf"),
            Pair("dr. Sari Utami", "Dokter Gizi")
        )

        // === HEADER DRAWER ===
        val header = navView.getHeaderView(0)
        val tvUserName = header.findViewById<TextView>(R.id.tvUserName)
        val tvUserRole = header.findViewById<TextView>(R.id.tvUserRole)
        val imgAvatar = header.findViewById<ImageView>(R.id.imgAvatar)

        val allDoctorsText = dummyDoctors.joinToString("\n") { "${it.first} - ${it.second}" }

        //  Daftar Dokter
        tvUserName.text = allDoctorsText
        tvUserRole.text = "Daftar Semua Dokter"
        imgAvatar.setImageResource(R.drawable.ic_doctor_avatar)
    }

    private fun handleDrawerNavigation(menuItem: MenuItem) {
        when (menuItem.itemId) {
            R.id.drawer_logout -> {
                startActivity(Intent(this, RoleSelectionActivity::class.java))
                finish()
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
    }
}
