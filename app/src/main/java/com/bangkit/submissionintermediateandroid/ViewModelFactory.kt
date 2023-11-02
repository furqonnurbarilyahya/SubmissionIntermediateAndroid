package com.bangkit.submissionintermediateandroid

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bangkit.submissionintermediateandroid.data.UserRepository
import com.bangkit.submissionintermediateandroid.data.di.Injection
import com.bangkit.submissionintermediateandroid.data.pref.UserPreference
import com.bangkit.submissionintermediateandroid.data.pref.dataStore
import com.bangkit.submissionintermediateandroid.ui.viewmodel.AddStoryViewModel
import com.bangkit.submissionintermediateandroid.ui.viewmodel.LoginViewModel
import com.bangkit.submissionintermediateandroid.ui.viewmodel.MainViewModel
import com.bangkit.submissionintermediateandroid.ui.viewmodel.MapsViewModel
import com.bangkit.submissionintermediateandroid.ui.viewmodel.SignUpViewModel

class ViewModelFactory(private val userRepository: UserRepository, private val userPreference: UserPreference) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(SignUpViewModel::class.java) -> {
                SignUpViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(userRepository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(userRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (INSTANCE == null) {
                synchronized(ViewModelFactory::class.java) {
                    INSTANCE = ViewModelFactory(Injection.provideRepository(context), UserPreference.getInstance(context.dataStore))
                }
            }
            return INSTANCE as ViewModelFactory
        }
        fun clearInstance() {
            INSTANCE == null
        }
    }
}