package com.example.a23_mju_mc_project

import android.graphics.Color
import android.graphics.Paint.Style
import android.graphics.Typeface
import android.os.Bundle
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.a23_mju_mc_project.databinding.ActivityMainBinding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener

import java.util.*
import kotlin.collections.HashSet

class MainActivity : AppCompatActivity() {
    private val fragmentManager = supportFragmentManager
    private val cameraFragment = CameraFragment();
    private val homeFragment = HomeFragment();
    private val myFragment = MyFragment();
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
        val toolbarLayout = binding.toolbarLayout
        val toolbar = toolbarLayout.findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


    }
}
