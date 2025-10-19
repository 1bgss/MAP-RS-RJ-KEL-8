package com.example.antrianrumahsakit.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.antrianrumahsakit.databinding.FragmentReportsBinding
import com.example.antrianrumahsakit.viewmodel.SharedQueueViewModel
import com.example.antrianrumahsakit.model.TicketStatus

class ReportsFragment : Fragment() {

    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedQueueViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.queueTickets.observe(viewLifecycleOwner) { tickets ->
            val total = tickets.size
            val waiting = tickets.count { it.status == TicketStatus.WAIT }
            val onCheck = tickets.count { it.status == TicketStatus.ON_CHECK }
            val done = tickets.count { it.status == TicketStatus.DONE }

            binding.tvTotalAntrian.text = total.toString()
            binding.tvWaiting.text = waiting.toString()
            binding.tvOnCheck.text = onCheck.toString()
            binding.tvDone.text = done.toString()
        }

        viewModel.polis.observe(viewLifecycleOwner) { list ->
            binding.tvTotalPoli.text = list.size.toString()
        }

        viewModel.doctors.observe(viewLifecycleOwner) { list ->
            binding.tvTotalDokter.text = list.size.toString()
        }

        viewModel.patients.observe(viewLifecycleOwner) { list ->
            binding.tvTotalPasien.text = list.size.toString()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
