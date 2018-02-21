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

        while (true) {
            current = openFieldWithMinimumFCost()
            open.remove(current)
            closed.add(current)
            if (current == playBoard.endField) {
                break
            }

            val neighbours: Set<FieldInBord> = playBoard.getNeighbours()
            neighbours.forEach {
                if (!it.fieldType.isTraversable || closed.contains(it)) {
                    return@forEach
                }

            }
        }

        return setOf()
    }

    private fun openFieldWithMinimumFCost(): FieldInBord {
        open.forEach {
            determineFCost(it)
        }
        return open.minBy { it.fCost!! }!!
    }

    private fun determineFCost(fieldInBord: FieldInBord) {
        val nrOfStepsToStartField = Path(playBoard.startField!!, fieldInBord).nrOfSteps
        val nrOfStepsToEndField = Path(fieldInBord, playBoard.endField!!).nrOfSteps
        fieldInBord.fCost = nrOfStepsToStartField + nrOfStepsToEndField
    }
}