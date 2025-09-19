package com.example.antrianrumahsakit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.antrianrumahsakit.databinding.FragmentPoliBinding
import com.example.antrianrumahsakit.model.Doctor
import com.example.antrianrumahsakit.viewmodel.QueueViewModel
import java.time.LocalDate
import java.time.ZoneId

class PoliFragment : Fragment() {
    private var _binding: FragmentPoliBinding? = null
    private val binding get() = _binding!!
    private val vm: QueueViewModel by activityViewModels()

    // simple map for open/close status per doctor per day (in-memory)
    private val closedDoctorsToday = mutableSetOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPoliBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // show all poli + doctors from ViewModel
        val poliSet = listOf("Poli Umum", "Poli Anak", "Poli Gigi")
        val container = binding.container
        container.removeAllViews()

        poliSet.forEach { poli ->
            val doctors = vm.getDoctorsByPoli(poli)
            if (doctors.isNotEmpty()) {
                // header
                val header = TextView(requireContext())
                header.text = poli
                header.textSize = 18f
                header.setPadding(12, 12, 12, 12)
                container.addView(header)

                doctors.forEach { doc ->
                    val card = layoutInflater.inflate(com.example.antrianrumahsakit.R.layout.item_poli_doctor, container, false)
                    val tvName = card.findViewById<TextView>(com.example.antrianrumahsakit.R.id.tvDoctorName)
                    val tvSchedule = card.findViewById<TextView>(com.example.antrianrumahsakit.R.id.tvDoctorSchedule)
                    val tvCapacity = card.findViewById<TextView>(com.example.antrianrumahsakit.R.id.tvDoctorCapacity)
                    val btnToggle = card.findViewById<TextView>(com.example.antrianrumahsakit.R.id.btnToggleOpen)

                    tvName.text = doc.name
                    tvSchedule.text = "Hari: ${doc.schedule.joinToString { it.name.substring(0,3) }}"
                    val todayCount = vm.tickets.value?.count { it.doctor == doc.name && sameDay(it.appointmentDate, System.currentTimeMillis()) } ?: 0
                    tvCapacity.text = "Hari ini: $todayCount / ${doc.capacityPerDay}"

                    btnToggle.text = if (closedDoctorsToday.contains(doc.name)) "Buka" else "Tutup"
                    btnToggle.setOnClickListener {
                        if (closedDoctorsToday.contains(doc.name)) {
                            closedDoctorsToday.remove(doc.name)
                            Toast.makeText(requireContext(), "${doc.name} dibuka untuk pendaftaran", Toast.LENGTH_SHORT).show()
                        } else {
                            closedDoctorsToday.add(doc.name)
                            Toast.makeText(requireContext(), "${doc.name} ditutup untuk pendaftaran", Toast.LENGTH_SHORT).show()
                        }
                        btnToggle.text = if (closedDoctorsToday.contains(doc.name)) "Buka" else "Tutup"
                    }

                    container.addView(card)
                }
            }
        }
    }

    private fun sameDay(ts1: Long, ts2: Long): Boolean {
        val d1 = LocalDate.ofEpochDay(ts1 / 86400000)
        val d2 = LocalDate.ofEpochDay(ts2 / 86400000)
        return d1 == d2
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
