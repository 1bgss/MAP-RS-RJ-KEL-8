package com.example.antrianrumahsakit.model

import java.time.DayOfWeek

data class Doctor(
    val name: String,
    val poli: String,
    val schedule: List<DayOfWeek>, // hari praktek
    val avgTimeNewPatient: Int = 12,   // menit
    val avgTimeRegular: Int = 6,       // menit
    val capacityPerDay: Int = 30
)
