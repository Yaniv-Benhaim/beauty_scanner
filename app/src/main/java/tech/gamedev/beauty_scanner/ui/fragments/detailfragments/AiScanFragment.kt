package tech.gamedev.beauty_scanner.ui.fragments.detailfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_ai_scan.*
import kotlinx.coroutines.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.viewmodels.ScanViewModel
import javax.inject.Inject


@AndroidEntryPoint
class AiScanFragment : Fragment(R.layout.fragment_ai_scan) {

    @Inject
    lateinit var glide: RequestManager
    private val scanViewModel: ScanViewModel by activityViewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers()
        delayedTransition()

    }

    private fun subscribeToObservers() {

        scanViewModel.uri.observe(viewLifecycleOwner) {
            glide.load(it).into(ivAiScanImage2)
        }
    }


    private fun delayedTransition() = CoroutineScope(Dispatchers.IO).launch {
        delay(4000)
        withContext(Dispatchers.Main) {
            findNavController().navigate(R.id.action_aiScanFragment_to_scanResultFragment)
        }
    }








}