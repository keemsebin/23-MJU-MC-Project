package com.example.a23_mju_mc_project

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnBoardingAdapter (fgManager : FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fgManager, lifecycle) {
    override fun getItemCount(): Int = 2
    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return IntroFragment()
            1 -> return InputFragment()
        }
        return IntroFragment()
    }
}