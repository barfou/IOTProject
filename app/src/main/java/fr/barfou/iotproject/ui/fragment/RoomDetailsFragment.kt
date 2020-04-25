package fr.barfou.iotproject.ui.fragment

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import fr.barfou.iotproject.R
import fr.barfou.iotproject.ui.activity.MainActivity
import fr.barfou.iotproject.ui.adapter.LightingAdapter
import kotlinx.android.synthetic.main.fragment_details_salle.*

class RoomDetailsFragment : Fragment() {

    val args: RoomDetailsFragmentArgs by navArgs()
    private lateinit var lightingAdapter: LightingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_details_salle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.supportActionBar?.apply {
            this.setTitle(R.string.app_name)
            this.setDisplayHomeAsUpEnabled(true)
        }

        val room = args.room

        holder_details_salle_nom.text = "Salle " + room.name
        when (room.presence) {
            true -> holder_details_salle_presence.text = "Oui"
            false -> holder_details_salle_presence.text = "Non"
        }

        lightingAdapter = LightingAdapter()
        holder_details_recycler_view.apply {
            adapter = lightingAdapter
            if (itemDecorationCount == 0) addItemDecoration(LightingAdapter.OffsetDecoration())
        }
        lightingAdapter.submitList(room.listLighting)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (args.room.isAlight() and !args.room.presence)
            inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.turn_off -> {
                //
            }
        }
        return super.onOptionsItemSelected(item)
    }
}