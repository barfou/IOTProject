package fr.barfou.iotproject.ui.adapter

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.barfou.iotproject.data.model.Lighting
import fr.barfou.iotproject.data.model.Room
import fr.barfou.iotproject.ui.utils.dp
import fr.barfou.iotproject.ui.widget.viewholder.OnRoomClickListener
import fr.barfou.iotproject.ui.widget.viewholder.RoomViewHolder

class RoomAdapter(
    private val onSalleClickListener: OnRoomClickListener
) : RecyclerView.Adapter<RoomViewHolder>() {

    private var _data = emptyList<Room>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        return RoomViewHolder.create(parent)
    }

    override fun getItemCount(): Int = _data.count()

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        holder.bind(_data[position], onSalleClickListener)
    }

    /**
     * Set new data in the list and refresh it
     */
    fun submitList(data: List<Room>) {
        _data = data
        notifyDataSetChanged()
    }

    fun update202Presence(presence: Boolean) {

        _data[0].presence = presence
        notifyItemChanged(0)
    }

    fun updateLightsRoom202(listLighting: MutableList<Lighting>) {
        _data[0].listLighting = listLighting
        notifyItemChanged(0)
    }

    /**
     * Define how decorate an item
     */
    class OffsetDecoration : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            super.getItemOffsets(outRect, view, parent, state)
            parent.run {
                outRect.set(
                    dp(16),
                    dp(4),
                    dp(16),
                    dp(4)
                )
            }

        }
    }
}