package nl.ns.pathfinder.algorithms

import nl.ns.pathfinder.FieldInBord
import nl.ns.pathfinder.Path
import nl.ns.pathfinder.ui.PlayBoard

/**
 * Created by paulvc on 21-02-18.
 */
class AStarAlgorithm(val playBoard: PlayBoard) : PathFindAlgorithm {

    val open: MutableSet<FieldInBord> = mutableSetOf()
    val closed: MutableSet<FieldInBord> = mutableSetOf()
    var current: FieldInBord = FieldInBord(-1, -1)

    override fun findPath(): Set<FieldInBord> {

        open.add(playBoard.startField!!)

        var pathFound = false
        while (!pathFound) {
            current = openFieldWithMinimumFCost()
            open.remove(current)
            closed.add(current)
            if (current == playBoard.endField) {
                pathFound = true
                break
            }

            val neighbours: Set<FieldInBord> = playBoard.getNeighbours(current)
            neighbours.forEach { neighbour ->
                if (!neighbour.fieldType.isTraversable || closed.contains(neighbour)) {
                    return@forEach
                } else {
                    val wouldBeFCost = determineFCost(neighbour, current)
                    if (neighbour.fCost == null || wouldBeFCost.first < neighbour.fCost!!) {
                        neighbour.fCost = wouldBeFCost.first
                        neighbour.hCost = wouldBeFCost.second
                        neighbour.aStarParent = current
                        if (!open.contains(neighbour)) {
                            open.add(neighbour)
                        }
                    }
                }

            }
        }

        return setOf() //todo find path and return it by following the line of parents
    }

    private fun openFieldWithMinimumFCost(): FieldInBord {
        if (open.size == 1) {
            return open.first()
        }
        return open.minBy {
            it.fCost!!
        }!!
    }

    private fun determineFCost(fieldInBord: FieldInBord, fieldToUseAsParent: FieldInBord): Pair<Int, Int> {
        var parentHCost = 0 //todo don't use fCost, but keep track of the cost to the startfield and use that cost of the parent field
        if (fieldToUseAsParent != playBoard.startField) {
            parentHCost = fieldToUseAsParent.hCost!!
        }

        val nrOfStepsToStartField = Path(fieldToUseAsParent, fieldInBord).nrOfSteps + parentHCost
        val nrOfStepsToEndField = Path(fieldInBord, playBoard.endField!!).nrOfSteps
        return Pair(nrOfStepsToStartField + nrOfStepsToEndField!!, nrOfStepsToStartField)
    }
}