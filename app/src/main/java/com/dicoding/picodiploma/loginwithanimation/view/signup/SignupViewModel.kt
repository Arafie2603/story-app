package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.response.RegisterResponse
import kotlinx.coroutines.launch

class SignupViewModel(private val repository: UserRepository) : ViewModel() {
    fun register(name: String, email: String, password: String, onResult: (Result<RegisterResponse>) -> Unit) {
        viewModelScope.launch {
            val response = repository.register(name, email, password)
            onResult(response)
        }
    }
}


