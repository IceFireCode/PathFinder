package nl.ns.pathfinder

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.view_control_panel.*
import kotlinx.android.synthetic.main.view_control_panel.view.*
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.sdk21.coroutines.onClick

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controlPanelFindPath.onClick {
            controlPanel.activeFieldType = FieldType.PATH
            playBoard.activeFieldType = FieldType.PATH
            playBoard.findPath()
        }

        controlClearAll.onClick {
            controlPanel.activeFieldType = FieldType.DEFAULT
            playBoard.activeFieldType = FieldType.DEFAULT
            playBoard.clearBoard()
        }

        controlPanelStartField.onClick {
            controlPanel.activeFieldType = FieldType.START
            playBoard.activeFieldType = FieldType.START
        }

        controlPanelEndField.onClick {
            controlPanel.activeFieldType = FieldType.END
            playBoard.activeFieldType = FieldType.END
        }

        controlPanelWallField.onClick {
            controlPanel.activeFieldType = FieldType.WALL
            playBoard.activeFieldType = FieldType.WALL
        }

    }

}
