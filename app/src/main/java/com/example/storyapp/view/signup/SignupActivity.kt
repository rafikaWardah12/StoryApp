package com.example.storyapp.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivitySignupBinding
import com.example.storyapp.utils.ResultState
import com.example.storyapp.utils.showToast
import com.example.storyapp.view.login.LoginActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder

@Suppress
class SignupActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignupBinding

    private val signupViewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
        playAnimation()
    }

    private fun setupAction() {
        binding.signup.setOnClickListener {
            with(binding){
                    val name = edRegisterName.text.toString()
                    val email = edRegisterEmail.text.toString()
                    val password = edRegisterPassword.text.toString()
                    signupViewModel.registerUser(name, email, password).observe(this@SignupActivity) {response ->
                        if(response != null){
                            when(response) {
                                ResultState.Loading -> {
                                    progressBar.isVisible = true
                                }
                                is ResultState.Error -> {
                                    progressBar.isVisible = false
                                        showToast(response.error)

                                }
                                is ResultState.Success -> {
                                    progressBar.isVisible = false
//                                    showToast(response.data.toString())
//                                    response.data.message.show
                                    showToast(response.data.toString())

                                    MaterialAlertDialogBuilder(this@SignupActivity).apply {
                                        setTitle("Already Create Account")
                                        setMessage("Your account $email has created. You have to login first.")
                                        setPositiveButton("yes") { _, _ ->
                                            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                                            intent.putExtra(EXTRA_EMAIL, EXTRA_PASSWORD)
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
        binding.login.setOnClickListener{
           val intent = Intent(this@SignupActivity, LoginActivity::class.java)
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

        val linearLayout = ObjectAnimator.ofFloat(binding.linearLayout, View.ALPHA, 1f).setDuration(300)
        val name = ObjectAnimator.ofFloat(binding.txtName, View.ALPHA, 1f).setDuration(300)
        val edRegisterName = ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(300)
        val email = ObjectAnimator.ofFloat(binding.txtEmail, View.ALPHA, 1f).setDuration(300)
        val edRegisterEmail = ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(300)
        val password = ObjectAnimator.ofFloat(binding.txtPassword, View.ALPHA, 1f).setDuration(300)
        val edRegisterPassword = ObjectAnimator.ofFloat(binding.edRegisterPasswordLayout, View.ALPHA, 1f).setDuration(300)
        val button = ObjectAnimator.ofFloat(binding.signup, View.ALPHA, 1f).setDuration(300)
        val haveAccount = ObjectAnimator.ofFloat(binding.txtHaveAccount, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(button)
        }

        AnimatorSet().apply {
            playSequentially(linearLayout,name,edRegisterName,email,edRegisterEmail, password, edRegisterPassword,haveAccount, together)
            start()
        }
    }
    companion object {
        const val EXTRA_EMAIL = "extra email"
        const val EXTRA_PASSWORD = "extra password"
    }

}
