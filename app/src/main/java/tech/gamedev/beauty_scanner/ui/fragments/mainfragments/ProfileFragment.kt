package tech.gamedev.beauty_scanner.ui.fragments.mainfragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.paging.PagedList
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_people.*
import kotlinx.android.synthetic.main.fragment_profile.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.adapters.PostGridAdapter
import tech.gamedev.beauty_scanner.data.models.UserImage


class ProfileFragment : Fragment(R.layout.fragment_profile) {

    lateinit var db: FirebaseFirestore
    lateinit var firestoreAdapter: PostGridAdapter
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFirestoreViewpager()
    }


    private fun setFirestoreViewpager() = rvProfilePosts.apply {
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
        val gridLayoutManager =
            GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        layoutManager = gridLayoutManager
        adapter = firestoreAdapter
    }

}