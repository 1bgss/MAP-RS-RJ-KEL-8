package com.example.antrianrumahsakit.model

enum class TicketStatus { WAIT, ON_CHECK, DONE }

data class QueueTicket(
    val ticketId: Int,
    val patient: Patient,
    val poli: String,
    val doctor: String,
    val appointmentDate: Long,
    var status: TicketStatus = TicketStatus.WAIT,
    val createdAt: Long = System.currentTimeMillis(),
    var estimatedServiceMinutes: Int = 0
)
