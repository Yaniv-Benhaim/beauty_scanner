package tech.gamedev.beauty_scanner.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import kotlinx.android.synthetic.main.item_user_image.view.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.data.models.UserImage


class PostAdapter(options: FirestorePagingOptions<UserImage>) :
    FirestorePagingAdapter<UserImage, PostAdapter.ItemHolder>(options) {
    private val numberRatingAdapter = NumberRatingAdapter()

    override fun onBindViewHolder(
        holder: ItemHolder,
        position: Int,
        post: UserImage
    ) {

        holder.itemView.apply {
            Glide.with(context).load(post.imageUrl).into(ivRateImage)
            tvItemUserName.text = post.user
            if (position > 0) {
                lottieSwipeUp.isVisible = false
            }

            vvNumberRating.apply {
                adapter = numberRatingAdapter
                clipToPadding = false
                clipChildren = false
                offscreenPageLimit = 3
                currentItem = 6
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_image, parent, false)
        )
    }

    override fun onLoadingStateChanged(state: LoadingState) {
        super.onLoadingStateChanged(state)
        when (state) {
            LoadingState.LOADED -> Log.d("paging", "AMOUNT OF ITEMS LOADED: $itemCount")
            LoadingState.ERROR -> Log.d("paging", "ERROR LOADING DATA")
            LoadingState.FINISHED -> Log.d("paging", "ALL DATA LOADED")
            LoadingState.LOADING_MORE -> Log.d("paging", "LOADING NEXT PAGE")
            LoadingState.LOADING_INITIAL -> Log.d("paging", "LOADING INITIAL DATA")
        }
    }

    inner class ItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }
}