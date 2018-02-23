package nl.ns.pathfinder

/**
 * Created by paulvc on 20-12-17.
 */
class FieldInBord(val xCoordinate: Int, val yCoordinate: Int) {

    var fieldType = FieldType.DEFAULT
    var aStarParent: FieldInBord? = null
    var fCost: Int? = null
    var hCost: Int? = null

    override fun toString(): String {
        return "x: $xCoordinate, y: $yCoordinate"
    }

}