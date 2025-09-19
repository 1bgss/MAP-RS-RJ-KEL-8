package com.example.antrianrumahsakit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antrianrumahsakit.databinding.FragmentQueueListBinding
import com.example.antrianrumahsakit.viewmodel.QueueViewModel
import com.example.antrianrumahsakit.model.TicketStatus

class QueueListFragment : Fragment() {
    private var _binding: FragmentQueueListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: QueueViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQueueListBinding.inflate(inflater, container, false)

        val adapter = QueueAdapter { ticket ->
            // contoh toggle status ke ON_CHECK
            val success = viewModel.updateStatus(ticket.ticketId, TicketStatus.ON_CHECK)
            if (!success) {
                Toast.makeText(requireContext(), "Masih ada pasien yang sedang diperiksa!", Toast.LENGTH_SHORT).show()
            } else {
                val action = QueueListFragmentDirections.actionListToDetail(ticket.ticketId)
                findNavController().navigate(action)
            }
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        viewModel.tickets.observe(viewLifecycleOwner) { tickets ->
            adapter.submitList(tickets.toList())
            binding.emptyView.visibility = if (tickets.isEmpty()) View.VISIBLE else View.GONE

            val current = tickets.firstOrNull { it.status == TicketStatus.ON_CHECK }
            if (current != null) {
                binding.tvNowServing.text = "Now Serving: No. ${current.ticketId} (${current.poli} - ${current.doctor})"
                binding.tvETA.text = "Est. Wait: ${current.estimatedServiceMinutes} min"
            } else {
                binding.tvNowServing.text = "Tidak ada antrian"
                binding.tvETA.text = "-"
            }
        }

        binding.fabAdd.setOnClickListener {
            val action = QueueListFragmentDirections.actionListToAdd()
            findNavController().navigate(action)
        }

        binding.fabPoli.setOnClickListener {
            val action = QueueListFragmentDirections.actionListToPoli()
            findNavController().navigate(action)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
