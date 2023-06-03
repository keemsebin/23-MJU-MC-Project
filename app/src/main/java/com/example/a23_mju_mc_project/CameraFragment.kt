package com.example.a23_mju_mc_project

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import WriteFragment
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.a23_mju_mc_project.databinding.FragmentCameraBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {
    private lateinit var binding: FragmentCameraBinding
    private lateinit var outputDirectory: File
    private lateinit var previewView: PreviewView
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private var isPreviewMode = true
    private var photoFilePath: String? = null
    private val CAMERA_PERMISSION_REQUEST_CODE = 123
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCameraBinding.inflate(inflater, container, false)
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.toolbarLayout)
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.navigationbar)
        toolbar.visibility = View.GONE
        bottomNavigationView.visibility = View.GONE

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startCamera()
        } else {
            requestCameraPermission()
        }
        return binding.root
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean("isPreviewMode", isPreviewMode)
        outState.putString("photoFilePath", photoFilePath)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        outputDirectory = getOutputDirectory()
        cameraExecutor = Executors.newSingleThreadExecutor()

        startCamera() //이 함수가 카메라 Preview함수입니다.

        binding.cameraCaptureButton.setOnClickListener {
            if (isPreviewMode) {
                takePhoto() // 사진 촬영 함수입니다. 버튼 누르면 바로 촬영함
            } else {
            }
        }
    }



    private fun takePhoto() { // 웬만하면 이 촬영함수는 이대로 고정시켜주시길 바랍니다 :)
        val imageCapture = imageCapture ?: return
        val photoFile = File(
            outputDirectory,
            newJpgFileName()
        )

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)

                    val bundle = Bundle() // 이거 번들로 옮기는거 진짜 짱짱굿입니다
                    bundle.putString("photoFilePath", savedUri.toString()) // 이미지 데이터 다른 프레그먼트로 옮길때, 꼭 이 savedUri로 옮겨주시길 바랍니다. 그래야 db에 byte_array로 저장되서리..

                    val checkCancelFragment = CheckCancelFragment()
                    checkCancelFragment.arguments = bundle

                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, checkCancelFragment)
                        .commit()
                }

                override fun onError(exc: ImageCaptureException) {
                    Log.d("CameraX-Debug", "Photo capture failed: ${exc.message}", exc)
                }
            }
        )
    }

    private fun startCamera() { //카메라 프리뷰 함수
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    cameraSelector,
                    preview,
                    imageCapture
                )
            } catch (exc: Exception) {
                Log.d("CameraX-Debug", "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(requireContext()))
    }

    private fun newJpgFileName(): String {
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "${filename}.jpg"
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun getOutputDirectory(): File {
        val mediaDir = requireContext().externalMediaDirs.firstOrNull()?.let { file ->
            File(file, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return mediaDir ?: requireContext().filesDir
    }

    private fun showToast(message: String) { //이거 사진 저장하면 저장 됐다고 토스트 띄우는건데 일단 버리지 말아주세용
        lifecycleScope.launch {
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        cameraExecutor.shutdown()
    }

    private fun requestCameraPermission() {  //카메라 권한 요청 팝업창
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.CAMERA
            )
        ) {
            // 권한 요청을 표시
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            // 권한 요청을 표시하지 않고 바로 권한 요청.
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCamera()
            } else {
            }
        }
    }
}
