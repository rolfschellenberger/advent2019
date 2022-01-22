package com.rolf.day04

import com.rolf.Day
import com.rolf.util.getCharacterCounts

fun main() {
    Day04().run()
}

class Day04 : Day() {
    override fun solve1(lines: List<String>) {
        val (from, to) = lines.first().split("-").map { it.toInt() }
        val options = (from..to)
            .filter { adjacentNumbers(it) }
            .filter { increases(it) }
            .count()
        println(options)
    }

    override fun solve2(lines: List<String>) {
        val (from, to) = lines.first().split("-").map { it.toInt() }
        val options = (from..to)
            .filter { adjacentNumbers2(it) }
            .filter { increases(it) }
            .count()
        println(options)
    }

    private fun adjacentNumbers(number: Int): Boolean {
        return getCharacterCounts(number.toString())
            .filter { it.key >= 2 }
            .count() > 0
    }

    private fun increases(number: Int): Boolean {
        return number.toString() == number.toString().toCharArray().sorted().joinToString("")
    }

    private fun adjacentNumbers2(number: Int): Boolean {
        return getCharacterCounts(number.toString())
            .filter { it.key == 2 }
            .count() > 0
    }
}
