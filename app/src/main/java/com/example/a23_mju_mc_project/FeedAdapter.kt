package com.example.a23_mju_mc_project

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a23_mju_mc_project.databinding.FeedItemBinding
import java.lang.System.load

class FeedAdapter(
    var feedItems: List<Feed>,
    private val onFeedClickListener: OnFeedClickListener
) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {
    inner class FeedViewHolder(val binding: FeedItemBinding) : RecyclerView.ViewHolder(binding.root)
    override fun getItemCount(): Int = feedItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val binding = FeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val item = feedItems[position]
        Glide.with(holder.binding.feedImage.context)
            .load(item.picture)
            .into(holder.binding.feedImage)
        holder.binding.feedImage.setOnClickListener {
            onFeedClickListener.onFeedClick(item)
        }

    }
    interface OnFeedClickListener {
        fun onFeedClick(feed: Feed)
    }
}


