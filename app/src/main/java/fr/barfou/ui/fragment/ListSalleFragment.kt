package fr.barfou.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import fr.barfou.R
import fr.barfou.data.model.Salle
import fr.barfou.ui.adapter.SalleAdapter
import fr.barfou.ui.widget.viewholder.OnSalleClickListener
import kotlinx.android.synthetic.main.fragment_list_salle.view.*

class ListSalleFragment : Fragment(), OnSalleClickListener {

    private lateinit var salleAdapter: SalleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        salleAdapter = SalleAdapter(this)

        view.salle_list_recycler_view.apply {
            adapter = salleAdapter
            if (itemDecorationCount == 0) addItemDecoration(SalleAdapter.OffsetDecoration())
        }

        var listSalle = mutableListOf<Salle>()
        listSalle.add(Salle(etablissement_id = 1, id = 1, nom = "Living Room", presence = true))
        listSalle.add(Salle(etablissement_id = 1, id = 2, nom = "BedRoom", presence = true))
        listSalle.add(Salle(etablissement_id = 1, id = 3, nom = "Kitchen", presence = true))

        salleAdapter.submitList(listSalle)
    }

    override fun invoke(view: View, salle: Salle) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    companion object {
        //
    }
}