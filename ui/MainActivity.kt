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

//        controlPanelFindPath.onClick {
//            controlPanel.activeFieldType = FieldType.PATH
//            playBoard.activeFieldType = FieldType.PATH
//            playBoard.findPath()
//        }
//
//        controlClearAll.onClick {
//            controlPanel.activeFieldType = FieldType.DEFAULT
//            playBoard.activeFieldType = FieldType.DEFAULT
//            playBoard.clearBoard()
//        }
//
//        controlPanelStartField.onClick {
//            controlPanel.activeFieldType = FieldType.START
//            playBoard.activeFieldType = FieldType.START
//        }
//
//        controlPanelEndField.onClick {
//            controlPanel.activeFieldType = FieldType.END
//            playBoard.activeFieldType = FieldType.END
//        }
//
//        controlPanelWallField.onClick {
//            controlPanel.activeFieldType = FieldType.WALL
//            playBoard.activeFieldType = FieldType.WALL
//        }

    }

    override fun onDestroy() {
        super.onDestroy()
        controlPanel.removeObserver(playBoard)
    }
}
