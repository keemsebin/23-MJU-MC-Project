package com.example.a23_mju_mc_project

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.a23_mju_mc_project.databinding.ActivityOnboardBinding

class OnBoardingActivity : AppCompatActivity() {
    lateinit var binding : ActivityOnboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val viewpage = binding.viewPager
        viewpage.adapter = OnBoardingAdapter(supportFragmentManager, lifecycle)
        viewpage.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){

            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                when (p0) {
                    1 -> {
                        binding.firstIndicator.setImageDrawable(getDrawable(R.drawable.shape_circle_grey))
                        binding.secondIndicator.setImageDrawable(getDrawable(R.drawable.shape_empty_circle_grey))
                    }
                    0 -> {
                        binding.firstIndicator.setImageDrawable(getDrawable(R.drawable.shape_empty_circle_grey))
                        binding.secondIndicator.setImageDrawable(getDrawable(R.drawable.shape_circle_grey))
                    }
                }

            }
        })
    }
}