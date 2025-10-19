package com.example.antrianrumahsakit.ui.dokter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antrianrumahsakit.databinding.FragmentDoctorHistoryBinding
import com.example.antrianrumahsakit.model.TicketStatus
import com.example.antrianrumahsakit.viewmodel.SharedQueueViewModel
import com.example.antrianrumahsakit.ui.pasien.QueueListAdapter

class DoctorHistoryFragment : Fragment() {

    private var _binding: FragmentDoctorHistoryBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedQueueViewModel by activityViewModels()
    private lateinit var adapter: QueueListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = QueueListAdapter { /* dokter tidak perlu klik detail di sini */ }

        binding.recyclerDoctorHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerDoctorHistory.adapter = adapter

        sharedViewModel.queueTickets.observe(viewLifecycleOwner) { list ->
            val filtered = list.filter {
                it.doctorName.contains("Andi", ignoreCase = true) &&
                        it.status == TicketStatus.DONE
            }
            adapter.submitList(filtered)
            binding.tvEmptyHistory.visibility = if (filtered.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
