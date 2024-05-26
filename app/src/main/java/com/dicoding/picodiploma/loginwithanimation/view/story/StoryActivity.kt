package com.dicoding.picodiploma.loginwithanimation.view.story

import StoryAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.loginwithanimation.data.story.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityStoryBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel
import kotlinx.coroutines.launch

class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private lateinit var storyAdapter: StoryAdapter
    private val viewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private val mainViewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        observeToken()
        lifecycleScope.launch {
            try {
                viewModel.getStories()
                setStoryData()
            } catch (e: Exception) {
                Log.e("MainActivity", "Error getting stories: ${e.message}", e)
            } finally {
            }
        }
    }

    private fun observeToken() {
        mainViewModel.token.observe(this) { token ->
            if (!token.isNullOrEmpty()) {
                lifecycleScope.launch {
                    try {
                        viewModel.getStories()
                        setStoryData()
                    } catch (e: Exception) {
                        Log.e("StoryActivity", "Error getting stories: ${e.message}", e)
                    }
                }
            }
        }
    }
    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.getStories()
        }
    }

    private fun setStoryData() {
        if (!::storyAdapter.isInitialized) {
            storyAdapter = StoryAdapter()
            storyAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
                override fun onItemClicked(item: ListStoryItem?) {
                    item.let {
                        val intent = Intent(this@StoryActivity, DetailStoryActivity::class.java)
                        intent.putExtra(DATA_STORY, it)

                        val optionsCompact: ActivityOptionsCompat =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                this@StoryActivity
                            )
                        startActivity(intent, optionsCompact.toBundle())
                    }
                }
            })
        }
        binding.rvStory.apply {
            layoutManager = LinearLayoutManager(this@StoryActivity)
            adapter = storyAdapter
        }
        binding.rvStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )
        viewModel.stories.observe(this) {
            storyAdapter.submitData(lifecycle, it)
        }

    }
    companion object {
        const val DATA_STORY = "data_story"
        const val TOKEN = "token"
    }
}