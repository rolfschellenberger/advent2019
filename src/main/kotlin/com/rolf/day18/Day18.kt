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
        val start = grid.find("@").first()
        val keys = getKeys(grid)
        val doors = getDoors(grid)
        val distance = travel(grid, start, keys, doors)
        println(distance)
    }

    private fun travel(
        grid: MatrixString,
        location: Point,
        keys: List<Point>,
        doors: List<Point>,
        distance: Int = 0,
        cache: MutableMap<Pair<Point, List<Point>>, Int> = mutableMapOf()
    ): Int {
        val keys = getKeys(grid)
        if (keys.isEmpty()) {
            return distance
        }

        val cacheKey = location to keys
        if (cache.containsKey(cacheKey)) {
            return cache.getValue(cacheKey)
        }

        var minDistance = Int.MAX_VALUE

        // What keys can we collect next?
        val keysToCollect = getKeysToCollect(grid, location, keys)
        for (keyToCollect in keysToCollect) {
            val distanceToKey = distance + keyToCollect.second
            if (distanceToKey < minDistance) {
                val newGrid = grid.copy()
                // Remove door(s)
                val keyValue = newGrid.get(keyToCollect.first)
                val doorValue = keyValue.uppercase()
                newGrid.find(doorValue).forEach { newGrid.set(it, ".") }
                // Remove key
                newGrid.set(keyToCollect.first, ".")
                // Travel from key location
                println("Keys: ${keys.size}, distance: $distanceToKey")
                val newDistance = travel(newGrid, keyToCollect.first, distance + keyToCollect.second, cache)
                minDistance = minOf(minDistance, newDistance)
            }
        }
        cache[cacheKey] = minDistance
        return minDistance
    }

    private fun getKeysToCollect(
        grid: MatrixString,
        location: Point,
        keys: List<Point>
    ): List<Pair<Point, Int>> {
        val toCollect = mutableListOf<Pair<Point, Int>>()
        val doors = getDoors(grid)
        val blockingValues = doors.map { grid.get(it) } + "#"

        for (key in keys) {
            val path = grid.findPathByValue(location, key, blockingValues.toSet(), diagonal = false)
            if (path.isNotEmpty()) {
                toCollect.add(key to path.size)
            }
        }
        return toCollect
    }

    private fun getDoors(grid: MatrixString): List<Point> {
        return grid.allPoints()
            .filter { grid.get(it).first() in 'A'..'Z' }
    }

    private fun getKeys(grid: MatrixString): List<Point> {
        return grid.allPoints()
            .filter { grid.get(it).first() in 'a'..'z' }
    }

    override fun solve2(lines: List<String>) {
    }
}
