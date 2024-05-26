package com.dicoding.picodiploma.loginwithanimation.view.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.story.ListStoryItem


class MapsViewModel(private val repository: StoryRepository, private val userRepository: UserRepository) : ViewModel()  {
    private val _storiesLocation = MutableLiveData<List<ListStoryItem>>()
    val storiesLocation: MutableLiveData<List<ListStoryItem>> = _storiesLocation

    fun getSession(): LiveData<UserModel> {
        return userRepository.getSession().asLiveData()
    }

    suspend fun getStoriesWithLocation() {
        try {
            val response = repository.getStoriesWithLocation()
            _storiesLocation.postValue(response.listStory)
        } catch (e: Exception) {
            throw e
        }
    }
}