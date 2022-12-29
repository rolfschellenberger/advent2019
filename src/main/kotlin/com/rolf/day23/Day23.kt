package com.rolf.day23

import com.rolf.Day
import com.rolf.util.IntcodeState
import com.rolf.util.readMemory

fun main() {
    Day23().run()
}

class Day23 : Day() {
    override fun solve1(lines: List<String>) {
        val (computers, threads) = parse(lines)

        while (!isDone(computers.values)) {
            for ((_, computer) in computers) {
                if (computer.output.peek() != null) {
                    val destination = computer.output.take().toInt()
                    val x = computer.output.take()
                    val y = computer.output.take()
                    if (destination == 255) {
                        println(y)
                        interrupt(threads)
                        return
                    }
                    computers[destination]?.input?.put(x)
                    computers[destination]?.input?.put(y)
                }
            }
        }
    }

    override fun solve2(lines: List<String>) {
        val (computers, threads) = parse(lines)
        var nat = 0L to 0L
        var lastDelivered = 0L to 0L

        while (!isDone(computers.values)) {
            var idle = true
            Thread.sleep(10)
            for ((_, computer) in computers) {
                if (computer.output.peek() != null) {
                    idle = false
                    val destination = computer.output.take().toInt()
                    val x = computer.output.take()
                    val y = computer.output.take()
                    if (destination == 255) {
                        nat = x to y
                    }
                    computers[destination]?.input?.put(x)
                    computers[destination]?.input?.put(y)
                }
            }
            if (idle) {
                if (lastDelivered.second == nat.second) {
                    println(nat.second)
                    interrupt(threads)
                    return
                }
                computers[0]?.input?.put(nat.first)
                computers[0]?.input?.put(nat.second)
                lastDelivered = nat
            }
        }
        // 17599 too high
        // 1754 too low
        // 16424?
    }

    private fun parse(lines: List<String>): Pair<MutableMap<Int, IntcodeState>, MutableMap<Int, Thread>> {
        val computers = mutableMapOf<Int, IntcodeState>()
        val threads = mutableMapOf<Int, Thread>()
        for (id in 0 until 50) {
            val memory = readMemory(lines.first(), 5000)
            val intcodeState = IntcodeState(memory)
            val thread = Thread(intcodeState)
            computers[id] = intcodeState
            threads[id] = thread

            intcodeState.input.add(id.toLong())
            intcodeState.input.add(-1)
            thread.start()
        }
        return Pair(computers, threads)
    }

    private fun isDone(computers: MutableCollection<IntcodeState>): Boolean {
        return computers.all { it.isDone() }
    }

    private fun interrupt(threads: MutableMap<Int, Thread>) {
        threads.values.forEach { it.interrupt() }
    }
}
