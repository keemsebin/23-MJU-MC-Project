package com.example.a23_mju_mc_project

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.a23_mju_mc_project.databinding.FeedItemBinding

class FeedAdapter(private val feedList: List<Feed>) :
    RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

    inner class FeedViewHolder(val binding: FeedItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
        val binding = FeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FeedViewHolder(binding)
    }

    override fun getItemCount(): Int = feedList.size

    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
        val feed = feedList[position]
        val bitmap = BitmapFactory.decodeByteArray(feed.picture, 0, feed.picture.size)
        holder.binding.feedImage.setImageBitmap(bitmap)
    }
}
