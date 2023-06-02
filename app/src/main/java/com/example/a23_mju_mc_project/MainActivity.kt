package com.example.a23_mju_mc_project

import android.content.Intent

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.a23_mju_mc_project.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


import java.util.*
import kotlin.collections.HashSet

class MainActivity : AppCompatActivity() {
    private val fragmentManager = supportFragmentManager
    private val homeFragment = HomeFragment();
    private val myFragment = MyFragment();
    private val cameraFragment = CameraFragment();
    lateinit var binding : ActivityMainBinding
    lateinit var bottomNavigationView: BottomNavigationView

    @Override
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(binding.container.id, homeFragment).commit()
        bottomNavigationView = findViewById(R.id.navigationbar)

        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragmentTransaction = fragmentManager.beginTransaction()
            when(item.itemId) {
                R.id.home -> {
                    fragmentTransaction.replace(R.id.container,homeFragment).commit()
                    true
                }
                R.id.camera -> {
                    fragmentTransaction.replace(R.id.container,cameraFragment).commit()
                    true
                }
                R.id.My -> {
                    fragmentTransaction.replace(R.id.container,myFragment).commit()
                    true
                }
                else -> false
            }
        }
    }
}
