package tech.gamedev.beauty_scanner.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import tech.gamedev.beauty_scanner.data.models.UserImage

class MainRepo {

    private val imageCollectionRef = Firebase.firestore.collection("community_images")
    private val _userImages = MutableLiveData<ArrayList<UserImage>>()
    private val userImages: LiveData<ArrayList<UserImage>> = _userImages




    fun getUserPostsList(): Task<QuerySnapshot> {
        return imageCollectionRef
            .get()
    }
}