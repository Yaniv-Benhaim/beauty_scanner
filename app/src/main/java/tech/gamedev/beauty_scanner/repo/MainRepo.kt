package tech.gamedev.beauty_scanner.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import tech.gamedev.beauty_scanner.data.models.UserImage

class MainRepo {

    private val imageCollectionRef = Firebase.firestore.collection("community_images")
    private val _userImages = MutableLiveData<ArrayList<UserImage>>()
    private val userImages: LiveData<ArrayList<UserImage>> = _userImages


    fun getUserPosts(): LiveData<ArrayList<UserImage>> {
        imageCollectionRef.addSnapshotListener { snapShot, error ->

            error?.let {
                Log.d("FIREBASE", error.message.toString())
                return@addSnapshotListener
            }

            snapShot?.let {
                for (document in it) {
                    val userImage = document.toObject<UserImage>()
                    _userImages.value?.add(userImage)
                }
            }

        }
        return userImages
    }

    fun getUserPostsList(): Task<QuerySnapshot> {
        return imageCollectionRef
            .get()
    }
}