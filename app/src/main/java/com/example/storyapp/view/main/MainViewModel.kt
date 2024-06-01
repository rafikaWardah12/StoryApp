package com.example.storyapp.view.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.UserRepository
import com.example.storyapp.di.Injection
import com.example.storyapp.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class MainViewModel(private val userRepository: UserRepository): ViewModel() {
//    fun getStoryUser() = userRepository.getStoryUser()
    fun getSession() = userRepository.getSession().asLiveData()

    fun logoutUser() {
        viewModelScope.launch { userRepository.logout()
        }
    }
    val story: LiveData<PagingData<ListStoryItem>> = userRepository.getStoryPaging().cachedIn(viewModelScope)

}

//Api Detail user