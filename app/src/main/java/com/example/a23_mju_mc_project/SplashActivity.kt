package com.example.a23_mju_mc_project

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.a23_mju_mc_project.databinding.ActivitySplashBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val database = MyAppDatabase.getDatabase(this)
        //만약 유저 데이터가 존재하면 바로 메인액티비티로 넘어감
        GlobalScope.launch(Dispatchers.IO) {
            val users = database.userDao().getAllUsers()
            if (users.isNotEmpty()) {
                delay(2000)
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            } else {
                Handler(Looper.getMainLooper()).postDelayed({
                    startActivity(Intent(this@SplashActivity, OnBoardingActivity::class.java))
                    finish()
                }, 2000)
            }
        }
    }
}