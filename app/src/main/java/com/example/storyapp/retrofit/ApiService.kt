package com.example.storyapp.retrofit

import com.example.storyapp.response.AddNewStoryResponse
import com.example.storyapp.response.DetailStoryResponse
import com.example.storyapp.response.LoginResponse
import com.example.storyapp.response.SignupResponse
import com.example.storyapp.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    suspend fun getRegisterUser(
        @Field ("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
        ): SignupResponse

    @FormUrlEncoded
    @POST("login")
   suspend fun getLoginUser(
        @Field("email") email: String, //text form
        @Field("password") password: String
    ): LoginResponse

   @GET("stories")
   suspend fun getStories(@Query("page") page: Int = 1,
                          @Query("size") size:Int = 15
   ): StoryResponse

   @GET("stories")
   suspend fun getStoriesWithLocation(
       @Query("location") location: Int = 1 //udah ada default value, jdi tdk wajib nmbahin parameter
   ): StoryResponse

   @GET("stories/{id}")
   suspend fun detailStory(
        @Path("id") id: String
   ): DetailStoryResponse

   @Multipart
   @POST("stories")
   suspend fun addNewStory(
       @Part("description") description: RequestBody,
       @Part file: MultipartBody.Part
   ): AddNewStoryResponse
}