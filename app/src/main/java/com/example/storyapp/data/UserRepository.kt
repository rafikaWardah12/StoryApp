package com.example.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.data.database.StoryDatabase
import com.example.storyapp.data.pref.UserModel
import com.example.storyapp.data.pref.UserPreference
import com.example.storyapp.response.AddNewStoryResponse
import com.example.storyapp.response.DetailStoryResponse
import com.example.storyapp.response.ListStoryItem
import com.example.storyapp.response.LoginResponse
import com.example.storyapp.response.SignupResponse
import com.example.storyapp.response.StoryResponse
import com.example.storyapp.retrofit.ApiConfig
import com.example.storyapp.retrofit.ApiService
import com.example.storyapp.utils.ResultState
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class UserRepository private constructor(
    private val apiService: ApiService,
    private val userPreference: UserPreference,
    private val storyDatabase: StoryDatabase

) {
    companion object {
        @Volatile
        private var instance: UserRepository? = null

        fun getInstance(
            apiService: ApiService,
            userPreference: UserPreference,
            storyDatabase: StoryDatabase
        ): UserRepository = UserRepository(apiService, userPreference, storyDatabase)
//            instance ?: synchronized(this) {
//                instance ?: UserRepository(apiService, userPreference, storyDatabase)
//            }.also { instance = it }
    }

    //BEST PRACTICE COROUTINES
    fun registerUser(name: String, email: String, password: String) =
        liveData {
            emit(ResultState.Loading)
            try {
                val response = apiService.getRegisterUser(name,email,password)
                emit(ResultState.Success(response))
            } catch (exc: HttpException){
                val error = exc.response()?.errorBody()?.string()
                val jsonResponse = Gson().fromJson(error, SignupResponse::class.java)
               emit(jsonResponse.message.let { ResultState.Error(it) })
            } catch (e: Exception){
                emit(ResultState.Error(e.message.toString()))
            }
        }

    fun loginUser(email: String, password: String): LiveData<ResultState<LoginResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                val request = apiService.getLoginUser(email, password)
                emit(ResultState.Success(request))
                saveSession(UserModel(email,request.loginResult.token))
            } catch (exc: HttpException) {
                emit(ResultState.Error(exc.message.toString()))
            } catch (e: Exception){
                emit(ResultState.Error(e.message.toString()))
            }
        }

//    fun getStoryUser() : LiveData<ResultState<StoryResponse>> =
//        liveData {
//            emit(ResultState.Loading)
//            val token = userPreference.getSession().first().token
//            val apiService = ApiConfig.getApiService(token)
//            try {
//                val result = apiService.getStories()
//                emit(ResultState.Success(result))
//            } catch (exc: HttpException) { //hanya terhubung online
//                emit(ResultState.Error(exc.message.toString()))
//            } catch (e: Exception){
//                emit(ResultState.Error(e.message.toString()))
//            }
//        }

    fun getDetailStory(id: String): LiveData<ResultState<DetailStoryResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                userPreference.getSession()
                val message = runBlocking {userPreference.getSession().first()}
                val callService = ApiConfig.getApiService(message.token)
                val result = callService.detailStory(id)
                emit(ResultState.Success(result))
            } catch (exc: HttpException) {
                emit(ResultState.Error(exc.message.toString()))
            } catch (e: Exception){
                emit(ResultState.Error(e.message.toString()))
            }

        }

    fun addStory(description: RequestBody, photo: MultipartBody.Part) : LiveData<ResultState<AddNewStoryResponse>> =
        liveData {
            emit(ResultState.Loading)
            try {
                userPreference.getSession()
                val message = runBlocking {userPreference.getSession().first()}
                val callService = ApiConfig.getApiService(message.token)
                val response = callService.addNewStory(description, photo)
                emit(ResultState.Success(response))
            } catch (exc: HttpException){
                emit(ResultState.Error(exc.message.toString()))
            } catch (e: Exception){
                emit(ResultState.Error(e.message.toString()))
            }
        }
    fun uploadFile(description: RequestBody, photo: MultipartBody.Part): LiveData<ResultState<AddNewStoryResponse>> =
        liveData {
            emit(ResultState.Loading)
            try{
                userPreference.getSession()
                val message = runBlocking {userPreference.getSession().first()}
                val callService = ApiConfig.getApiService(message.token)
                val succesResponse = callService.addNewStory(description, photo)
                emit(ResultState.Success(succesResponse))
                if (succesResponse.error == true) {
                    emit(ResultState.Error(succesResponse.message!!))
                }
            } catch (exc: HttpException) {
                val errorBody = exc.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, AddNewStoryResponse::class.java )
                emit(ResultState.Error(errorResponse.toString()) )
            } catch (e: Exception){
                emit(ResultState.Error(e.message.toString()))
            }
        }

    fun getStoriesWithLocation(): LiveData<ResultState<StoryResponse>> =
        liveData {
            emit(ResultState.Loading)
            try{
                userPreference.getSession()
                val message = runBlocking {userPreference.getSession().first()}
                val callService = ApiConfig.getApiService(message.token)
                val successResponse = callService.getStoriesWithLocation()
                emit(ResultState.Success(successResponse))
            } catch (exc: HttpException) {
                val errorBody = exc.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, StoryResponse::class.java)
                emit(ResultState.Error(errorResponse.toString()))
            } catch (e: Exception) {
                emit(ResultState.Error(e.message.toString()))
            }
        }

    //PAGING
    fun getStoryPaging(): LiveData<PagingData<ListStoryItem>> {
        userPreference.getSession()
        val message = runBlocking {userPreference.getSession().first()}
        val callService = ApiConfig.getApiService(message.token)
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, callService),
            pagingSourceFactory = {
//                StoryPagingSource(apiService)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
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


}
