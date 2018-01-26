package nl.ns.pathfinder.Extensions

import android.os.Build
import android.text.Html
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import android.widget.TextView
import nl.ns.pathfinder.R

/**
 * Created by paulvc on 10-11-17.
 */

fun TextView.setShrinkGrowAnimation(): Unit {
    this.setOnTouchListener { view, motionEvent ->
        val shrinkAnimation = AnimationUtils.loadAnimation(context, R.anim.shrink)
        val growAnimation = AnimationUtils.loadAnimation(context, R.anim.grow)
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> view.startAnimation(shrinkAnimation)
            MotionEvent.ACTION_UP -> view.startAnimation(growAnimation)
        }
        false
    }
}

