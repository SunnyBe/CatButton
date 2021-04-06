package com.buchi.buttoned.authentication.presentation

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.buchi.buttoned.R
import com.buchi.buttoned.authentication.model.User
import com.buchi.buttoned.databinding.ActivityAuthenticationBinding
import com.buchi.buttoned.databinding.DialogFailedBinding
import com.buchi.buttoned.databinding.DialogProcessingBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthenticationBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: AuthViewModel by viewModels { viewModelFactory }
    private var processDialog: Dialog? = null

    private val navController: NavController by lazy {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navHostFragment.navController
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthenticationBinding.inflate(layoutInflater)

        lifecycleScope.launch {
            viewModel.user.collectLatest { user ->
                Log.d("AuthActivity", "Init user request ${user?.username}")
                setStartDestination(user)
                setContentView(binding.root)
            }
        }

        viewModel.loading.mapLatest { isLoading ->
            Log.d(javaClass.simpleName, "Loading status: $isLoading")
            if (isLoading) showProgress() else dismissProgress()
        }.launchIn(lifecycleScope)

        viewModel.error.mapLatest { cause ->
            Log.d(javaClass.simpleName, "Error status: $cause")
            cause?.let {
                alertError(cause ?: "")
            }
        }.launchIn(lifecycleScope)

        processDialog = processDialog()
    }

    // Set start destination based on activation status
    private fun setStartDestination(user: User?) {
        val graphInflater = navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.auth_nav)
        val destination = if (user == null) R.id.signUpFragment else R.id.loginFragment
        navGraph.startDestination = destination
        navController.graph = navGraph
    }

    private fun processDialog(): Dialog {
        val dialogBinding = DialogProcessingBinding.inflate(layoutInflater)
        return MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
            .create()
    }

    private fun showProgress() {
        processDialog?.show()
    }

    private fun dismissProgress() {
        processDialog?.dismiss()
    }

    private fun alertError(cause: String) {
        val dialogBinding = DialogFailedBinding.inflate(layoutInflater)
        val dialogBuilder = MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .setCancelable(false)
        val dialog = dialogBuilder.show()
        dialogBinding.failedMessage.text = cause
        dialogBinding.okAction.setOnClickListener {
            dialog.dismiss()
        }
    }
}