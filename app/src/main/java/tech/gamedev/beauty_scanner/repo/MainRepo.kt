package tech.gamedev.beauty_scanner.repo

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import tech.gamedev.beauty_scanner.data.models.Post
import tech.gamedev.beauty_scanner.data.models.User
import tech.gamedev.beauty_scanner.data.models.UserImage
import tech.gamedev.beauty_scanner.other.Constants.USER_COLLECTION
import tech.gamedev.beauty_scanner.utils.FirestoreUtil
import java.util.*

class MainRepo {

    private val imageCollectionRef = Firebase.firestore.collection("community_images")
    private val userCollectionRef = Firebase.firestore.collection(USER_COLLECTION)
    private val _downloadUrl = MutableLiveData<String>()
    val downloadUrl: LiveData<String> = _downloadUrl
    private val _uploadFinished = MutableLiveData<Boolean>()
    val uploadFinished = _uploadFinished
    private val _userImages = MutableLiveData<ArrayList<UserImage>>()
    private val userImages: LiveData<ArrayList<UserImage>> = _userImages
    private val _uploadProgress = MutableLiveData<Double>()
    val uploadProgress: LiveData<Double> = _uploadProgress
    private lateinit var storageRef: StorageReference
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> = _user


    fun getUserPostsList(): Task<QuerySnapshot> {
        return imageCollectionRef
            .get()
    }

    fun setUserName(account: FirebaseUser) {
        val users = ArrayList<User>()
        CoroutineScope(Dispatchers.IO).launch {
            val querySnapshot = userCollectionRef
                .document(account.email.toString())
                .collection("user_information")
                .whereEqualTo("email", account.email)
                .get()
                .await()



            for (document in querySnapshot.documents) {
                val user = document.toObject<User>()
                users.add(user!!)
                _user.postValue(user)
            }

        }

    }


    fun checkIfUserExists(account: GoogleSignInAccount): Boolean {
        val users = ArrayList<User>()
        CoroutineScope(Dispatchers.IO).launch {
            val querySnapshot = userCollectionRef
                .document(account.email.toString())
                .collection("user_information")
                .whereEqualTo("email", account.email)
                .get()
                .await()



            for (document in querySnapshot.documents) {
                val user = document.toObject<User>()
                users.add(user!!)
            }

        }
        _user.value = users[0]
        return users.isNotEmpty()
    }

    fun getCurrentUser(): User? {
        CoroutineScope(Dispatchers.IO).launch {
            FirestoreUtil.currentUserDocRef.get()
                    .addOnSuccessListener {
                        it.toObject<User>()?.let { userObject ->
                            _user.postValue(userObject)
                            Log.d("USER", user.value.toString())
                        }

                    }.addOnFailureListener {
                        _user.postValue(null)
                    }
        }
        Log.d("USER", user.value.toString())
        return user.value

    }


    fun uploadImage(filePath: Uri, rating: Int) = CoroutineScope(Dispatchers.IO).launch {

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        val ref = storageRef?.child("post_images/" + UUID.randomUUID().toString())
        val uploadTask = ref?.putFile(filePath)

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
                    _downloadUrl.value = downloadUri.toString()
                    _uploadFinished.value = true


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

    fun uploadImageAndCreatePost(filePath: Uri) = CoroutineScope(Dispatchers.IO).launch {

        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        val ref = storageRef?.child("post_images/" + UUID.randomUUID().toString())
        val uploadTask = ref?.putFile(filePath)

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
                    _downloadUrl.value = downloadUri.toString()
                    _uploadFinished.value = true


                    val uid = auth.currentUser!!.email + UUID.randomUUID()
                    val post = UserImage(
                        user.value!!.userName,
                        downloadUri.toString(),
                        uid = uid
                    )

                    GlobalScope.launch(Dispatchers.IO) {
                        userCollectionRef.document(user.value!!.email)
                            .collection("posts").add(post).await()

                        imageCollectionRef.document(uid).set(post).await()
                        Log.d("UPLOAD", "UPLOAD SUCCESSFUL")

                    }

                } else {
                    Log.d("UPLOAD", "UPLOAD NOT COMPLETE")
                }
            }.addOnFailureListener {
                Log.d("UPLOAD", it.message.toString())
            }
    }

    fun updateProfileImage(pathFile: Uri) = CoroutineScope(Dispatchers.IO).launch {


        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        storageRef = storage.reference

        val ref = storageRef?.child("post_images/" + UUID.randomUUID().toString())
        val uploadTask = ref?.putFile(pathFile)

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
                        _downloadUrl.value = downloadUri.toString()
                        _uploadFinished.value = true

                        GlobalScope.launch(Dispatchers.IO) {

                            FirestoreUtil.currentUserDocRef.update("profilePicturePath", downloadUri.toString()).await()
                            Log.d("UPLOAD", "UPLOAD SUCCESSFUL")
                            getCurrentUser()

                        }

                    } else {
                        Log.d("UPLOAD", "UPLOAD NOT COMPLETE")
                    }
                }.addOnFailureListener {
                    Log.d("UPLOAD", it.message.toString())
                }
    }


}