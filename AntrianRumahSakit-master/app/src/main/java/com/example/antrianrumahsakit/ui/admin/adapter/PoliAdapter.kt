package com.example.antrianrumahsakit.ui.admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.antrianrumahsakit.R
import com.example.antrianrumahsakit.model.Poli

class PoliAdapter(
    private val polis: MutableList<Poli>,
    private val onEdit: (Poli) -> Unit,
    private val onDelete: (Poli) -> Unit
) : RecyclerView.Adapter<PoliAdapter.PoliViewHolder>() {

    inner class PoliViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvName: TextView = view.findViewById(R.id.tvPoliName)
        val btnEdit: ImageButton = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PoliViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_poli, parent, false)
        return PoliViewHolder(view)
    }

    override fun onBindViewHolder(holder: PoliViewHolder, position: Int) {
        val poli = polis[position]
        holder.tvName.text = poli.name
        holder.btnEdit.setOnClickListener { onEdit(poli) }
        holder.btnDelete.setOnClickListener { onDelete(poli) }
    }

    override fun getItemCount() = polis.size

    fun updateList(newList: List<Poli>) {
        polis.clear()
        polis.addAll(newList)
        notifyDataSetChanged()
    }
}
