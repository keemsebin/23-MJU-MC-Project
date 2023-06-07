package com.example.a23_mju_mc_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.a23_mju_mc_project.databinding.FragmentMyBinding

class MyActivity : AppCompatActivity() {

    private lateinit var binding: FragmentMyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentMyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val userDao = MyAppDatabase.getDatabase(this).userDao()
        val feedDao = MyAppDatabase.getDatabase(this).feedDao()

        // Assume the user with nickname "test" exists.
        val user_id = 1
        val user = userDao.getUserById(user_id)
        val feeds = feedDao.getFeedsByNickname(user.nickname)

        binding.nickname.text = user.nickname

        binding.feedRecyclerView.layoutManager = GridLayoutManager(this, 3) // 3 columns grid
        binding.feedRecyclerView.adapter = FeedAdapter(feeds)
    }
}
