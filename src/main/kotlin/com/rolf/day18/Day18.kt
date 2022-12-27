package com.rolf.day18

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.Point
import com.rolf.util.splitLines

fun main() {
    Day18().run()
}

class Day18 : Day() {

    override fun solve1(lines: List<String>) {
        val grid = MatrixString.build(splitLines(lines))
        val location = grid.find("@").first()
        val keys = getKeysOnGrid(grid)
        val doors = getDoorsOnGrid(grid)
        println(travel(grid, State(location, keys, doors)))
    }

    private fun travel(
        grid: MatrixString,
        state: State,
        cache: MutableMap<State, Int> = mutableMapOf()
    ): Int {
        // When all keys are retrieved, return the distance traveled
        if (state.keys.isEmpty()) {
            return 0
        }
        println(state)

        if (cache.containsKey(state)) {
            return cache.getValue(state)
        }

        val defaultBoard = buildDefaultBoard(grid, state.doors)
        // Travel to each key
        var minDistance = Int.MAX_VALUE
        for (key in state.keys) {
            val path = defaultBoard.findPathByValue(state.location, key.point, setOf("#"))
            if (path.isNotEmpty()) {
                // When the key can be reached, open its door
                val doors = openDoor(state.doors, key)
                val newState = State(key.point, state.keys - key, doors)
                val distance = path.size + travel(grid, newState, cache)
                minDistance = minOf(minDistance, distance)
            }
        }

        cache[state] = minDistance
        return minDistance
    }

    private fun getKeysOnGrid(grid: MatrixString): Set<Position> {
        return grid.allPoints()
            .map { Position(grid.get(it), it) }
            .filter { it.value.first() in 'a'..'z' }
            .toSet()
    }

    private fun getDoorsOnGrid(grid: MatrixString): Set<Position> {
        return grid.allPoints()
            .map { Position(grid.get(it), it) }
            .filter { it.value.first() in 'A'..'Z' }
            .toSet()
    }

    private fun buildDefaultBoard(
        grid: MatrixString,
        doors: Set<Position>
    ): MatrixString {
        val defaultBoard = MatrixString.buildDefault(grid.width(), grid.height(), ".")
        grid.find("#").forEach { defaultBoard.set(it, "#") }
        doors.forEach { defaultBoard.set(it.point, "#") }
        return defaultBoard
    }

    private fun openDoor(doors: Set<Position>, key: Position): Set<Position> {
        return doors.filterNot {
            it.value == key.value.uppercase()
        }.toSet()
    }

    override fun solve2(lines: List<String>) {
    }
}

data class Position(val value: String, val point: Point)

data class State(val location: Point, val keys: Set<Position>, val doors: Set<Position>) {
    override fun toString(): String {
        return "State(location=$location, keys=${keys.size}, doors=${doors.size})"
    }
}
