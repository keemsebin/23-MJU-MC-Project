package com.example.a23_mju_mc_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.a23_mju_mc_project.Database.MyAppDatabase
import com.example.a23_mju_mc_project.databinding.FragmentMyBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MyFragment : Fragment() {
    private lateinit var binding: FragmentMyBinding
    private lateinit var feedAdapter: FeedAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedAdapter = FeedAdapter(listOf(), object : FeedAdapter.OnFeedClickListener {
            override fun onFeedClick(feed: Feed) {
                // Here, handle the click event. For example, navigate to a new fragment with the clicked feed.
                val feedDetailFragment = FeedDetailFragment()
                feedDetailFragment.arguments = bundleOf("feed_id" to feed.feed_Id)

                parentFragmentManager.beginTransaction()
                    .replace(R.id.container, feedDetailFragment)
                    .addToBackStack(null)
                    .commit()
            }
        })
        binding.recyclerView.adapter = feedAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)

        lifecycleScope.launch(Dispatchers.IO) {
            val db = MyAppDatabase.getDatabase(requireContext())
            val feeds = db.feedDao().getAllFeeds()

            val currentUser = db.userDao().getAllUsers().firstOrNull()
            withContext(Dispatchers.Main) {
                feedAdapter.feedItems = feeds
                feedAdapter.notifyDataSetChanged()

                currentUser?.let { user ->
                    binding.userName.text = user.nickname
                    binding.userTime.text = user.alarm_Time
                }
            }
        }
    }
}
