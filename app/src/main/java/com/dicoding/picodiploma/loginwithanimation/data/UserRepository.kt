package com.dicoding.picodiploma.loginwithanimation.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

import com.dicoding.picodiploma.loginwithanimation.api.AuthApiService
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.response.ErrorResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.RegisterResponse
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.HttpException
import retrofit2.await

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: AuthApiService
) {
    val userSession: Flow<UserModel> = userPreference.getSession()

    suspend fun register(name: String, email: String, password: String): Result<RegisterResponse> {
        return try {
            val response = apiService.register(name, email, password)
            Result.success(response)
        } catch (e: HttpException) {
            val jsonIntString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonIntString, ErrorResponse::class.java)
            Result.failure(Exception(errorBody.message))
        }
    }

    suspend fun login(email: String, password: String): LiveData<Result<LoginResponse>> {
        return try {
            val response = apiService.login(email, password).await()
            val token = response.loginResult?.token.toString()
            userPreference.saveLogin(token)
            MutableLiveData(Result.success(response))
        } catch (e: HttpException) {
            val jsonInString = e.response()?.errorBody()?.string()
            val errorBody = Gson().fromJson(jsonInString, ErrorResponse::class.java)
            Log.e("UserRepository", "Login failed: $errorBody")
            MutableLiveData(Result.failure(e))
        } catch (e: Exception) {
            MutableLiveData(Result.failure(e))
        }
    }





    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    fun getToken() : Flow<String?> {
        return userPreference.getToken()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    companion object {
        fun getInstance(
            userPreference: UserPreference,
            apiService: AuthApiService
        ) = UserRepository(userPreference, apiService)
    }
}