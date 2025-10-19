package com.example.antrianrumahsakit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.antrianrumahsakit.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.tvProfileName.text = "Bagas Pratama"
        binding.tvProfileNim.text = "00000012345"
        binding.tvProfileProgram.text = "Teknik Informatika - UMN"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
