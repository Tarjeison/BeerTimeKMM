package com.pd.beertimer.feature.me

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pd.beertimer.R
import com.pd.beertimer.util.getIconFromName
import com.tlapp.beertimemm.models.MePageItem
import com.tlapp.beertimemm.models.MePageNavigationType
import kotlinx.android.synthetic.main.me_item.view.*

class MeAdapter(
    private val meItems: MutableList<MePageItem>,
    val onClick: (navigationType: MePageNavigationType) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MeViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.me_item, parent, false)
        )
    }

    override fun getItemCount(): Int = meItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return (holder as MeViewHolder).bind(meItems[position])
    }

    inner class MeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(meItem: MePageItem) {
            itemView.itemMe.setOnClickListener { onClick.invoke(meItem.navigation) }
            itemView.context.getIconFromName(meItem.iconName)?.let {
                itemView.ivMe.setImageDrawable(
                    ContextCompat.getDrawable(itemView.context, it)
                )
            }
            itemView.tvMe.text = meItem.title
        }
    }
}
