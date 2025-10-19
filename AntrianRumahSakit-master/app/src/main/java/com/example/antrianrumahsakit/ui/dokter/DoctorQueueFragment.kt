package com.example.antrianrumahsakit.ui.dokter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antrianrumahsakit.R
import com.example.antrianrumahsakit.databinding.FragmentDoctorQueueBinding
import com.example.antrianrumahsakit.viewmodel.SharedQueueViewModel

class DoctorQueueFragment : Fragment() {

    private var _binding: FragmentDoctorQueueBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedQueueViewModel by activityViewModels()
    private lateinit var adapter: DoctorQueueAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorQueueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Setup adapter untuk daftar pasien
        adapter = DoctorQueueAdapter(emptyList()) { ticket ->
            val bundle = Bundle().apply { putInt("ticketId", ticket.id) }
            findNavController().navigate(
                R.id.action_doctorQueueFragment_to_doctorDetailFragment,
                bundle
            )
        }

        binding.recyclerDoctorQueue.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDoctorQueue.adapter = adapter

        // Observasi data antrian dari ViewModel
        sharedViewModel.queueTickets.observe(viewLifecycleOwner) { list ->

            val filtered = list.sortedBy { it.doctorName } // biar rapi urut nama dokter

            adapter.updateData(filtered)
            binding.tvEmptyState.visibility =
                if (filtered.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
