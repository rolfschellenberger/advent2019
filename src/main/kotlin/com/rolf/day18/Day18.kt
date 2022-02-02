package com.rolf.day18

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.Point
import com.rolf.util.splitLines

fun main() {
    Day18().run()
}

typealias Position = Pair<String, Point>

class Day18 : Day() {
    override fun solve1(lines: List<String>) {
//        val maze = Maze.from(lines)
//        println(maze.minimumSteps())

        val grid = MatrixString.build(splitLines(lines))
        val start = grid.find("@").first()
        val distance = travel(grid, start, mutableSetOf("c"))
        println(distance)
//        val keys = getKeys(grid)
//        val doors = getDoors(grid)
//        val path = travelPath(grid, start, keys, doors)
//        println(path)
        // 7758 too high
        // 6718 too high
        // 6689 too high
        // 7308????
    }

    private fun travel(
        grid: MatrixString,
        location: Point,
        keysCollected: Set<String> = setOf(),
        cache: MutableMap<Pair<Point, Set<String>>, Int> = mutableMapOf()
    ): Int {
        val cacheKey = Pair(location, keysCollected)
        if (cacheKey in cache) {
            return cache.getValue(cacheKey)
        }

        val locations = locationsToTravel(grid, location, keysCollected)
        println(keysCollected)
        val distance = locations.map {
            it.second.size + travel(grid, it.second.last(), keysCollected + it.first, cache)
        }.minOrNull() ?: 0
        cache[cacheKey] = distance
        return distance
    }

    private fun locationsToTravel(
        grid: MatrixString,
        location: Point,
        keysCollected: Set<String>
    ): List<Pair<String, List<Point>>> {
        // Return the key locations with the ending key value and the path
        val locations = mutableListOf<Pair<String, List<Point>>>()

        val keys = getKeyToTravel(grid, keysCollected)
        val doors = getDoorsNotToTravel(grid, keysCollected)

        // Find paths to every key
        val paths = grid.findPathsByValue(location, keys.map { it.second }.toSet(), doors + "#", false)
        return paths.map { grid.get(it.last()) to it }
    }

    private fun getKeyToTravel(grid: MatrixString, keysCollected: Set<String>): List<Position> {
        return grid.allPoints()
            .map { Position(grid.get(it), it) }
            .filter { it.first.first() in 'a'..'z' }
            .filterNot { it.first in keysCollected }
    }

    private fun getDoorsNotToTravel(grid: MatrixString, keysCollected: Set<String>): Set<String> {
        return grid.allPoints()
            .map { Position(grid.get(it), it) }
            .filter { it.first.first() in 'A'..'Z' }
            .filterNot { it.first.lowercase() in keysCollected }
            .map { it.first }
            .toSet()
    }

    private fun travelPath(
        grid: MatrixString, location: Point, keys: Set<Point>, doors: Set<String>,
        distance: Int = 0,
        cache: MutableMap<Pair<Point, Set<Point>>, Int> = mutableMapOf()
    ): Int {
        if (keys.isEmpty()) {
            return distance
        }

        val cacheKey = location to keys
        if (cache.containsKey(cacheKey)) {
            return distance + cache.getValue(cacheKey)
        }

        val value = grid.get(location)
        val keyValues = keys.map { grid.get(it) }.toSet()
        println("Traveling from $value")
        println("Remaining keys: $keyValues")
        val test = setOf("g", "n", "h", "d", "l", "o", "e", "p", "c", "i", "k", "m")
        if (keyValues == test && value == "j") {
            println("test!")
        }
        println("Remaining doors: $doors")
        println("Distance: $distance")

        val keysToCollect = getKeysToCollect(grid, location, keys, doors)
//        val g = grid.copy()
//        keysToCollect.forEachIndexed { index, path -> g.set(path.last(), index.toString()) }
//        println(g)
//        println()

        var minDistance = Int.MAX_VALUE
        for (keyToCollect in keysToCollect) {
            val key = grid.get(keyToCollect.last())
            val newKeys = keys - keyToCollect.last()
            val newDoors = doors - key.uppercase()
            val totalDistance =
                travelPath(grid, keyToCollect.last(), newKeys, newDoors, keyToCollect.size, cache)
//            println(totalDistance)
            minDistance = minOf(minDistance, totalDistance)
        }
        cache[cacheKey] = minDistance
        return distance + minDistance
    }

    private fun getKeysToCollect(
        grid: MatrixString,
        location: Point,
        keys: Set<Point>,
        doors: Set<String>
    ): List<List<Point>> {
        val toCollect = mutableListOf<List<Point>>()
        val blockingValues = doors + "#"

        for (key in keys) {
            val path = grid.findPathByValue(location, key, blockingValues, diagonal = false)
            if (path.isNotEmpty()) {
                toCollect.add(path)
            }
        }
        return toCollect.sortedBy { it.size }
    }

    private fun getDoors(grid: MatrixString): Set<String> {
        return grid.allPoints()
            .map { grid.get(it) }
            .filter { it.first() in 'A'..'Z' }
            .toSet()
    }

    private fun getKeys(grid: MatrixString): Set<Point> {
        return grid.allPoints()
            .filter { grid.get(it).first() in 'a'..'z' }
            .toSet()
    }

    override fun solve2(lines: List<String>) {
    }
}

class Maze(
    private val starts: Set<Point2D>,
    private val keys: Map<Point2D, Char>,
    private val doors: Map<Point2D, Char>,
    private val openSpaces: Set<Point2D>
) {

    private fun findReachableKeys(from: Point2D, haveKeys: Set<Char> = mutableSetOf()): Map<Char, Pair<Point2D, Int>> {
        val queue = ArrayDeque<Point2D>().apply { add(from) }
        val distance = mutableMapOf(from to 0)
        val keyDistance = mutableMapOf<Char, Pair<Point2D, Int>>()
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            next.neighbors()
                .filter { it in openSpaces }
                .filterNot { it in distance }
                .forEach { point ->
                    distance[point] = distance[next]!! + 1
                    val door = doors[point]
                    val key = keys[point]
                    if (door == null || door.lowercaseChar() in haveKeys) {
                        if (key != null && key !in haveKeys) {
                            keyDistance[key] = Pair(point, distance[point]!!)
                        } else {
                            queue.add(point)
                        }
                    }
                }
        }
        return keyDistance
    }

    private fun findReachableFromPoints(
        from: Set<Point2D>,
        haveKeys: Set<Char>
    ): Map<Char, Triple<Point2D, Int, Point2D>> =
        from.map { point ->
            findReachableKeys(point, haveKeys).map { entry ->
                entry.key to Triple(entry.value.first, entry.value.second, point)
            }
        }.flatten().toMap()

    fun minimumSteps(
        from: Set<Point2D> = starts,
        haveKeys: Set<Char> = mutableSetOf(),
        seen: MutableMap<Pair<Set<Point2D>, Set<Char>>, Int> = mutableMapOf()
    ): Int {
        val state = Pair(from, haveKeys)

        if (state in seen) return seen.getValue(state)

        val answer = findReachableFromPoints(from, haveKeys).map { entry ->
            val (at, dist, cause) = entry.value
            dist + minimumSteps((from - cause) + at, haveKeys + entry.key, seen)
        }.minOrNull() ?: 0
        seen[state] = answer
        return answer
    }

    companion object {
        fun from(input: List<String>): Maze {
            val starts = mutableSetOf<Point2D>()
            val keys = mutableMapOf<Point2D, Char>()
            val doors = mutableMapOf<Point2D, Char>()
            val openSpaces = mutableSetOf<Point2D>()

            input.forEachIndexed { y, row ->
                row.forEachIndexed { x, c ->
                    val place = Point2D(x, y)
                    if (c == '@') starts += place
                    if (c != '#') openSpaces += place
                    if (c in ('a'..'z')) keys[place] = c
                    if (c in ('A'..'Z')) doors[place] = c
                }
            }
            return Maze(starts, keys, doors, openSpaces)
        }
    }
}

data class Point2D(val x: Int, val y: Int) {
    fun up(): Point2D = copy(y = y + 1)
    fun down(): Point2D = copy(y = y - 1)
    fun left(): Point2D = copy(x = x - 1)
    fun right(): Point2D = copy(x = x + 1)

    fun neighbors(): List<Point2D> =
        listOf(up(), down(), left(), right())
}
