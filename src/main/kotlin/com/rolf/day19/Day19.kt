package com.rolf.day19

import com.rolf.Day
import com.rolf.util.IntcodeState
import com.rolf.util.MatrixString
import com.rolf.util.Point
import com.rolf.util.readMemory

fun main() {
    Day19().run()
}

class Day19 : Day() {
    override fun solve1(lines: List<String>) {
        val grid = buildGrid(lines)
        println(grid.count("#"))
    }

    override fun solve2(lines: List<String>) {
        val memory = readMemory(lines.first(), 5000)
        val intcodeState = IntcodeState(memory)

        var topLeft = Point(0, 1000)
        var found = false
        while (!found) {
            val point = getRowStart(intcodeState, topLeft)
            topLeft = Point(point.second.x, topLeft.y + 1)
            if (point.first >= 100) {
                val column = getColumn(intcodeState, point.second)
                if (column >= 100) {
                    found = true
                }
            }
        }
        println(10000 * topLeft.x + topLeft.y - 1)
    }

    private fun buildGrid(lines: List<String>): MatrixString {
        val memory = readMemory(lines.first(), 5000)
        val grid = MatrixString.buildDefault(50, 50, " ")

        val intcodeState = IntcodeState(memory)
        for (point in grid.allPoints()) {
            val value = getValue(intcodeState, point)
            if (value != 0) {
                grid.set(point, "#")
            }
        }
        return grid
    }

    private fun getValue(intcodeState: IntcodeState, point: Point): Int {
        intcodeState.reset()
        intcodeState.input.add(point.x.toLong())
        intcodeState.input.add(point.y.toLong())
        intcodeState.execute()
        return intcodeState.output.take().toInt()
    }

    private fun getRowStart(intcodeState: IntcodeState, topLeft: Point): Pair<Int, Point> {
        var count = 0
        var xStart: Int? = null
        var xEnd: Int? = null
        var x = topLeft.x
        while (xEnd == null) {
            val value = getValue(intcodeState, Point(x, topLeft.y))
            if (value == 1 && xStart == null) {
                xStart = x
            }
            if (value == 0 && xStart != null) {
                xEnd = x
            }
            x++
            count += value
        }
        return count to Point(xEnd - 100, topLeft.y)
    }

    private fun getColumn(intcodeState: IntcodeState, topLeft: Point): Int {
        var count = 0
        for (y in topLeft.y until topLeft.y + 100) {
            count += getValue(intcodeState, Point(topLeft.x, y))
        }
        return count
    }
}
