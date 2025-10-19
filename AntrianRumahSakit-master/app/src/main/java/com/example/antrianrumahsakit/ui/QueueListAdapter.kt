package com.example.antrianrumahsakit.ui.pasien

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.antrianrumahsakit.R
import com.example.antrianrumahsakit.databinding.ItemQueueBinding
import com.example.antrianrumahsakit.model.QueueTicket
import com.example.antrianrumahsakit.model.TicketStatus

class QueueListAdapter(
    private var queueList: List<QueueTicket> = listOf(),
    private val onItemClick: (QueueTicket) -> Unit
) : RecyclerView.Adapter<QueueListAdapter.QueueViewHolder>() {

    inner class QueueViewHolder(private val binding: ItemQueueBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(ticket: QueueTicket) {
            binding.txtQueueNumber.text = "Q${ticket.id.toString().padStart(3, '0')}"
            binding.txtPatientName.text = ticket.patientName
            binding.txtPoli.text = ticket.poliName
            binding.txtStatus.text = when (ticket.status) {
                TicketStatus.WAIT -> "Menunggu"
                TicketStatus.ON_CHECK -> "Sedang Diperiksa"
                TicketStatus.DONE -> "Selesai"
            }

            // Warna indikator berdasarkan status antrian/penanganan
            val colorRes = when (ticket.status) {
                TicketStatus.WAIT -> R.color.yellow
                TicketStatus.ON_CHECK -> R.color.umn_blue
                TicketStatus.DONE -> R.color.green
            }

            binding.statusIndicator.setCardBackgroundColor(
                ContextCompat.getColor(binding.root.context, colorRes)
            )

            // Klik item → panggil callback ke fragment
            binding.root.setOnClickListener { onItemClick(ticket) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QueueViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemQueueBinding.inflate(inflater, parent, false)
        return QueueViewHolder(binding)
    }

    override fun getItemCount(): Int = queueList.size

    override fun onBindViewHolder(holder: QueueViewHolder, position: Int) {
        holder.bind(queueList[position])
    }

    fun submitList(newList: List<QueueTicket>) {
        queueList = newList
        notifyDataSetChanged()
    }
}
