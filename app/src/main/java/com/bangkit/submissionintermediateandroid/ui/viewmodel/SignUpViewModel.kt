package com.bangkit.submissionintermediateandroid.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.submissionintermediateandroid.data.Result
import com.bangkit.submissionintermediateandroid.data.response.RegisterResponse
import com.bangkit.submissionintermediateandroid.data.UserRepository

class SignUpViewModel (private val userRepository: UserRepository): ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun registerUser(name: String, email: String, password: String) : LiveData<Result<RegisterResponse>> {
        _isLoading.value = true
        return userRepository.registerUser(name, email, password)
    }

}