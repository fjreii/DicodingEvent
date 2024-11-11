package com.mdproject.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mdproject.dicodingevent.data.local.entity.EventEntity
import com.mdproject.dicodingevent.data.response.ListEventsItem

class DetailViewModel : ViewModel() {

    private val _event = MutableLiveData<EventEntity>()
    val event: LiveData<EventEntity> get() = _event

    fun setEvent(eventData: EventEntity) {
        _event.value = eventData
    }
}