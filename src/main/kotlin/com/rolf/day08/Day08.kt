package com.rolf.day08

import com.rolf.Day
import com.rolf.util.MatrixString

fun main() {
    Day08().run()
}

class Day08 : Day() {
    override fun solve1(lines: List<String>) {
        val input = lines.first()
            .chunked(25)
            .chunked(6)
            .map {
                it.map { string ->
                    string.chunked(1)
                        .map { value -> value.toInt() }
                }
            }
        val digitCountPerLayer = input.map { layer ->
            layer.flatten()
                .groupingBy { it }.eachCount()
        }

        val minZeroCount = digitCountPerLayer.filter { it.containsKey(0) }.minByOrNull { it[0]!! }!!
        println(minZeroCount[1]!! * minZeroCount[2]!!)
    }

    override fun solve2(lines: List<String>) {
        val layers = lines.first()
            .chunked(25)
            .chunked(6)
            .map {
                it.map { string ->
                    string.chunked(1)
                }
            }

        val grid = MatrixString.build(layers.first())
        for (layer in layers) {
            for (y in layer.indices) {
                val row = layer[y]
                for (x in row.indices) {
                    val value = grid.get(x, y)
                    // Update transparent pixels with the next layer
                    if (value == "2") {
                        grid.set(x, y, row[x])
                    }
                }
            }
        }
        grid.replace(
            mapOf(
                "1" to "#",
                "0" to " "
            )
        )
        println(grid)
    }
}
