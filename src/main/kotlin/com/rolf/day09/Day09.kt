package com.rolf.day09

import com.rolf.Day
import com.rolf.util.IntcodeState
import com.rolf.util.readMemory
import java.util.concurrent.LinkedBlockingQueue

fun main() {
    Day09().run()
}

class Day09 : Day() {
    override fun solve1(lines: List<String>) {
        val memory = readMemory(lines.first(), 5000)
        val intcodeState = IntcodeState(memory, LinkedBlockingQueue(listOf(1L)))
        intcodeState.execute()
        println(intcodeState.output.first())
    }

    override fun solve2(lines: List<String>) {
        val memory = readMemory(lines.first(), 5000)
        val intcodeState = IntcodeState(memory, LinkedBlockingQueue(listOf(2L)))
        intcodeState.execute()
        println(intcodeState.output.first())
    }
}
