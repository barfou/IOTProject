package fr.barfou.ui.widget.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.barfou.R
import fr.barfou.data.model.Salle
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
            this.holder_salle_imgview.setImageResource(R.drawable.ic_arrow_back)
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