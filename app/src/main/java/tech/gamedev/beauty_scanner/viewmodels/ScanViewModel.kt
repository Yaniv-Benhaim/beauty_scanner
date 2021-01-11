package tech.gamedev.beauty_scanner.viewmodels

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScanViewModel : ViewModel() {

    private val _mutableBitmap = MutableLiveData<Bitmap>()
    val liveBitmap: LiveData<Bitmap> = _mutableBitmap

    private val _lastRating = MutableLiveData<Int>()
    val lastRating: LiveData<Int> = _lastRating

    private val _isVisible = MutableLiveData<Boolean>()
    val isVisible: LiveData<Boolean> = _isVisible

    fun setIfVisible(isVisible: Boolean) {
        _isVisible.value = isVisible
    }

    fun calculateRating(bitmap: Bitmap) : Int {
        if(bitmap.sameAs(_mutableBitmap.value)){
            _lastRating.value?.let {
                return lastRating.value!!
            }

        }else{
            _mutableBitmap.value = bitmap
            _lastRating.value = (5..10).random()
            return _lastRating.value!!
        }
        _mutableBitmap.value = bitmap
        _lastRating.value = (5..10).random()
        return _lastRating.value!!
    }

    fun setCurrentBitmap(bitmap: Bitmap) {
        _mutableBitmap.value = bitmap
    }
}