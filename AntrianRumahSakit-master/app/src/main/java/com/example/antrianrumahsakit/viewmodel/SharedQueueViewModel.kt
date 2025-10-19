package com.example.antrianrumahsakit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.antrianrumahsakit.model.Patient
import com.example.antrianrumahsakit.model.Doctor
import com.example.antrianrumahsakit.model.Poli
import com.example.antrianrumahsakit.model.QueueTicket
import com.example.antrianrumahsakit.model.TicketStatus

class SharedQueueViewModel : ViewModel() {
    //SharedQueue ini merupakan model utama untuk menghubungkan data dummy di dashboard admin, pasien, dan dokter
    // ===== ROLE SAAT INI =====
    private val _currentRole = MutableLiveData<String>()
    val currentRole: LiveData<String> get() = _currentRole

    fun setCurrentRole(role: String) {
        _currentRole.value = role
    }

    // ===== DATA IN-MEMORY =====
    val patients = MutableLiveData<MutableList<Patient>>(mutableListOf())
    val doctors = MutableLiveData<MutableList<Doctor>>(mutableListOf())
    val polis = MutableLiveData<MutableList<Poli>>(mutableListOf())
    val queueTickets = MutableLiveData<MutableList<QueueTicket>>(mutableListOf())

    // ===== TAMBAH DATA =====
    fun addPatient(patient: Patient) {
        val list = patients.value ?: mutableListOf()
        list.add(patient)
        patients.value = list
    }

    fun addDoctor(doctor: Doctor) {
        val list = doctors.value ?: mutableListOf()
        list.add(doctor)
        doctors.value = list
    }

    fun addPoli(poli: Poli) {
        val list = polis.value ?: mutableListOf()
        list.add(poli)
        polis.value = list
    }

    fun addQueue(ticket: QueueTicket) {
        val list = queueTickets.value ?: mutableListOf()
        list.add(ticket)
        queueTickets.value = list
    }

    // ===== UPDATE STATUS ANTRIAN =====
    fun updateQueueStatus(ticketId: Int, newStatus: TicketStatus): Boolean {
        val list = queueTickets.value ?: return false
        val ticket = list.find { it.id == ticketId } ?: return false

        // (opsional) contoh validasi: hanya satu pasien ON_CHECK per dokter
        if (newStatus == TicketStatus.ON_CHECK) {
            val sameDoctor = list.any {
                it.doctorName == ticket.doctorName &&
                        it.status == TicketStatus.ON_CHECK &&
                        it.id != ticket.id
            }
            if (sameDoctor) return false
        }

        ticket.status = newStatus
        queueTickets.value = list
        return true
    }

    // ===== HITUNG ESTIMASI WAKTU TUNGGU =====
    fun calculateEstimatedWaitTime(ticketId: Int): Int {
        val list = queueTickets.value ?: return 0
        val target = list.find { it.id == ticketId } ?: return 0

        val index = list.indexOf(target)
        if (index <= 0) return 0

        var totalMinutes = 0
        for (i in 0 until index) {
            val patientBefore = list[i]
            totalMinutes += if (patientBefore.isRegular) 6 else 12
        }
        return totalMinutes
    }

    // ===== HAPUS SEMUA DATA =====
    fun clearAll() {
        patients.value?.clear()
        doctors.value?.clear()
        polis.value?.clear()
        queueTickets.value?.clear()
    }

    fun getTicketById(id: Int): QueueTicket? {
        return queueTickets.value?.find { it.id == id }
    }

    fun updateStatus(id: Int, newStatus: TicketStatus) {
        val currentList = queueTickets.value ?: mutableListOf()
        val updated = currentList.map {
            if (it.id == id) it.copy(status = newStatus) else it
        }.toMutableList()
        queueTickets.value = updated
    }

    init {
        // ===== DUMMY DOKTER (10) =====
        if (doctors.value.isNullOrEmpty()) {
            val dummyDoctors = mutableListOf(
                Doctor(1, "dr. Andi Santoso", "Umum", "Poli Umum"),
                Doctor(2, "drg. Rina Puspita", "Gigi", "Poli Gigi"),
                Doctor(3, "dr. Ahmad Yusuf", "Mata", "Poli Mata"),
                Doctor(4, "dr. Lestari Dewi", "Anak", "Poli Anak"),
                Doctor(5, "dr. Bambang Wijaya", "THT", "Poli THT"),
                Doctor(6, "dr. Sri Mulyani", "Kandungan", "Poli Kandungan"),
                Doctor(7, "dr. Fajar Rahman", "Penyakit Dalam", "Poli Penyakit Dalam"),
                Doctor(8, "dr. Wulan Citra", "Kulit", "Poli Kulit"),
                Doctor(9, "dr. Bima Prasetyo", "Saraf", "Poli Saraf"),
                Doctor(10, "dr. Sari Utami", "Gizi", "Poli Gizi")
            )
            doctors.value = dummyDoctors
        }

        // ===== DUMMY PASIEN (10) =====
        if (patients.value.isNullOrEmpty()) {
            val dummyPatients = mutableListOf(
                Patient(1, "Budi Hartono", 32, "Laki-laki"),
                Patient(2, "Siti Aminah", 28, "Perempuan"),
                Patient(3, "Ahmad Fauzi", 40, "Laki-laki"),
                Patient(4, "Dewi Lestari", 22, "Perempuan"),
                Patient(5, "Rizki Saputra", 35, "Laki-laki"),
                Patient(6, "Putri Maharani", 27, "Perempuan"),
                Patient(7, "Teguh Hidayat", 30, "Laki-laki"),
                Patient(8, "Yuni Kartika", 33, "Perempuan"),
                Patient(9, "Eko Pranoto", 45, "Laki-laki"),
                Patient(10, "Nina Anggraini", 26, "Perempuan")
            )
            patients.value = dummyPatients
        }

        // ===== DUMMY POLI (10) =====
        if (polis.value.isNullOrEmpty()) {
            val dummyPoli = mutableListOf(
                Poli(1, "Poli Umum", "Pemeriksaan umum"),
                Poli(2, "Poli Gigi", "Perawatan gigi dan mulut"),
                Poli(3, "Poli Mata", "Konsultasi dan pemeriksaan mata"),
                Poli(4, "Poli Anak", "Perawatan kesehatan anak"),
                Poli(5, "Poli THT", "Telinga, Hidung, dan Tenggorokan"),
                Poli(6, "Poli Kandungan", "Kesehatan ibu dan janin"),
                Poli(7, "Poli Penyakit Dalam", "Konsultasi penyakit dalam"),
                Poli(8, "Poli Kulit", "Masalah kulit dan kecantikan"),
                Poli(9, "Poli Saraf", "Gangguan sistem saraf"),
                Poli(10, "Poli Gizi", "Konsultasi gizi dan diet")
            )
            polis.value = dummyPoli
        }

        // ===== DUMMY ANTRIAN (10) =====
        if (queueTickets.value.isNullOrEmpty()) {
            val dummyTickets = mutableListOf(
                QueueTicket(1, "Budi Hartono", "Poli Umum", "dr. Andi Santoso", TicketStatus.WAIT),
                QueueTicket(2, "Siti Aminah", "Poli Umum", "dr. Andi Santoso", TicketStatus.ON_CHECK),
                QueueTicket(3, "Ahmad Fauzi", "Poli Gigi", "drg. Rina Puspita", TicketStatus.WAIT),
                QueueTicket(4, "Dewi Lestari", "Poli Anak", "dr. Lestari Dewi", TicketStatus.WAIT),
                QueueTicket(5, "Rizki Saputra", "Poli THT", "dr. Bambang Wijaya", TicketStatus.WAIT),
                QueueTicket(6, "Putri Maharani", "Poli Kandungan", "dr. Sri Mulyani", TicketStatus.WAIT),
                QueueTicket(7, "Teguh Hidayat", "Poli Penyakit Dalam", "dr. Fajar Rahman", TicketStatus.WAIT),
                QueueTicket(8, "Yuni Kartika", "Poli Kulit", "dr. Wulan Citra", TicketStatus.WAIT),
                QueueTicket(9, "Eko Pranoto", "Poli Saraf", "dr. Bima Prasetyo", TicketStatus.WAIT),
                QueueTicket(10, "Nina Anggraini", "Poli Gizi", "dr. Sari Utami", TicketStatus.WAIT)
            )
            queueTickets.value = dummyTickets
        }
    }
}
