package com.example.antrianrumahsakit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.antrianrumahsakit.databinding.FragmentQueueDetailBinding
import com.example.antrianrumahsakit.model.TicketStatus
import com.example.antrianrumahsakit.viewmodel.QueueViewModel

class QueueDetailFragment : Fragment() {
    private val vm: QueueViewModel by activityViewModels()
    private var _binding: FragmentQueueDetailBinding? = null
    private val binding get() = _binding!!

    private val args: QueueDetailFragmentArgs by navArgs()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQueueDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val ticketId = args.ticketId
        val ticket = vm.getTicketById(ticketId)
        if (ticket != null) {
            binding.tvDetailId.text = "No. ${ticket.ticketId}"
            binding.tvDetailName.text = ticket.patient.name
            binding.tvDetailComplaint.text = ticket.patient.uniqueMedicalId?.let { "ID: $it\n${ticket.patient.name}" } ?: ticket.patient.name
            binding.tvDetailPoli.text = "Poli: ${ticket.poli}"
            binding.tvDetailDokter.text = "Dokter: ${ticket.doctor}"
            binding.tvEtaDetail.text = "Est. service: ${ticket.estimatedServiceMinutes} min"

            updateButtons(ticket.status)
        } else {
            binding.tvDetailName.text = "Data tidak ditemukan"
            binding.btnCallComplete.isEnabled = false
            binding.btnStartCheck.isEnabled = false
        }

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnStartCheck.setOnClickListener {
            vm.updateStatus(ticketId, TicketStatus.ON_CHECK)
            Toast.makeText(requireContext(), "Status: ON-CHECK", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        binding.btnCallComplete.setOnClickListener {
            vm.updateStatus(ticketId, TicketStatus.DONE)
            Toast.makeText(requireContext(), "Pasien selesai", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }
    }

    private fun updateButtons(status: TicketStatus) {
        when (status) {
            TicketStatus.WAIT -> {
                binding.btnStartCheck.isEnabled = true
                binding.btnCallComplete.isEnabled = false
            }
            TicketStatus.ON_CHECK -> {
                binding.btnStartCheck.isEnabled = false
                binding.btnCallComplete.isEnabled = true
            }
            TicketStatus.DONE -> {
                binding.btnStartCheck.isEnabled = false
                binding.btnCallComplete.isEnabled = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
