package com.example.antrianrumahsakit.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antrianrumahsakit.R
import com.example.antrianrumahsakit.databinding.FragmentManageDoctorBinding
import com.example.antrianrumahsakit.model.Doctor
import com.example.antrianrumahsakit.ui.admin.adapter.DoctorAdapter
import com.example.antrianrumahsakit.viewmodel.SharedQueueViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText


class ManageDoctorFragment : Fragment() {

    private var _binding: FragmentManageDoctorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedQueueViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManageDoctorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecyclerView()
        binding.fabAddDoctor.setOnClickListener { showAddDoctorDialog() }
    }

    private fun setupRecyclerView() {
        viewModel.doctors.observe(viewLifecycleOwner) { doctorList ->
            val adapter = DoctorAdapter(
                doctors = doctorList.toMutableList(),
                onEdit = { selected -> showEditDoctorDialog(selected) },
                onDelete = { deleted ->
                    viewModel.doctors.value?.remove(deleted)
                    viewModel.doctors.value = viewModel.doctors.value
                }
            )
            binding.recyclerDoctor.layoutManager = LinearLayoutManager(requireContext())
            binding.recyclerDoctor.adapter = adapter

            binding.tvEmptyState.visibility =
                if (doctorList.isEmpty()) View.VISIBLE else View.GONE
        }
    }


    private fun showAddDoctorDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_doctor, null)
        val nameInput = dialogView.findViewById<TextInputEditText>(R.id.etDoctorName)
        val specInput = dialogView.findViewById<TextInputEditText>(R.id.etDoctorSpec)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Tambah Dokter")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                val doctor = Doctor(
                    id = (0..9999).random(),
                    name = nameInput.text.toString(),
                    specialization = specInput.text.toString(),
                    poliName = "Poli Umum"
                )
                viewModel.addDoctor(doctor)
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showEditDoctorDialog(doctor: Doctor) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_doctor, null)
        val nameInput = dialogView.findViewById<TextInputEditText>(R.id.etDoctorName)
        val specInput = dialogView.findViewById<TextInputEditText>(R.id.etDoctorSpec)

        nameInput.setText(doctor.name)
        specInput.setText(doctor.specialization)

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Dokter")
            .setView(dialogView)
            .setPositiveButton("Simpan") { _, _ ->
                doctor.name = nameInput.text.toString()
                doctor.specialization = specInput.text.toString()
                viewModel.doctors.value = viewModel.doctors.value
            }
            .setNegativeButton("Hapus") { _, _ ->
                viewModel.doctors.value?.remove(doctor)
                viewModel.doctors.value = viewModel.doctors.value
            }
            .setNeutralButton("Batal", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
