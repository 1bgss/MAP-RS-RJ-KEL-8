package com.example.antrianrumahsakit.ui.dokter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.antrianrumahsakit.R
import com.example.antrianrumahsakit.model.QueueTicket

class DoctorHistoryAdapter(
    private var historyList: List<QueueTicket>
) : RecyclerView.Adapter<DoctorHistoryAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPatientName: TextView = itemView.findViewById(R.id.tvPatientName)
        val tvPoli: TextView = itemView.findViewById(R.id.tvPoli)
        val tvDoctorName: TextView = itemView.findViewById(R.id.tvDoctorName)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor_history, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ticket = historyList[position]
        holder.tvPatientName.text = ticket.patientName
        holder.tvPoli.text = ticket.poliName
        holder.tvDoctorName.text = ticket.doctorName
        holder.tvStatus.text = "Status: ${ticket.status}"
    }

    override fun getItemCount() = historyList.size

    fun updateData(newList: List<QueueTicket>) {
        historyList = newList
        notifyDataSetChanged()
    }
}
