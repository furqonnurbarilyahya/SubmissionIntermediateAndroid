package com.bangkit.submissionintermediateandroid.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.bangkit.submissionintermediateandroid.data.UserRepository
import java.io.File

class AddStoryViewModel (private val userRepository: UserRepository) : ViewModel() {
    fun uploadImage(file: File, description: String) = userRepository.uploadImage(file, description)
}