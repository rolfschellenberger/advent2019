package com.rolf.day13

import com.rolf.Day
import com.rolf.util.IntcodeState
import com.rolf.util.MatrixString
import com.rolf.util.readMemory

fun main() {
    Day13().run()
}

class Day13 : Day() {
    override fun solve1(lines: List<String>) {
        val memory = readMemory(lines.first(), 5000)
        val intcodeState = IntcodeState(memory)
        intcodeState.execute()

        val grid = buildGrid(intcodeState)
        println(grid.count("+"))
    }

    override fun solve2(lines: List<String>) {
        val memory = readMemory(lines.first(), 5000).toMutableList()
        memory[0] = 2
        val intcodeState = IntcodeState(memory)
        Thread(intcodeState).start()

        var paddleX = 0
        var score = 0
        while (!intcodeState.isDone()) {
            if (intcodeState.output.peek() != null) {
                val x = intcodeState.output.take().toInt()
                val y = intcodeState.output.take().toInt()
                val tileId = intcodeState.output.take().toInt()
                when (tileId) {
                    3 -> paddleX = x
                    4 -> {
                        val direction = x.compareTo(paddleX)
                        intcodeState.input.add(direction.toLong())
                    }
                }
                if (x == -1 && y == 0) {
                    score = tileId
                }
            }
        }
        println(score)
    }

    private fun buildGrid(intcodeState: IntcodeState): MatrixString {
        val grid = MatrixString.buildDefault(42, 23, " ")
        for ((x, y, tileId) in intcodeState.output.map { it.toInt() }.chunked(3)) {
            grid.set(
                x, y, when (tileId) {
                    0 -> " " // empty
                    1 -> "#" // wall
                    2 -> "+" // block
                    3 -> "-" // paddle
                    4 -> "o" // ball
                    else -> throw Exception("Unknown tile: $tileId")
                }
            )
        }
        return grid
    }
}
