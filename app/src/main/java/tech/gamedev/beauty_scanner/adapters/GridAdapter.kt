package tech.gamedev.beauty_scanner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_grid_image.view.*
import tech.gamedev.beauty_scanner.R
import tech.gamedev.beauty_scanner.data.models.UserImage

class GridAdapter(private val userImages: List<UserImage>, private val listener: OnPostClicked) :
    RecyclerView.Adapter<GridAdapter.GradeViewHolder>() {

    class GradeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun initialize(position: Int, action: OnPostClicked) {
            itemView.setOnClickListener {
                action.onPostClicked(position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GradeViewHolder {
        return GradeViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_grid_image, parent, false))
    }

    override fun onBindViewHolder(holder: GradeViewHolder, position: Int) {
        val user = userImages[position]
        holder.itemView.apply {
            Glide.with(this).load(user.imageUrl).into(ivGridImage)

        }
        holder.initialize(position, listener)
    }

    override fun getItemCount(): Int {
        return userImages.size
    }

    interface OnPostClicked {
        fun onPostClicked(position: Int)
    }
}