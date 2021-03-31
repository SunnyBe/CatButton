package com.buchi.buttoned.authentication.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.buchi.buttoned.databinding.ActivityAuthenticationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthenticationBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: AuthViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launchWhenStarted {
            viewModel.user.collect { user ->

            }

            viewModel.loading.collect { isLoading ->
                if (isLoading) showProgress() else dismissProgress()
            }

            viewModel.error.collect { cause ->
                alertError(cause ?: "")
            }
        }
    }

    private fun showProgress() {

    }

    private fun dismissProgress() {

    }

    private fun alertError(cause: String) {

    }
}