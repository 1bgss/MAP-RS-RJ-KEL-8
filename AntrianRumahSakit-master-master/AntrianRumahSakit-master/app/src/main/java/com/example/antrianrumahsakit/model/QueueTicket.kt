package com.example.antrianrumahsakit.model

data class QueueTicket(
    val id: Int,
    val name: String,
    val complaint: String,
    val poli: String,
    val dokter: String,
    val timestamp: Long = System.currentTimeMillis()
)
