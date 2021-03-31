package com.buchi.buttoned.authentication.presentation.signup

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
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.buchi.buttoned.R
import com.buchi.buttoned.authentication.model.User
import com.buchi.buttoned.authentication.presentation.AuthViewModel
import com.buchi.buttoned.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

/**
 * SignUpFragment to handle authentication login processes and is a [Fragment] subclass.
 * This LoginFragment is part of the navigation graph [auth_nav]
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SignUpFragment : Fragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var binding: FragmentSignUpBinding

    // Shared auth viewModel for auth processes
    private val authViewModel: AuthViewModel by activityViewModels { viewModelFactory }
    private val viewModel by viewModels<SignUpViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerAction.setOnClickListener {
            val fullName = binding.fullnameEntry.text.toString()
            val userName = binding.usernameEntry.text.toString()
            val password = binding.passwordEntry.text.toString()
            if (viewModel.entryCheck(fullName, userName, password)) {
                val user = User(fullName = fullName, username = userName, password = password)
                viewModel.setStateEvent(SignupStateEvent.SignUp(user = user))
            } else {
                Toast.makeText(requireContext(), "Entry error", Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.dataState.mapLatest { ds ->
                authViewModel.dataStateChanged(ds)
                ds.data?.let { event ->
                    event.getContentIfNotHandled()?.let { loginViewState ->
                        viewModel.processViewState(loginViewState)
                    }
                }
            }.launchIn(lifecycleScope)

            viewModel.viewState.collectLatest { vs ->
                vs.user?.let {
                    val action =
                        SignUpFragmentDirections.actionSignUpFragmentToLoginFragment()
                    Navigation.findNavController(view).navigate(action)
                }
            }
        }
    }
}