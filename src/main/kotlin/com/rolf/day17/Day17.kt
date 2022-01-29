package com.rolf.day17

import com.rolf.Day
import com.rolf.util.*

fun main() {
    Day17().run()
}

class Day17 : Day() {
    override fun solve1(lines: List<String>) {
        val grid = buildGrid(lines)
        val alignment = grid.allPoints().map {
            it to grid.getNeighbours(it, diagonal = false)
        }.filter {
            it.second.all { grid.get(it) == "#" }
        }.map { it.first }
            .map { it.x * it.y }
            .sum()
        println(alignment)
    }

    override fun solve2(lines: List<String>) {
        val grid = buildGrid(lines)
        val path = findPath(grid)
        val chunks = path.chunked(20)
        chunks.forEach { println(it) }
        val memory = readMemory(lines.first(), 5000)
        val intcodeState = IntcodeState(memory)
        memory[0] = 2
        val thread = Thread(intcodeState)
        thread.start()

        // Feed the pattern + the path chunks
//        val MAX_LENGTH = 20
//        println("A,A,B,C,B,C,B,C\n".map { it.code })

        thread.interrupt()
    }

    private fun buildGrid(lines: List<String>): MatrixString {
        val memory = readMemory(lines.first(), 5000)
        val intcodeState = IntcodeState(memory)
        intcodeState.execute()
        val map = intcodeState.output.map { Char(it.toInt()) }.joinToString("")
        val grid = MatrixString.build(splitLines(map.lines().filterNot { it.isEmpty() }))
        return grid
    }

    private fun findPath(grid: MatrixString): List<String> {
        val path = mutableListOf<String>()

        // Get starting point and direction
        val (start, facing) = getStartingPoint(grid)

        var done = false
        var location = start
        var direction = facing
        while (!done) {
            // Find max forward
            val forward = findForward(grid, location, direction)
            if (forward.isNotEmpty()) {
                path.add(forward.size.toString())
                location = forward.last()
            }
            // What direction can we go?
            else {
                val left = grid.getForward(location, direction.left())
                val right = grid.getForward(location, direction.right())
                when {
                    left != null && grid.get(left) == "#" -> {
                        path.add("L")
                        direction = direction.left()
                    }
                    right != null && grid.get(right) == "#" -> {
                        path.add("R")
                        direction = direction.right()
                    }
                    else -> done = true
                }
            }
        }

        return path
    }

    private fun findForward(grid: MatrixString, start: Point, direction: Direction): List<Point> {
        val forward = mutableListOf<Point>()
        var next = grid.getForward(start, direction)
        while (next != null && grid.get(next) == "#") {
            forward.add(next)
            next = grid.getForward(next, direction)
        }
        return forward
    }

    private fun getStartingPoint(grid: MatrixString): Pair<Point, Direction> {
        val start = (grid.find("^") + grid.find(">") + grid.find("<") + grid.find("v")).first()
        val direction = when (grid.get(start)) {
            "^" -> Direction.NORTH
            ">" -> Direction.EAST
            "<" -> Direction.WEST
            "v" -> Direction.SOUTH
            else -> throw Exception("Wrong direction found.")
        }
        return Pair(start, direction)
    }
}
