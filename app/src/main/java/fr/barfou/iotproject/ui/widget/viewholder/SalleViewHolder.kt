package fr.barfou.iotproject.ui.widget.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.barfou.iotproject.R
import fr.barfou.iotproject.data.model.Salle
import fr.barfou.iotproject.ui.utils.invisible
import fr.barfou.iotproject.ui.utils.show
import kotlinx.android.synthetic.main.holder_salle.view.*

/**
 * SAM (Single Abstract Method) to listen a click.
 *
 * This callback contains the view clicked, and the character attached to the view
 */
typealias OnSalleClickListener = (view: View, salle: Salle) -> Unit

class SalleViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(model: Salle, onClick: OnSalleClickListener) {
        itemView.apply {
            this.setOnClickListener { onClick(it, model) }
            this.holder_salle_nom.text = model.nom
            if (model.presence) {
                holder_salle_imgview_presence.show()
                holder_salle_textview_presence.invisible()
            } else {
                holder_salle_imgview_presence.invisible()
                holder_salle_textview_presence.show()
            }
            if (model.isAlight()) {
                holder_salle_imgview_light.setImageResource(R.drawable.light_on)
            } else {
                holder_salle_imgview_light.setImageResource(R.drawable.light_off)
            }
        }
    }

    companion object {
        /**
         * Create a new Instance of [SalleViewHolder]
         */
        fun create(parent: ViewGroup): SalleViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.holder_salle,
                parent,
                false
            )
            return SalleViewHolder(view)
        }
    }
}