package tech.gamedev.beauty_scanner.ui.fragments.detailfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.RequestManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_post_detail.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.viewmodels.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class PostDetailFragment : Fragment(R.layout.fragment_post_detail) {

    @Inject
    lateinit var glide: RequestManager

    private val args: PostDetailFragmentArgs by navArgs()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        mainViewModel.userPosts.observe(viewLifecycleOwner) {
            //SET POST IMAGE
            glide.load(it[args.position].imageUrl).into(ivDetailImage)
            //SET POST USERNAME
            tvDetailItemUserName.text = it[args.position].user
        }
    }

}