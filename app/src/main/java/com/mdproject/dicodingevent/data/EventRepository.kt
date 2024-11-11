package com.mdproject.dicodingevent.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.mdproject.dicodingevent.data.local.entity.EventEntity
import com.mdproject.dicodingevent.data.local.room.EventDao
import com.mdproject.dicodingevent.data.response.EventResponse
import com.mdproject.dicodingevent.data.retrofit.ApiService
import com.mdproject.dicodingevent.utils.AppExecutor
import com.mdproject.dicodingevent.utils.Result
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.withContext

class EventRepository private constructor(
    private val apiService: ApiService,
    private val eventDao: EventDao,
    private val appExecutors: AppExecutor
) {
    fun getAllEvents(status: Int): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)
        try {
            val response = apiService.getEvents(active = status)
            val eventList = response.listEvents.map { event ->
                val isFavorite = event.name.let {
                    eventDao.isEventFavorite(it)
                }
                EventEntity(
                    event.id,
                    event.name,
                    event.summary,
                    event.description,
                    event.imageLogo,
                    event.mediaCover,
                    event.category,
                    event.ownerName,
                    event.cityName,
                    event.quota,
                    event.registrants,
                    event.beginTime,
                    event.endTime,
                    event.link,
                    isFavorite,
                    status == 0,
                    status == 1
                )
            }

            eventDao.insertEventsData(eventList)
            emit(Result.Success(eventList))

        } catch (e: Exception) {
            emit(Result.Error(e.message.toString()))
        }
    }

    fun getFavoriteEvents(): LiveData<List<EventEntity>> {
        return eventDao.getFavoriteEvent()
    }

    fun searchEvent(query: String): LiveData<Result<List<EventEntity>>> = liveData {
        emit(Result.Loading)

        try {
            val dataLocal = eventDao.searchFinishedEvent(query).map { eventList ->
                if (eventList.isNotEmpty()) {
                    Result.Success(eventList)
                } else {
                    Result.Error("Error: $query")
                }
            }
            emitSource(dataLocal)
        } catch (exception: Exception) {
            emit(Result.Error(exception.message.toString()))
        }
    }

    suspend fun setFavoriteEvent(event: EventEntity, favorite: Boolean) {
        event.isFavorite = favorite
        withContext(appExecutors.diskIO.asCoroutineDispatcher()) {
            eventDao.updateEventsData(event)
        }
    }

    suspend fun getNearestEvent(): EventResponse? {
        val getEvent = try {
            apiService.getUpdatedEvent(active = -1, limit = 1)
        } catch (e: Exception) {
            null
        }
        return getEvent
    }

    companion object {
        @Volatile
        private var INSTANCE: EventRepository? = null

        fun getInstance(apiService: ApiService, eventDao: EventDao, appExecutors: AppExecutor): EventRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: EventRepository(apiService, eventDao, appExecutors).also { INSTANCE = it }
            }
        }
    }
}