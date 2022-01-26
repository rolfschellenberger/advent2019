package com.rolf.day13

import com.rolf.Day
import com.rolf.util.IntcodeState
import com.rolf.util.MatrixString
import com.rolf.util.readMemory
import java.lang.Thread.sleep

fun main() {
    Day13().run()
}

class Day13 : Day() {
    override fun solve1(lines: List<String>) {
        val memory = readMemory(lines.first(), 5000)
        val intcodeState = IntcodeState(memory)
        intcodeState.execute()

        val (grid, _) = buildGrid(intcodeState)
        println(grid.count("+"))
    }

    override fun solve2(lines: List<String>) {
        val memory = readMemory(lines.first(), 5000).toMutableList()
        memory[0] = 2
        val intcodeState = IntcodeState(memory)
        Thread(intcodeState).start()

        var highScore = 0
        while (!intcodeState.isDone()) {
            sleep(100)
            val (grid, score) = buildGrid(intcodeState)
            val ball = grid.find("o").first()
            val paddle = grid.find("-").first()
            highScore = maxOf(highScore, score)
            val direction = ball.x.compareTo(paddle.x)
            val remaining = grid.count("+")
//            println(grid)
            println("left: $remaining | direction: $direction | score: $highScore ")
            intcodeState.input.add(direction.toLong())
        }
        println(highScore)
        // 11991
    }

    private fun buildGrid(intcodeState: IntcodeState): Pair<MatrixString, Int> {
        val grid = MatrixString.buildDefault(42, 23, " ")
        var score = 0
        for ((x, y, tileId) in intcodeState.output.map { it.toInt() }.chunked(3)) {
            if (x == -1 && y == 0) {
                score = tileId
            } else {
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
        }
        return grid to score
    }
}
