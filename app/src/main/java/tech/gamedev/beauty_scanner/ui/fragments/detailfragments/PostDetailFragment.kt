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
import tech.gamedev.beauty_scanner.databinding.FragmentPostDetailBinding
import tech.gamedev.beauty_scanner.viewmodels.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class PostDetailFragment : Fragment(R.layout.fragment_post_detail) {

    @Inject
    lateinit var glide: RequestManager
    lateinit var binding: FragmentPostDetailBinding

    private val args: PostDetailFragmentArgs by navArgs()
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentPostDetailBinding.bind(view)
        setPostDetails()

    }

    private fun setPostDetails() {
        glide.load(args.imageUrl).into(ivRateImage)
        binding.tvItemUserName.text = args.user

    }


}