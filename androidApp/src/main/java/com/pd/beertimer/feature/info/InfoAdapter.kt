package com.pd.beertimer.feature.info

import android.graphics.Color
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pd.beertimer.R
import com.pd.beertimer.models.InfoDto

class InfoAdapter(private val infoDtos: List<InfoDto>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return InfoViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_info, parent, false)
        )
    }

    override fun getItemCount(): Int = infoDtos.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return (holder as InfoViewHolder).bind(infoDtos[position], position)
    }

    class InfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(infoDto: InfoDto, position: Int) {
            if (position % 2 == 0) {
                setLeftWeightedInfo(infoDto)
            } else {
                setRightWeightedInfo(infoDto)
            }

            itemView.findViewById<TextView>(R.id.tvInfoText).apply {
                movementMethod = LinkMovementMethod.getInstance()
                setLinkTextColor(Color.BLUE)
            }
        }

        private fun setLeftWeightedInfo(infoDto: InfoDto) {
            itemView.findViewById<TextView>(R.id.tvInfoLeft).text = infoDto.title
            itemView.findViewById<ImageView>(R.id.ivInfoRight).setImageDrawable(itemView.context.getDrawable(infoDto.iconId))
            itemView.findViewById<TextView>(R.id.tvInfoText).text = infoDto.infoText

            itemView.findViewById<TextView>(R.id.tvInfoLeft).visibility = View.VISIBLE
            itemView.findViewById<ImageView>(R.id.ivInfoRight).visibility = View.VISIBLE
        }

        private fun setRightWeightedInfo(infoDto: InfoDto) {
            itemView.findViewById<TextView>(R.id.tvInfoRight).text = infoDto.title
            itemView.findViewById<ImageView>(R.id.ivInfoLeft).setImageDrawable(itemView.context.getDrawable(infoDto.iconId))
            itemView.findViewById<TextView>(R.id.tvInfoLeft).text = infoDto.infoText

            itemView.findViewById<TextView>(R.id.tvInfoRight).visibility = View.VISIBLE
            itemView.findViewById<ImageView>(R.id.ivInfoLeft).visibility = View.VISIBLE

        }
    }
}
