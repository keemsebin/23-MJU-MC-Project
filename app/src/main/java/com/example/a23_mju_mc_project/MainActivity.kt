package com.example.a23_mju_mc_project

import android.content.Intent

import android.os.Bundle
import android.view.View
import android.widget.Toast
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
    private var backPressedTime: Long = 0
    private val BACK_PRESSED_INTERVAL: Long = 2000 // 간격 2초

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


    @Suppress("DEPRECATION")
    //메인화면에서 뒤로가기 버튼 두번 누르면 앱 종료
    override fun onBackPressed() {
        if (backPressedTime + BACK_PRESSED_INTERVAL > System.currentTimeMillis()) {
            super.onBackPressed() // 앱 종료
        } else {
            Toast.makeText(this, "한 번 더 누르면 앱이 종료됩니다.", Toast.LENGTH_SHORT).show()
        }
        backPressedTime = System.currentTimeMillis()
    }
}
