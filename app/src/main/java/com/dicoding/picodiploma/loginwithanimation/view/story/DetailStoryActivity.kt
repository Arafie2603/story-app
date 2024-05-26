package com.dicoding.picodiploma.loginwithanimation.view.story

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.picodiploma.loginwithanimation.data.story.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityDetailStoryBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding
    private val viewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val getDataStory = intent.getParcelableExtra<ListStoryItem>(DATA_STORY)
        val idStory = getDataStory?.id.toString()
        viewModel.getDetailStory(idStory)
        Glide.with(this)
            .load(getDataStory?.photoUrl)
            .into(binding.imageView2)
        binding.tvHeader.text = getDataStory?.name
        binding.tvDesc.text = getDataStory?.description
    }

    companion object {
        const val DATA_STORY = "data_story"
    }
}