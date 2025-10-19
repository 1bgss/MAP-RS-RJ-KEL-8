package com.example.antrianrumahsakit.ui

import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.antrianrumahsakit.databinding.FragmentQueueDetailBinding
import com.example.antrianrumahsakit.model.QueueTicket
import com.example.antrianrumahsakit.model.TicketStatus
import com.example.antrianrumahsakit.viewmodel.SharedQueueViewModel
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileOutputStream

class QueueDetailFragment : Fragment() {

    private var _binding: FragmentQueueDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedQueueViewModel by activityViewModels()

    private var ticketId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQueueDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        ticketId = arguments?.getInt("ticketId") ?: -1
        val ticket = viewModel.queueTickets.value?.find { it.id == ticketId }

        if (ticket == null) {
            Snackbar.make(view, "Data antrian tidak ditemukan", Snackbar.LENGTH_SHORT).show()
            findNavController().popBackStack()
            return
        }

        // Tampilkan data antrian
        bindTicketData(ticket)

        // Tombol: Mulai periksa
        binding.btnStartCheck.setOnClickListener {
            viewModel.updateQueueStatus(ticket.id, TicketStatus.ON_CHECK)
            Snackbar.make(view, "✅ Status diubah menjadi: Sedang Diperiksa", Snackbar.LENGTH_SHORT).show()
            bindTicketData(ticket)
        }

        // Tombol: Selesaikan
        binding.btnCallComplete.setOnClickListener {
            viewModel.updateQueueStatus(ticket.id, TicketStatus.DONE)
            Snackbar.make(view, "✅ Pemeriksaan selesai", Snackbar.LENGTH_SHORT).show()
            bindTicketData(ticket)
        }

        // Tombol: Simpan struk dummy
        binding.btnSaveReceipt.setOnClickListener {
            saveDummyReceipt(ticket)
        }

        // Tombol: Kembali
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun bindTicketData(ticket: QueueTicket) {
        binding.tvDetailId.text = "Nomor Antrian: Q${ticket.id.toString().padStart(3, '0')}"
        binding.tvDetailName.text = "Nama Pasien: ${ticket.patientName}"
        binding.tvDetailPoli.text = "Poli: ${ticket.poliName}"
        binding.tvDetailDokter.text = "Dokter: ${ticket.doctorName}"
        binding.tvEtaDetail.text =
            "Estimasi Waktu Tunggu: ${viewModel.calculateEstimatedWaitTime(ticket.id)} menit"
    }

    private fun saveDummyReceipt(ticket: QueueTicket) {
        val fileName = "struk_antrian_${ticket.id}.txt"
        val file = File(
            requireContext().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),
            fileName
        )
        FileOutputStream(file).use {
            it.write(
                """
                STRUK ANTRIAN RUMAH SAKIT
                ----------------------------
                Nomor: Q${ticket.id.toString().padStart(3, '0')}
                Pasien: ${ticket.patientName}
                Poli: ${ticket.poliName}
                Dokter: ${ticket.doctorName}
                Status: ${ticket.status}
                ----------------------------
                Terima kasih telah berkunjung.
                """.trimIndent().toByteArray()
            )
        }
        Snackbar.make(binding.root, "📄 Struk disimpan di: ${file.absolutePath}", Snackbar.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
