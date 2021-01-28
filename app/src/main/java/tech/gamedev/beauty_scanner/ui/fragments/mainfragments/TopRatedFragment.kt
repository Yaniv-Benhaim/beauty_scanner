package tech.gamedev.beauty_scanner.ui.fragments.mainfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.android.synthetic.main.fragment_top_rated.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.adapters.PostGridAdapter
import tech.gamedev.beauty_scanner.data.models.UserImage
import tech.gamedev.beauty_scanner.viewmodels.MainViewModel


class TopRatedFragment : Fragment(R.layout.fragment_top_rated), PostGridAdapter.PostClickedListenerGrid {

    private lateinit var gridAdapter: PostGridAdapter
    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        subscribeToObservers()
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()


    }

    private fun subscribeToObservers() {
        mainViewModel.userPosts.observe(viewLifecycleOwner) {
            setupGridRecycler(it)
            gridAdapter.notifyDataSetChanged()
        }
    }

    private fun setupGridRecycler(topPosts: ArrayList<UserImage>) = rvGridImages.apply {


        val query = db.collection("community_images")

        val config = PagedList.Config.Builder()
            .setInitialLoadSizeHint(17)
            .setPageSize(3)
            .build()

        val options = FirestorePagingOptions.Builder<UserImage>().setQuery(
            query, config,
            UserImage::class.java
        )
            .setLifecycleOwner(this@TopRatedFragment).build()



        gridAdapter = PostGridAdapter(options)
        gridAdapter.setOnPostClickListener(this@TopRatedFragment)
        val gridLayoutManager =
                GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        layoutManager = gridLayoutManager
        adapter = gridAdapter
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