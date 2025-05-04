package ru.netology.nmedia.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.auth.AuthResponse
import ru.netology.nmedia.auth.AuthState
import ru.netology.nmedia.repository.AuthRepository

class AuthViewModel(private val repository: AuthRepository) : ViewModel() {
    val data: LiveData<AuthState> = AppAuth.getInstance()
        .authStateFlow
        .asLiveData(Dispatchers.Default)
    val authenticated: Boolean
        get() = AppAuth.getInstance().authStateFlow.value.id != 0L

    private val _authResult = MutableLiveData<AuthResult>()
    val authResult: LiveData<AuthResult> get() = _authResult

    fun authenticate(login: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.authenticate(login, password)
                _authResult.value = AuthResult.Success(response)
            } catch (e: Exception) {
                _authResult.value = AuthResult.Error(e.message ?: "Unknown error")
            }

        }
    }

}

sealed class AuthResult {
    data class Success(val data: AuthResponse) : AuthResult()
    data class Error(val message: String) : AuthResult()
}