package tech.gamedev.beauty_scanner.ui.fragments


import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_scan.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.data.Constants.IMAGE_REQUEST_CODE
import java.util.*


class ScanFragment : Fragment(R.layout.fragment_scan) {


    lateinit var storage: FirebaseStorage
    lateinit var filePath: Uri
    lateinit var storageRef: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fabChooseImage.setOnClickListener { chooseImage() }
        fabUploadImage.setOnClickListener { uploadImage() }

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
    }

    private fun uploadImage() {
        if (filePath != null) {

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

                    // Progress Listener for loading
                    // percentage on the dialog box
                    val progress = (100.0
                            * taskSnapshot.bytesTransferred
                            / taskSnapshot.totalByteCount)
                    progressDialog.setMessage(
                        "Uploaded "
                                + progress.toInt() + "%"
                    )
                }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.data != null) {

            filePath = data.data!!
            try {
                val bitMap = MediaStore
                    .Images
                    .Media
                    .getBitmap(requireActivity().contentResolver, filePath)

                ivDisplayImage.setImageBitmap(bitMap)

            }catch (e: Exception) {
                Log.d("IMAGE", e.message.toString())
                Toast.makeText(
                    requireContext(),
                    "Something wen't wrong please try again",
                    Toast.LENGTH_SHORT
                ).show()
            }




        }
    }
}