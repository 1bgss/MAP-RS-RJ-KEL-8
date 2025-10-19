package com.example.antrianrumahsakit.manager

import com.example.antrianrumahsakit.model.QueueTicket
import com.example.antrianrumahsakit.model.TicketStatus

object QueueManager {

    private val avgTimeRegular = 15  // menit
    private val avgTimeNewPatient = 25 // menit

    fun calculateEstimatedWaitTime(queueList: List<QueueTicket>, targetTicket: QueueTicket): Int {
        val position = queueList.indexOfFirst { it.id == targetTicket.id }
        if (position == -1) return 0

        // Hitung estimasi berdasarkan urutan dan status pasien
        var totalTime = 0
        for (i in 0 until position) {
            val q = queueList[i]
            totalTime += if (q.isRegular) avgTimeRegular else avgTimeNewPatient
        }
        return totalTime
    }

    fun getActiveQueues(list: List<QueueTicket>): List<QueueTicket> {
        return list.filter { it.status == TicketStatus.WAIT || it.status == TicketStatus.ON_CHECK }
    }
}
