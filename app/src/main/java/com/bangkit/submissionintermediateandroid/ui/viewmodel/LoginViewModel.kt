package com.bangkit.submissionintermediateandroid.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.submissionintermediateandroid.data.Result
import com.bangkit.submissionintermediateandroid.data.response.LoginResponse
import com.bangkit.submissionintermediateandroid.data.UserRepository
import com.bangkit.submissionintermediateandroid.data.pref.UserModel
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        _isLoading.value = true
        return userRepository.login(email, password)
    }
    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            userRepository.saveSession(user)
        }
    }
}