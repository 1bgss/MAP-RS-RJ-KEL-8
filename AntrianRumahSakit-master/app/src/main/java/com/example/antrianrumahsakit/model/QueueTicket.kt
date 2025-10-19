package com.example.antrianrumahsakit.model

enum class TicketStatus { WAIT, ON_CHECK, DONE }

data class QueueTicket(
    val id: Int,
    val patientName: String,
    val poliName: String,
    val doctorName: String,
    var status: TicketStatus = TicketStatus.WAIT,
    var isRegular: Boolean = false,
    var appointmentDate: String? = null
)
