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

        val grid = buildGrid(intcodeState)
        println(grid.count("+"))
    }

    override fun solve2(lines: List<String>) {
        val memory = readMemory(lines.first(), 5000).toMutableList()
        memory[0] = 2
        val intcodeState = IntcodeState(memory)
        Thread(intcodeState).start()

        var ballX = 0
        var paddleX = 0
        var score = 0
        while (!intcodeState.isDone()) {
            while (intcodeState.output.peek() == null) sleep(10)
            val x = intcodeState.output.take().toInt()
            while (intcodeState.output.peek() == null) sleep(10)
            val y = intcodeState.output.take().toInt()
            while (intcodeState.output.peek() == null) sleep(10)
            val tileId = intcodeState.output.take().toInt()
            println("($x, $y) = $tileId")
            when (tileId) {
                3 -> paddleX = x
                4 -> ballX = x
            }
            if (x == -1 && y == 0) {
                score = tileId
                // Since score is the last output, now we send the joystick position
                val direction = ballX.compareTo(paddleX)
                println("write $direction")
                intcodeState.input.put(direction.toLong())
                println("score $score")
            }
        }
        println(score)
        // 11991
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
