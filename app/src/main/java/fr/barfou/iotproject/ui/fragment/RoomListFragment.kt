package fr.barfou.iotproject.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import fr.barfou.iotproject.R
import fr.barfou.iotproject.data.model.Room
import fr.barfou.iotproject.ui.activity.MainActivity
import fr.barfou.iotproject.ui.adapter.RoomAdapter
import fr.barfou.iotproject.ui.utils.hide
import fr.barfou.iotproject.ui.viewmodel.RoomListViewModel
import fr.barfou.iotproject.ui.widget.viewholder.OnRoomClickListener
import kotlinx.android.synthetic.main.fragment_list_salle.*
import kotlinx.android.synthetic.main.fragment_list_salle.view.*

class RoomListFragment : Fragment(),
    OnRoomClickListener {

    private lateinit var roomListViewModel: RoomListViewModel
    private lateinit var roomAdapter: RoomAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        roomListViewModel = ViewModelProvider(this,
            RoomListViewModel
        ).get()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_list_salle, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? MainActivity)?.supportActionBar?.apply {
            this.setTitle(R.string.app_name)
            this.setDisplayHomeAsUpEnabled(false)
        }

        roomAdapter = RoomAdapter(this)

        view.salle_list_recycler_view.apply {
            adapter = roomAdapter
            if (itemDecorationCount == 0) addItemDecoration(RoomAdapter.OffsetDecoration())
        }

        roomListViewModel.retrieveData { roomList ->
            Toast.makeText(requireContext(), "Init Ok", Toast.LENGTH_LONG).show()
            salle_list_progress_bar.hide()
            roomAdapter.submitList(roomList)
        }
    }

    override fun invoke(view: View, room: Room) {
        val direction =
            RoomListFragmentDirections.actionSalleListFragmentToSalleDetailsFragment(
                room
            )
        findNavController().navigate(direction)
    }
}