package tech.gamedev.beauty_scanner.ui.fragments.mainfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_people.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.adapters.PostAdapter
import tech.gamedev.beauty_scanner.data.models.UserImage
import tech.gamedev.beauty_scanner.viewmodels.MainViewModel

@AndroidEntryPoint
class GradeFragment : Fragment(R.layout.fragment_people), PostAdapter.PostClickedListener, PostAdapter.PostLikedListener, PostAdapter.PostDislikedListener {


    private val mainViewModel: MainViewModel by activityViewModels()
    lateinit var db: FirebaseFirestore
    lateinit var firestoreAdapter: PostAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()
        setFirestoreViewpager()
    }





    private fun setFirestoreViewpager() = vvRatePeople.apply {
        val query = db.collection("community_images")
        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(10)
            .setPageSize(4)
            .build()

        val options = FirestorePagingOptions.Builder<UserImage>()
            .setQuery(query, config, UserImage::class.java)
            .setLifecycleOwner(this@GradeFragment).build()

        firestoreAdapter = PostAdapter(options)
        firestoreAdapter.setOnPostClickListener(this@GradeFragment)
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

    override fun onPostLiked(documentSnapshot: DocumentSnapshot) {
        TODO("Not yet implemented")
    }

    override fun onPostDisliked(documentSnapshot: DocumentSnapshot) {
        TODO("Not yet implemented")
    }
}