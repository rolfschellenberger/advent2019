package com.rolf.day07

import com.rolf.Day
import com.rolf.util.IntcodeState
import com.rolf.util.getPermutations
import com.rolf.util.splitLine
import java.lang.Thread.sleep
import java.util.concurrent.Executors
import java.util.concurrent.LinkedBlockingQueue

fun main() {
    Day07().run()
}

class Day07 : Day() {
    override fun solve1(lines: List<String>) {
        return
        val memory = splitLine(lines.first(), ",").map { it.toInt() }
        var maxOutput = Int.MIN_VALUE

        fun onNextPermutation(combination: List<Int>) {
            val options = combination.toMutableList()
            val executor = Executors.newFixedThreadPool(combination.size)
            var input = LinkedBlockingQueue(listOf(options.removeFirst(), 0))
            for (i in combination.indices) {
                val output = if (options.isNotEmpty())
                    LinkedBlockingQueue(listOf(options.removeFirst()))
                else
                    LinkedBlockingQueue()
                executor.submit(IntcodeState(memory.toMutableList(), input, output))
                input = output
            }

            executor.shutdown()
            while (!executor.isTerminated) {
                sleep(100)
            }
            maxOutput = maxOf(maxOutput, input.take(input.size).last())
        }
        getPermutations(listOf(0, 1, 2, 3, 4), ::onNextPermutation)
        println(maxOutput)
    }

    override fun solve2(lines: List<String>) {
        val memory = splitLine(lines.first(), ",").map { it.toInt() }
        var maxOutput = Int.MIN_VALUE

        fun onNextPermutation(combination: List<Int>) {
            val options = combination.toMutableList()
            val executor = Executors.newFixedThreadPool(combination.size)

            // Create all input/output queues
            val inputs = mutableListOf<LinkedBlockingQueue<Int>>()
            for (i in combination.indices) {
                inputs.add(LinkedBlockingQueue(listOf(options.removeFirst())))
            }
            // Put 0 on the first input
            inputs.first().add(0)

            // Build the intcode states
            for (i in combination.indices) {
                val input = inputs[i]
                val output = inputs[((i + 1) % inputs.size)] // The output wraps around to the first input
                val intcodeState = IntcodeState(memory.toMutableList(), input, output)
                executor.submit(intcodeState)
            }

            executor.shutdown()
            while (!executor.isTerminated) {
                sleep(100)
            }
            maxOutput = maxOf(maxOutput, inputs.first().last())
        }
        getPermutations(listOf(5, 6, 7, 8, 9), ::onNextPermutation)
        println(maxOutput)
    }
}
