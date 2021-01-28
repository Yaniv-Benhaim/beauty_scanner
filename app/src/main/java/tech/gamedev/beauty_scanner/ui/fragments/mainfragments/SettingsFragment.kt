package tech.gamedev.beauty_scanner.ui.fragments.mainfragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.RequestManager
import com.github.dhaval2404.imagepicker.ImagePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_settings.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.utils.extensions.signOut
import tech.gamedev.beauty_scanner.viewmodels.LoginViewModel
import tech.gamedev.beauty_scanner.viewmodels.MainViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val loginViewModel: LoginViewModel by activityViewModels()
    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var _filePath: Uri

    @Inject
    lateinit var glide: RequestManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnLogout.setOnClickListener { signOut() }
        cvProfileImage.setOnClickListener { updateProfileImage() }
        subscribeToObservers()

    }

    private fun subscribeToObservers() {
        loginViewModel.user.observe(viewLifecycleOwner) {
            tvUserName.text = it.userName
            glide.load(it.profilePicturePath).into(ivProfileImage)
            ivUpdateProfilePic.isVisible = false
        }
    }

    private fun updateProfileImage() {
        ImagePicker.with(this)
                .crop(
                        1f,
                        1f
                )                    //Crop image(Optional), Check Customization for more option
                .compress(1024)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                        1080,
                        1080
                )    //Final image resolution will be less than 1080 x 1080(Optional)
                .start()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (resultCode) {
            Activity.RESULT_OK -> {
                _filePath = data?.data!!
                glide.load(_filePath).into(ivProfileImage)
                mainViewModel.updateProfileImage(_filePath)

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