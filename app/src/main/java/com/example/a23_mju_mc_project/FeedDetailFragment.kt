package com.example.a23_mju_mc_project

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.a23_mju_mc_project.Database.MyAppDatabase
import com.example.a23_mju_mc_project.databinding.FragmentDetailBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val feedId = arguments?.getInt("feed_id") ?: 0
        val userDao = MyAppDatabase.getDatabase(requireContext()).userDao()
        GlobalScope.launch(Dispatchers.IO) {
            val user = userDao.getUserById(1)
            binding.userName.text = user.nickname
            binding.userTime.text = user.alarm_Time
        }
        // Initiate ViewModel and LiveData observers
        viewModel = ViewModelProvider(this).get(FeedDetailViewModel::class.java)
        viewModel.getFeedById(feedId).observe(viewLifecycleOwner, Observer { feed ->
            Glide.with(this)
                .load(feed.picture)
                .into(binding.imageView)
            val originalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd", Locale.getDefault())
            val targetFormat = DateTimeFormatter.ofPattern("MM/dd", Locale.getDefault())
            val originalDate = LocalDate.parse(feed.upload_Date, originalFormat)
            val reformattedDate = originalDate.format(targetFormat)
            viewModel.analyzeSentiment(feed.feed_Text).observe(viewLifecycleOwner, Observer { result ->
                val sentimentDrawable = when(result) {
                    "positive" -> R.drawable.smile
                    "negative" -> R.drawable.sad
                    // 필요에 따라 더 많은 케이스를 추가
                    else -> R.drawable.soso
                }
                binding.sentimentResult.setImageResource(sentimentDrawable)
            })
            binding.deleteBtn.setOnClickListener {
                Toast.makeText(context, "피드가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
                viewModel.deleteFeed(feed)

                val myFragment = MyFragment()
                val fragmentManager = requireActivity().supportFragmentManager

                fragmentManager.beginTransaction()
                    .replace(R.id.container, myFragment)
                    .commit()
            }


            binding.writeDate.text = reformattedDate
            binding.writeTime.text = feed.upload_Time
            binding.Comment.text = feed.feed_Text
            viewModel.analyzeSentiment(feed.feed_Text).observe(viewLifecycleOwner, Observer { result ->
                // Update the sentiment result TextView
            // binding.sentimentResult.setImageDrawable() = result.toString()
            })

        })
    }
}

