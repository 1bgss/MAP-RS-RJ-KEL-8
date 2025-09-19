package com.example.antrianrumahsakit.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.antrianrumahsakit.databinding.FragmentAddQueueBinding
import com.example.antrianrumahsakit.viewmodel.QueueViewModel
import java.util.*

class AddQueueFragment : Fragment() {
    private val vm: QueueViewModel by activityViewModels()
    private var _binding: FragmentAddQueueBinding? = null
    private val binding get() = _binding!!

    private val poliList = listOf("Poli Umum", "Poli Anak", "Poli Gigi")
    private var selectedDate: Long? = null

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

        // Setup dropdown Poli
        val poliAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, poliList)
        binding.dropdownPoli.setAdapter(poliAdapter)

        // Update dokter saat poli dipilih
        binding.dropdownPoli.setOnItemClickListener { _, _, position, _ ->
            val selectedPoli = poliList[position]
            val dokterList = vm.getDoctorsByPoli(selectedPoli).map { it.name }
            val dokterAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, dokterList)
            binding.dropdownDokter.setAdapter(dokterAdapter)
            binding.dropdownDokter.text = null
        }

        // Pilih tanggal
        binding.btnSelectDate.setOnClickListener {
            val c = Calendar.getInstance()
            val dialog = DatePickerDialog(
                requireContext(),
                { _, y, m, d ->
                    val cal = Calendar.getInstance()
                    cal.set(y, m, d, 0, 0, 0)
                    selectedDate = cal.timeInMillis
                    binding.tvSelectedDate.text = "${d}/${m + 1}/$y"
                },
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            )
            dialog.show()
        }

        // Tombol tambah
        binding.btnAdd.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val complaint = binding.etComplaint.text.toString().trim()
            val medicalId = binding.etMedicalId.text.toString().trim().ifEmpty { null }
            val poli = binding.dropdownPoli.text.toString().trim()
            val dokter = binding.dropdownDokter.text.toString().trim()

            if (name.isEmpty() || complaint.isEmpty() || poli.isEmpty() || dokter.isEmpty() || selectedDate == null) {
                Toast.makeText(requireContext(), "Lengkapi semua data", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val patient = vm.addPatient(name, medicalId)
            val ticket = vm.addTicket(patient, poli, dokter, selectedDate!!)

            if (ticket == null) {
                Toast.makeText(requireContext(), "Jadwal tidak valid atau kapasitas penuh", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val action = AddQueueFragmentDirections.actionAddToDetail(ticket.ticketId)
            findNavController().navigate(action)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
