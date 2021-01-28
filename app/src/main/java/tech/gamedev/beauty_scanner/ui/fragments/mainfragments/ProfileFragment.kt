package tech.gamedev.beauty_scanner.ui.fragments.mainfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_people.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_settings.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.adapters.PostGridAdapter
import tech.gamedev.beauty_scanner.data.models.UserImage
import tech.gamedev.beauty_scanner.databinding.FragmentProfileBinding
import tech.gamedev.beauty_scanner.viewmodels.LoginViewModel
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile), PostGridAdapter.PostClickedListenerGrid {

    lateinit var binding: FragmentProfileBinding
    lateinit var db: FirebaseFirestore
    lateinit var firestoreAdapter: PostGridAdapter
    lateinit var auth: FirebaseAuth

    @Inject
    lateinit var glide: RequestManager
    private val loginViewModel: LoginViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        setFirestoreViewpager()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        loginViewModel.user.observe(viewLifecycleOwner) {
            binding.tvUserName.text = it.userName
            glide.load(it.profilePicturePath).into(binding.ivProfileImage)

        }
    }


    private fun setFirestoreViewpager() = binding.rvProfilePosts.apply {
        val query = db.collection("users")
                .document(auth.currentUser!!.email.toString())
                .collection("posts")

        val config = PagedList.Config.Builder()
                .setInitialLoadSizeHint(10)
                .setPageSize(4)
                .build()

        val options = FirestorePagingOptions.Builder<UserImage>().setQuery(
                query, config,
                UserImage::class.java
        )
                .setLifecycleOwner(this@ProfileFragment).build()

        firestoreAdapter = PostGridAdapter(options)
        firestoreAdapter.setOnPostClickListener(this@ProfileFragment)
        val gridLayoutManager =
                GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        layoutManager = gridLayoutManager

        adapter = firestoreAdapter
    }

    override fun onPostClicked(documentSnapshot: DocumentSnapshot) {
        val userPost = documentSnapshot.toObject<UserImage>()
        val action = GradeFragmentDirections.actionGlobalToPostDetailFragment(
                userPost!!.user,
                userPost.imageUrl,
                userPost.likes,
                userPost.disLikes,
                userPost.uid
        )
        findNavController().navigate(action)
    }
}