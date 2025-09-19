package com.example.antrianrumahsakit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.antrianrumahsakit.databinding.FragmentQueueDetailBinding
import com.example.antrianrumahsakit.viewmodel.QueueViewModel

class QueueDetailFragment : Fragment() {
    private val vm: QueueViewModel by activityViewModels()
    private var _binding: FragmentQueueDetailBinding? = null
    private val binding get() = _binding!!

    private val args: QueueDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQueueDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ticketId = args.ticketId
        val ticket = vm.getTicketById(ticketId)
        if (ticket != null) {
            binding.tvDetailId.text = "No. ${ticket.id}"
            binding.tvDetailName.text = ticket.name
            binding.tvDetailComplaint.text = ticket.complaint
            binding.tvDetailPoli.text = "Poli: ${ticket.poli}"
            binding.tvDetailDokter.text = "Dokter: ${ticket.dokter}"
        } else {
            binding.tvDetailName.text = "Data tidak ditemukan"
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnCallComplete.setOnClickListener {
            vm.removeTicket(ticketId)
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
