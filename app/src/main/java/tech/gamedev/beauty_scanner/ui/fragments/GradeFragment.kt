package tech.gamedev.beauty_scanner.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_people.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.adapters.GradeAdapter
import tech.gamedev.beauty_scanner.data.models.UserImage


class GradeFragment : Fragment(R.layout.fragment_people) {

    lateinit var gradeAdapter: GradeAdapter
    private val imageCollectionRef = Firebase.firestore.collection("community_images")
    private val userImages = ArrayList<UserImage>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToLiveUpdates()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            setupViewPager()
        }
    }

    private fun setupViewPager() = vvRatePeople.apply {
        gradeAdapter = GradeAdapter(userImages)
        adapter = gradeAdapter

    }

}