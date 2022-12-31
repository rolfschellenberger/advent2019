package com.rolf.day25

import com.rolf.Day
import com.rolf.util.Direction
import com.rolf.util.IntcodeState
import com.rolf.util.getCombinations
import com.rolf.util.readMemory

fun main() {
    Day25().run()
}

class Day25 : Day() {
    override fun solve1(lines: List<String>) {
        /*
        [cake] too light
        [coin] too light
        [hypercube] too light
        [pointer] too light
        [tambourine] too light
        [mug] too light
        [monolith] too light
        [mouse] too heavy
        [infinite loop] x
        [giant electromagnet] x
        [photons] x
        [escape pod] x
        [molten lava] x
         */
        val combinations = getCombinations(
            listOf(
                "cake",
                "coin",
                "hypercube",
                "pointer",
                "tambourine",
                "mug",
                "monolith"
            )
        )
        val results = mutableMapOf<List<String>, String>()
        for (combination in combinations) {
            println("Trying $combination...")
            val result = walkMaze(lines, combination)
            results[combination] = result
            if (!result.contains("lighter") && !result.contains("heavier")) {
                println(result)
                break
            }
        }
    }

    private fun walkMaze(lines: List<String>, pickUp: List<String>): String {
        val memory = readMemory(lines.first(), 5000)
        val intcodeState = IntcodeState(memory)
        val thread = Thread(intcodeState)
        thread.start()

        val commands = mutableListOf(
            "north",
            "south",
            "west",
            "west",
            "south",
            "north",
            "west",
            "south",
            "east",
            "south",
            "north",
            "east",
            "east",
            "west",
            "west",
            "west",
            "north",
            "east",
            "east",
            "east",
            "south",
            "east",
            "south",
            "south",
            "north",
            "north",
            "west",
            "south",
            "west",
            "north",
            "north",
            "north"
        )

        val pickedUp = mutableListOf<String>()

        var lastResponse = ""
        while (!intcodeState.isDone()) {
            Thread.sleep(10)
            if (intcodeState.isReading()) {
                val output = intcodeState.output.toList()
                intcodeState.output.clear()
                lastResponse = output.map { it.toInt().toChar() }.joinToString("")

                // Items
                if (lastResponse.contains("Items here:")) {
                    val itemString = lastResponse.substring(
                        lastResponse.lastIndexOf("Items here:") + 12,
                        lastResponse.lastIndexOf("Command?")
                    )
                    val list = itemString.replace("- ", "").split("\n").filterNot { it.isEmpty() }
                    for (item in list.filter { it in pickUp }) {
                        pickedUp.add(item)
                        intcodeState.input.addAll(toASCII("take $item"))
                    }
                }

                if (commands.isEmpty()) break
                val direction = Direction.valueOf(commands.removeFirst().uppercase())
                intcodeState.input.addAll(toASCII(direction.name.lowercase()))
            }
        }
        thread.interrupt()
        // Return when successfully passed the last point
        if (intcodeState.output.isNotEmpty()) {
            return intcodeState.output.toList().map { it.toInt().toChar() }.joinToString("")
        }
        return lastResponse
    }

    private fun toASCII(input: String): Collection<Long> {
        return (input + "\n").map { it.code.toLong() }
    }

    override fun solve2(lines: List<String>) {
    }
}
