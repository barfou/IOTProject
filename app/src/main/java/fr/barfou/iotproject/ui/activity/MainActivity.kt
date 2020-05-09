package fr.barfou.iotproject.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import fr.barfou.iotproject.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initToolBar()
    }

    override fun onNavigateUp(): Boolean {
        return findNavController(R.id.main_fragment_container).navigateUp()
    }

    private fun initToolBar() {
        setSupportActionBar(tool_bar)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow_back)
        tool_bar.setNavigationOnClickListener { onNavigateUp() }
    }
}
