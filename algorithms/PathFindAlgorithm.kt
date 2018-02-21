package nl.ns.pathfinder.algorithms

import nl.ns.pathfinder.FieldInBord

/**
 * Created by paulvc on 21-02-18.
 */
interface PathFindAlgorithm {
    fun findPath(): Set<FieldInBord>
}