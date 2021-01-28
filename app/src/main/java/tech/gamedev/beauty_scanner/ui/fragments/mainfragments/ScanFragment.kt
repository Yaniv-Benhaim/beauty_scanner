package tech.gamedev.beauty_scanner.ui.fragments.mainfragments


import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_scan.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.viewmodels.ScanViewModel


class ScanFragment : Fragment(R.layout.fragment_scan) {

    private lateinit var auth: FirebaseAuth

    private lateinit var filePath: Uri
    private lateinit var storageRef: StorageReference
    private lateinit var storage: FirebaseStorage
    private var uploadImage: Boolean = false


    private val scanViewModel: ScanViewModel by activityViewModels()
    private lateinit var bitmap: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnAIScan.setOnClickListener {
            scanViewModel.setIfVisible(false)
            chooseImage(false)

        }
        btnCommunityRating.setOnClickListener {
            scanViewModel.setIfVisible(false)
            chooseImage(true)

        }

        subscribeToObservers()


    }



    private fun subscribeToObservers() {
        scanViewModel.isVisible.observe(viewLifecycleOwner) {

        }
    }


    private fun chooseImage(communityRating: Boolean) {
        scanViewModel.setIfVisible(true)
        ImagePicker.with(this)
                .crop(
                        9f,
                        16f
                )                    //Crop image(Optional), Check Customization for more option
                .compress(2048)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                        1080,
                        1920
                )    //Final image resolution will be less than 1080 x 1080(Optional)
            .start()

        uploadImage = communityRating
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            RESULT_OK -> {
                filePath = data?.data!!
                Glide.with(requireContext()).load(filePath).into(ivDisplayImage)
                scanViewModel.setUriForUpload(filePath)
                if (uploadImage) {
                    findNavController().navigate(R.id.action_scanFragment_to_postFragment)
                } else
                    findNavController().navigate(R.id.action_scanFragment_to_aiScanFragment)
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT)
                    .show()
            }
            else -> {
                Toast.makeText(requireContext(), "Cancelled :)", Toast.LENGTH_SHORT).show()

            }
        }
    }



}