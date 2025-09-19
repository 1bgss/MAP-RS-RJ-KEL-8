package com.example.antrianrumahsakit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.antrianrumahsakit.manager.QueueManager
import com.example.antrianrumahsakit.model.*
import java.time.LocalDate

class QueueViewModel : ViewModel() {

    private val _tickets = MutableLiveData<MutableList<QueueTicket>>(mutableListOf())
    val tickets: LiveData<MutableList<QueueTicket>> = _tickets

    private val _history = MutableLiveData<MutableList<QueueTicket>>(mutableListOf())
    val history: LiveData<MutableList<QueueTicket>> = _history

    // daftar pasien global
    private val patients = mutableListOf<Patient>()

    private val doctors = listOf(
        Doctor("Dr. Andi", "Poli Umum", listOf(java.time.DayOfWeek.MONDAY, java.time.DayOfWeek.WEDNESDAY)),
        Doctor("Dr. Sari", "Poli Umum", listOf(java.time.DayOfWeek.FRIDAY)),
        Doctor("Dr. Lina", "Poli Anak", listOf(java.time.DayOfWeek.TUESDAY, java.time.DayOfWeek.THURSDAY)),
        Doctor("Dr. Yanto", "Poli Gigi", listOf(java.time.DayOfWeek.MONDAY, java.time.DayOfWeek.THURSDAY))
    )

    private var nextPatientId = 1
    private var nextTicketId = 1
    private var nextMedicalId = 1   // counter untuk kode rekam medis otomatis

    fun getDoctorsByPoli(poli: String): List<Doctor> =
        doctors.filter { it.poli == poli }

    fun addPatient(name: String, medicalId: String?): Patient {
        if (!medicalId.isNullOrEmpty()) {
            val existing = patients.find { it.uniqueMedicalId == medicalId }
            if (existing != null) return existing
        }
        val newPatient = if (!medicalId.isNullOrEmpty()) {
            Patient(nextPatientId++, name, medicalId)
        } else {
            Patient(nextPatientId++, name, null)
        }
        patients.add(newPatient)
        return newPatient
    }

    fun addTicket(patient: Patient, poli: String, doctorName: String, date: Long): QueueTicket? {
        val doctor = doctors.find { it.name == doctorName && it.poli == poli } ?: return null

        if (!QueueManager.validateSchedule(doctor, date)) return null

        val ticketsToday = _tickets.value?.count {
            it.doctor == doctor.name &&
                    it.poli == doctor.poli &&
                    sameDay(it.appointmentDate, date)
        } ?: 0

        if (QueueManager.isCapacityFull(doctor, ticketsToday)) return null

        val estMinutes = QueueManager.computeEstimatedMinutes(doctor, patient, ticketsToday)

        val ticket = QueueTicket(
            ticketId = nextTicketId++,
            patient = patient,
            poli = poli,
            doctor = doctor.name,
            appointmentDate = date,
            estimatedServiceMinutes = estMinutes
        )

        val ticketsList = _tickets.value ?: mutableListOf()
        ticketsList.add(ticket)
        _tickets.value = ticketsList

        return ticket
    }

    /**
     * Update status tiket. Kalau pasien baru selesai (DONE) → generate RM otomatis.
     * Return false kalau gagal (misalnya sudah ada pasien ON_CHECK untuk dokter yang sama).
     */
    fun updateStatus(ticketId: Int, status: TicketStatus): Boolean {
        val ticketsList = _tickets.value ?: return false
        val idx = ticketsList.indexOfFirst { it.ticketId == ticketId }
        if (idx == -1) return false

        val oldTicket = ticketsList[idx]

        // 🚨 Batasi hanya 1 pasien ON_CHECK per dokter
        if (status == TicketStatus.ON_CHECK) {
            val alreadyOnCheck = ticketsList.any {
                it.doctor == oldTicket.doctor &&
                        it.poli == oldTicket.poli &&
                        it.status == TicketStatus.ON_CHECK &&
                        it.ticketId != oldTicket.ticketId
            }
            if (alreadyOnCheck) return false
        }

        val updatedTicket = if (status == TicketStatus.DONE && oldTicket.patient.uniqueMedicalId.isNullOrEmpty()) {
            val newMedicalId = "RM%03d".format(nextMedicalId++)
            val newPatient = oldTicket.patient.copy(uniqueMedicalId = newMedicalId)
            patients.removeIf { it.id == oldTicket.patient.id }
            patients.add(newPatient)
            oldTicket.copy(patient = newPatient, status = status)
        } else {
            oldTicket.copy(status = status)
        }

        if (status == TicketStatus.DONE) {
            ticketsList.removeAt(idx)
            _tickets.value = ticketsList

            val historyList = _history.value ?: mutableListOf()
            historyList.add(updatedTicket)
            _history.value = historyList
        } else {
            ticketsList[idx] = updatedTicket
            _tickets.value = ticketsList
        }
        return true
    }

    fun getTicketById(id: Int): QueueTicket? {
        return _tickets.value?.find { it.ticketId == id }
    }

    fun getCurrentServing(poli: String, doctor: String): QueueTicket? {
        return _tickets.value?.firstOrNull {
            it.poli == poli && it.doctor == doctor && it.status == TicketStatus.ON_CHECK
        }
    }

    private fun sameDay(ts1: Long, ts2: Long): Boolean {
        val d1 = LocalDate.ofEpochDay(ts1 / 86400000)
        val d2 = LocalDate.ofEpochDay(ts2 / 86400000)
        return d1 == d2
    }
}
