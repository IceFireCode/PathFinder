package nl.ns.pathfinder.Extensions

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * Created by paulvc on 03-01-18.
 */

fun Context.getSmallestScreenSize(): Int {
    val dm = DisplayMetrics()
    val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getMetrics(dm)
    val height = dm.heightPixels
    val width = dm.widthPixels
    val smallestScreenSize = Math.min(height, width)
    return smallestScreenSize
}