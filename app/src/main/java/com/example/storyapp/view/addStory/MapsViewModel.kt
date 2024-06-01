package com.example.storyapp.view.addStory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.example.storyapp.data.UserRepository

data class MapsViewModel(private val userRepository: UserRepository): ViewModel() {
    fun getStoriesWithLocation() = userRepository.getStoriesWithLocation()
    fun getSession() = userRepository.getSession().asLiveData()
}
