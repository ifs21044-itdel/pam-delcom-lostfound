package com.ifs21044.lostfoundapp.presentation.profile

import android.app.ProgressDialog.show
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.ifs21044.lostfoundapp.R
import com.ifs21044.lostfoundapp.data.remote.MyResult
import com.ifs21044.lostfoundapp.databinding.ActivityProfileManageBinding
import com.ifs21044.lostfoundapp.helper.Utils.Companion.observeOnce
import com.ifs21044.lostfoundapp.helper.getImageUri
import com.ifs21044.lostfoundapp.presentation.ViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProfileManageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileManageBinding
    private var currentImageUri: Uri? = null
    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory.getInstance(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileManageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()
    }

    private fun setupView() {
        showLoading(false)
    }

    private fun setupAction() {
        binding.apply {
            appbarLostandFoundManageProfile.title = "Edit Profile"

            btnLostandFoundProfileSave.setOnClickListener {

                if (currentImageUri != null) {
                    val imageFile = currentImageUri?.let { uri ->
                        contentResolver.openInputStream(uri)?.use { inputStream ->
                            val imageRequestBody =
                                inputStream.readBytes().toRequestBody("image/*".toMediaType())
                            MultipartBody.Part.createFormData("photo", "photo.jpg", imageRequestBody)
                        }
                    }
                    if (imageFile != null) {
                        editPhoto(imageFile)

                    }
                } else {
                    AlertDialog.Builder(this@ProfileManageActivity).apply {
                        setTitle("Oh No!")
                        setMessage("Pilih gambar terlebih dahulu!")
                        setPositiveButton("Oke") { _, _ -> }
                        create()
                        show()
                    }
                }
            }

            btnLostandFoundProfileCamera.setOnClickListener {
                startCamera()
            }

            btnLostandFoundProfileGallery.setOnClickListener {
                startGallery()
            }
        }
    }

    private fun startGallery() {
        launcherGallery.launch(
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
        )
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val data: Intent? = result.data
            val selectedImageUri: Uri? = data?.data
            selectedImageUri?.let {
                currentImageUri = it
                showImage()
            }
        } else {
            Toast.makeText(
                applicationContext,
                "Tidak ada media yang dipilih!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Glide.with(this@ProfileManageActivity)
                .load(it)
                .placeholder(R.drawable.ic_image_24)
                .into(binding.ivProfileManage)
        }
    }

    private fun editPhoto( cover: MultipartBody.Part) {
        viewModel.editPhoto(cover).observeOnce { result ->
            when (result) {
                is MyResult.Loading -> {
                    showLoading(true)
                }
                is MyResult.Success -> {
                    // Handle success
                    showLoading(false)
                    Toast.makeText(
                        applicationContext,
                        "Berhasil mengubah foto profil",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(Intent(this@ProfileManageActivity, ProfileActivity::class.java))
                    finish()
                }
                is MyResult.Error -> {
                    // Handle error
                    showLoading(false)
                    AlertDialog.Builder(this@ProfileManageActivity).apply {
                        setTitle("Oh No!")
                        setMessage(result.error)
                        setPositiveButton("Oke") { _, _ -> }
                        create()
                        show()
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbManageEditProfile.visibility = if (isLoading) View.VISIBLE else View.GONE
        binding.btnLostandFoundProfileSave.isActivated = !isLoading
        binding.btnLostandFoundProfileSave.text = if (isLoading) "" else "Simpan"
    }


}