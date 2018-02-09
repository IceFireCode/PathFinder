package nl.ns.pathfinder

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.view_control_panel.view.*
import nl.ns.pathfinder.Extensions.setShrinkGrowAnimation
import org.jetbrains.anko.backgroundColorResource
import org.jetbrains.anko.sdk21.coroutines.onClick

/**
 * Created by paulvc on 07-01-18.
 */
class ControlPanel @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), Observable {
    private val TAG = ControlPanel::class.java.simpleName

    private val observers = mutableSetOf<ControlPanelObserver>()

    var activeFieldType = FieldType.DEFAULT
        set(value) {
            activeFieldStateIndicator.backgroundColorResource = value.colorResId
            field = value
        }

    init {
        View.inflate(context, R.layout.view_control_panel, this)

        controlPanelFindPath.fieldType = FieldType.PATH
        controlPanelFindPath.onClick {
            onClickButton(it as ControlPanelButton)
        }

        controlClearAll.setShrinkGrowAnimation()
        controlPanelStartField.setShrinkGrowAnimation()
        controlPanelEndField.setShrinkGrowAnimation()
        controlPanelWallField.setShrinkGrowAnimation()
    }

    private fun onClickButton(button: ControlPanelButton) {
        activeFieldType = button.fieldType
        notifyObservers(ControlPanelState(activeFieldType))
    }

    override fun registerObserver(observer: ControlPanelObserver) {
        observers.add(observer)
    }

    override fun removeObserver(observer: ControlPanelObserver) {
        observers.remove(observer)
    }

    override fun notifyObservers(observableState: ObservableState) {
        observers.forEach {
            try {
                it.update(observableState as ControlPanelState)
            } catch (e: Exception) {
                Log.i(TAG, e.message)
            }
        }
    }

    data class ControlPanelState(
            val activeFieldType: FieldType
    ) : ObservableState

}

