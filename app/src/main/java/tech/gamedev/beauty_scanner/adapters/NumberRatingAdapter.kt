package tech.gamedev.beauty_scanner.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_rating.view.*
import tech.gamedev.beauty_scanner.R

class NumberRatingAdapter : RecyclerView.Adapter<NumberRatingAdapter.NumberViewHolder>() {

    private val numbers = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

    class NumberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NumberViewHolder {
        return NumberViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_rating, parent, false)
        )
    }

    override fun onBindViewHolder(holder: NumberViewHolder, position: Int) {
        holder.itemView.apply {
            tvNumber.text = numbers[position].toString()
        }
    }

    override fun getItemCount(): Int {
        return numbers.size
    }
}


