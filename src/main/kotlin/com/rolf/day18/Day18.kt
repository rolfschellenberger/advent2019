package com.rolf.day18

import com.rolf.Day

fun main() {
    Day18().run()
}

class Day18 : Day() {
    override fun solve1(lines: List<String>) {
        val maze = Maze.from(lines)
        println(maze.minimumSteps())
    }

    override fun solve2(lines: List<String>) {
        val maze = Maze.from(lines)
        maze.openSpaces.remove(Point2D(39, 39))
        maze.openSpaces.remove(Point2D(40, 39))
        maze.openSpaces.remove(Point2D(41, 39))
        maze.openSpaces.remove(Point2D(39, 40))
        maze.openSpaces.remove(Point2D(41, 40))
        maze.openSpaces.remove(Point2D(39, 41))
        maze.openSpaces.remove(Point2D(40, 41))
        maze.openSpaces.remove(Point2D(41, 41))

        maze.starts.remove(Point2D(40, 40))
        maze.starts.add(Point2D(39, 39))
        maze.starts.add(Point2D(41, 39))
        maze.starts.add(Point2D(39, 41))
        maze.starts.add(Point2D(41, 41))

        println(maze.minimumSteps())
    }
}

class Maze(
    val starts: MutableSet<Point2D>,
    private val keys: Map<Point2D, Char>,
    private val doors: Map<Point2D, Char>,
    val openSpaces: MutableSet<Point2D>
) {

    private fun findReachableKeys(from: Point2D, haveKeys: Set<Char> = mutableSetOf()): Map<Char, Pair<Point2D, Int>> {
        val queue = ArrayDeque<Point2D>().apply { add(from) }
        val distance = mutableMapOf(from to 0)
        val keyDistance = mutableMapOf<Char, Pair<Point2D, Int>>()
        while (queue.isNotEmpty()) {
            val next = queue.removeFirst()
            next.neighbors()
                .filter { it in openSpaces }
                .filterNot { it in distance }
                .forEach { point ->
                    distance[point] = distance[next]!! + 1
                    val door = doors[point]
                    val key = keys[point]
                    if (door == null || door.lowercaseChar() in haveKeys) {
                        if (key != null && key !in haveKeys) {
                            keyDistance[key] = Pair(point, distance[point]!!)
                        } else {
                            queue.add(point)
                        }
                    }
                }
        }
        return keyDistance
    }

    private fun findReachableFromPoints(
        from: Set<Point2D>,
        haveKeys: Set<Char>
    ): Map<Char, Triple<Point2D, Int, Point2D>> =
        from.map { point ->
            findReachableKeys(point, haveKeys).map { entry ->
                entry.key to Triple(entry.value.first, entry.value.second, point)
            }
        }.flatten().toMap()

    fun minimumSteps(
        from: Set<Point2D> = starts,
        haveKeys: Set<Char> = mutableSetOf(),
        seen: MutableMap<Pair<Set<Point2D>, Set<Char>>, Int> = mutableMapOf()
    ): Int {
        val state = Pair(from, haveKeys)

        if (state in seen) return seen.getValue(state)

        val answer = findReachableFromPoints(from, haveKeys).map { entry ->
            val (at, dist, cause) = entry.value
            dist + minimumSteps((from - cause) + at, haveKeys + entry.key, seen)
        }.minOrNull() ?: 0
        seen[state] = answer
        return answer
    }

    companion object {
        fun from(input: List<String>): Maze {
            val starts = mutableSetOf<Point2D>()
            val keys = mutableMapOf<Point2D, Char>()
            val doors = mutableMapOf<Point2D, Char>()
            val openSpaces = mutableSetOf<Point2D>()

            input.forEachIndexed { y, row ->
                row.forEachIndexed { x, c ->
                    val place = Point2D(x, y)
                    if (c == '@') starts += place
                    if (c != '#') openSpaces += place
                    if (c in ('a'..'z')) keys[place] = c
                    if (c in ('A'..'Z')) doors[place] = c
                }
            }
            return Maze(starts, keys, doors, openSpaces)
        }
    }
}

data class Point2D(val x: Int, val y: Int) {
    fun up(): Point2D = copy(y = y + 1)
    fun down(): Point2D = copy(y = y - 1)
    fun left(): Point2D = copy(x = x - 1)
    fun right(): Point2D = copy(x = x + 1)

    fun neighbors(): List<Point2D> =
        listOf(up(), down(), left(), right())
}
