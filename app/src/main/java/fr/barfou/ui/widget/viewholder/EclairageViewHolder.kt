package fr.barfou.ui.widget.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.barfou.R
import fr.barfou.data.model.Eclairage
import kotlinx.android.synthetic.main.holder_eclairage.view.*
import kotlinx.android.synthetic.main.holder_salle.view.*

class EclairageViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(model: Eclairage) {
        itemView.apply {
            this.holder_eclairage_nom.text = model.nom
            if (model.allume) {
                holder_eclairage_imgview_light.setImageResource(R.drawable.light_on)
            } else {
                holder_eclairage_imgview_light.setImageResource(R.drawable.light_off)
            }
        }
    }

    companion object {
        /**
         * Create a new Instance of [EclairageViewHolder]
         */
        fun create(parent: ViewGroup): EclairageViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.holder_eclairage,
                parent,
                false
            )
            return EclairageViewHolder(view)
        }
    }
}