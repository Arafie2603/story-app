package com.dicoding.picodiploma.loginwithanimation.data

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.picodiploma.loginwithanimation.api.StoryApiService
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.response.ErrorResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.FileUploadResponse
import com.dicoding.picodiploma.loginwithanimation.data.story.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.story.Story
import com.dicoding.picodiploma.loginwithanimation.data.story.StoryPagingSource
import com.dicoding.picodiploma.loginwithanimation.data.story.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.firstOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import java.io.File

class StoryRepository private constructor(
    private val apiService: StoryApiService,
    private val userPreference: UserPreference
) {

    fun getStory() : LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService)
            }
        ).liveData
    }

    suspend fun uploadStory(image: File, description: String, lat:Double, lon:Double) : Result<FileUploadResponse> {
        return try {
            val descriptionBody = RequestBody.create("text/plain".toMediaTypeOrNull(), description)
            val requestFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), image)
            val imagePart = MultipartBody.Part.createFormData("photo", image.name, requestFile)
            val response = apiService.uploadImage(imagePart, descriptionBody, lat, lon)
            Result.success(response)
        } catch (e: HttpException) {
            val jsonIntString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonIntString, ErrorResponse::class.java)
            Result.failure(Exception(errorBody.message))
        }
    }

    suspend fun getStoriesWithLocation() : StoryResponse {
        try {
            userPreference.getSession().firstOrNull()?.token ?: throw NullPointerException("Token is null")
            return apiService.getStoriesWithLocation()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getStories(): StoryResponse {
        try {
            userPreference.getSession().firstOrNull()?.token
                ?: throw NullPointerException("Token is null")
            return apiService.getStories()
        } catch (e: Exception) {
            throw e
        }
    }

    suspend fun getDetailStory(id: String): Result<Story> {
        return try {
            val response = apiService.getDetailStory(id)
            if (response.error == false) {
                response.story?.let {
                    Result.success(it)
                } ?: Result.failure(Exception("Story is null"))
            } else {
                Result.failure(Exception(response.message ?: "Unknown error"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


    companion object {
        fun getInstance(
            storyApiService: StoryApiService,
            userPreference: UserPreference
        ) = StoryRepository(storyApiService, userPreference)
    }
}


