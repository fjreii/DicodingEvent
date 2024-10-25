package com.mdproject.dicodingevent.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mdproject.dicodingevent.data.response.EventResponse
import com.mdproject.dicodingevent.data.response.ListEventsItem
import com.mdproject.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UpcomingViewModel : ViewModel() {

    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents: LiveData<List<ListEventsItem>> = _listEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private companion object {
        const val TAG = "UpcomingViewModel"
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    }

    init {
        getUpcomingEvents()
    }

    private fun getUpcomingEvents() {
        _isLoading.value = true
        ApiConfig.getApiService().getUpcomingEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    response.body()?.let { eventResponse ->
                        _listEvents.value = filterUpcomingEvents(eventResponse.listEvents)
                    } ?: run {
                        Log.e(TAG, "Response body is null")
                        _errorMessage.value = "Tidak ada diterima dari server."
                    }
                } else {
                    Log.e(TAG, "Response error: ${response.message()}")
                    _errorMessage.value = "Gagal memuat event: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "Request failed: ${t.message}", t)
                _errorMessage.value = "Tidak dapat mengambil event. Silakan periksa koneksi Anda."
            }
        })
    }

    private fun filterUpcomingEvents(events: List<ListEventsItem>?): List<ListEventsItem> {
        val currentTime = LocalDateTime.now()
        return events?.filter { event ->
            try {
                val eventEndTime = LocalDateTime.parse(event.endTime, formatter)
                currentTime.isBefore(eventEndTime)
            } catch (e: Exception) {
                Log.e(TAG, "Error parsing event time: ${event.endTime}", e)
                false
            }
        } ?: emptyList()
    }
}

