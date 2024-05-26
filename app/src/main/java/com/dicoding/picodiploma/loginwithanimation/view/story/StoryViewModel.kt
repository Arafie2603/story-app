package com.dicoding.picodiploma.loginwithanimation.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.story.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.story.Story
import kotlinx.coroutines.launch


class StoryViewModel(private val repository: StoryRepository) : ViewModel() {
    private val _stories = MutableLiveData<List<ListStoryItem>>()
    private val _detailStory = MutableLiveData<Result<Story>>()

    val stories: LiveData<PagingData<ListStoryItem>> = repository.getStory().cachedIn(viewModelScope)

    suspend fun getStories() {
        try {
            val response = repository.getStories()
            _stories.postValue(response.listStory)
        } catch (e: Exception) {
            throw e
        }
    }

    fun getDetailStory(id: String) {
        viewModelScope.launch {
            val result = repository.getDetailStory(id)
            _detailStory.postValue(result)
        }
    }
}