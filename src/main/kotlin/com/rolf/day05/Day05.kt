package com.rolf.day05

import com.rolf.Day
import com.rolf.util.IntcodeState
import com.rolf.util.splitLine

fun main() {
    Day05().run()
}

class Day05 : Day() {
    override fun solve1(lines: List<String>) {
        val memory = splitLine(lines.first(), ",").map { it.toInt() }.toMutableList()
        val state = IntcodeState(memory, mutableListOf(1))
        state.execute()
        println(state.output.last())
    }

    override fun solve2(lines: List<String>) {
        val memory = splitLine(lines.first(), ",").map { it.toInt() }.toMutableList()
        val state = IntcodeState(memory, mutableListOf(5))
        state.execute()
        println(state.output.last())
    }
}
