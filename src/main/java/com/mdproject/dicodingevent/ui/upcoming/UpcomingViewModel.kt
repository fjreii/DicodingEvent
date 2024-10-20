package com.mdproject.dicodingevent.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mdproject.dicodingevent.data.response.EventResponse
import com.mdproject.dicodingevent.data.response.ListEventsItem
import com.mdproject.dicodingevent.data.retrofit.ApiConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel : ViewModel() {

    // LiveData untuk menampung data event
    private val _listEvents = MutableLiveData<List<ListEventsItem>>()
    val listEvents: LiveData<List<ListEventsItem>> get() = _listEvents

    // LiveData untuk menandai status loading
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    init {
        getEvents()
    }

   private fun getEvents() {
        _isLoading.value = true
        val call = ApiConfig.getApiService().getUpcomingEvents()
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

