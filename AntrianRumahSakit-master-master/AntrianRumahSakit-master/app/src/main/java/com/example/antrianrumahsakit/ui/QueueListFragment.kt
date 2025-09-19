package com.example.antrianrumahsakit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antrianrumahsakit.databinding.FragmentQueueListBinding
import com.example.antrianrumahsakit.viewmodel.QueueViewModel

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
            val action = QueueListFragmentDirections.actionListToDetail(ticket.id)
            findNavController().navigate(action)
        }

        // ✅ Setup RecyclerView lengkap
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // Observe LiveData dari ViewModel
        viewModel.tickets.observe(viewLifecycleOwner) { tickets ->
            adapter.submitList(tickets.toList())
            binding.emptyView.visibility = if (tickets.isEmpty()) View.VISIBLE else View.GONE
        }

        // FAB untuk tambah antrian
        binding.fabAdd.setOnClickListener {
            val action = QueueListFragmentDirections.actionListToAdd()
            findNavController().navigate(action)
        }

        // FAB untuk PoliFragment
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
