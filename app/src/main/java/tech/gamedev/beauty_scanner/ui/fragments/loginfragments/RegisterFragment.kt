package tech.gamedev.beauty_scanner.ui.fragments.loginfragments

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.data.models.User
import tech.gamedev.beauty_scanner.other.Constants.USER_COLLECTION


class RegisterFragment : Fragment(R.layout.fragment_register) {


    lateinit var auth: FirebaseAuth
    lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnCreateUsername.setOnClickListener {
            if (etCreateUsername.text.isNotEmpty()) {
                createNewUser()
            } else {
                Toast.makeText(requireContext(), "Please enter a username", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun createNewUser() = CoroutineScope(Dispatchers.IO).launch {
        val querySnapshot = firestore.collection(USER_COLLECTION)
            .whereEqualTo("userName", etCreateUsername.text.toString())
            .get()
            .await()
        if (querySnapshot.documents.isNullOrEmpty()) {
            val user = User(
                auth.currentUser!!.email.toString(),
                etCreateUsername.text.toString()
            )
            firestore.collection(USER_COLLECTION).add(user).await()
            withContext(Dispatchers.Main) {
                Toast.makeText(
                        requireContext(),
                        "Saved username successfully",
                        Toast.LENGTH_LONG
                ).show()
                findNavController().navigate(R.id.actionGlobalToGradeFragment)
            }


        } else {
            withContext(Dispatchers.Main) {
                Toast.makeText(
                    requireContext(),
                    "Sorry that username exists already, please try a different one",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}