package com.example.a23_mju_mc_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.a23_mju_mc_project.databinding.FragmentDetailBinding
import com.example.a23_mju_mc_project.databinding.FragmentFeedDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FeedDetailFragment : Fragment() {
    private lateinit var binding: FragmentDetailBinding
    private lateinit var viewModel: FeedDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val feedId = arguments?.getInt("feed_id") ?: 0

        // Initiate ViewModel and LiveData observers
        viewModel = ViewModelProvider(this).get(FeedDetailViewModel::class.java)
        viewModel.getFeedById(feedId).observe(viewLifecycleOwner, Observer { feed ->
            Glide.with(this)
                .load(feed.picture)
                .into(binding.imageView)
            binding.today.text = feed.upload_Date
            binding.time.text = feed.upload_Time
            binding.Comment.text = feed.feed_Text
        })
    }
}

