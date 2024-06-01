package com.example.storyapp.view.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp.data.UserRepository
import com.example.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch

//Fungsi view model antar view ke repository
class LoginViewModel(private val userRepository: UserRepository): ViewModel() {
    //get repository
    //ambil live data dari repository loginuser

    fun loginUser(email: String, password: String) =
        userRepository.loginUser(email, password)
}