package com.mdproject.dicodingevent.data.retrofit

import com.mdproject.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    suspend fun getEvents(
        @Query("active")
        active: Int
    ): EventResponse

    @GET("events?active=1")
    fun getUpcomingEvents(): Call<EventResponse>

    @GET("events?active=0")
    fun getFinishedEvents(): Call<EventResponse>

    @GET("events")
    suspend fun getUpdatedEvent(
        @Query("active")
        active: Int = -1,
        @Query("limit")
        limit: Int = 40
    ): EventResponse

}