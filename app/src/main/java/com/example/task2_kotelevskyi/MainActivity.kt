package com.example.task2_kotelevskyi

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

class MainActivity : ComponentActivity() {

    private lateinit var imageView: ImageView
    private lateinit var captureButton: Button
    private lateinit var saveButton: Button
    private var imageBitmap: Bitmap? = null

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(this, "Разрешение на использование камеры отклонено", Toast.LENGTH_SHORT).show()
        }
    }

    private val cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicturePreview()
    ) { bitmap: Bitmap? ->
        if (bitmap != null) {
            imageBitmap = bitmap
            imageView.setImageBitmap(bitmap)
        } else {
            Toast.makeText(this, "Фото не было сделано", Toast.LENGTH_SHORT).show()
        }
    }

    private val createDocumentLauncher = registerForActivityResult(
        ActivityResultContracts.CreateDocument("image/jpeg")
    ) { uri ->
        if (uri != null) {
            try {
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    val success = imageBitmap?.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    if (success == true) {
                        Toast.makeText(this, "Изображение сохранено", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Не удалось сохранить изображение", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Ошибка при сохранении изображения: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(this, "Сохранение отменено", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView2)
        captureButton = findViewById(R.id.pic)
        saveButton = findViewById(R.id.savetogal)

        captureButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                openCamera()
            } else {
                cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }

        saveButton.setOnClickListener {
            if (imageBitmap != null) {
                val fileName = "IMG_${System.currentTimeMillis()}.jpg"
                createDocumentLauncher.launch(fileName)
            } else {
                Toast.makeText(this, "Сначала сделайте фото", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openCamera() {
        cameraLauncher.launch(null)
    }
}
