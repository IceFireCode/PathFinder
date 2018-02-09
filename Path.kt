package nl.ns.pathfinder

import nl.ns.pathfinder.ui.PlayBoard
import java.lang.Math.max
import java.lang.Math.min


/**
 * Created by paulvc on 23-01-18.
 */
class Path (val startX: Int, val endX: Int, val startY: Int, val endY: Int){

    var xDistance: Int = 0
    var yDistance: Int = 0
    var nrOfDiagonalSteps: Int = 0
    var nrOfStraightSteps: Int = 0
    val pathFields: MutableSet<FieldInBord> = mutableSetOf()

    init {
        xDistance = max(startX, endX) - min(startX, endX)
        yDistance = max(startY, endY) - min(startY, endY)
        nrOfDiagonalSteps = min(xDistance, yDistance)
        nrOfStraightSteps = max(xDistance, yDistance) - min(xDistance, yDistance)
    }

    fun determinePathFields(playBoard: PlayBoard): Set<FieldInBord> {
        var x = startX
        var y = startY
        pathFields.add(playBoard.allFields[x][y])

        // diagonal steps in the path
        for (i in 0 until nrOfDiagonalSteps){

            // check in which direction the x should change to take a diagonal step in the path
            if (startX > endX){
                x--
            } else {
                x++
            }

            // check in which direction the y should change to take a diagonal step in the path
            if (startY > endY){
                y--
            } else {
                y++
            }

            pathFields.add(playBoard.allFields[x][y])
        }

        // straight steps in the path
        for (i in 0 until nrOfStraightSteps){

            // check in which direction the x OR y should change to take a straight step in the path
            if (x > endX){
                x--
            } else if (x < endX){
                x++
            } else if (y > endY){
                y--
            } else {
                y++
            }

            pathFields.add(playBoard.allFields[x][y])
        }

        //todo add val nrOfSteps
        return pathFields.toSet()
    }

}