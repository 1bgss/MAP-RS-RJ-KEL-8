package com.example.antrianrumahsakit.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.antrianrumahsakit.R
import com.example.antrianrumahsakit.databinding.FragmentAddQueueBinding
import com.example.antrianrumahsakit.model.QueueTicket
import com.example.antrianrumahsakit.model.TicketStatus
import com.example.antrianrumahsakit.viewmodel.SharedQueueViewModel
import com.google.android.material.snackbar.Snackbar

class AddQueueFragment : Fragment() {

    private var _binding: FragmentAddQueueBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedQueueViewModel by activityViewModels()

    private val CAMERA_REQUEST_CODE = 101
    private var photoBitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddQueueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Dropdown daftar poli
        val poliList = listOf("Poli Umum", "Poli Gigi", "Poli Anak", "Poli Mata", "Poli THT")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_item_dropdown, poliList)
        binding.dropdownPoli.setAdapter(adapter)

        // Tombol ambil foto
        binding.btnTakePhoto.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        }

        // Tombol submit
        binding.btnSubmit.setOnClickListener {
            val name = binding.inputPatientName.text.toString().trim()
            val poli = binding.dropdownPoli.text.toString().trim()
            val doctor = binding.inputDoctor.text.toString().trim()

            if (name.isEmpty() || poli.isEmpty() || doctor.isEmpty()) {
                Snackbar.make(binding.root, "⚠️ Isi semua field terlebih dahulu", Snackbar.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nextId = (viewModel.queueTickets.value?.size ?: 0) + 1

            // Buat tiket baru sesuai model QueueTicket
            val newTicket = QueueTicket(
                id = nextId,
                patientName = name,
                poliName = poli,
                doctorName = doctor,
                status = TicketStatus.WAIT,
                isRegular = true
            )

            viewModel.addQueue(newTicket)

            Snackbar.make(binding.root, "✅ Antrian baru berhasil ditambahkan", Snackbar.LENGTH_SHORT).show()
            findNavController().navigate(R.id.queueListFragment)
        }
    }

    // Menangani hasil kamera
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as? Bitmap
            imageBitmap?.let {
                binding.imgPreview.visibility = View.VISIBLE
                binding.imgPreview.setImageBitmap(it)
                photoBitmap = it
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
