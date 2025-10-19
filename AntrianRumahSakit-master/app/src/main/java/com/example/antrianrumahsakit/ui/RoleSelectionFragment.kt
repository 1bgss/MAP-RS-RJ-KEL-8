package com.example.antrianrumahsakit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.antrianrumahsakit.R
import com.example.antrianrumahsakit.databinding.FragmentRoleSelectionBinding
import com.example.antrianrumahsakit.viewmodel.SharedQueueViewModel
import androidx.fragment.app.activityViewModels

class RoleSelectionFragment : Fragment() {
    private var _binding: FragmentRoleSelectionBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedQueueViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRoleSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnPatient.setOnClickListener {
            sharedViewModel.setCurrentRole("patient")
            Toast.makeText(requireContext(), "Masuk sebagai Pasien", Toast.LENGTH_SHORT).show()
            navigateToMain()
        }

        binding.btnDoctor.setOnClickListener {
            sharedViewModel.setCurrentRole("doctor")
            Toast.makeText(requireContext(), "Masuk sebagai Dokter", Toast.LENGTH_SHORT).show()
            navigateToMain()
        }

        binding.btnAdmin.setOnClickListener {
            sharedViewModel.setCurrentRole("admin")
            Toast.makeText(requireContext(), "Masuk sebagai Admin", Toast.LENGTH_SHORT).show()
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        findNavController().navigate(R.id.action_roleSelection_to_mainActivity)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
