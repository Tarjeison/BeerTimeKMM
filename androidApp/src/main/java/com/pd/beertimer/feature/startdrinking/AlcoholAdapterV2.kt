package com.pd.beertimer.feature.startdrinking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.pd.beertimer.R
import com.tlapp.beertimemm.utils.VolumeConverter
import com.tlapp.beertimemm.models.AlcoholUnit
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class AlcoholAdapterV2(
    private val alcoholUnits: MutableList<AlcoholUnit>,
    private val onSelected: (AlcoholUnit) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), KoinComponent {
    private val volumeConverter: VolumeConverter by inject()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return AlcoholViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_drink_v2, parent, false)
        )
    }

    override fun getItemCount(): Int = alcoholUnits.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        return (holder as AlcoholViewHolder).bind(alcoholUnits[position])
    }

    fun setData(newAlcoholUnits: List<AlcoholUnit>) {
        alcoholUnits.clear()
        alcoholUnits.addAll(newAlcoholUnits)
        this.notifyDataSetChanged()
    }

    inner class AlcoholViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(alcoholUnit: AlcoholUnit) {

            setIcon(alcoholUnit)
            itemView.findViewById<TextView>(R.id.tvDrinkName).text = alcoholUnit.name
            itemView.findViewById<RadioButton>(R.id.rbDrinkSelect).isChecked = alcoholUnit.isSelected
            itemView.findViewById<TextView>(R.id.tvPercentAndVolume).text = String.format(
                itemView.context.getString(R.string.startdrinking_percent_volume_drink),
                (alcoholUnit.percentage * 100).toString(),
                volumeConverter.floatLiterToVolumeString(alcoholUnit.volume)
            )
            itemView.findViewById<RadioButton>(R.id.rbDrinkSelect).setOnClickListener {
                onSelected.invoke(alcoholUnit)
            }
            itemView.setOnClickListener {
                onSelected.invoke(alcoholUnit)
            }
        }

        private fun setIcon(alcoholUnit: AlcoholUnit) {
            val iconResId = itemView.resources.getIdentifier(
                alcoholUnit.iconName,
                "drawable",
                itemView.context.packageName
            )

            if (iconResId == 0) {
                itemView.findViewById<ImageView>(R.id.icDrink).setImageDrawable(
                    ContextCompat.getDrawable(itemView.context, R.drawable.ic_beer)
                )
            } else {
                itemView.findViewById<ImageView>(R.id.icDrink).setImageDrawable(
                    ContextCompat.getDrawable(itemView.context, iconResId)
                )
            }
        }
    }
}
