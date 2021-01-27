package tech.gamedev.beauty_scanner.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseUser
import tech.gamedev.beauty_scanner.data.models.User
import tech.gamedev.beauty_scanner.repo.MainRepo

class LoginViewModel @ViewModelInject constructor(val repo: MainRepo) : ViewModel() {


    val user: LiveData<User> = repo.user

    fun checkIfUserExists(account: GoogleSignInAccount) = repo.checkIfUserExists(account)

    fun setUserName(account: FirebaseUser) = repo.setUserName(account)


}