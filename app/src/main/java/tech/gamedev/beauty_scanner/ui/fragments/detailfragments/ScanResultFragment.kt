package tech.gamedev.beauty_scanner.ui.fragments.detailfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_scan_result.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.utils.extensions.calculateAvarageRating
import tech.gamedev.beauty_scanner.utils.extensions.getRating
import tech.gamedev.beauty_scanner.viewmodels.ScanViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ScanResultFragment : Fragment(R.layout.fragment_scan_result) {

    @Inject
    lateinit var glide: RequestManager

    private val scanViewModel: ScanViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        calculateRandomRating()
        subscribeToObservers()
        fabBtnBack.setOnClickListener {
            findNavController().navigate(R.id.actionGlobalToGradeFragment)
        }
    }

    private fun subscribeToObservers() {
        scanViewModel.uri.observe(viewLifecycleOwner) {
            glide.load(it).into(ivResult2)
        }
    }

    private fun calculateRandomRating() {

        val facialStructureRating = getRating()
        val symmetryRating: Int = getRating()
        val goldenRatioRating: Int = getRating()


        tvBoneStructureRating.text = facialStructureRating.toString()
        tvSymmatryRating.text = symmetryRating.toString()
        tvGoldenRatioRating.text = goldenRatioRating.toString()

        tvTotalAvarageRating.text = calculateAvarageRating(
            tvBoneStructureRating.text.toString().toInt(),
            tvSymmatryRating.text.toString().toInt(),
            tvGoldenRatioRating.text.toString().toInt()
        )
            .toString()

        scanViewModel.setAverageRating(tvTotalAvarageRating.text.toString().toInt())
        scanViewModel.uploadImageToStorage()

    }


}