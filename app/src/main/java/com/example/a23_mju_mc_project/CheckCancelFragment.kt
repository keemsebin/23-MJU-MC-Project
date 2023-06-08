package com.example.a23_mju_mc_project

import WriteFragment
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.a23_mju_mc_project.databinding.FragmentCheckcancelBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.Executors

class CheckCancelFragment: Fragment() {
    private lateinit var savedUri: String
    private lateinit var binding: FragmentCheckcancelBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCheckcancelBinding.inflate(inflater, container, false)
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbarLayout)
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.navigationbar)
        toolbar.visibility = View.GONE
        bottomNavigationView.visibility = View.GONE

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedUri = arguments?.getString("photoFilePath").orEmpty()

        requireActivity().runOnUiThread {
            if (savedUri.isNotEmpty()) {
                val photoUri = Uri.parse(savedUri)
                binding.imageView.setImageURI(photoUri)
            }
        }

        //체크 버튼 누르면 write 프레그먼트로 이동
        binding.checkButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("photoFilePath", savedUri)

            val writeFragment = WriteFragment()
            writeFragment.arguments = bundle

            parentFragmentManager.beginTransaction()
                .replace(R.id.container, writeFragment)
                .commit()
        }

        //취소 버튼 누르면 카메라 프레그먼트로 돌아감
        binding.cancelButton.setOnClickListener {
            val cameraFragment = CameraFragment()
            val fragmentManager = requireActivity().supportFragmentManager

            fragmentManager.beginTransaction()
                .replace(R.id.container, cameraFragment)
                .commit()
        }
    }
}