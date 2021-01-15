package tech.gamedev.beauty_scanner.ui.fragments.detailfragments

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_ai_scan.*
import kotlinx.coroutines.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.viewmodels.ScanViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import javax.inject.Inject


@AndroidEntryPoint
class AiScanFragment : Fragment(R.layout.fragment_ai_scan) {

    @Inject
    lateinit var glide: RequestManager

    private val scanViewModel: ScanViewModel by activityViewModels()
    private lateinit var storageRef: StorageReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers()
        scanImage()
    }

    private fun subscribeToObservers() {

        scanViewModel.downloadUrl.observe(viewLifecycleOwner) { imageUrl ->
            /*glide.load(imageUrl).into(ivAiScanImage2)*/
        }

        scanViewModel.liveBitmap.observe(viewLifecycleOwner) {
            val uri = getImageUriFromBitmap(requireContext(), it)
            glide.load(uri).into(ivAiScanImage2)
        }

        scanViewModel.isUploadFinished.observe(viewLifecycleOwner) {
            if (it) {
                delayedTransition()
            }
        }
    }

    private fun scanImage() = lifecycleScope.launch {
        scanViewModel.uploadImageToStorage()
    }

    private fun delayedTransition() = CoroutineScope(Dispatchers.IO).launch {
        delay(4000)
        withContext(Dispatchers.Main) {
            findNavController().navigate(R.id.action_aiScanFragment_to_scanResultFragment)
        }
    }

    @Throws(IOException::class)
    fun rotateImage(bitmap: Bitmap): Bitmap? {


        val tempUri: Uri = getImageUri(requireContext(), bitmap)
        val finalFile = File(getRealPathFromURI(tempUri, requireContext()))
        var rotate = 0
        val exif = ExifInterface(finalFile)
        val orientation: Int = exif.getAttributeInt(
            ExifInterface.TAG_ORIENTATION,
            ExifInterface.ORIENTATION_NORMAL
        )
        when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_270 -> rotate = 270
            ExifInterface.ORIENTATION_ROTATE_180 -> rotate = 180
            ExifInterface.ORIENTATION_ROTATE_90 -> rotate = 90
        }
        val matrix = Matrix()
        matrix.postRotate(rotate.toFloat())
        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width,
            bitmap.height, matrix, true
        )
    }

    private fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    private fun getRealPathFromURI(uri: Uri?, context: Context): String? {
        val cursor: Cursor? = context.contentResolver.query(uri!!, null, null, null, null)
        cursor!!.moveToFirst()
        val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
        return cursor.getString(idx)
    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }


}