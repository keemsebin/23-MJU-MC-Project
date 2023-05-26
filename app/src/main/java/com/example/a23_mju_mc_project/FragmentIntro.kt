package com.example.a23_mju_mc_project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a23_mju_mc_project.databinding.FragmentIntroBinding

class FragmentIntro : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentIntroBinding.inflate(inflater, container, false).root
    }
}