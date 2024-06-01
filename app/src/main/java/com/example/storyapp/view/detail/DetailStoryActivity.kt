package com.example.storyapp.view.detail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivityDetailStoryBinding
import com.example.storyapp.utils.ResultState
import com.example.storyapp.view.main.MainActivity

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        detailViewModel.getSession().observe(this) { user ->
            if(user.isLogin) {
                val extraId = intent.getStringExtra(EXTRA_ID)
                Log.d("id", extraId ?: "")
                if (extraId != null) {
                    detailViewModel.getDetailStory(extraId).observe(this) { response ->
                        if (response != null) {
                            when (response) {
                                ResultState.Loading -> {
                                    binding.progressBar.isVisible = true
                                }

                                is ResultState.Error -> {
                                    binding.progressBar.isVisible = true
                                }

                                is ResultState.Success -> {
                                    Log.d("char" , response.data.story?.photoUrl ?: "")
                                    Log.d("name" , response.data.story?.name ?: "")
                                    Log.d("dsc" , response.data.story?.description ?: "")

                                    binding.apply {
                                        progressBar.isVisible = false
                                        Glide.with(this@DetailStoryActivity)
                                            .load(response.data.story?.photoUrl)
                                            .into(imgPhotoRs)
                                        txtTitleRs.text = response.data.story?.name
                                        txtDescriptionRs.text = response.data.story?.description
                                    }
                                }
                            }
                        }
                    }
                }
            } else{
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }
    }
    companion object {
        const val EXTRA_ID = "extraId"
    }
}
