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
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.item_user_image.view.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.data.models.UserImage


class PostAdapter(options: FirestorePagingOptions<UserImage>) :
    FirestorePagingAdapter<UserImage, PostAdapter.ItemHolder>(options) {


    var listener: PostClickedListener? = null
    var listenerLiked: PostLikedListener? = null
    var listenerDisliked: PostDislikedListener? = null

    override fun onBindViewHolder(
            holder: ItemHolder,
            position: Int,
            post: UserImage
    ) {

        holder.initialize()

        holder.itemView.apply {
            Glide.with(context).load(post.imageUrl).into(ivRateImage)
            tvItemUserName.text = post.user
            if (position > 0) {
                lottieSwipeUp.isVisible = false
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
        fun initialize() {
            itemView.btnShare.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onPostClicked(getItem(adapterPosition)!!)
                }
            }
            itemView.tvLike.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listenerLiked?.onPostLiked(getItem(adapterPosition)!!)
                }
            }
            itemView.tvDislike.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listenerDisliked?.onPostDisliked(getItem(adapterPosition)!!)
                }
            }
        }

    }

    interface PostClickedListener {
        fun onPostClicked(documentSnapshot: DocumentSnapshot)
    }

    interface PostLikedListener {
        fun onPostLiked(documentSnapshot: DocumentSnapshot)
    }

    interface PostDislikedListener {
        fun onPostDisliked(documentSnapshot: DocumentSnapshot)
    }

    fun setOnPostClickListener(listener: PostClickedListener) {
        this.listener = listener
    }

    fun setOnPostLikedListener(listener: PostLikedListener) {
        this.listenerLiked = listener
    }

    fun setOnPostDislikedListener(listener: PostDislikedListener) {
        this.listenerDisliked = listener
    }
}