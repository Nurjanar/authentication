package ru.netology.nmedia.repository

import android.util.Log
import ru.netology.nmedia.api.AuthsApi
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.auth.AuthResponse

class AuthRepository {
    suspend fun authenticate(login: String, password: String): AuthResponse {
        Log.d("Auth", "Repostory после вызова authenticate ")
        val response = AuthsApi.service.updateUser(login, password)
        if (response.isSuccessful) {
            response.body()?.let { authResponse ->
                AppAuth.getInstance().setAuth(authResponse.id, authResponse.token)
            }
        } else {
            throw Exception("Authentication failed: ${response.errorBody()?.string()}")
        }
        return response.body()!!
    }
}