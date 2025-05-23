package ru.netology.nmedia.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.auth.AppAuth
import ru.netology.nmedia.databinding.FragmentAuthBinding
import ru.netology.nmedia.viewmodel.AuthResult
import ru.netology.nmedia.viewmodel.AuthViewModel

class AuthFragment : Fragment() {
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentAuthBinding.inflate(
            inflater,
            container,
            false
        )
        viewModel.authResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is AuthResult.Success -> {
                    AppAuth.getInstance().setAuth(result.data.id, result.data.token)
                    findNavController().navigateUp()
                }

                is AuthResult.Error -> {
                    Snackbar.make(binding.root, result.message, Snackbar.LENGTH_LONG).show()
                }
            }
        }

        binding.loginButton.setOnClickListener {
            val login = binding.login.text.toString()
            val pass = binding.password.text.toString()
            Log.d("Auth", "Login attempt with login: $login and password: $pass")
            viewModel.authenticate(login, pass)
            Log.d("Auth", "AuthFragment после вызова viewModel.authenticate ")
        }
        return binding.root
    }
}
