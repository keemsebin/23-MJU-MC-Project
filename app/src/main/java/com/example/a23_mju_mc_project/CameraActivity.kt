//package com.example.a23_mju_mc_project
//
//import android.content.Intent
//import android.net.Uri
//import android.os.Build
//import android.os.Bundle
//import android.os.PersistableBundle
//import android.util.Log
//import android.view.View
//import android.widget.Toast
//import androidx.annotation.RequiresApi
//import androidx.appcompat.app.AppCompatActivity
//import androidx.camera.core.CameraSelector
//import androidx.camera.core.ImageCapture
//import androidx.camera.core.ImageCaptureException
//import androidx.camera.core.Preview
//import androidx.camera.lifecycle.ProcessCameraProvider
//import androidx.core.content.ContentProviderCompat.requireContext
//import androidx.core.content.ContextCompat
//import androidx.lifecycle.lifecycleScope
//import com.example.a23_mju_mc_project.databinding.ActivityCameraBinding
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.withContext
//import java.io.File
//import java.text.SimpleDateFormat
//import java.util.concurrent.ExecutorService
//import java.util.concurrent.Executors
//
//class CameraActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityCameraBinding
//    private lateinit var outputDirectory: File
//    private lateinit var cameraExecutor: ExecutorService
//    private var imageCapture: ImageCapture? = null
//    private var isPreviewMode = true
//
//    @Override
//    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityCameraBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        outputDirectory = getOutputDirectory()
//        cameraExecutor = Executors.newSingleThreadExecutor()
//
//        binding.cameraCaptureButton.setOnClickListener {
//            if (isPreviewMode) {
//                takePhoto()
//            } else {
//                showToast("Photo Saved")
//                switchWriteActivity()
//            }
//            switchToSaveMode()
//
//        }
//        startCamera()
//    }
//    private fun takePhoto() {
//        val imageCapture = imageCapture ?: return
//
//        val photoFile = File(
//            outputDirectory,
//            newJpgFileName()
//        )
//
//        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()
//
//        imageCapture.takePicture(
//            outputOptions,
//            ContextCompat.getMainExecutor(this),
//            object : ImageCapture.OnImageSavedCallback {
//                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
//                    val savedUri = Uri.fromFile(photoFile)
//                    val msg = "Photo capture succeeded: $savedUri"
//                    showToast(msg)
//                    Log.d("CameraX-Debug", msg)
//                }
//
//                override fun onError(exc: ImageCaptureException) {
//                    Log.d("CameraX-Debug", "Photo capture failed: ${exc.message}", exc)
//                }
//            }
//        )
//    }
//
//        private fun startCamera() {
//            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
//
//            cameraProviderFuture.addListener({
//                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//
//                val preview = Preview.Builder().build().also {
//                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
//                }
//                imageCapture = ImageCapture.Builder().build()
//
//                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
//                try {
//                    cameraProvider.unbindAll()
//                    cameraProvider.bindToLifecycle(
//                        this,
//                        cameraSelector,
//                        preview,
//                        imageCapture
//                    )
//                } catch (exc: Exception) {
//                    Log.d("CameraX-Debug", "Use case binding failed", exc)
//                }
//            }, ContextCompat.getMainExecutor(this))
//        }
//
//        private fun newJpgFileName(): String {
//            val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
//            val filename = sdf.format(System.currentTimeMillis())
//            return "${filename}.jpg"
//        }
//
//        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
//        private fun getOutputDirectory(): File {
//            val mediaDir = this.externalMediaDirs.firstOrNull()?.let { file ->
//                File(file, resources.getString(R.string.app_name)).apply { mkdirs() }
//            }
//            return mediaDir ?: this.filesDir
//        }
//
//        private fun showToast(message: String) {
//            lifecycleScope.launch {
//                withContext(Dispatchers.Main) {
//                    Toast.makeText(this@CameraActivity, message, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//        private fun switchToSaveMode() {
//            binding.cameraCaptureButton.setImageResource(R.drawable.check)
//            binding.cameraRefreshButton.setImageResource(R.drawable.cancel)
//            isPreviewMode = false
//        }
//        private fun switchWriteActivity() {
//            val intent = Intent(this, WriteActivity::class.java)
//            intent.putExtra("Picture", outputDirectory.absolutePath)
//            startActivity(intent)
//        }
//
//        override fun onDestroy() {
//            super.onDestroy()
//            cameraExecutor.shutdown()
//        }
//
//}