package com.bangkit.submissionintermediateandroid.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.bangkit.submissionintermediateandroid.data.database.StoryDatabase
import com.bangkit.submissionintermediateandroid.data.pref.UserModel
import com.bangkit.submissionintermediateandroid.data.response.RegisterResponse
import com.bangkit.submissionintermediateandroid.data.retrofit.ApiService
import com.bangkit.submissionintermediateandroid.data.pref.UserPreference
import com.bangkit.submissionintermediateandroid.data.response.ListStoryItem
import com.bangkit.submissionintermediateandroid.data.response.LoginResponse
import com.bangkit.submissionintermediateandroid.data.response.StoryResponse
import com.bangkit.submissionintermediateandroid.data.response.UploadFileResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException
import java.io.File

class UserRepository private constructor(
    private val storyDatabase: StoryDatabase,
    private val apiService: ApiService,
    private val userPreference: UserPreference
) {

    private val registerProcess = MediatorLiveData<Result<RegisterResponse>>()

    private val _story = MutableLiveData<List<ListStoryItem>>()
    val story : LiveData<List<ListStoryItem>> = _story

    private val _location = MutableLiveData<List<ListStoryItem>>()
    val location : LiveData<List<ListStoryItem>> = _location

    fun getAllStories(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService),
            pagingSourceFactory = {
            storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun registerUser(name: String, email: String, password: String): LiveData<Result<RegisterResponse>>  {
        return liveData {
            registerProcess.value = Result.Loading
            try {
                val createNewUser = apiService.registerUser(name, email, password)
                emit(Result.Success(createNewUser))
            } catch (e: Exception) {
                Log.e(TAG_SIGNUPVIEWMODEL, "User gagal dibuat: ${e.message.toString()}")
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        return liveData {
            emit(Result.Loading)
            try {
                val response= apiService.loginUser(email, password)
                emit(Result.Success(response))
            } catch (e: Exception) {
                Log.e(TAG_LOGINVIEWMODEL, "Login: ${e.message.toString()}")
                emit(Result.Error(e.message.toString()))
            }
        }
    }

    suspend fun getStoriesWithLocation() {
        try {
            _location.value = apiService.getStoriesWithLocation().listStory
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
            val errorMessage = errorBody.message
            Log.e("GETSTORY", "$errorMessage")
        }
    }

    fun uploadImage(imageFile: File, description: String): LiveData<Result<UploadFileResponse>> {
        return liveData {

        emit(Result.Loading)
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        try {
            val succesResponse = apiService.uploadImage(multipartBody, requestBody)
            emit(Result.Success(succesResponse))
        } catch (e: HttpException) {
            val errorBody = e.response()?.errorBody()?.string()
            val errorResponse = Gson().fromJson(errorBody, UploadFileResponse::class.java)
            emit(Result.Error(errorResponse.message))
        }
        }
    }

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        private const val TAG_SIGNUPVIEWMODEL = "SignUpViewModel"
        private const val TAG_LOGINVIEWMODEL = "LoginViewModel"

        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            storyDatabase: StoryDatabase,
            apiService: ApiService,
            userPreference: UserPreference,
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(storyDatabase,apiService,userPreference)
            }.also { instance = it }
    }
}