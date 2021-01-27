package tech.gamedev.beauty_scanner.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tech.gamedev.beauty_scanner.repo.MainRepo


class ScanViewModel @ViewModelInject constructor(
    private val mainRepo: MainRepo
) : ViewModel() {

    private val _mutableBitmap = MutableLiveData<Bitmap>()
    val liveBitmap: LiveData<Bitmap> = _mutableBitmap


    private val _uri = MutableLiveData<Uri>()
    val uri: LiveData<Uri> = _uri

    val downloadUrl = mainRepo.downloadUrl
    val isUploadFinished = mainRepo.uploadFinished

    private val _lastRating = MutableLiveData<Int>()
    private val lastRating: LiveData<Int> = _lastRating

    private val _isVisible = MutableLiveData<Boolean>()
    val isVisible: LiveData<Boolean> = _isVisible

    fun setIfVisible(isVisible: Boolean) {
        _isVisible.value = isVisible
    }

    fun calculateRating(bitmap: Bitmap) : Int {


        if (bitmap!!.sameAs(_mutableBitmap.value)) {
            _lastRating.value?.let {
                return lastRating.value!!
            }

        } else {
            _mutableBitmap.value = bitmap
            _lastRating.value = (5..10).random()
            return _lastRating.value!!
        }
        _mutableBitmap.value = bitmap
        _lastRating.value = (5..10).random()
        return _lastRating.value!!
    }

    fun setUriForUpload(uri: Uri) {
        _uri.value = uri

    }

    fun uploadImageToStorage() = mainRepo.uploadImage(_uri.value!!, _lastRating.value!!)

    fun createCommunityPost() = mainRepo.uploadImageAndCreatePost(_uri.value!!)

    fun setAverageRating(averageRating: Int) {
        _lastRating.value = averageRating
    }


}