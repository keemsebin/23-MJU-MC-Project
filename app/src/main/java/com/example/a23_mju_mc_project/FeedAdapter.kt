package com.example.a23_mju_mc_project

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.a23_mju_mc_project.databinding.FeedItemBinding

class FeedAdapter(
    var feedItems: List<Feed>
) : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    inner class FeedViewHolder(val binding: FeedItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val binding = FeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val item = feedItems[position]
        Glide.with(holder.binding.feedImage.context).load(item.picture).into(holder.binding.feedImage)
    }

    override fun getItemCount(): Int = feedItems.size
}


