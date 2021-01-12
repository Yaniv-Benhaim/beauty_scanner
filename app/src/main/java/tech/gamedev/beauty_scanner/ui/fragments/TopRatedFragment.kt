package tech.gamedev.beauty_scanner.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_top_rated.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.adapters.GridAdapter
import tech.gamedev.beauty_scanner.data.models.UserImage


class TopRatedFragment : Fragment(R.layout.fragment_top_rated) {

    lateinit var gridAdapter: GridAdapter
    private val imageCollectionRef = Firebase.firestore.collection("community_images")
    private val userImages = ArrayList<UserImage>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subscribeToLiveUpdates()
    }


    private fun subscribeToLiveUpdates() {
        imageCollectionRef.addSnapshotListener { snapShot, error ->

            error?.let {
                Log.d("FIREBASE", error.message.toString())
                return@addSnapshotListener
            }

            snapShot?.let {
                for (document in it) {
                    val userImage = document.toObject<UserImage>()
                    userImages.add(userImage)
                }
            }
            setupGridRecycler()
        }
    }

    private fun setupGridRecycler() = rvGridImages.apply {
        gridAdapter = GridAdapter(userImages)
        val gridLayoutManager = GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        layoutManager = gridLayoutManager
        adapter = gridAdapter
    }

}