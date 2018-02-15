package nl.ns.pathfinder

/**
 * Created by paulvc on 09-02-18.
 */
interface Observable {
    fun registerObserver(observer: ControlPanelObserver)
    fun removeObserver(observer: ControlPanelObserver)
    fun notifyObservers(observableState: ControlPanel.ControlPanelState)
}