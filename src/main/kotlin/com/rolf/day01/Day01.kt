package com.rolf.day01

import com.rolf.Day

fun main() {
    Day01().run()
}

class Day01 : Day() {
    override fun solve1(lines: List<String>) {
        val modules = lines.map { it.toInt() }
        println(modules.map { calculateFuel(it) }.sum())
    }

    override fun solve2(lines: List<String>) {
        val modules = lines.map { it.toInt() }
        println(modules.map { calculateFuel2(it) }.sum())
    }

    private fun calculateFuel(mass: Int): Int {
        return maxOf(0, mass / 3 - 2)
    }

    private fun calculateFuel2(mass: Int): Int {
        var sum = 0
        var fuel = calculateFuel(mass)
        while (fuel > 0) {
            sum += fuel
            fuel = calculateFuel(fuel)
        }
        return sum
    }
}
