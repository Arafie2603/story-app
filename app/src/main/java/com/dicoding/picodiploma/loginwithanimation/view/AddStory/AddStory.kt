package com.dicoding.picodiploma.loginwithanimation.view.AddStory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.api.ApiConfig
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserPreference
import com.dicoding.picodiploma.loginwithanimation.data.pref.dataStore
import com.dicoding.picodiploma.loginwithanimation.data.response.FileUploadResponse
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityAddStoryBinding
import com.dicoding.picodiploma.loginwithanimation.view.camera.getImageUri
import com.dicoding.picodiploma.loginwithanimation.view.camera.reduceFileImage
import com.dicoding.picodiploma.loginwithanimation.view.camera.uriToFile
import com.dicoding.picodiploma.loginwithanimation.view.story.StoryActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class AddStory : AppCompatActivity() {
    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var userPreference: UserPreference
    private var currentImageUri: Uri? = null
    private lateinit var apiConfig: ApiConfig
    private var isLocationEnabled: Boolean = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lat: Double? = null
    private var lon: Double? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        apiConfig = ApiConfig
        userPreference = UserPreference.getInstance(this.dataStore)
        binding.apply {
            cameraButton.setOnClickListener { startCamera() }
            galleryButton.setOnClickListener { startGallery() }
            uploadButton.setOnClickListener { uploadImage() }
            switch1.setOnCheckedChangeListener { _, isChecked ->
                isLocationEnabled = isChecked
            }
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        if (isLocationEnabled) {
            if (ContextCompat.checkSelfPermission(
                    this.applicationContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                isLocationEnabled = true
            } else {
                requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener {
                    lat = it.latitude
                    lon = it.longitude
                }
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {isGranted: Boolean ->
            if (isGranted) {
                uploadImage()
            }
        }

    private fun uploadImage() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            val description = binding.editTextArea.text.toString()
            showLoading(true)
            lifecycleScope.launch {
                var token = withContext(Dispatchers.IO) {
                    val getToken = intent.getStringExtra(TOKEN).toString()
                    userPreference.getSession().firstOrNull()?.token
                }
                token?.let {

                    val requestBody = description.toRequestBody("text/plain".toMediaType())
                    val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                    val multipartBody = MultipartBody.Part.createFormData(
                        "photo",
                        imageFile.name,
                        requestImageFile,

                    )
                    try {
                        val apiService = ApiConfig.getStoryApiService(it)
                        val response = apiService.uploadImage(multipartBody, requestBody, lat, lon)
                        Log.e("Testya", "Ini lat = $lat, init lon = $lon")
                        startActivity(Intent(this@AddStory, StoryActivity::class.java))
                        response.message.let {
                            Log.d("Testya", "Res = $it")
                        }
                        finish()
                    } catch (e: HttpException) {
                        val errorBody = e.response()?.errorBody()?.string()
                        val errorResponse = Gson().fromJson(errorBody, FileUploadResponse::class.java)
                        errorResponse.message.let {
                            Log.d("Testya", "err = $it")
                        }
                    } finally {

                        showLoading(false)
                    }
                }
            }

        } ?: showToast(getString(R.string.empty_image_warning))
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri!!)
    }


    private fun startGallery() {
        launchGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) {isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No Media Selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
    companion object {
        const val TOKEN = "token"
    }
}