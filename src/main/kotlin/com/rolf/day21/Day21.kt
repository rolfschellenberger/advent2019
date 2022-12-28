package com.rolf.day21

import com.rolf.Day
import com.rolf.util.IntcodeState
import com.rolf.util.readMemory

fun main() {
    Day21().run()
}

class Day21 : Day() {
    override fun solve1(lines: List<String>) {
        runRobot(
            lines, listOf(
                "NOT C J",
                "AND D J",
                "NOT A T",
                "OR T J",
                "WALK"
            )
        )
    }

    override fun solve2(lines: List<String>) {
        runRobot(
            lines, listOf(
                "NOT C J",
                "AND D J",
                "AND H J",
                "NOT B T",
                "AND D T",
                "OR T J",
                "NOT A T",
                "OR T J",
                "RUN"
            )
        )
    }

    private fun runRobot(lines: List<String>, instructions: List<String>) {
        val memory = readMemory(lines.first(), 5000)
        val intcodeState = IntcodeState(memory)

        for (line in instructions) {
            intcodeState.input.addAll(toASCII(line))
        }
        intcodeState.run()
        val output = intcodeState.output.toList()
        println(output.map { it.toInt().toChar() }.joinToString(""))
        println(output.last())
    }

    private fun toASCII(input: String): Collection<Long> {
        return (input + "\n").map { it.code.toLong() }
    }
}
