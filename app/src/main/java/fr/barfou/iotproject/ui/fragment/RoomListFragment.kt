package fr.barfou.iotproject.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import fr.barfou.iotproject.R
import fr.barfou.iotproject.data.model.Lighting
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
        roomListViewModel = ViewModelProvider(this.requireActivity(),
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
            salle_list_progress_bar.hide()
            roomAdapter.submitList(roomList)

            // Observe Changes Here
            roomListViewModel.observePresenceChangesRoom202 { updatedRoom ->
                roomAdapter.updateRoom202(updatedRoom)
            }
            /*.observeAlightChangesRoom202 { listLighting ->
                roomAdapter.updateLightsRoom202(listLighting)
            }*/
        }

        btnTest.setOnClickListener {

            /*val lightingNames = listOf("Classe", "Tableau")
            val roomsRef = Firebase.database.reference.child("Rooms")
            val updatedRoom = Room("Rm202", "Salle 2.02", false, mutableListOf())
            roomsRef.child("Rm202").setValue(updatedRoom)
            for (name in lightingNames) {
                val lightingRef = roomsRef.child("Rm202").child("listLighting")
                val lightId = lightingRef.push().key!!
                val lighting = Lighting(
                    lightId, "Rm202",
                    name,
                    false
                )
                lightingRef.child(lightId).setValue(lighting)
            }*/

            val presence = false
            val turnedOn = true
            val lightingNames = listOf("Classe", "Tableau")
            val roomsRef = Firebase.database.reference.child("Rooms")
            roomsRef.child("Rm202").child("presence").setValue(presence)
            val updatedLight = Lighting("Classe", "Rm2.02", "Classe", turnedOn)
            roomsRef.child("Rm202").child("listLighting").child(lightingNames[0]).setValue(updatedLight)
            val updatedLight2 = Lighting("Tableau", "Rm2.02", "Tableau", turnedOn)
            roomsRef.child("Rm202").child("listLighting").child(lightingNames[1]).setValue(updatedLight2)
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