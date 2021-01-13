package tech.gamedev.beauty_scanner.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.launch
import tech.gamedev.beauty_scanner.data.models.UserImage
import tech.gamedev.beauty_scanner.repo.MainRepo


class MainViewModel @ViewModelInject constructor(
   private val mainRepo: MainRepo
) : ViewModel() {

    private val _userPosts = MutableLiveData<ArrayList<UserImage>>()
    val userPosts: LiveData<ArrayList<UserImage>> = _userPosts


    init {
        getAllUserPosts()
    }

    private fun getAllUserPosts() = viewModelScope.launch {
        _userPosts.value = ArrayList()
        mainRepo.getUserPostsList().addOnCompleteListener {
            if (it.isSuccessful) {
                for (document in it.result!!) {
                    val post = document.toObject<UserImage>()
                    _userPosts.value!!.add(post)
                }
            }
        }
    }
}