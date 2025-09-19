package com.example.antrianrumahsakit.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.antrianrumahsakit.databinding.ItemQueueBinding
import com.example.antrianrumahsakit.model.QueueTicket
import com.example.antrianrumahsakit.model.TicketStatus

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
            binding.tvTicketId.text = t.ticketId.toString()
            binding.tvName.text = t.patient.name
            binding.tvMedicalId.text = t.patient.uniqueMedicalId ?: "-"

            binding.tvPoli.text = t.poli
            binding.tvDokter.text = t.doctor
            binding.tvEta.text = "${t.estimatedServiceMinutes} min"

            // status badge
            binding.tvStatus.text = when (t.status) {
                TicketStatus.WAIT -> "WAIT"
                TicketStatus.ON_CHECK -> "ON-CHECK"
                TicketStatus.DONE -> "DONE"
            }
            val bgRes = when (t.status) {
                TicketStatus.WAIT -> android.R.color.holo_orange_light
                TicketStatus.ON_CHECK -> android.R.color.holo_green_light
                TicketStatus.DONE -> android.R.color.darker_gray
            }
            binding.tvStatus.setBackgroundResource(bgRes)

            // kategori pasien (New vs Regular)
            if (t.patient.isRegular) {
                binding.tvCategory.text = "Regular"
                binding.tvCategory.setBackgroundResource(android.R.color.holo_green_light)
            } else {
                binding.tvCategory.text = "New"
                binding.tvCategory.setBackgroundResource(android.R.color.holo_blue_light)
            }

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
