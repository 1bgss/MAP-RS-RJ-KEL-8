package com.example.antrianrumahsakit.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.antrianrumahsakit.databinding.ItemPoliPasienBinding
import com.example.antrianrumahsakit.model.Poli

class PoliAdapter :
    ListAdapter<Poli, PoliAdapter.PoliViewHolder>(DIFF_CALLBACK) {

    inner class PoliViewHolder(private val binding: ItemPoliPasienBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(poli: Poli) {
            binding.tvPoliName.text = poli.name
            binding.tvPoliDesc.text = poli.description ?: "Tidak ada deskripsi"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoliViewHolder {
        val binding = ItemPoliPasienBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PoliViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PoliViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Poli>() {
            override fun areItemsTheSame(oldItem: Poli, newItem: Poli): Boolean = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Poli, newItem: Poli): Boolean = oldItem == newItem
        }
    }
}
