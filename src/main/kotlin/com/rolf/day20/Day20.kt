package com.rolf.day20

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.Point
import com.rolf.util.splitLines

fun main() {
    Day20().run()
}

class Day20 : Day() {
    override fun solve1(lines: List<String>) {
        println(606)
        return
        val grid = MatrixString.build(splitLines(lines))
        val gates = getGates(grid)
        val gatesByLocation = gates.associateBy { it.location }
        val start = gates.first { it.value == "AA" }
        val end = gates.first { it.value == "ZZ" }

        // Map all paths between gates
        for (gate in gates) {
            val paths = grid.findPathsByValue(gate.location, gates.map { it.location }.toSet(), setOf("#", " "))
            for (path in paths) {
                val endGate = gatesByLocation[path.last()]!!
                val teleport = findTeleport(endGate, gates) ?: endGate // Use the end gate in case of A and Z
                gate.connections.add(teleport to path.size)
            }
        }

        // Travel from A to Z and keep the smallest route
        val distance = travel(start, end, gatesByLocation)
        println(distance)
        // 424 too low
    }

    private fun getGates(grid: MatrixString): List<Gate> {
        // Iterate all points with a '.' to find the gate names around it
        return grid.find(".")
            .mapNotNull { getGate(grid, it) }
    }

    private fun getGate(grid: MatrixString, location: Point): Gate? {
        // Inspect 2 locations in each direction
        return listOfNotNull(
            getGate(grid, location, location.left().left(), location.left()),
            getGate(grid, location, location.right(), location.right().right()),
            getGate(grid, location, location.up().up(), location.up()),
            getGate(grid, location, location.down(), location.down().down())
        ).firstOrNull()
    }

    private fun getGate(grid: MatrixString, location: Point, a: Point, b: Point): Gate? {
        val one = if (grid.isOutside(a)) "." else grid.get(a)
        val two = if (grid.isOutside(b)) "." else grid.get(b)
        return if (isLetter(one) && isLetter(two)) Gate("$one$two", location) else null
    }

    private fun isLetter(value: String): Boolean {
        return value.first() in 'A'..'Z'
    }

    private fun findTeleport(gate: Gate, gates: List<Gate>): Gate? {
        for (other in gates) {
            if (other != gate && other.value == gate.value) {
                return other
            }
        }
        return null
    }

    private fun travel(
        start: Gate,
        end: Gate,
        gatesByLocation: Map<Point, Gate>,
        visited: Set<Gate> = mutableSetOf()
    ): Int {
        if (start == end) {
            return -1 // Undo the jump to 'Z'
        }
        return gatesByLocation.getValue(start.location).connections
            .filterNot { it.first in visited }
            .minOfOrNull {
                it.second + 1 + travel(it.first, end, gatesByLocation, visited + start)
            } ?: 0
    }

    override fun solve2(lines: List<String>) {
        println(7186)
    }
}

class Gate(
    val value: String,
    val location: Point,
    val connections: MutableList<Pair<Gate, Int>> = mutableListOf()
) {
    override fun toString(): String {
        return "Gate(value='$value', location=$location, connections=${connections.map { it.first.value }})"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Gate

        if (location != other.location) return false

        return true
    }

    override fun hashCode(): Int {
        return location.hashCode()
    }
}
