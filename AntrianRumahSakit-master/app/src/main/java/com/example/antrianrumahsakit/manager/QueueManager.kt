package com.example.antrianrumahsakit.manager

import com.example.antrianrumahsakit.model.*
import java.time.LocalDate
import java.time.ZoneId

object QueueManager {

    fun validateSchedule(doctor: Doctor, appointmentDate: Long): Boolean {
        val day = LocalDate.ofEpochDay(appointmentDate / 86400000)
            .dayOfWeek
        return doctor.schedule.contains(day)
    }

    fun computeEstimatedMinutes(
        doctor: Doctor,
        patient: Patient,
        ticketsBefore: Int
    ): Int {
        val base = if (patient.isRegular) doctor.avgTimeRegular else doctor.avgTimeNewPatient
        return base * (ticketsBefore + 1)
    }

    fun isCapacityFull(doctor: Doctor, ticketsToday: Int): Boolean {
        return ticketsToday >= doctor.capacityPerDay
    }
}
