package fr.barfou.ui.widget.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.barfou.data.model.Salle

/**
 * SAM (Single Abstract Method) to listen a click.
 *
 * This callback contains the view clicked, and the character attached to the view
 */
typealias OnUserClickListener = (view: View, salle: Salle, type: ClickType) -> Unit

enum class ClickType {
    NORMAL,
    LONG
}

class SalleViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    fun bind(model: Salle, onClick: OnUserClickListener) {
        itemView.apply {
            this.setOnClickListener { onClick(it, model, ClickType.NORMAL) }
            this.setOnLongClickListener {
                onClick(it, model, ClickType.LONG)
                true
            }
            this.holder_user_login.text = model.login
            this.holder_user_type.text = model.type
            this.holder_user_site_admin.isChecked = model.site_admin
            Glide.with(this)
                .load(model.avatar_url)
                .into(this.holder_user_avatar)
        }
    }

    companion object {
        /**
         * Create a new Instance of [UserViewHolder]
         */
        fun create(parent: ViewGroup): UserViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(
                R.layout.holder_user,
                parent,
                false
            )
            return UserViewHolder(view)
        }
    }
}