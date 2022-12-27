package com.rolf.day18

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.Point
import com.rolf.util.getPermutations
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

        // Pre-compute paths from start or key to another key, returning the length + the required keys needed to get there
        preComputePaths(grid, location, keys, doors)

        println(travel(State(location, emptySet()), keys))
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

    // Map with Pair(from -> to) to Pair(requiredKeys, distance)
    private val memory: MutableMap<Pair<Point, Point>, Pair<Set<String>, Int>> = mutableMapOf()

    private fun preComputePaths(grid: MatrixString, location: Point, keys: Set<Position>, doors: Set<Position>) {
        val defaultBoard = buildDefaultBoard(grid, emptySet())

        val list = keys.map { it.point } + location
        val permutations = getPermutations(list, 2)
        for ((from, to) in permutations) {
            val path = defaultBoard.findPathByValue(from, to, setOf("#"))
            // Find doors on path
            val doorsOnPath = doors.filter { path.contains(it.point) }
            val requiredKeys = doorsOnPath.map { it.value.lowercase() }.toSet()
            memory[from to to] = requiredKeys to path.size
        }
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

    private fun getPathLength(from: Point, to: Point): Int {
        return memory.getValue(from to to).second
    }

    private fun canTravelTo(from: Point, to: Point, keys: Set<Position>): Boolean {
        val keysNeeded = memory.getValue(from to to).first
        return keys.map { it.value }.containsAll(keysNeeded)
    }

    private fun travel(
        state: State,
        keysToCollect: Set<Position>,
        cache: MutableMap<State, Int> = mutableMapOf()
    ): Int {
        // When all keys are retrieved, return the distance traveled
        if (state.keys.size == keysToCollect.size) {
            return 0
        }

        if (cache.containsKey(state)) {
            return cache.getValue(state)
        }

        // Travel to each key
        var minDistance = Int.MAX_VALUE
        val from = state.location
        for (key in keysToCollect - state.keys) {
            // Can we travel to this location with our current key set?
            val to = key.point
            val collectedKeys = state.keys
            if (canTravelTo(from, to, collectedKeys)) {
                // When the key can be reached, open its door
                val newState = State(key.point, state.keys + key)
                val distance = getPathLength(from, key.point) + travel(newState, keysToCollect, cache)
                minDistance = minOf(minDistance, distance)
            }
        }

        cache[state] = minDistance
        return minDistance
    }

    override fun solve2(lines: List<String>) {
        val grid = MatrixString.build(splitLines(lines))
        val location = grid.find("@").first()
        grid.set(grid.getLeftUp(location)!!, "@")
        grid.set(grid.getRightUp(location)!!, "@")
        grid.set(grid.getLeftDown(location)!!, "@")
        grid.set(grid.getRightDown(location)!!, "@")
        grid.set(location, "#")
        grid.set(grid.getUp(location)!!, "#")
        grid.set(grid.getDown(location)!!, "#")
        grid.set(grid.getLeft(location)!!, "#")
        grid.set(grid.getRight(location)!!, "#")
        println(grid)
        val keys = getKeysOnGrid(grid)
        val doors = getDoorsOnGrid(grid)

        // Pre-compute paths from start or key to another key, returning the length + the required keys needed to get there
        preComputePaths(grid, location, keys, doors)

        println(travel(State(location, emptySet()), keys))
    }
}

data class Position(val value: String, val point: Point)

data class State(val location: Point, val keys: Set<Position>) {
    override fun toString(): String {
        return "State(location=$location, keys=${keys.size})"
    }
}
