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
import com.example.antrianrumahsakit.R
import com.example.antrianrumahsakit.databinding.FragmentManagePoliBinding
import com.example.antrianrumahsakit.model.Poli
import com.example.antrianrumahsakit.ui.admin.adapter.PoliAdapter
import com.example.antrianrumahsakit.viewmodel.SharedQueueViewModel
import com.google.android.material.snackbar.Snackbar

class ManagePoliFragment : Fragment() {

    private var _binding: FragmentManagePoliBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SharedQueueViewModel by activityViewModels()

    private lateinit var adapter: PoliAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManagePoliBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PoliAdapter(
            mutableListOf(),
            onEdit = { showEditDialog(it) },
            onDelete = { confirmDelete(it) }
        )

        binding.recyclerPoli.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerPoli.adapter = adapter

        // Observasi perubahan data poli
        viewModel.polis.observe(viewLifecycleOwner) { list ->
            adapter.updateList(list)
            binding.tvEmptyState.visibility =
                if (list.isNullOrEmpty()) View.VISIBLE else View.GONE
        }

        // Tombol tambah poli
        binding.fabAddPoli.setOnClickListener { showAddDialog() }
    }

    private fun showAddDialog() {
        val input = EditText(requireContext())
        input.hint = "Nama Poli"

        AlertDialog.Builder(requireContext())
            .setTitle("Tambah Poli Baru")
            .setView(input)
            .setPositiveButton("Tambah") { _, _ ->
                val name = input.text.toString().trim()
                if (name.isNotEmpty()) {
                    val newPoli = Poli(
                        id = (viewModel.polis.value?.size ?: 0) + 1,
                        name = name
                    )
                    viewModel.addPoli(newPoli)
                    Snackbar.make(requireView(), "Poli berhasil ditambahkan ✅", Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun showEditDialog(poli: Poli) {
        val input = EditText(requireContext())
        input.setText(poli.name)

        AlertDialog.Builder(requireContext())
            .setTitle("Edit Poli")
            .setView(input)
            .setPositiveButton("Simpan") { _, _ ->
                val newName = input.text.toString().trim()
                if (newName.isNotEmpty()) {
                    val list = viewModel.polis.value ?: mutableListOf()
                    val index = list.indexOfFirst { it.id == poli.id }
                    if (index != -1) {
                        list[index] = list[index].copy(name = newName)
                        viewModel.polis.value = list
                        Snackbar.make(requireView(), "Data poli diperbarui ✏️", Snackbar.LENGTH_SHORT).show()
                    }
                }
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    private fun confirmDelete(poli: Poli) {
        AlertDialog.Builder(requireContext())
            .setTitle("Hapus Poli")
            .setMessage("Yakin ingin menghapus ${poli.name}?")
            .setPositiveButton("Hapus") { _, _ ->
                val list = viewModel.polis.value ?: mutableListOf()
                list.removeAll { it.id == poli.id }
                viewModel.polis.value = list
                Snackbar.make(requireView(), "Poli dihapus ❌", Snackbar.LENGTH_SHORT).show()
            }
            .setNegativeButton("Batal", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
