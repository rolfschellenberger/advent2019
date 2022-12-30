package com.rolf.day24

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.Point
import com.rolf.util.splitLines
import kotlin.math.pow

fun main() {
    Day24().run()
}

class Day24 : Day() {
    override fun solve1(lines: List<String>) {
        var grid = MatrixString.build(splitLines(lines))

        // Look for a duplicate configuration
        val unique = mutableSetOf<Int>()
        while (unique.add(grid.toString().hashCode())) {
            grid = next(grid)
        }

        // Calculate the biodiversity rating
        println(getBiodiversity(grid))
    }

    private fun getBiodiversity(grid: MatrixString): Long {
        return grid.allPoints()
            .map { grid.get(it) }
            .mapIndexed { index, value -> value to 2.0.pow(index).toLong() }
            .filter { it.first == "#" }
            .sumOf { it.second }
    }

    private fun next(grid: MatrixString): MatrixString {
        val copy = grid.copy()
        for (point in grid.allPoints()) {
            val neighbours = grid.getNeighbours(point, diagonal = false).toList()
            copy.set(point, nextValue(grid.get(point), grid.bugCount(neighbours)))
        }
        return copy
    }

    private fun nextValue(value: String, bugCount: Int): String {
        return when (value) {
            "#" -> if (bugCount == 1) "#" else "."
            else -> if (bugCount in 1..2) "#" else "."
        }
    }

    private fun MatrixString.bugCount(): Int {
        return bugCount(allPoints())
    }

    private fun MatrixString.bugCount(points: List<Point>): Int {
        return points.map { get(it) }.count { it == "#" }
    }

    private fun MatrixString.isEmpty(): Boolean {
        return bugCount() == 0
    }

    override fun solve2(lines: List<String>) {
        val grid = MatrixString.build(splitLines(lines))

        // Keep track of all layers
        var layers = mutableMapOf<Int, MatrixString>()
        layers[0] = grid

        for (time in 0 until 200) {
            layers = next2(layers)
        }
        println(layers.values.sumOf { it.bugCount() })
    }

    private fun next2(layers: MutableMap<Int, MatrixString>): MutableMap<Int, MatrixString> {
        // First expand the layers where needed
        val minLayer = layers.minOf { it.key }
        val maxLayer = layers.maxOf { it.key }

        if (!layers.getValue(minLayer).isEmpty()) {
            layers[minLayer - 1] = MatrixString.buildDefault(5, 5, ".")
        }
        if (!layers.getValue(maxLayer).isEmpty()) {
            layers[maxLayer + 1] = MatrixString.buildDefault(5, 5, ".")
        }

        // Now move every layer
        val newLayers = layers.toMutableMap()
        for (index in newLayers.keys) {
            val newGrid = next2(index, layers)
            newLayers[index] = newGrid
        }
        return newLayers
    }

    private fun next2(index: Int, layers: MutableMap<Int, MatrixString>): MatrixString {
        val grid = layers.getValue(index)
        val copy = grid.copy()

        // Determine for every point its new value, except the center point
        for (point in grid.allPoints().filterNot { it == grid.center() }) {
            val bugCount = getBugCount(index, layers, point)
            copy.set(point, nextValue(grid.get(point), bugCount))
        }
        return copy
    }

    private fun getBugCount(index: Int, layers: MutableMap<Int, MatrixString>, point: Point): Int {
        val grid = layers.getValue(index)

        // We get all neighbours for this point
        val center = grid.center()
        val neighbours = grid.getNeighbours(point, diagonal = false).filterNot { it == center }

        // When there are 4 neighbours, we can count the number of bugs adjacent
        if (neighbours.size != 4) {
            // When there are less than 4 neighbours, we should look 1 layer up or 1 layer down
            val centerNeighbours = grid.getNeighbours(center, diagonal = false)
            val otherLayerIndex = if (point in centerNeighbours) index + 1 else index - 1
            if (layers.containsKey(otherLayerIndex)) {
                return grid.bugCount(neighbours) + layers.getValue(otherLayerIndex).bugCount(mapping.getValue(point))
            }
        }
        return grid.bugCount(neighbours)
    }

    private val mapping = mutableMapOf(
        // Top row
        Point(0, 0) to listOf(Point(2, 1), Point(1, 2)),
        Point(1, 0) to listOf(Point(2, 1)),
        Point(2, 0) to listOf(Point(2, 1)),
        Point(3, 0) to listOf(Point(2, 1)),
        Point(4, 0) to listOf(Point(2, 1), Point(3, 2)),
        // Right column
        Point(4, 1) to listOf(Point(3, 2)),
        Point(4, 2) to listOf(Point(3, 2)),
        Point(4, 3) to listOf(Point(3, 2)),
        Point(4, 4) to listOf(Point(3, 2), Point(2, 3)),
        // Bottom row
        Point(3, 4) to listOf(Point(2, 3)),
        Point(2, 4) to listOf(Point(2, 3)),
        Point(1, 4) to listOf(Point(2, 3)),
        Point(0, 4) to listOf(Point(2, 3), Point(1, 2)),
        // Left column
        Point(0, 3) to listOf(Point(1, 2)),
        Point(0, 2) to listOf(Point(1, 2)),
        Point(0, 1) to listOf(Point(1, 2)),

        // Inwards top
        Point(2, 1) to listOf(Point(0, 0), Point(1, 0), Point(2, 0), Point(3, 0), Point(4, 0)),
        // Inwards right
        Point(3, 2) to listOf(Point(4, 0), Point(4, 1), Point(4, 2), Point(4, 3), Point(4, 4)),
        // Inwards bottom
        Point(2, 3) to listOf(Point(4, 4), Point(3, 4), Point(2, 4), Point(1, 4), Point(0, 4)),
        // Inwards left
        Point(1, 2) to listOf(Point(0, 4), Point(0, 3), Point(0, 2), Point(0, 1), Point(0, 0))
    )
}
