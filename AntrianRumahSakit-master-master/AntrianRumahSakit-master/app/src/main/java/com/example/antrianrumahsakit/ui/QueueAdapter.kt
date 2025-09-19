package com.example.antrianrumahsakit.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.antrianrumahsakit.databinding.ItemQueueBinding
import com.example.antrianrumahsakit.model.QueueTicket

class QueueAdapter(private val onClick: (QueueTicket) -> Unit) :
    RecyclerView.Adapter<QueueAdapter.VH>() {

    private var items: List<QueueTicket> = emptyList()

    fun submitList(list: List<QueueTicket>) {
        items = list
        notifyDataSetChanged()
    }

    inner class VH(private val binding: ItemQueueBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(t: QueueTicket) {
            binding.tvTicketId.text = t.id.toString()
            binding.tvName.text = t.name
            binding.tvComplaint.text = t.complaint
            binding.tvPoli.text = t.poli
            binding.tvDokter.text = t.dokter
            binding.root.setOnClickListener { onClick(t) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemQueueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount() = items.size
}
