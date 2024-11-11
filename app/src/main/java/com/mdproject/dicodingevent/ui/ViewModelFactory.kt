package com.mdproject.dicodingevent.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mdproject.dicodingevent.data.EventRepository
import com.mdproject.dicodingevent.di.Injection

class ViewModelFactory(private val pref: SettingPreferences, private val repo: EventRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel(pref, repo) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    SettingPreferences.getInstance(context.dataStore),
                    Injection.provideRepository(context)
                ).also { instance = it }
            }
    }

}