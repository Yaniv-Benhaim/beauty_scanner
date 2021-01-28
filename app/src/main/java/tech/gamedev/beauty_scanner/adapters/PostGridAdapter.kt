package tech.gamedev.beauty_scanner.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.firebase.ui.firestore.paging.FirestorePagingAdapter
import com.firebase.ui.firestore.paging.FirestorePagingOptions
import com.firebase.ui.firestore.paging.LoadingState
import com.google.firebase.firestore.DocumentSnapshot
import kotlinx.android.synthetic.main.item_grid_image.view.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.data.models.UserImage


class PostGridAdapter(options: FirestorePagingOptions<UserImage>) :
    FirestorePagingAdapter<UserImage, PostGridAdapter.ItemHolder>(options) {

    var listener: PostClickedListenerGrid? = null

    override fun onBindViewHolder(
            holder: ItemHolder,
            i: Int,
            post: UserImage
    ) {
        holder.initialize()

        holder.itemView.apply {
            Glide.with(context).load(post.imageUrl).into(ivGridImage)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_grid_image, parent, false)
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
            itemView.setOnClickListener {
                if (adapterPosition != RecyclerView.NO_POSITION) {
                    listener?.onPostClicked(getItem(adapterPosition)!!)
                }
            }
        }
    }

    interface PostClickedListenerGrid {
        fun onPostClicked(documentSnapshot: DocumentSnapshot)
    }

    fun setOnPostClickListener(listener: PostClickedListenerGrid) {
        this.listener = listener
    }
}