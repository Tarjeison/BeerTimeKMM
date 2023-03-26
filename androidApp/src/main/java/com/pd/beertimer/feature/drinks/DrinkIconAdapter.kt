package com.pd.beertimer.feature.drinks

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.pd.beertimer.R
import com.tlapp.beertimemm.models.DrinkIconItem
import com.pd.beertimer.util.getIconFromName

class DrinkIconAdapter(
    private val drinkIconItems: MutableList<DrinkIconItem>
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return DrinkIconViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_drink_icon, parent, false)
        )
    }

    override fun getItemCount(): Int = drinkIconItems.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return (holder as DrinkIconViewHolder).bind(drinkIconItems[position])
    }

    fun getSelectedDrinkName(): String? {
        return drinkIconItems.firstOrNull { it.selected }?.iconString
    }

    inner class DrinkIconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(drinkIconItem: DrinkIconItem) {
            itemView.findViewById<View>(R.id.clDrink).setOnClickListener {
                drinkIconItems.filter { it != drinkIconItem }
                    .forEach { drinkIconItem ->
                        if (drinkIconItem.selected) {
                            drinkIconItem.selected = false
                            notifyItemChanged(drinkIconItems.indexOf(drinkIconItem))
                        }
                    }
                drinkIconItem.selected = true
                itemView.isSelected = true
            }
            itemView.isSelected = drinkIconItem.selected
            itemView.context.getIconFromName(drinkIconItem.iconString)?.let { icon ->
                itemView.findViewById<ImageView>(R.id.ivDrinkIcon).setImageResource(icon)
            }
        }
    }
}