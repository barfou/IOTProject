package fr.barfou.iotproject.ui.fragment

import android.app.AlertDialog
import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import androidx.navigation.fragment.navArgs
import fr.barfou.iotproject.R
import fr.barfou.iotproject.ui.activity.MainActivity
import fr.barfou.iotproject.ui.adapter.LightingAdapter
import fr.barfou.iotproject.ui.viewmodel.RoomListViewModel
import kotlinx.android.synthetic.main.fragment_details_salle.*

class RoomDetailsFragment : Fragment() {

    val args: RoomDetailsFragmentArgs by navArgs()
    private lateinit var lightingAdapter: LightingAdapter
    private lateinit var roomListViewModel: RoomListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomListViewModel = ViewModelProvider(this.requireActivity(), RoomListViewModel).get()
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
        holder_details_salle_nom.text = "Salle " + args.room.name
        updatePresence(args.room.presence)
        setupAdapter()
        observeChangesIfRoom202()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        if (args.room.isAlight() and !args.room.presence)
            inflater.inflate(R.menu.menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.turn_off -> {
                roomListViewModel.turnOffTheLight(args.room.firebaseId) { updatedRoom ->
                    lightingAdapter.submitList(updatedRoom.listLighting)
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun observeChangesIfRoom202() {
        if (args.room.firebaseId == "Rm202") {
            roomListViewModel.observePresenceChangesRoom202 { updatedRoom ->
                updatePresence(updatedRoom.presence)
                lightingAdapter.submitList(updatedRoom.listLighting)
            }
        }
    }

    private fun setupAdapter() {
        lightingAdapter = LightingAdapter()
        holder_details_recycler_view.apply {
            adapter = lightingAdapter
            if (itemDecorationCount == 0) addItemDecoration(LightingAdapter.OffsetDecoration())
        }
        lightingAdapter.submitList(args.room.listLighting)
    }

    private fun updatePresence(value: Boolean) {
        when (value) {
            true -> holder_details_salle_presence?.text = "Oui"
            false -> holder_details_salle_presence?.text = "Non"
        }
    }
}