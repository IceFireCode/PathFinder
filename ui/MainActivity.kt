package nl.ns.pathfinder.ui

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import nl.ns.pathfinder.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controlPanel.registerObserver(playBoard)

    }

    override fun onDestroy() {
        super.onDestroy()
        controlPanel.removeObserver(playBoard)
    }
}
