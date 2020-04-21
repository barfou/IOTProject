package fr.barfou.iotproject.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import fr.barfou.iotproject.R
import fr.barfou.iotproject.data.model.Salle
import fr.barfou.iotproject.ui.activity.MainActivity
import fr.barfou.iotproject.ui.adapter.SalleAdapter
import fr.barfou.iotproject.ui.utils.hide
import fr.barfou.iotproject.ui.viewmodel.ListSalleViewModel
import fr.barfou.iotproject.ui.widget.viewholder.OnSalleClickListener
import fr.barfou.iotproject.ui.fragment.ListSalleFragmentDirections
import kotlinx.android.synthetic.main.fragment_list_salle.*
import kotlinx.android.synthetic.main.fragment_list_salle.view.*

class ListSalleFragment : Fragment(),
    OnSalleClickListener {

    private lateinit var salleViewModel: ListSalleViewModel
    private lateinit var salleAdapter: SalleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        salleViewModel = ViewModelProvider(this,
            ListSalleViewModel
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
        val direction =
            ListSalleFragmentDirections.actionSalleListFragmentToSalleDetailsFragment(
                salle
            )
        findNavController().navigate(direction)
    }
}