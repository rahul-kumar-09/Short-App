package com.programmingz.miniclip

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.programmingz.miniclip.databinding.ActivityVideoUploadBinding
import com.programmingz.miniclip.util.UiUtil

class VideoUploadActivity : AppCompatActivity() {
    lateinit var binding: ActivityVideoUploadBinding
    private var selectedVideoUrl: Uri? = null
    lateinit var videoLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videoLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){result->
            if (result.resultCode == RESULT_OK){
                selectedVideoUrl = result.data?.data
                UiUtil.showToast(this,"Got Video" + selectedVideoUrl.toString())
            }
        }

        binding.uploadView.setOnClickListener {
            checkPermissionAndOpenVideoPicker()
        }
    }

    private fun checkPermissionAndOpenVideoPicker(){
        var readExternalVideo: String = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            readExternalVideo = android.Manifest.permission.READ_MEDIA_VIDEO
        } else {
            readExternalVideo = android.Manifest.permission.READ_EXTERNAL_STORAGE
        }
        if (ContextCompat.checkSelfPermission(this, readExternalVideo)== PackageManager.PERMISSION_GRANTED){
            //we have permission
            openVideoPicker()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(readExternalVideo),
                100
            )
        }
    }

    private fun openVideoPicker() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        intent.type = "video/*"
        videoLauncher.launch(intent)
    }
}