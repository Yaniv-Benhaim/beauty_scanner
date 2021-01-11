package tech.gamedev.beauty_scanner.ui.fragments


import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_scan.*
import kotlinx.coroutines.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.data.Constants.IMAGE_REQUEST_CODE
import tech.gamedev.beauty_scanner.viewmodels.ScanViewModel
import java.util.*
import kotlin.properties.Delegates


class ScanFragment : Fragment(R.layout.fragment_scan) {


    lateinit var storage: FirebaseStorage
    lateinit var filePath: Uri
    lateinit var storageRef: StorageReference

    private var isAi: Boolean = false
    private val scanViewModel: ScanViewModel by activityViewModels()
    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAIScan.setOnClickListener {
            scanViewModel.setIfVisible(false)
            isAi = true
            chooseImage()

        }
        btnCommunityRating.setOnClickListener {
            scanViewModel.setIfVisible(false)
            isAi = false
            chooseImage()
        }

        subscribeToObservers()


        }

    private fun subscribeToObservers () {
        scanViewModel.isVisible.observe(viewLifecycleOwner) {
            if(it)(
                showButtons()
            )else{
                hideButtons()
            }
        }
    }

    private fun rateImage(bitmap: Bitmap)  {
        val rating = scanViewModel.calculateRating(bitmap)

            tvRating.text = rating.toString()

    }


    private fun chooseImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select the image you want to scan"
            ),
            IMAGE_REQUEST_CODE
        )
        scanViewModel.setIfVisible(true)
    }

    private fun uploadImage() = CoroutineScope(Dispatchers.IO).launch {

        // Code for showing progressDialog while uploading
        val progressDialog = ProgressDialog(requireContext())
        progressDialog.setTitle("Uploading...")
        progressDialog.show()

        // Defining the child of storageReference
        val ref: StorageReference = storageRef
            .child(
                "images/"
                        + UUID.randomUUID().toString()
            )

        // adding listeners on upload
        // or failure of image
        ref.putFile(filePath)
            .addOnSuccessListener { // Image uploaded successfully
                // Dismiss dialog
                progressDialog.dismiss()
                Toast
                    .makeText(
                        requireContext(),
                        "Image Uploaded!!",
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
            .addOnFailureListener { e -> // Error, Image not uploaded
                progressDialog.dismiss()
                Toast
                    .makeText(
                        requireContext(),
                        "Failed " + e.message,
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
            .addOnProgressListener { taskSnapshot ->
                val progress = (100.0
                        * taskSnapshot.bytesTransferred
                        / taskSnapshot.totalByteCount)
                progressDialog.setMessage(
                    "Uploaded "
                            + progress.toInt() + "%"
                )
            }
    }

    private fun displayImage(data: Intent) {
        filePath = data.data!!
        try {
            val newBitMap = MediaStore
                .Images
                .Media
                .getBitmap(requireActivity().contentResolver, filePath)

            ivDisplayImage.setImageBitmap(newBitMap)
            bitmap = newBitMap
            if(isAi){
                rateImage(bitmap)
            }
            showButtons()


        }catch (e: Exception) {
            Log.d("IMAGE", e.message.toString())
            Toast.makeText(
                requireContext(),
                "Something wen't wrong please try again",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.data != null) {
            displayImage(data)
        }
    }

    private fun hideButtons() = CoroutineScope(Dispatchers.Main).launch {
        btnAIScan.isVisible = false
        btnCommunityRating.isVisible = false
        lottieScanning.isVisible = true
        tvRating.isVisible = false
        tvPleaseWait.isVisible = true
        tvPleaseWait.isVisible = true

    }

    private fun showButtons() = CoroutineScope(Dispatchers.IO).launch {
        delay(9000L)
        withContext(Dispatchers.Main){
        btnAIScan.isVisible = true
        btnCommunityRating.isVisible = true
        lottieScanning.isVisible = false
        tvRating.isVisible = true
        tvPleaseWait.isVisible = false
        }
    }
}