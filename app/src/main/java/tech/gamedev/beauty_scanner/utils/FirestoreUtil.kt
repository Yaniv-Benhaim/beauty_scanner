package tech.gamedev.beauty_scanner.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tech.gamedev.beauty_scanner.data.models.User

object FirestoreUtil {

    private val firestoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }


    val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document("users/${FirebaseAuth.getInstance().currentUser!!.email}/user_information/${FirebaseAuth.getInstance().currentUser!!.email}")

    fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapShot ->
            if (!documentSnapShot.exists()) {
                val newUser = User(
                        FirebaseAuth.getInstance().currentUser?.email ?: "",
                        "",
                        null
                )
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            } else {
                onComplete()
            }
        }
    }

    fun updateCurrentUser(
            email: String = "",
            userName: String = "",
            profilePicturePath: String? = null
    ) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (userName.isNotBlank()) userFieldMap["userName"] = userName
        if (profilePicturePath != null) userFieldMap["profilePicturePath"] = profilePicturePath

        currentUserDocRef.update(userFieldMap)
    }


    fun signOut() = CoroutineScope(Dispatchers.IO).launch {
        FirebaseAuth.getInstance().signOut()
    }
}