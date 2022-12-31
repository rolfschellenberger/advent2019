package com.rolf.day25

import com.rolf.Day
import com.rolf.util.*

fun main() {
    Day25().run()
}

class Day25 : Day() {
    override fun solve1(lines: List<String>) {
        val result = walkMaze(lines)
        println(result)
        return

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
//        for (command in commands) {
//            intcodeState.input.addAll(toASCII(command))
//        }

        /*
        Point(x=4, y=5)=[cake] too light
        Point(x=5, y=6)=[coin] too light
        Point(x=6, y=8)=[hypercube] too light
        Point(x=3, y=5)=[pointer] too light
        Point(x=2, y=6)=[tambourine] too light
        Point(x=5, y=6)=[mug] too light
        Point(x=3, y=6)=[monolith] too light
        Point(x=6, y=6)=[mouse] too heavy
        Point(x=4, y=7)=[infinite loop] x
        Point(x=4, y=6)=[giant electromagnet] x
        Point(x=3, y=6)=[photons] x
        Point(x=2, y=5)=[escape pod] x
        Point(x=3, y=7)=[molten lava] x
         */
        val pickUp = listOf(
            "coin",
            "mug",

            "cake",
//            "hypercube",
            "pointer",
            "tambourine",
            "monolith"
        )

        val grid = MatrixString.buildDefault(10, 10, " ")
        var location = grid.center()
        var previousLocation = location
        val items = mutableMapOf<Point, List<String>>()
        val pickedUp = mutableListOf<String>()

        while (!intcodeState.isDone()) {
            Thread.sleep(100)
            if (intcodeState.isReading()) {
                val output = intcodeState.output.toList()
                intcodeState.output.clear()
                val response = output.map { it.toInt().toChar() }.joinToString("")
                println(response)
                val directions = mutableListOf<Direction>()
                if (response.contains("- north")) directions.add(Direction.NORTH)
                if (response.contains("- east")) directions.add(Direction.EAST)
                if (response.contains("- south")) directions.add(Direction.SOUTH)
                if (response.contains("- west")) directions.add(Direction.WEST)
                val back = response.contains("you are ejected back to the checkpoint")

                // Items
                if (response.contains("Items here:")) {
                    val itemString = response.substring(
                        response.lastIndexOf("Items here:") + 12,
                        response.lastIndexOf("Command?")
                    )
                    val list = itemString.replace("- ", "").split("\n").filterNot { it.isEmpty() }
                    items[location] = list
                    for (item in list.filter { it in pickUp }) {
                        pickedUp.add(item)
                        intcodeState.input.addAll(toASCII("take $item"))
                    }
                }

                // Where did we already go?
                val options = directions.filter {
                    grid.get(grid.getForward(location, it)!!) == " "
                }
//                println(directions)
//                println(options)
                println(items)
                println(pickedUp)
                if (pickUp.size == pickedUp.size && response.contains("Pressure-Sensitive Floor")) {
                    break
                }

                if (back) {
                    location = previousLocation
                }
                var direction = if (back || options.isEmpty()) directions.random() else options.random()
                direction = Direction.valueOf(commands.removeFirst().uppercase())
                println("Moving $direction")
                previousLocation = location
                location = grid.getForward(location, direction)!!
                grid.set(location, ".")
                printGrid(grid, location)
//                items.forEach { println(it) }

                intcodeState.input.addAll(toASCII(direction.name.lowercase()))
//                intcodeState.input.addAll(toASCII(readln()))
            }
        }

        grid.set(grid.center(), "@")
        println(grid)

        thread.interrupt()
    }

    private fun walkMaze(lines: List<String>) : String {
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
        val pickUp = listOf(
            "coin",
            "mug",

            "cake",
//            "hypercube",
            "pointer",
            "tambourine",
            "monolith"
        )

        val items = mutableListOf<String>()
        val pickedUp = mutableListOf<String>()

        var lastResponse = ""
        while (!intcodeState.isDone()) {
            Thread.sleep(100)
            if (intcodeState.isReading()) {
                val output = intcodeState.output.toList()
                intcodeState.output.clear()
                lastResponse = output.map { it.toInt().toChar() }.joinToString("")
//                println(response)

                // Items
                if (lastResponse.contains("Items here:")) {
                    val itemString = lastResponse.substring(
                        lastResponse.lastIndexOf("Items here:") + 12,
                        lastResponse.lastIndexOf("Command?")
                    )
                    val list = itemString.replace("- ", "").split("\n").filterNot { it.isEmpty() }
                    items.addAll(list)
                    for (item in list.filter { it in pickUp }) {
                        pickedUp.add(item)
                        intcodeState.input.addAll(toASCII("take $item"))
                    }
                }
//                println(pickedUp)

                if (commands.isEmpty()) break
                val direction = Direction.valueOf(commands.removeFirst().uppercase())
//                println("Moving $direction")
                intcodeState.input.addAll(toASCII(direction.name.lowercase()))
            }
        }
        thread.interrupt()
        return lastResponse
    }

    private fun printGrid(grid: MatrixString, location: Point) {
        val copy = grid.copy()
        copy.set(location, "@")
        copy.set(copy.center(), "S")
        println(copy)
    }

    private fun toASCII(input: String): Collection<Long> {
        return (input + "\n").map { it.code.toLong() }
    }

    override fun solve2(lines: List<String>) {
    }
}
