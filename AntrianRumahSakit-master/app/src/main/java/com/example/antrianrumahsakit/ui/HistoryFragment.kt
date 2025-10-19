package com.example.antrianrumahsakit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antrianrumahsakit.databinding.FragmentHistoryBinding
import com.example.antrianrumahsakit.model.QueueTicket
import com.example.antrianrumahsakit.model.TicketStatus
import com.example.antrianrumahsakit.ui.pasien.QueueListAdapter
import com.example.antrianrumahsakit.viewmodel.SharedQueueViewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedQueueViewModel by activityViewModels()
    private lateinit var adapter: QueueListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = QueueListAdapter(onItemClick = { /* Optional: detail riwayat */ })

        binding.recyclerHistory.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerHistory.adapter = adapter

        // Filter hanya antrian yang sudah selesai (DONE)
        viewModel.queueTickets.observe(viewLifecycleOwner) { list ->
            val historyList = list.filter { it.status == TicketStatus.DONE }
            adapter.submitList(historyList)
        }

        // Dummy
        if (viewModel.queueTickets.value.isNullOrEmpty()) {
            val dummy = listOf(
                QueueTicket(10, "Andi Rahman", "Poli Umum", "dr. Budi Santoso", TicketStatus.DONE, true),
                QueueTicket(11, "Nina Anggraini", "Poli Gigi", "drg. Putri Sari", TicketStatus.DONE, false)
            )
            dummy.forEach { viewModel.addQueue(it) }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
