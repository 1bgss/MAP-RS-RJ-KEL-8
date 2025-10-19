package com.example.antrianrumahsakit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antrianrumahsakit.R
import com.example.antrianrumahsakit.databinding.FragmentQueueListBinding
import com.example.antrianrumahsakit.model.QueueTicket
import com.example.antrianrumahsakit.model.TicketStatus
import com.example.antrianrumahsakit.ui.pasien.QueueListAdapter
import com.example.antrianrumahsakit.viewmodel.SharedQueueViewModel

class QueueListFragment : Fragment() {

    private var _binding: FragmentQueueListBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedQueueViewModel by activityViewModels()

    private lateinit var adapter: QueueListAdapter

    private var selectedPoli: String = "Semua Poli"
    private var searchQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQueueListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = QueueListAdapter { ticket ->
            val bundle = Bundle().apply { putInt("ticketId", ticket.id) }
            findNavController().navigate(R.id.action_queueListFragment_to_queueDetailFragment, bundle)
        }

        binding.recyclerQueue.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerQueue.adapter = adapter

        // Setup dropdown filter poli
        val poliAdapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.poli_list,
            android.R.layout.simple_spinner_item
        )
        poliAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFilterPoli.adapter = poliAdapter

        binding.spinnerFilterPoli.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedPoli = parent.getItemAtPosition(position).toString()
                applyFilters()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Setup search nama pasien
        binding.inputSearch.addTextChangedListener { text ->
            searchQuery = text.toString().trim()
            applyFilters()
        }

        // Observasi data antrian
        viewModel.queueTickets.observe(viewLifecycleOwner) {
            applyFilters()
        }

        if (viewModel.queueTickets.value.isNullOrEmpty()) {
            val dummy = listOf(
                QueueTicket(1, "Bagas Pratama", "Poli Umum", "dr. Andi Santoso", TicketStatus.WAIT, true, "2025-10-17"),
                QueueTicket(2, "Dina Lestari", "Poli Gigi", "drg. Rina Puspita", TicketStatus.ON_CHECK, false, "2025-10-17"),
                QueueTicket(3, "Rafi Nugraha", "Poli Mata", "dr. Ahmad Yusuf", TicketStatus.DONE, true, "2025-10-16")
            )
            dummy.forEach { viewModel.addQueue(it) }
        }

        // Tombol tambah antrian
        binding.btnAddQueue.setOnClickListener {
            findNavController().navigate(R.id.addQueueFragment)
        }
    }

    private fun applyFilters() {
        val list = viewModel.queueTickets.value ?: return
        val filtered = list.filter { ticket ->
            (selectedPoli == "Semua Poli" || ticket.poliName == selectedPoli) &&
                    (searchQuery.isEmpty() || ticket.patientName.contains(searchQuery, ignoreCase = true))
        }
        adapter.submitList(filtered)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
