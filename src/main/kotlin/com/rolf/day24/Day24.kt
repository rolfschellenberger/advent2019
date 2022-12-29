package com.rolf.day24

import com.rolf.Day
import com.rolf.util.MatrixString
import com.rolf.util.splitLines
import kotlin.math.pow

fun main() {
    Day24().run()
}

class Day24 : Day() {
    override fun solve1(lines: List<String>) {
        val grid = MatrixString.build(splitLines(lines))

        val unique = mutableSetOf(grid.toString().hashCode())
        while (true) {
            val copy = grid.copy()
            for (point in copy.allPoints()) {
                val neighbours = copy.getNeighbours(point, diagonal = false)
                val bugCount = neighbours.map { copy.get(it) }.count { it == "#" }
                val value = when (copy.get(point)) {
                    "#" -> if (bugCount == 1) "#" else "."
                    else -> if (bugCount in 1..2) "#" else "."
                }
                grid.set(point, value)
            }
            if (!unique.add(grid.toString().hashCode())) {
                var sum = 0L
                for ((index, point) in grid.allPoints().withIndex()) {
                    if (grid.get(point) == "#") {
                        sum += 2.0.pow(index).toLong()
                    }
                }
                println(sum)
                break
            }
        }
    }

    override fun solve2(lines: List<String>) {
    }
}
