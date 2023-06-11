package com.example.a23_mju_mc_project

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import WriteFragment
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Typeface
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
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.a23_mju_mc_project.databinding.FragmentCameraBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class CameraFragment : Fragment() {
    private lateinit var binding: FragmentCameraBinding
    private lateinit var outputDirectory: File
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private var isPreviewMode = true
    private var photoFilePath: String? = null
    private val CAMERA_PERMISSION_REQUEST_CODE = 123
    private var currentCamera = CameraSelector.DEFAULT_BACK_CAMERA
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

        startCamera()

        binding.cameraCaptureButton.setOnClickListener {
            if (isPreviewMode) {
                takePhoto()
            } else {
            }
        }

        binding.cameraRefreshButton.setOnClickListener {
            val newCamera = if (currentCamera == CameraSelector.DEFAULT_BACK_CAMERA) {
                CameraSelector.DEFAULT_FRONT_CAMERA
            } else {
                CameraSelector.DEFAULT_BACK_CAMERA
            }

            currentCamera = newCamera
            startCamera()
        }
    }



    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        val photoFile = File(outputDirectory, newJpgFileName())

        val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

        imageCapture.takePicture(
            outputOptions,
            ContextCompat.getMainExecutor(requireContext()),
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                    val savedUri = Uri.fromFile(photoFile)

                    val options = BitmapFactory.Options().apply {
                        inMutable = true
                    }
                    val bitmap = BitmapFactory.decodeFile(photoFile.absolutePath, options)

                    val rotatedBitmap = rotate(bitmap, 90f)

                    val backgroundBitmap = Bitmap.createBitmap(
                        rotatedBitmap.width,
                        rotatedBitmap.height,
                        Bitmap.Config.ARGB_8888
                    )
                    val canvas = Canvas(backgroundBitmap)
                    canvas.drawColor(Color.BLACK)

                    val left = (backgroundBitmap.width - rotatedBitmap.width) / 2f
                    val top = (backgroundBitmap.height - rotatedBitmap.height) / 2f
                    canvas.drawBitmap(rotatedBitmap, left, top, null)

                    val customTypeface =
                        context?.let { ResourcesCompat.getFont(it, R.font.pretendard_bold) }
                    val borderColor =
                        context?.let { ContextCompat.getColor(it, R.color.grey2) }
                    val borderWidth = 64f

                    val paint = Paint().apply {
                        color = Color.WHITE
                        textSize = 250f
                        textAlign = Paint.Align.CENTER
                        typeface = customTypeface
                        isAntiAlias = true
                        if (borderColor != null) {
                            setShadowLayer(borderWidth, 0f, 0f, borderColor)
                        }
                    }
                    val currentTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(System.currentTimeMillis())
                    val textX = backgroundBitmap.width / 2f
                    val textY = (backgroundBitmap.height / 2f) - ((paint.descent() + paint.ascent()) / 2f)

                    canvas.drawText(currentTime, textX, textY, paint)

                    val modifiedPhotoFile = File(outputDirectory, newJpgFileName())
                    val modifiedOutputStream = FileOutputStream(modifiedPhotoFile)
                    backgroundBitmap.compress(Bitmap.CompressFormat.JPEG, 100, modifiedOutputStream)
                    modifiedOutputStream.close()

                    val bundle = Bundle()
                    bundle.putString("photoFilePath", modifiedPhotoFile.absolutePath)

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
    private fun rotate(bitmap: Bitmap, degree: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degree)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())


        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
            }

            imageCapture = ImageCapture.Builder().build()

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    viewLifecycleOwner,
                    currentCamera, // Use the currentCamera variable here
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