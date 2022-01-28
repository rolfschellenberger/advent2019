package com.rolf.day16

import com.rolf.Day
import com.rolf.util.lastDigit
import com.rolf.util.pushLeft
import com.rolf.util.splitLine
import kotlin.math.absoluteValue

fun main() {
    Day16().run()
}

class Day16 : Day() {
    override fun solve1(lines: List<String>) {
        val input = splitLine(lines.first()).map { it.toInt() }.toIntArray()

        var step = input.toList().toIntArray()
        for (i in 0 until 100) {
            step = step(step)
        }
        println(step.take(8).joinToString(""))
    }

    override fun solve2(lines: List<String>) {
        val input = lines.first()
        val offset = input.take(7).toInt()
        val signal: IntArray = input.map { Character.getNumericValue(it) }.toIntArray()
        val stretchedInput = (offset until 10_000 * input.length).map { signal[it % input.length] }.toIntArray()
        repeat(100) {
            stretchedInput.indices.reversed().fold(0) { carry, idx ->
                (stretchedInput[idx] + carry).lastDigit().also { stretchedInput[idx] = it }
            }
        }
        println(stretchedInput.take(8).joinToString(""))
    }

    private fun step(input: IntArray): IntArray {
        val newValues = IntArray(input.size) { 0 }
        for (numberIndex in input.indices) {
            val pattern = getPattern(numberIndex)
            var sum = 0
            for ((index, value) in input.withIndex()) {
                sum += value * pattern[index % pattern.size]
            }
            newValues[numberIndex] = sum.absoluteValue % 10
        }
        return newValues
    }

    private fun getPattern(numberIndex: Int): IntArray {
        val basePattern = listOf(0, 1, 0, -1).toIntArray()
        val pattern = IntArray(basePattern.size * (numberIndex + 1)) { 0 }
        for (n in basePattern.indices) {
            // Repeat based on the i-th number
            for (j in 0..numberIndex) {
                pattern[numberIndex * n + n + j] = basePattern[n]
            }
        }
        pattern.pushLeft(1)
        return pattern
    }
}
