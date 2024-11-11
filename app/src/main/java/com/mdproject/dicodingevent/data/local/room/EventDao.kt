package com.mdproject.dicodingevent.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mdproject.dicodingevent.data.local.entity.EventEntity

@Dao
interface EventDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventsData(events: List<EventEntity>)

    @Update
    suspend fun updateEventsData(events: EventEntity)

    @Query("SELECT * FROM dicoding_event WHERE isFinished = 1")
    fun getFinishedEvent(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM dicoding_event WHERE isUpcoming = 1")
    fun getUpcomingEvent(): LiveData<List<EventEntity>>

    @Query("SELECT * FROM dicoding_event WHERE isFavorite = 1")
    fun getFavoriteEvent(): LiveData<List<EventEntity>>

    @Query("DELETE FROM dicoding_event WHERE isFavorite = 0")
    suspend fun deleteAll()

    @Query("SELECT EXISTS(SELECT * FROM dicoding_event WHERE isFavorite = 1 AND name = :name)")
    suspend fun isEventFavorite(name: String): Boolean

    @Query(
        """
        SELECT * FROM dicoding_event 
        WHERE isFinished = 1 
        AND(
            name LIKE '%' || :query || '%' OR
            description LIKE '%' || :query || '%' OR
            summary LIKE '%' || :query || '%' 
            )
    """
    )
    fun searchFinishedEvent(query: String): LiveData<List<EventEntity>>
}