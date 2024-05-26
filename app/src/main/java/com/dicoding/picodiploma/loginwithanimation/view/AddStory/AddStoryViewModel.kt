package com.dicoding.picodiploma.loginwithanimation.view.AddStory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.StoryRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.FileUploadResponse
import kotlinx.coroutines.launch
import java.io.File

class AddStoryViewModel(private val repository: StoryRepository) : ViewModel() {
    fun uploadFile(image: File, description: String, lat: Double, lon:Double, onResult: (Result<FileUploadResponse>) -> Unit) {
        viewModelScope.launch {
            val result = repository.uploadStory(image, description, lat, lon)
            onResult(result)
        }
    }
}

