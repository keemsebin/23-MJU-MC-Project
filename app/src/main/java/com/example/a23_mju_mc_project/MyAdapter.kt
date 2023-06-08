//package com.example.a23_mju_mc_project
//
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.a23_mju_mc_project.databinding.FeedItemBinding
//import java.util.Base64
//
//class MyAdapter(private val feedList: List<Feed>, private val clickListener: (Feed) -> Unit) : RecyclerView.Adapter<MyAdapter.FeedViewHolder>() {
//
//    inner class FeedViewHolder(val binding: FeedItemBinding) : RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
//        val binding = FeedItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return FeedViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
//        val currentFeed = feedList[position]
//        with(holder) {
//            binding.feedImage.setImageBitmap(convertStringToBitmap(currentFeed.picture)) // Assuming picture is stored as Base64 string
//            binding.feedImage.setOnClickListener {
//                clickListener(currentFeed)
//            }
//        }
//    }
//
//    override fun getItemCount() = feedList.size
//
//    private fun convertStringToBitmap(imageString: String): Bitmap {
//        val decodedString = android.util.Base64.decode(imageString, Base64.DEFAULT)
//        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
//    }
//}
