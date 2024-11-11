package com.mdproject.dicodingevent.di

import android.content.Context
import com.mdproject.dicodingevent.data.EventRepository
import com.mdproject.dicodingevent.data.local.room.EventDatabase
import com.mdproject.dicodingevent.data.retrofit.ApiConfig
import com.mdproject.dicodingevent.utils.AppExecutor

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiservice = ApiConfig.getApiService()
        val db = EventDatabase.getDatabase(context)
        val dao = db.eventDao()
        val executor = AppExecutor()
        return EventRepository.getInstance(apiservice, dao, executor)
    }
}