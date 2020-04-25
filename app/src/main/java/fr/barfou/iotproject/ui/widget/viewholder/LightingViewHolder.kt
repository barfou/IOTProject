package fr.barfou.iotproject.ui.widget.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.barfou.iotproject.R
import fr.barfou.iotproject.data.model.Lighting
import kotlinx.android.synthetic.main.holder_eclairage.view.*

class LightingViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(model: Lighting) {
        itemView.apply {
            this.holder_eclairage_nom.text = model.name
            if (model.turnedOn) {
                holder_eclairage_imgview_light.setImageResource(R.drawable.light_on)
            } else {
                holder_eclairage_imgview_light.setImageResource(R.drawable.light_off)
            }
        }
    }

    companion object {
        /**
         * Create a new Instance of [LightingViewHolder]
         */
        fun create(parent: ViewGroup): LightingViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.holder_eclairage,
                parent,
                false
            )
            return LightingViewHolder(
                view
            )
        }
    }
}