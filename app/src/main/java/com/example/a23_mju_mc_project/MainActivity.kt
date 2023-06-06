package com.example.a23_mju_mc_project

import WriteFragment
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
    private val writeFragment = WriteFragment();
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

    override fun onBackPressed() { //각 프레그먼트에서 디바이스 뒤로가기 버튼을 눌렀을 시
        // 현재 보여지는 프래그먼트를 가져옵니다.
        val currentFragment = supportFragmentManager.findFragmentById(R.id.container)

        if (currentFragment is CameraFragment) { //메인화면으로
            val toolbar = findViewById<Toolbar>(R.id.toolbarLayout)
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigationbar)
            fragmentManager.beginTransaction().replace(binding.container.id, homeFragment).commit()
            toolbar.visibility = View.VISIBLE
            bottomNavigationView.visibility = View.VISIBLE
        }
        else if (currentFragment is MyFragment) { //메인화면으로
            val toolbar = findViewById<Toolbar>(R.id.toolbarLayout)
            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.navigationbar)
            fragmentManager.beginTransaction().replace(binding.container.id, homeFragment).commit()
            toolbar.visibility = View.VISIBLE
            bottomNavigationView.visibility = View.VISIBLE
        }
        else if (currentFragment is WriteFragment) { //카메라화면으로
            val cameraFragment = CameraFragment()
            val fragmentManager = supportFragmentManager

            fragmentManager.beginTransaction()
                .replace(R.id.container, cameraFragment)
                .commit()
        }
        else if (currentFragment is CheckCancelFragment) { //카메라화면으로
            val cameraFragment = CameraFragment()
            val fragmentManager = supportFragmentManager

            fragmentManager.beginTransaction()
                .replace(R.id.container, cameraFragment)
                .commit()
        }
        else { // 메인화면에서 뒤로가기 두번 누르면 앱 종료
            // 뒤로가기 버튼을 누른 시간과 현재 시간을 비교하여 2초 이내에 다시 누르면 앱 종료
            if (System.currentTimeMillis() - backPressedTime < BACK_PRESSED_INTERVAL) {
                super.onBackPressed() // 기본 동작인 액티비티 종료 호출
            } else {
                backPressedTime = System.currentTimeMillis()
                Toast.makeText(this, "한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
