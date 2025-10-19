package com.example.antrianrumahsakit.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.antrianrumahsakit.databinding.FragmentAdminHomeBinding
import com.example.antrianrumahsakit.viewmodel.SharedQueueViewModel

class AdminHomeFragment : Fragment() {

    private var _binding: FragmentAdminHomeBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedQueueViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedViewModel.doctors.observe(viewLifecycleOwner) {
            binding.tvTotalDoctors.text = it.size.toString()
        }

        sharedViewModel.patients.observe(viewLifecycleOwner) {
            binding.tvTotalPatients.text = it.size.toString()
        }

        sharedViewModel.polis.observe(viewLifecycleOwner) {
            binding.tvTotalPoli.text = it.size.toString()
        }

        sharedViewModel.queueTickets.observe(viewLifecycleOwner) {
            val active = it.count { ticket -> ticket.status.name != "DONE" }
            binding.tvActiveQueue.text = active.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
