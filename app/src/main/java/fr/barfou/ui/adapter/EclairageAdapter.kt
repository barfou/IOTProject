package fr.barfou.ui.adapter

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.barfou.data.model.Eclairage
import fr.barfou.ui.utils.dp
import fr.barfou.ui.widget.viewholder.EclairageViewHolder

class EclairageAdapter() : RecyclerView.Adapter<EclairageViewHolder>() {

    private var _data = emptyList<Eclairage>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EclairageViewHolder {
        return EclairageViewHolder.create(parent)
    }

    override fun getItemCount(): Int = _data.count()

    override fun onBindViewHolder(holder: EclairageViewHolder, position: Int) {
        holder.bind(_data[position])
    }

    /**
     * Set new data in the list and refresh it
     */
    fun submitList(data: List<Eclairage>) {
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