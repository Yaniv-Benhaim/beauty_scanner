package tech.gamedev.beauty_scanner.ui.fragments.detailfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_ai_scan.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.viewmodels.ScanViewModel


class AiScanFragment : Fragment(R.layout.fragment_ai_scan) {

    private val scanViewModel: ScanViewModel by activityViewModels()
    private lateinit var storageRef: StorageReference

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers()
        scanImage()
    }

    private fun subscribeToObservers() {
        scanViewModel.liveBitmap.observe(viewLifecycleOwner) {
            ivAiScanImage.setImageBitmap(it)
        }
    }

    private fun scanImage() = lifecycleScope.launch {
        delay(8000)
        scanViewModel.uploadImageToStorage()
        withContext(Dispatchers.Main) {
            findNavController().navigate(R.id.action_aiScanFragment_to_scanResultFragment)
        }
    }


}