package fr.barfou.iotproject.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        val salle = args.room

        holder_details_salle_nom.text = "Salle " + salle.name
        when (salle.presence) {
            true -> holder_details_salle_presence.text = "Oui"
            false -> holder_details_salle_presence.text = "Non"
        }

        lightingAdapter = LightingAdapter()
        holder_details_recycler_view.apply {
            adapter = lightingAdapter
            if (itemDecorationCount == 0) addItemDecoration(LightingAdapter.OffsetDecoration())
        }
        lightingAdapter.submitList(salle.listLighting)
    }
}