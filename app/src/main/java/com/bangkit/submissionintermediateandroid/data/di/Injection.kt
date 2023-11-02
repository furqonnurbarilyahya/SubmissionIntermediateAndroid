package com.bangkit.submissionintermediateandroid.data.di

import android.content.Context
import com.bangkit.submissionintermediateandroid.data.retrofit.ApiConfig
import com.bangkit.submissionintermediateandroid.data.UserRepository
import com.bangkit.submissionintermediateandroid.data.database.StoryDatabase
import com.bangkit.submissionintermediateandroid.data.pref.UserPreference
import com.bangkit.submissionintermediateandroid.data.pref.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking


object Injection {
    fun provideRepository(context: Context): UserRepository {
        val database = StoryDatabase.getDatabase(context)
        val pref = UserPreference.getInstance(context.dataStore)
        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService(user.token)
        return UserRepository.getInstance(database, apiService, pref)
    }
}