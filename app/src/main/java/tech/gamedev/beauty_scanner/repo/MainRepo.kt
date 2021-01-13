package tech.gamedev.beauty_scanner.repo

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.gamedev.beauty_scanner.data.models.Post
import tech.gamedev.beauty_scanner.data.models.UserImage
import java.util.*

class MainRepo {

    private val imageCollectionRef = Firebase.firestore.collection("community_images")
    private val userCollectionRef = Firebase.firestore.collection("users")
    private val _userImages = MutableLiveData<ArrayList<UserImage>>()
    private val userImages: LiveData<ArrayList<UserImage>> = _userImages
    private val _uploadProgress = MutableLiveData<Double>()
    val uploadProgress: LiveData<Double> = _uploadProgress
    private lateinit var storageRef: StorageReference
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth


    fun getUserPostsList(): Task<QuerySnapshot> {
        return imageCollectionRef
            .get()
    }

    fun uploadImage(filePath: Uri, rating: Int?) = CoroutineScope(Dispatchers.IO).launch {

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        val ref = storageRef?.child("post_images/" + UUID.randomUUID().toString())
        val uploadTask = ref?.putFile(filePath!!)

        val urlTask =
            uploadTask?.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                return@Continuation ref.downloadUrl
            }).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val downloadUri = task.result

                    val post = Post(
                        downloadUri.toString(),
                        rating!!.toLong()
                    )

                    userCollectionRef.document(auth.currentUser!!.email.toString())
                        .collection("posts").add(post)

                } else {
                    Log.d("UPLOAD", "UPLOAD NOT COMPLETE")
                }
            }.addOnFailureListener {
                Log.d("UPLOAD", it.message.toString())
            }

    }


}