package com.example.antrianrumahsakit.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.antrianrumahsakit.databinding.ActivityRoleSelectionBinding
import com.example.antrianrumahsakit.ui.pasien.MainActivityPasien
import com.example.antrianrumahsakit.ui.dokter.MainActivityDokter
import com.example.antrianrumahsakit.ui.admin.MainActivityAdmin
// Bagian ini digunakan untuk pemilihan dashboard untuk admin, pasien, dan dokter
class RoleSelectionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRoleSelectionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoleSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tombol untuk masing-masing role
        binding.btnPasien.setOnClickListener {
            startActivity(Intent(this, MainActivityPasien::class.java))
        }

        binding.btnDokter.setOnClickListener {
            startActivity(Intent(this, MainActivityDokter::class.java))
        }

        binding.btnAdmin.setOnClickListener {
            startActivity(Intent(this, MainActivityAdmin::class.java))
        }
    }
}
