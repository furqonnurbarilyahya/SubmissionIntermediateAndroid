package com.bangkit.submissionintermediateandroid.data.retrofit

import com.bangkit.submissionintermediateandroid.data.response.LoginResponse
import com.bangkit.submissionintermediateandroid.data.response.RegisterResponse
import com.bangkit.submissionintermediateandroid.data.response.StoryResponse
import com.bangkit.submissionintermediateandroid.data.response.UploadFileResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): StoryResponse

    @Multipart
    @POST("stories")
    suspend fun uploadImage (
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): UploadFileResponse

    @GET("stories")
    suspend fun getStoriesWithLocation(
        @Query("location") location : Int = 1,
    ): StoryResponse
}