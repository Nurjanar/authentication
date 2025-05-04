package ru.netology.nmedia.repository

import ru.netology.nmedia.api.PostsApi
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.auth.AuthResponse

class AuthRepository(private val api: PostsApi) {
    suspend fun authenticate(login: String, password: String): AuthResponse {
        val response = api.service.updateUser(login, password)
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