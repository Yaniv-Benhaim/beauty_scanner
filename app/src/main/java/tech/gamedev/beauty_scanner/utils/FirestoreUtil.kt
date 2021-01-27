package tech.gamedev.beauty_scanner.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import tech.gamedev.beauty_scanner.data.models.User

object FirestoreUtil {

    private val firestoreInstance: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private val currentUserDocRef: DocumentReference
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

    fun getCurrentUser(onComplete: (User) -> Unit) {
        currentUserDocRef.get()
            .addOnSuccessListener {
                it.toObject<User>()?.let { it1 -> onComplete(it1) }
            }
    }
}