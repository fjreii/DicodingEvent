package com.mdproject.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mdproject.dicodingevent.data.response.ListEventsItem

class DetailViewModel : ViewModel() {

    private val _event = MutableLiveData<ListEventsItem>()

    val event: LiveData<ListEventsItem> = _event

    fun setEvent(eventData: ListEventsItem) {
        _event.value = eventData
    }
}