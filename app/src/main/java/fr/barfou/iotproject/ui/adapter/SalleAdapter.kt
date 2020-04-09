package fr.barfou.iotproject.ui.adapter

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.barfou.iotproject.data.model.Salle
import fr.barfou.iotproject.ui.utils.dp
import fr.barfou.iotproject.ui.widget.viewholder.OnSalleClickListener
import fr.barfou.iotproject.ui.widget.viewholder.SalleViewHolder

class SalleAdapter(
    private val onSalleClickListener: OnSalleClickListener
) : RecyclerView.Adapter<SalleViewHolder>() {

    private var _data = emptyList<Salle>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SalleViewHolder {
        return SalleViewHolder.create(parent)
    }

    override fun getItemCount(): Int = _data.count()

    override fun onBindViewHolder(holder: SalleViewHolder, position: Int) {
        holder.bind(_data[position], onSalleClickListener)
    }

    /**
     * Set new data in the list and refresh it
     */
    fun submitList(data: List<Salle>) {
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
                    dp(16),
                    dp(4),
                    dp(16),
                    dp(4)
                )
            }

        }
    }
}