package com.buchi.buttoned.authentication.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.buchi.buttoned.MainActivity
import com.buchi.buttoned.authentication.extensions.isValidPassword
import com.buchi.buttoned.authentication.extensions.isValidUserName
import com.buchi.buttoned.authentication.model.User
import com.buchi.buttoned.authentication.presentation.AuthViewModel
import com.buchi.buttoned.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * LoginFragment to handle authentication login processes and is a [Fragment] subclass.
 * This LoginFragment is part of the navigation graph [auth_nav]
 */
@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    // Shared auth viewModel for auth processes
    private val authViewModel: AuthViewModel by activityViewModels { viewModelFactory }
    private val viewModel: LoginViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginAction.setOnClickListener {
            val userName = binding.loginUsernameEntry.text.toString().trim()
            val password = binding.loginPasswordEntry.text.toString().trim()
            if (userName.isValidUserName && password.isValidPassword) {
                val loginEvent = LoginStateEvent.Login(userName = userName, password = password)
                viewModel.setStateEvent(loginEvent)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.dataState.mapLatest { ds->
                authViewModel.dataStateChanged(ds)

                ds.data?.let { event ->
                    event.getContentIfNotHandled()?.let { loginViewState ->
                        viewModel.processViewState(loginViewState)
                    }
                }
            }.launchIn(lifecycleScope)

            viewModel.viewState.collectLatest { vs->
                vs.user?.let {
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                }
            }
        }
    }
}