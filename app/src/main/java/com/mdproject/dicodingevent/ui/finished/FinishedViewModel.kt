package com.mdproject.dicodingevent.ui.finished

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

class FinishedViewModel : ViewModel() {

    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents: LiveData<List<ListEventsItem>> get() = _listEvents

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        getEvents()
    }

    private fun getEvents() {
        _isLoading.value = true
        val call = ApiConfig.getApiService().getFinishedEvents()
        call.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                if (response.isSuccessful) {
                    val eventResponse = response.body()
                    eventResponse?.let {
                        // Process the list of events here
                        Log.d("MainActivity", "Event list: ${it.listEvents}")
                    }
                } else {
                    Log.e("MainActivity", "API call failed with response code: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                Log.e("MainActivity", "API call failed: ${t.message}")
            }
        })
    }
}