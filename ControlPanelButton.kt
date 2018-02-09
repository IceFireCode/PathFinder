package nl.ns.pathfinder

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import nl.ns.pathfinder.Extensions.setShrinkGrowAnimation

/**
 * Created by paulvc on 09-02-18.
 */
class ControlPanelButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : TextView(context, attrs, defStyleAttr) {

    var fieldType = FieldType.DEFAULT

    init {
        setShrinkGrowAnimation()
    }
}