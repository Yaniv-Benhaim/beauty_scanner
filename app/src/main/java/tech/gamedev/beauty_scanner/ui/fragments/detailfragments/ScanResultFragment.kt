package tech.gamedev.beauty_scanner.ui.fragments.detailfragments

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_scan_result.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.viewmodels.ScanViewModel
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class ScanResultFragment : Fragment(R.layout.fragment_scan_result) {

    @Inject
    lateinit var glide: RequestManager

    private val scanViewModel: ScanViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        calculateRandomRating()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {


        scanViewModel.downloadUrl.observe(viewLifecycleOwner) { imageUrl ->
            /* glide.load(imageUrl).into(ivResult2)*/

        }

        scanViewModel.liveBitmap.observe(viewLifecycleOwner) {
            val uri = getImageUriFromBitmap(requireContext(), it)
            glide.load(uri).into(ivResult2)
        }
    }

    private fun calculateRandomRating() {

        var facialStructureRating: Int = Random.nextInt(5, 10)
        var symmetryRating: Int = Random.nextInt(5, 10)
        var goldenRatioRating: Int = Random.nextInt(5, 10)

        tvBoneStructureRating.text = facialStructureRating.toString()
        tvSymmatryRating.text = symmetryRating.toString()
        tvGoldenRatioRating.text = goldenRatioRating.toString()

        tvTotalAvarageRating.text =
            ((facialStructureRating + symmetryRating + goldenRatioRating) / 3).toString()

    }

    fun getImageUriFromBitmap(context: Context, bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path.toString())
    }

}