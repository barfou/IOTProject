package fr.barfou.iotproject.ui.adapter

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.barfou.iotproject.data.model.Lighting
import fr.barfou.iotproject.ui.utils.dp
import fr.barfou.iotproject.ui.widget.viewholder.LightingViewHolder

class LightingAdapter() : RecyclerView.Adapter<LightingViewHolder>() {

    private var _data = emptyList<Lighting>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LightingViewHolder {
        return LightingViewHolder.create(parent)
    }

    override fun getItemCount(): Int = _data.count()

    override fun onBindViewHolder(holder: LightingViewHolder, position: Int) {
        holder.bind(_data[position])
    }

    /**
     * Set new data in the list and refresh it
     */
    fun submitList(data: List<Lighting>) {
        _data = data
        notifyDataSetChanged()
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
                    dp(0),
                    dp(1),
                    dp(0),
                    dp(1)
                )
            }
        }
    }
}