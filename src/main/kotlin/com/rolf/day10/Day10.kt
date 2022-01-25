package com.rolf.day10

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.Point
import com.rolf.util.splitLines

fun main() {
    Day10().run()
}

class Day10 : Day() {
    override fun solve1(lines: List<String>) {
        val grid = MatrixString.build(splitLines(lines))
        println(getBestLocationAndInView(grid).second)
    }

    override fun solve2(lines: List<String>) {
        val grid = MatrixString.build(splitLines(lines))
        val location = getBestLocationAndInView(grid).first

        val directions = grid.getAllDirections(location)
        val nextSteps = directions.map { it to grid.getNextDirection(location, it) }
        val sortedDirections = sortByAngle(location, nextSteps)
        var asteroids = 0
        while (true) {
            for (direction in sortedDirections) {
                val asteroid = findAsteroid(grid, location, direction)
                if (asteroid != null) {
                    asteroids++
                    // Remove the asteroid
                    grid.set(asteroid, ".")

                    // We found the 200th asteroid!
                    if (asteroids == 200) {
                        println(asteroid.x * 100 + asteroid.y)
                        return
                    }
                }
            }
        }
    }

    private fun findAsteroid(grid: MatrixString, location: Point, direction: Point): Point? {
        var current = grid.getNextDirection(location, direction)
        while (!grid.isOutside(current)) {
            if (grid.get(current) == "#") {
                return current
            }
            current = grid.getNextDirection(current, direction)
        }
        return null
    }

    private fun getBestLocationAndInView(grid: MatrixString): Pair<Point, Int> {
        var maxView = Int.MIN_VALUE
        var maxPoint = Point(0, 0)
        for (point in grid.find("#")) {
            var inView = 0
            val directions = grid.getAllDirections(point)
            for (direction in directions) {
                var current = grid.getNextDirection(point, direction)
                while (!grid.isOutside(current)) {
                    if (grid.get(current) == "#") {
                        inView++
                        break
                    }
                    current = grid.getNextDirection(current, direction)
                }
            }
            if (inView > maxView) {
                maxView = inView
                maxPoint = point
            }
        }
        return maxPoint to maxView
    }

    private fun sortByAngle(location: Point, directionToNextStep: List<Pair<Point, Point>>): List<Point> {
        // Add 90 degrees, since the angle is calculated from the x-axis
        return directionToNextStep.sortedBy { (location.angleBetween(it.second) + 90.0) % 360.0 }.map { it.first }
    }
}
