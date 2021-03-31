package com.buchi.buttoned.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.buchi.buttoned.authentication.model.User
import com.buchi.buttoned.authentication.presentation.AuthActivity
import com.buchi.buttoned.databinding.ActivityMainBinding
import com.buchi.buttoned.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: MainViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = intent.getParcelableExtra<User>(Constants.KeyPairs.LOGGED_IN_USER)
//        val user = User("c825cae8-91f8-11eb-a8b3-0242ac130003", "Thomas Muller", "tMuller", "password123")
        appendUserDetail(user)
        binding.mainLogoutAction.setOnClickListener {
            if (user != null) {
                lifecycleScope.launchWhenStarted {
                    viewModel.logoutUser(user)
                        .onCompletion {
                            val loginIntent = Intent(this@MainActivity, AuthActivity::class.java)
                            startActivity(loginIntent)
                            finish()
                        }.collect()
                }
            } else {
                Toast.makeText(this, "No user was found", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun appendUserDetail(user: User?) {
        user?.let {
            binding.mainWelcomeLabel.text = "Welcome ${user.fullName}"
        }
    }
}