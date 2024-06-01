package com.example.storyapp.view.addStory

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.UserRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStoryViewModel(private val userRepository: UserRepository): ViewModel() {
    fun addStory (description: RequestBody, photo: MultipartBody.Part) = userRepository.addStory(description, photo)
    fun uploadFile (description: RequestBody, photo: MultipartBody.Part) = userRepository.uploadFile(description, photo)
}