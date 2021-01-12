package tech.gamedev.beauty_scanner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user_image.view.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.data.models.UserImage

class GradeAdapter(private val userImages: List<UserImage>) : RecyclerView.Adapter<GradeAdapter.GradeViewHolder>() {

    private val numberRatingAdapter = NumberRatingAdapter()

    class GradeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeViewHolder {
        return GradeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_user_image, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GradeViewHolder, position: Int) {
        val user = userImages[position]
        holder.itemView.apply {
            tvItemUserName.text = user.user
            if (position != 0) {
                lottieSwipeUp.visibility = View.GONE
            }
            Glide.with(this).load(user.imageUrl).into(ivRateImage)
            vvNumberRating.apply {
                adapter = numberRatingAdapter
                clipToPadding = false
                clipChildren = false
                offscreenPageLimit = 3
                currentItem = 6
            }
        }
    }

    override fun getItemCount(): Int {
        return userImages.size
    }
}