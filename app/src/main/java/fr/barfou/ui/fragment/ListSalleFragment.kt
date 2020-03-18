package fr.barfou.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import fr.barfou.R
import fr.barfou.data.model.Salle
import fr.barfou.ui.adapter.SalleAdapter
import fr.barfou.ui.utils.hide
import fr.barfou.ui.viewmodel.ListSalleViewModel
import fr.barfou.ui.widget.viewholder.OnSalleClickListener
import kotlinx.android.synthetic.main.fragment_list_salle.*
import kotlinx.android.synthetic.main.fragment_list_salle.view.*

class ListSalleFragment : Fragment(), OnSalleClickListener {

    private lateinit var salleViewModel: ListSalleViewModel
    private lateinit var salleAdapter: SalleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        salleViewModel = ViewModelProvider(this, ListSalleViewModel).get()
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

        salleViewModel.getAllSalles(1) {
            salleAdapter.submitList(it)
            salle_list_progress_bar.hide()
        }
    }

    override fun invoke(view: View, salle: Salle) {
        //
    }

    companion object {
        //
    }
}