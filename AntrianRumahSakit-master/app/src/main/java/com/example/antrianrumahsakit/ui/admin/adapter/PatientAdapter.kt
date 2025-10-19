package com.example.antrianrumahsakit.ui.admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.antrianrumahsakit.R
import com.example.antrianrumahsakit.model.Patient

class PatientAdapter(
    private val patients: MutableList<Patient>,
    private val onEdit: (Patient) -> Unit,
    private val onDelete: (Patient) -> Unit
) : RecyclerView.Adapter<PatientAdapter.PatientViewHolder>() {

    inner class PatientViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvPatientName)
        val tvAge: TextView = view.findViewById(R.id.tvPatientAge)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_patient, parent, false)
        return PatientViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val patient = patients[position]
        holder.tvName.text = patient.name
        holder.tvAge.text = "Usia: ${patient.age} tahun"
        holder.btnEdit.setOnClickListener { onEdit(patient) }
        holder.btnDelete.setOnClickListener { onDelete(patient) }
    }

    override fun getItemCount() = patients.size

    fun updateList(newList: List<Patient>) {
        patients.clear()
        patients.addAll(newList)
        notifyDataSetChanged()
    }
}
