package ru.netology.nmedia.viewmodel

import android.util.Log
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

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()
    val data: LiveData<AuthState> = AppAuth.getInstance()
        .authStateFlow
        .asLiveData(Dispatchers.Default)
    val authenticated: Boolean
        get() = AppAuth.getInstance().authStateFlow.value.id != 0L

    private val _authResult = MutableLiveData<AuthResult>()
    val authResult: LiveData<AuthResult> get() = _authResult

    fun authenticate(login: String, pass: String) {
        Log.d("Auth", "Model внутри authenticate ")
        viewModelScope.launch {
            try {
                val response = repository.authenticate(login, pass)
                Log.d("Auth", "Model repository.authenticate ")
                _authResult.value = response.let { AuthResult.Success(it) }
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