package com.example.antrianrumahsakit.ui.admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.antrianrumahsakit.R
import com.example.antrianrumahsakit.model.Doctor

class DoctorAdapter(
    private val doctors: MutableList<Doctor>,
    private val onEdit: (Doctor) -> Unit,
    private val onDelete: (Doctor) -> Unit
) : RecyclerView.Adapter<DoctorAdapter.DoctorViewHolder>() {

    inner class DoctorViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvDoctorName)
        val tvPoli: TextView = view.findViewById(R.id.tvDoctorPoli)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_doctor, parent, false)
        return DoctorViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorViewHolder, position: Int) {
        val doctor = doctors[position]
        holder.tvName.text = doctor.name
        holder.tvPoli.text = "Poli: ${doctor.poliName}"
        holder.btnEdit.setOnClickListener { onEdit(doctor) }
        holder.btnDelete.setOnClickListener { onDelete(doctor) }
    }

    override fun getItemCount() = doctors.size

    fun updateList(newList: List<Doctor>) {
        doctors.clear()
        doctors.addAll(newList)
        notifyDataSetChanged()
    }
}
