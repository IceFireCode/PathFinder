package nl.ns.pathfinder

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_control_panel.view.*
import nl.ns.pathfinder.Extensions.setShrinkGrowAnimation
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.backgroundResource
import org.jetbrains.anko.sdk21.coroutines.onClick

/**
 * Created by paulvc on 07-01-18.
 */
class ControlPanel @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var activeFieldType = FieldType.DEFAULT
        set(value) {
            activeFieldStateIndicator.backgroundColorResource = value.colorResId
        }

    init {
        View.inflate(context, R.layout.view_control_panel, this)

        controlPanelFindPath.setShrinkGrowAnimation()
        controlClearAll.setShrinkGrowAnimation()
        controlPanelStartField.setShrinkGrowAnimation()
        controlPanelEndField.setShrinkGrowAnimation()
        controlPanelWallField.setShrinkGrowAnimation()

    }

}