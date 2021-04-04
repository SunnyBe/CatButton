package com.buchi.buttoned.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.buchi.buttoned.R
import com.buchi.buttoned.authentication.model.User
import com.buchi.buttoned.authentication.presentation.AuthActivity
import com.buchi.buttoned.databinding.ActivityMainBinding
import com.buchi.buttoned.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    private val sessionScope = CoroutineScope(Job() + Dispatchers.IO)

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }
    private var loggedUser: User? = null
    private var fGroundJob: Job? = null
    private var bGroundJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainLogoutAction.setIcon(ResourcesCompat.getDrawable(resources, R.drawable.ic_account, null))
        loggedUser = intent.getParcelableExtra(Constants.KeyPairs.LOGGED_IN_USER)
        appendUserDetailToView(loggedUser)

        // Start a foreground scope attached to session scope
        fGroundJob =
            viewModel.sessionProcess(loggedUser, sessionScope, Constants.TIMEOUT_FOREGROUND_MILLI) {
                navigateToAuthScreen()
            }

        binding.mainLogoutAction.setOnClickListener {
            if (loggedUser != null) {
                viewModel.logout(loggedUser!!) {
                    navigateToAuthScreen()
                }
            } else {
                Toast.makeText(this, "No user was found", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun navigateToAuthScreen() {
        val loginIntent = Intent(this@MainActivity, AuthActivity::class.java)
        startActivity(loginIntent)
        finish()
    }

    private fun appendUserDetailToView(user: User?) {
        user?.let {
            binding.mainWelcomeLabel.text = "Welcome ${user.fullName}"
        }
    }

    override fun onPause() {
        super.onPause()
        // Only cancel foreground Job attached to active sessionScope and start a background scope
        if (sessionScope.isActive) fGroundJob?.cancel(CancellationException("Session timeout"))
        bGroundJob =
            viewModel.sessionProcess(loggedUser, sessionScope, Constants.TIMEOUT_BACKGROUND_MILLI) {
                navigateToAuthScreen()
            }
    }

    override fun onResume() {
        super.onResume()
        // Only cancel background Job attached to active sessionScope
        if (sessionScope.isActive) bGroundJob?.cancel(CancellationException("Session timeout"))
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancel all jobs attach to sessionScope
        if (sessionScope.isActive) sessionScope.cancel(CancellationException("Session timeout"))
    }
}