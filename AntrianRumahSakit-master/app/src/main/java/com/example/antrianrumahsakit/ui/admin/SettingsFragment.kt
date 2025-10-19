package com.example.antrianrumahsakit.ui.admin

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.antrianrumahsakit.databinding.FragmentSettingsBinding
import com.example.antrianrumahsakit.ui.RoleSelectionActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvAdminName.text = "Admin Klinik"
        binding.tvRole.text = "Administrator"

        binding.btnLogout.setOnClickListener {
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Keluar")
                .setMessage("Yakin ingin keluar dari akun admin?")
                .setPositiveButton("Ya") { _, _ ->
                    startActivity(Intent(requireContext(), RoleSelectionActivity::class.java))
                    requireActivity().finish()
                }
                .setNegativeButton("Batal", null)
                .show()
        }

        binding.btnResetData.setOnClickListener {
            Snackbar.make(requireView(), "Fitur reset data coming soon ⚙️", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
