package com.rolf.day02

import com.rolf.Day
import com.rolf.util.splitLine

fun main() {
    Day02().run()
}

class Day02 : Day() {
    override fun solve1(lines: List<String>) {
        val data = splitLine(lines.first(), ",").map { it.toInt() }.toMutableList()
        data[1] = 12
        data[2] = 2
        val intCode = IntCode(data.toIntArray())
        intCode.execute()
        println(intCode.data[0])
    }

    override fun solve2(lines: List<String>) {
        val answer = 19690720
        while (true) {
            val data = splitLine(lines.first(), ",").map { it.toInt() }.toMutableList()
            for (i in 0 until 100) {
                for (j in 0 until 100) {
                    data[1] = i
                    data[2] = j
                    val intCode = IntCode(data.toIntArray())
                    intCode.execute()
                    if (intCode.data[0] == answer) {
                        println(100 * i + j)
                        return
                    }
                }
            }
        }
    }
}

class IntCode(val data: IntArray) {
    var pointer = 0
    var stop = false

    fun isDone(): Boolean {
        return pointer >= data.size || stop
    }

    fun execute() {
        while (!isDone()) {
            executeNext()
        }
    }

    fun executeNext() {
        when (data[pointer]) {
            1 -> {
                data[data[pointer + 3]] = data[data[pointer + 1]] + data[data[pointer + 2]]
                pointer += 4
            }
            2 -> {
                data[data[pointer + 3]] = data[data[pointer + 1]] * data[data[pointer + 2]]
                pointer += 4
            }
            99 -> stop = true
        }
    }
}
