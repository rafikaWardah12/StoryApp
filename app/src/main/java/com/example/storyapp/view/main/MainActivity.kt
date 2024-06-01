package com.example.storyapp.view.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapp.view.addStory.AddStoryActivity
import com.example.storyapp.R
import com.example.storyapp.ViewModelFactory
import com.example.storyapp.databinding.ActivityMainBinding
import com.example.storyapp.response.ListStoryItem
import com.example.storyapp.utils.ResultState
import com.example.storyapp.view.adapter.ListStoryAdapter
import com.example.storyapp.view.adapter.LoadingStateAdapter
import com.example.storyapp.view.addStory.MapsActivity
import com.example.storyapp.view.welcome.WelcomeActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val mainViewModel by viewModels<MainViewModel> {
        MainViewModelFactory(this)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mainViewModel.getSession().observe(this) { user -> //cek login atau belum
            if(user.isLogin)
            {
                binding.rvList.layoutManager = LinearLayoutManager(this@MainActivity)
                val adapter = ListStoryAdapter()
                binding.rvList.adapter = adapter.withLoadStateFooter(
                    footer = LoadingStateAdapter {
                        adapter.retry()
                    }
                )
                mainViewModel.story.observe(this) {
                    adapter.submitData(lifecycle, it)
                }
            } else {
                val intent = Intent(this, WelcomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        }

        binding.fabAddStory.setOnClickListener{
            val intent = Intent(this, AddStoryActivity::class.java )
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_logout -> {
                mainViewModel.logoutUser()

            }
            R.id.menu_language -> {
                startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            }

            R.id.maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }


//    private fun setStoryUser(listStory: List<ListStoryItem>) {
//        val listStoryAdapter = ListStoryAdapter(listStory)
//        binding.apply {
//            rvList.layoutManager = LinearLayoutManager(this@MainActivity)
//            rvList.addItemDecoration(DividerItemDecoration(this@MainActivity, LinearLayoutManager(this@MainActivity).orientation))
//            rvList.adapter = listStoryAdapter
//        }
//    }
}