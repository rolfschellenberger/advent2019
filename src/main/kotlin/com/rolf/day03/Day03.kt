package com.rolf.day03

import com.rolf.Day
import com.rolf.util.Point
import com.rolf.util.splitLine

fun main() {
    Day03().run()
}

class Day03 : Day() {
    override fun solve1(lines: List<String>) {
        val instructionsA = parseInstructions(lines[0])
        val instructionsB = parseInstructions(lines[1])

        val start = Point(0, 0)
        val locationsA = buildLocations(start, instructionsA)
        val locationsB = buildLocations(start, instructionsB)

        val minDistance = locationsA.toSet().intersect(locationsB.toSet())
            .filterNot { it == start }
            .map { it.distance(start) }
            .minOrNull()
        println(minDistance)
    }

    override fun solve2(lines: List<String>) {
        val instructionsA = parseInstructions(lines[0])
        val instructionsB = parseInstructions(lines[1])

        val start = Point(0, 0)
        val locationsA = buildLocations(start, instructionsA)
        val locationsB = buildLocations(start, instructionsB)

        val minIntersectionDistance = locationsA.toSet().intersect(locationsB.toSet())
            .filterNot { it == start }
            .map { locationsA.indexOf(it) + locationsB.indexOf(it) }
            .minOrNull()
        println(minIntersectionDistance)
    }

    private fun parseInstructions(line: String): List<Instruction> {
        return splitLine(line, ",").map { parseInstruction(it) }
    }

    private fun parseInstruction(input: String): Instruction {
        val direction = input.first()
        val steps = input.substring(1, input.length).toInt()
        return Instruction(direction, steps)
    }

    private fun buildLocations(start: Point, instructionsA: List<Instruction>): List<Point> {
        val result = mutableListOf(start)
        var current = start
        for (instruction in instructionsA) {
            val next = instruction.next(current)
            result.addAll(next)
            current = next.last()
        }
        return result
    }
}

data class Instruction(val direction: Char, val steps: Int) {
    fun next(current: Point): List<Point> {
        val result = mutableListOf<Point>()
        var next = current
        for (i in 0 until steps) {
            next = when (direction) {
                'L' -> Point(next.x - 1, next.y)
                'R' -> Point(next.x + 1, next.y)
                'U' -> Point(next.x, next.y - 1)
                'D' -> Point(next.x, next.y + 1)
                else -> throw Exception("Unknown direction $direction")
            }
            result.add(next)
        }
        return result
    }
}
