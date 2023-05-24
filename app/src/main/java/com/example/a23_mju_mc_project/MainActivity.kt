package com.example.a23_mju_mc_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.a23_mju_mc_project.databinding.ActivityMainBinding
import com.example.a23_mju_mc_project.databinding.ActivitySplashBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}