package com.example.storyapp.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivityLoginBinding
import com.example.storyapp.utils.ResultState
import com.example.storyapp.view.main.MainActivity
import com.example.storyapp.view.signup.SignupActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val email = intent.getStringExtra(SignupActivity.EXTRA_EMAIL)
        val password = intent.getStringExtra(SignupActivity.EXTRA_PASSWORD)

        if (email != null && password != null) {
            //apply tdk bisa dipakai apabila
            binding.apply {
                edLoginEmail.setText(email)
                edLoginPassword.setText(password)
            }
        }

        setUpAction()
        playAnimation()
    }

    private fun setUpAction() {
        binding.login.setOnClickListener {
            with(binding) {
                    val email = edLoginEmail.text.toString()
                    val password = edLoginPassword.text.toString()
                    loginViewModel.loginUser(email, password)
                        .observe(this@LoginActivity) { response ->
                            if (response != null) {
                                when (response) {
                                    ResultState.Loading -> {
                                        progressBar.isVisible = true
                                    }

                                    is ResultState.Error -> {
                                        progressBar.isVisible = false
                                            Toast.makeText(
                                                this@LoginActivity,
                                                "Not Found Account. Register First",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        Log.e("Login Error", response.toString())
                                    }

                                    is ResultState.Success -> {
                                        progressBar.isVisible = false

                                        MaterialAlertDialogBuilder(this@LoginActivity).apply {
                                            setTitle("Found Account")
                                            setMessage("You had login. Enjoy your time.")
                                            setPositiveButton("yes") { _, _ ->
                                                //set the user token in ApiConfig
                                                val intent = Intent(
                                                    applicationContext,
                                                    MainActivity::class.java
                                                )
                                                startActivity(intent)
                                            }
                                            create()
                                            show()
                                        }
                                    }
                                }
                            }
                }
            }
        }
        binding.signup.setOnClickListener {
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    //animation
    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.lock, View.TRANSLATION_X, -10f, 30f).apply {
            duration = 2000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val linearLayout =
            ObjectAnimator.ofFloat(binding.linearLayout, View.ALPHA, 1f).setDuration(300)
        val email = ObjectAnimator.ofFloat(binding.txtEmail, View.ALPHA, 1f).setDuration(300)
        val edLoginEmail =
            ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(300)
        val password = ObjectAnimator.ofFloat(binding.txtPassword, View.ALPHA, 1f).setDuration(300)
        val edLoginPasswordLayout =
            ObjectAnimator.ofFloat(binding.edLoginPasswordLayout, View.ALPHA, 1f).setDuration(300)
        val button = ObjectAnimator.ofFloat(binding.login, View.ALPHA, 1f).setDuration(300)
        val haveAccount =
            ObjectAnimator.ofFloat(binding.txtHaveAccount, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(button)
        }

        AnimatorSet().apply {
            playSequentially(
                linearLayout,
                email,
                edLoginEmail,
                password,
                edLoginPasswordLayout,
                haveAccount,
                together
            )
            start()
        }
    }
}