package com.example.a23_mju_mc_project

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.a23_mju_mc_project.databinding.FragmentInputBinding

class FragmentInput: Fragment() {
    lateinit var binding: FragmentInputBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentInputBinding.inflate(inflater,container,false)
        binding.startBtn.setOnClickListener{
            Handler().postDelayed({
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
                },1000)
            }
        return binding.root
    }
}