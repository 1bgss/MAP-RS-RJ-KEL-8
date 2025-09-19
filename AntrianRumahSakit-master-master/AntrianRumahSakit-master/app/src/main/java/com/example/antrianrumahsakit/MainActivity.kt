package com.example.antrianrumahsakit

import android.os.Bundle
import android.widget.ImageView
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ambil navHostFragment dari layout
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // AppBarConfiguration: fragment utama tanpa tombol back
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.queueListFragment)
        )

        // Set toolbar dengan NavController
        val toolbar = findViewById<MaterialToolbar>(R.id.topAppBar)
        toolbar.setupWithNavController(navController, appBarConfiguration)

        // ===================== ANIMASI LOGO =====================
        val logo: ImageView = findViewById(R.id.logo)
        val animation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        logo.startAnimation(animation)
    }
}
