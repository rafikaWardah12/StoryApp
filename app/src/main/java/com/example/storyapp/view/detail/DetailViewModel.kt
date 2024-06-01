package com.example.storyapp.view.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.data.UserRepository

class DetailViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getDetailStory(id: String)=
        userRepository.getDetailStory(id) //passing data
    fun getSession()=userRepository.getSession().asLiveData()
}