package com.rolf.day15

import com.rolf.Day
import com.rolf.util.*
import java.util.concurrent.LinkedBlockingQueue

fun main() {
    Day15().run()
}

class Day15 : Day() {
    override fun solve1(lines: List<String>) {
        val (thread, grid) = buildGrid(lines)

        val to = grid.find("X").first()
        val path = grid.findPathByValue(grid.center(), setOf(to), setOf("#"), diagonal = false)
        println(path.size)
        thread.interrupt()
    }

    override fun solve2(lines: List<String>) {
        val (thread, grid) = buildGrid(lines)

        val from = grid.find("X").first()
        var maxDistance = 0
        for (point in grid.allPoints()) {
            if (grid.get(point) == ".") {
                val path = grid.findPathByValue(from, setOf(point), setOf("#"), diagonal = false)
                maxDistance = maxOf(maxDistance, path.size)
            }
        }
        println(maxDistance)
        thread.interrupt()
    }

    private fun buildGrid(lines: List<String>): Pair<Thread, MatrixString> {
        val memory = readMemory(lines.first(), 5000)
        val intcodeState = IntcodeState(memory)

        val thread = Thread(intcodeState)
        thread.start()

        val grid = MatrixString.buildDefault(44, 44, " ")
        explore(grid, grid.center(), intcodeState.input, intcodeState.output)
        grid.set(grid.center(), "B")
        return Pair(thread, grid)
    }

    private fun explore(
        grid: MatrixString,
        location: Point,
        input: LinkedBlockingQueue<Long>,
        output: LinkedBlockingQueue<Long>
    ) {
        // Try all neighbours that are not yet explored and when done, step back
        for (direction in Direction.values()) {
            val neighbour = grid.getForward(location, direction)!!
            // Travel only unexplored areas
            if (grid.get(neighbour) == " ") {
                input.add(move(direction))
                when (output.take()) {
                    0L -> {
                        grid.set(neighbour, "#")
                    }
                    1L -> {
                        grid.set(neighbour, ".")
                        explore(grid, neighbour, input, output)
                        input.add(move(direction.opposite()))
                        output.take() // Ignore the result
                    }
                    2L -> {
                        grid.set(neighbour, "X")
                        explore(grid, neighbour, input, output)
                        input.add(move(direction.opposite()))
                        output.take() // Ignore the result
                    }
                }
            }
        }
    }

    private fun move(direction: Direction): Long {
        return when (direction) {
            Direction.NORTH -> 1
            Direction.SOUTH -> 2
            Direction.WEST -> 3
            Direction.EAST -> 4
        }
    }
}
