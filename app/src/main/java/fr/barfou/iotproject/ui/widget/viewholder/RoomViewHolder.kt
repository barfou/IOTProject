package fr.barfou.iotproject.ui.widget.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.barfou.iotproject.R
import fr.barfou.iotproject.data.model.Room
import fr.barfou.iotproject.ui.utils.invisible
import fr.barfou.iotproject.ui.utils.show
import kotlinx.android.synthetic.main.holder_salle.view.*

/**
 * SAM (Single Abstract Method) to listen a click.
 *
 * This callback contains the view clicked, and the character attached to the view
 */
typealias OnRoomClickListener = (view: View, room: Room) -> Unit

class RoomViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(model: Room, onClick: OnRoomClickListener) {
        itemView.apply {
            this.setOnClickListener { onClick(it, model) }
            this.holder_salle_nom.text = model.name
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
         * Create a new Instance of [RoomViewHolder]
         */
        fun create(parent: ViewGroup): RoomViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.holder_salle,
                parent,
                false
            )
            return RoomViewHolder(view)
        }
    }
}