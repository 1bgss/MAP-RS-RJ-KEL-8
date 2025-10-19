package com.example.antrianrumahsakit.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.antrianrumahsakit.databinding.FragmentManagePatientBinding
import com.example.antrianrumahsakit.model.Patient
import com.example.antrianrumahsakit.ui.admin.adapter.PatientAdapter
import com.example.antrianrumahsakit.viewmodel.SharedQueueViewModel
import com.google.android.material.snackbar.Snackbar
import android.text.InputType
import android.widget.LinearLayout


class ManagePatientFragment : Fragment() {

    private var _binding: FragmentManagePatientBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedQueueViewModel by activityViewModels()

    private lateinit var adapter: PatientAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManagePatientBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = PatientAdapter(
            mutableListOf(),
            onEdit = { showEditDialog(it) },
            onDelete = { confirmDelete(it) }
        )

        binding.recyclerPatient.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerPatient.adapter = adapter

        viewModel.patients.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)
            binding.tvEmptyState.visibility =
                if (list.isNullOrEmpty()) View.VISIBLE else View.GONE
        }

        binding.fabAddPatient.setOnClickListener { showAddDialog() }
    }

    private fun showAddDialog() {

        val context = requireContext()

        val layout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 40, 50, 10)
        }

        val inputName = EditText(context).apply {
            hint = "Nama Pasien"
        }

        val inputAge = EditText(context).apply {
            hint = "Umur"
            inputType = InputType.TYPE_CLASS_NUMBER
        }

        layout.addView(inputName)
        layout.addView(inputAge)

        androidx.appcompat.app.AlertDialog.Builder(context)
            .setTitle("Tambah Pasien Baru")
            .setView(layout)
            .setPositiveButton("Tambah") { dialog, _ ->
                val name = inputName.text.toString().trim()
                val ageText = inputAge.text.toString().trim()
                val age = ageText.toIntOrNull()

                if (name.isNotEmpty() && age != null) {
                    val newPatient = com.example.antrianrumahsakit.model.Patient(
                        id = (viewModel.patients.value?.size ?: 0) + 1,
                        name = name,
                        age = age,
                        gender = "Laki-laki"
                    )
                    viewModel.addPatient(newPatient)
                    com.google.android.material.snackbar.Snackbar.make(
                        requireView(),
                        "Pasien berhasil ditambahkan ✅",
                        com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
            .setNegativeButton("Batal") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showEditDialog(patient: Patient) {
        val input = EditText(requireContext())
        input.setText(patient.name)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Pasien")
            .setView(input)
            .setPositiveButton("Simpan") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    val list = viewModel.patients.value ?: mutableListOf()
                    val index = list.indexOfFirst { it.id == patient.id }
                    if (index != -1) {
                        list[index] = list[index].copy(name = newName)
                        viewModel.patients.value = list
                        Snackbar.make(requireView(), "Data pasien diperbarui ✏️", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun confirmDelete(patient: Patient) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Pasien")
            .setMessage("Yakin ingin menghapus ${patient.name}?")
            .setPositiveButton("Hapus") { _, _ ->
                val list = viewModel.patients.value ?: mutableListOf()
                list.removeAll { it.id == patient.id }
                viewModel.patients.value = list
                Snackbar.make(requireView(), "Pasien dihapus ❌", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
