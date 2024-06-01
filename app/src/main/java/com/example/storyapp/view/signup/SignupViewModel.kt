package com.example.storyapp.view.signup

import android.os.Handler.Callback
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.response.SignupResponse
import com.example.storyapp.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.http.Body

//Hubungan dg aktivity
class SignupViewModel(private val userRepository: UserRepository): ViewModel() {
   //Nulis register yg ada di repository
    //Ngasih live data ke aktivity ambil dari repository
    //BIkin fun ambil live data

    fun registerUser(name: String, email: String, password: String) =
        userRepository.registerUser(name,email, password)
}