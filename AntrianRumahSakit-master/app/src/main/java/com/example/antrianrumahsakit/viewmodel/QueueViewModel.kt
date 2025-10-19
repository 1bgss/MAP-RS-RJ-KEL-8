package com.example.antrianrumahsakit.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.antrianrumahsakit.model.QueueTicket
import com.example.antrianrumahsakit.model.TicketStatus
import com.example.antrianrumahsakit.manager.QueueManager

class QueueViewModel : ViewModel() {

    private val _queueList = MutableLiveData<List<QueueTicket>>(emptyList())
    val queueList: LiveData<List<QueueTicket>> = _queueList

    fun addQueue(ticket: QueueTicket) {
        val currentList = _queueList.value?.toMutableList() ?: mutableListOf()
        currentList.add(ticket)
        _queueList.value = currentList
    }

    fun updateQueueStatus(id: Int, newStatus: TicketStatus) {
        val updatedList = _queueList.value?.map {
            if (it.id == id) it.copy(status = newStatus) else it
        }
        _queueList.value = updatedList
    }

    fun getEstimatedWaitTime(ticketId: Int): Int {
        val list = _queueList.value ?: return 0
        val ticket = list.find { it.id == ticketId } ?: return 0
        return QueueManager.calculateEstimatedWaitTime(list, ticket)
    }

    fun getCompletedQueues(): List<QueueTicket> {
        return _queueList.value?.filter { it.status == TicketStatus.DONE } ?: emptyList()
    }
}
