package com.pd.beertimer.feature.countdown

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import com.pd.beertimer.R

class ImageAdapter(private val imageIds: MutableList<Int>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_image, parent, false)
        )
    }

    override fun getItemCount(): Int = imageIds.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return (holder as ImageViewHolder).bind(imageIds[position])
    }

    fun setData(newImageIds: List<Int>) {
        imageIds.clear()
        imageIds.addAll(newImageIds)
        this.notifyDataSetChanged()
    }

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(imageId: Int) {
            itemView.findViewById<ImageView>(R.id.ivItemDrink).setImageDrawable(AppCompatResources.getDrawable(itemView.context, imageId))
        }
    }
}
