package com.example.antrianrumahsakit.ui.dokter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.antrianrumahsakit.databinding.FragmentDoctorDetailBinding
import com.example.antrianrumahsakit.model.TicketStatus
import com.example.antrianrumahsakit.viewmodel.SharedQueueViewModel
import com.google.android.material.snackbar.Snackbar

class DoctorDetailFragment : Fragment() {

    private var _binding: FragmentDoctorDetailBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: SharedQueueViewModel by activityViewModels()

    private var ticketId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDoctorDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ticketId = arguments?.getInt("ticketId") ?: -1
        val ticket = sharedViewModel.queueTickets.value?.find { it.id == ticketId }

        if (ticket == null) {
            Snackbar.make(requireView(), "Data pasien tidak ditemukan ❌", Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        ticket?.let {
            updateUI(it.status.name)
            binding.tvPatientName.text = it.patientName
            binding.tvPoli.text = it.poliName
            binding.tvDoctorName.text = it.doctorName
            binding.tvStatus.text = "Status: ${it.status.name}"

            // Tombol mulai pemeriksaan
            binding.btnStartCheck.setOnClickListener {
                val updated = sharedViewModel.updateQueueStatus(ticketId, TicketStatus.ON_CHECK)
                if (updated) {
                    Snackbar.make(requireView(), "Pemeriksaan dimulai ✅", Snackbar.LENGTH_SHORT).show()
                    updateUI("ON_CHECK")
                } else {
                    Snackbar.make(requireView(), "Gagal memulai, dokter sedang memeriksa pasien lain!", Snackbar.LENGTH_SHORT).show()
                }
            }

            // Tombol selesai pemeriksaan
            binding.btnFinishCheck.setOnClickListener {
                sharedViewModel.updateQueueStatus(ticketId, TicketStatus.DONE)
                Snackbar.make(requireView(), "Pemeriksaan selesai ✅", Snackbar.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        }
    }

    private fun updateUI(status: String) {
        when (status) {
            "WAIT" -> {
                binding.btnStartCheck.visibility = View.VISIBLE
                binding.btnFinishCheck.visibility = View.GONE
            }
            "ON_CHECK" -> {
                binding.btnStartCheck.visibility = View.GONE
                binding.btnFinishCheck.visibility = View.VISIBLE
            }
            "DONE" -> {
                binding.btnStartCheck.visibility = View.GONE
                binding.btnFinishCheck.visibility = View.GONE
            }
        }
        binding.tvStatus.text = "Status: $status"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
