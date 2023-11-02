package com.bangkit.submissionintermediateandroid.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.submissionintermediateandroid.data.UserRepository
import kotlinx.coroutines.launch

class MapsViewModel(private val userRepository: UserRepository) : ViewModel() {
    val location = userRepository.location

    fun getStoriesWithLocation() {
        viewModelScope.launch {
            userRepository.getStoriesWithLocation()
        }
    }
}