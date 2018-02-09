package nl.ns.pathfinder

/**
 * Created by paulvc on 09-02-18.
 */
interface ControlPanelObserver {
    fun update(controlPanelState: ControlPanel.ControlPanelState)
}