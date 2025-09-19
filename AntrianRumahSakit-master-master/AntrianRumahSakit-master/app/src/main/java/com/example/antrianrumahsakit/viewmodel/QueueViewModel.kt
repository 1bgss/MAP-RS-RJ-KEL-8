package com.example.antrianrumahsakit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.antrianrumahsakit.model.QueueTicket

class QueueViewModel : ViewModel() {
    private val _tickets = MutableLiveData<MutableList<QueueTicket>>(mutableListOf())
    val tickets: LiveData<MutableList<QueueTicket>> = _tickets

    private var nextId = 1

    // 🔹 Hilangkan data dummy biar kosong dari awal
    // init {
    //     addTicket("Budi", "Sakit kepala", "Poli Umum", "Dr. Andi")
    //     addTicket("Sinta", "Demam", "Poli Anak", "Dr. Lina")
    // }

    fun addTicket(name: String, complaint: String, poli: String, dokter: String): QueueTicket {
        val t = QueueTicket(nextId++, name, complaint, poli, dokter)
        val list = _tickets.value ?: mutableListOf()
        list.add(0, t) // newest top
        _tickets.value = list
        return t
    }

    fun getTicketById(id: Int): QueueTicket? {
        return _tickets.value?.find { it.id == id }
    }

    fun removeTicket(id: Int) {
        val list = _tickets.value ?: return
        list.removeAll { it.id == id }
        _tickets.value = list
    }
}
