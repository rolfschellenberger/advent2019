package com.rolf.day11

import com.rolf.Day
import com.rolf.util.*
import java.util.concurrent.TimeUnit

fun main() {
    Day11().run()
}

class Day11 : Day() {
    override fun solve1(lines: List<String>) {
        val (uniqueLocations, _) = paint(lines, 0)
        println(uniqueLocations.size)
    }

    override fun solve2(lines: List<String>) {
        val (uniqueLocations, grid) = paint(lines, 1)
        grid.cutOut(uniqueLocations.minOrNull()!!, uniqueLocations.maxOrNull()!!)
        val output = MatrixString.build(grid)
        output.replace(
            mapOf(
                "0" to " ",
                "1" to "#"
            )
        )
        println(output)
    }

    private fun paint(lines: List<String>, defaultValue: Int): Pair<MutableSet<Point>, MatrixInt> {
        val memory = readMemory(lines.first(), 5000)
        val intcodeState = IntcodeState(memory)
        Thread(intcodeState).start()
        val uniqueLocations = mutableSetOf<Point>()
        // 0 = black, 1 = white
        val grid = MatrixInt.buildDefault(600, 600, defaultValue)
        var robot = grid.center()
        var direction = Direction.NORTH
        while (!intcodeState.isDone()) {
            // Read current color
            val color = grid.get(robot)
            intcodeState.input.add(color.toLong())

            // Wait for the intcode to produce output
            val paint = intcodeState.output.poll(2, TimeUnit.SECONDS)

            // Maybe this was the last input to take?
            if (intcodeState.isDone()) break

            // Read the color to paint
            grid.set(robot, paint!!.toInt())
            uniqueLocations.add(robot)

            // Move robot (0 = left, 1 = right)
            val turn = intcodeState.output.take()
            direction = when (turn) {
                0L -> direction.left()
                1L -> direction.right()
                else -> throw Exception("Unknown turn $turn")
            }
            robot = grid.getForward(robot, direction)!!
        }
        return Pair(uniqueLocations, grid)
    }
}
