package com.dicoding.picodiploma.loginwithanimation.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }
    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?>
        get() = _token

    fun getToken() {
        viewModelScope.launch {
            try {
                repository.getToken().collect {
                    _token.value = it
                }
            } catch (e: Exception) {
                // Tangani kesalahan jika diperlukan
                Log.e("MainViewModel", "Error getting token: ${e.message}", e)
            }
        }
    }
}