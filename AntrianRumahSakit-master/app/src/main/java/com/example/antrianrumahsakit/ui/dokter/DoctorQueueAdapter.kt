package com.example.antrianrumahsakit.ui.dokter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.antrianrumahsakit.R
import com.example.antrianrumahsakit.model.QueueTicket

class DoctorQueueAdapter(
    private var queueList: List<QueueTicket>,
    private val onItemClick: (QueueTicket) -> Unit
) : RecyclerView.Adapter<DoctorQueueAdapter.DoctorQueueViewHolder>() {

    inner class DoctorQueueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPatientName: TextView = itemView.findViewById(R.id.tvPatientName)
        val tvPoliName: TextView = itemView.findViewById(R.id.tvPoliName)
        val tvAppointmentDate: TextView = itemView.findViewById(R.id.tvAppointmentDate)
        val tvQueueNumber: TextView = itemView.findViewById(R.id.tvQueueNumber)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorQueueViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor_queue, parent, false)
        return DoctorQueueViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorQueueViewHolder, position: Int) {
        val ticket = queueList[position]
        holder.tvPatientName.text = ticket.patientName
        holder.tvPoliName.text = "Poli: ${ticket.poliName}"
        holder.tvAppointmentDate.text = "Tanggal: ${ticket.appointmentDate}"
        holder.tvQueueNumber.text = "Nomor Antrian: ${ticket.id}"

        holder.itemView.setOnClickListener { onItemClick(ticket) }
    }

    override fun getItemCount(): Int = queueList.size

    fun updateData(newList: List<QueueTicket>) {
        queueList = newList
        notifyDataSetChanged()
    }
}
