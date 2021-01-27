package tech.gamedev.beauty_scanner.ui.fragments.createpost

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.utils.extensions.backToHome
import tech.gamedev.beauty_scanner.utils.extensions.backToHomeAfterPost
import tech.gamedev.beauty_scanner.viewmodels.ScanViewModel
import javax.inject.Inject

@AndroidEntryPoint
class PostFragment : Fragment(R.layout.fragment_post) {


    @Inject
    lateinit var glide: RequestManager
    private val scanViewModel: ScanViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        btnCreatePost.setOnClickListener {
            scanViewModel.createCommunityPost()
            backToHomeAfterPost()
        }
        btnCancelPost.setOnClickListener { backToHome() }
    }

    private fun subscribeToObservers() {
        scanViewModel.uri.observe(viewLifecycleOwner) {
            glide.load(it).into(ivPostImage)
        }
    }

}