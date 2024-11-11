package com.mdproject.dicodingevent.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.mdproject.dicodingevent.data.EventRepository
import com.mdproject.dicodingevent.data.local.entity.EventEntity
import com.mdproject.dicodingevent.data.response.ListEventsItem
import com.mdproject.dicodingevent.utils.Result
import kotlinx.coroutines.launch

class MainViewModel(private val pref: SettingPreferences, private val repo: EventRepository) : ViewModel() {
    fun getUpcomingEvent(): LiveData<Result<List<EventEntity>>> {
        return repo.getAllEvents(1)
    }

    fun getFinishedEvent(): LiveData<Result<List<EventEntity>>> {
        return repo.getAllEvents(0)
    }

    fun getFavoriteEvent(): LiveData<List<EventEntity>> {
        return repo.getFavoriteEvents()
    }

    fun searchEvent(query: String): LiveData<Result<List<EventEntity>>> {
        return repo.searchEvent(query)
    }

    private val _event = MutableLiveData<EventEntity>()
    val event: LiveData<EventEntity> get() = _event

    fun setEvent(eventData: EventEntity) {
        _event.value = eventData
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    fun addEventToFavorite(event: EventEntity) {
        viewModelScope.launch {
            repo.setFavoriteEvent(event, true)
        }
    }

    fun removeEventFromFavorite(event: EventEntity) {
        viewModelScope.launch {
            repo.setFavoriteEvent(event, false)
        }
    }

    fun getNotificationSetting(): LiveData<Boolean> {
        return pref.getNotificationSetting().asLiveData()
    }

    fun saveNotificationSetting(isActive: Boolean) {
        viewModelScope.launch {
            pref.saveNotificationSetting(isActive)
        }
    }
}