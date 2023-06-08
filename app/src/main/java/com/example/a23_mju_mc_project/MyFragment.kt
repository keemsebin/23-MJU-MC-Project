package com.example.a23_mju_mc_project

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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

        feedAdapter = FeedAdapter(listOf())
        binding.recyclerView.adapter = feedAdapter
        binding.recyclerView.layoutManager = GridLayoutManager(context, 3)

        lifecycleScope.launch(Dispatchers.IO) {
            val db = MyAppDatabase.getDatabase(requireContext())
            val feeds = db.feedDao().getAllFeeds()

            withContext(Dispatchers.Main) {
                feedAdapter.feedItems = feeds
                feedAdapter.notifyDataSetChanged()
            }
        }
    }
}
