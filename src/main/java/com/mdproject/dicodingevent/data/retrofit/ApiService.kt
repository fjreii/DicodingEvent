package com.mdproject.dicodingevent.data.retrofit

import com.mdproject.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvent(
        @Query("active") active: String
    ): Call<EventResponse>
}