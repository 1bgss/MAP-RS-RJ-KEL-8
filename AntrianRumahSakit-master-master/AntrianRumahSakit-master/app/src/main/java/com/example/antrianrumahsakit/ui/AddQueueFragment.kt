package com.example.antrianrumahsakit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.antrianrumahsakit.databinding.FragmentAddQueueBinding
import com.example.antrianrumahsakit.viewmodel.QueueViewModel

class AddQueueFragment : Fragment() {
    private val vm: QueueViewModel by activityViewModels()
    private var _binding: FragmentAddQueueBinding? = null
    private val binding get() = _binding!!

    private val poliList = listOf("Poli Umum", "Poli Anak", "Poli Gigi")
    private val dokterMap = mapOf(
        "Poli Umum" to listOf("Dr. Andi", "Dr. Sari", "Dr. Budi"),
        "Poli Anak" to listOf("Dr. Lina", "Dr. Dodi", "Dr. Rina"),
        "Poli Gigi" to listOf("Dr. Yanto", "Dr. Citra", "Dr. Dani")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddQueueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 🔹 Setup dropdown Poli
        val poliAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, poliList)
        binding.dropdownPoli.setAdapter(poliAdapter)

        // 🔹 Kalau Poli dipilih → update dropdown Dokter
        binding.dropdownPoli.setOnItemClickListener { _, _, position, _ ->
            val selectedPoli = poliList[position]
            val dokterList = dokterMap[selectedPoli] ?: emptyList()
            val dokterAdapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, dokterList)
            binding.dropdownDokter.setAdapter(dokterAdapter)
            binding.dropdownDokter.text = null // reset pilihan dokter
        }

        // 🔹 Tombol Tambah Pasien
        binding.btnAdd.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val complaint = binding.etComplaint.text.toString().trim()
            val poli = binding.dropdownPoli.text.toString().trim()
            val dokter = binding.dropdownDokter.text.toString().trim()

            if (name.isNotEmpty() && complaint.isNotEmpty() && poli.isNotEmpty() && dokter.isNotEmpty()) {
                // Simpan ke ViewModel
                val ticket = vm.addTicket(name, complaint, poli, dokter)

                // Navigasi ke halaman Detail pasien baru
                val action = AddQueueFragmentDirections.actionAddToDetail(ticket.id)
                findNavController().navigate(action)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
