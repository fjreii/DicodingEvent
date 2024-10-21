package com.mdproject.dicodingevent.data.retrofit

import com.mdproject.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    fun getEvents(): Call<EventResponse>

    @GET("events?active=1")
    fun getUpcomingEvents(): Call<EventResponse>

    @GET("events?active=0")
    fun getFinishedEvents(): Call<EventResponse>

    @GET("events")
    fun searchEvents(
        @Query("q") keyword: String,
        @Query("active") active: Int = -1
    ): Call<EventResponse>

}