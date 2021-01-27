package tech.gamedev.beauty_scanner.ui.fragments.loginfragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.other.Constants.AUTH_REQUEST_CODE
import tech.gamedev.beauty_scanner.utils.extensions.createToast
import tech.gamedev.beauty_scanner.viewmodels.LoginViewModel


class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val loginViewModel: LoginViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogin.setOnClickListener { signIn() }
    }

    private fun signIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val signInClient = GoogleSignIn.getClient(requireContext(), gso)

        signInClient.signInIntent.also {
            startActivityForResult(it, AUTH_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == AUTH_REQUEST_CODE) {
            try {
                val account = GoogleSignIn.getSignedInAccountFromIntent(data).result
                account?.let {
                    googleAuthForFirebase(it)
                }
            } catch (e: Exception) {
                Log.d("LOGIN", e.message.toString())
            }
        }
    }

    private fun googleAuthForFirebase(account: GoogleSignInAccount) {
        val credentials = GoogleAuthProvider.getCredential(account.idToken, null)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                auth.signInWithCredential(credentials).await()
                withContext(Dispatchers.Main) {
                    createToast("Logged in successfully")


                    if (loginViewModel.checkIfUserExists(account)) {
                        findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
                    } else {
                        findNavController().navigate(R.id.actionGlobalToGradeFragment)
                    }

                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()

        auth.currentUser?.let {
            loginViewModel.setUserName(auth.currentUser!!)
            findNavController().navigate(R.id.actionGlobalToGradeFragment)
        }

    }
}