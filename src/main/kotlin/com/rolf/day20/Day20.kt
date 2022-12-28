package com.rolf.day20

import com.rolf.Day
import com.rolf.util.*

fun main() {
    Day20().run()
}

class Day20 : Day() {
    override fun solve1(lines: List<String>) {
        val grid = buildGrid(lines)
        val portals = findPortals(grid)

        // Build graph from each portal to each portal.
        val graph = Graph<Void>()
        buildGraph(grid, graph, portals)

        // Calculate the shortest path from AA to ZZ
        println(graph.shortestPathAndWeight("AA", "ZZ").second.toInt())
    }

    override fun solve2(lines: List<String>) {
        val grid = buildGrid(lines)
        val portals = findPortals(grid)

        // Build graph from each portal to each portal.
        val graph = Graph<Void>()

        // Build a graph with X levels
        // Every level, the nodes on the outer range, receive a suffix '-$level'
        // and the nodes on the inner range, receive a suffix of '-${level+1}'
        // While adding 1 level every iteration, check if there is a path from AA-0 to ZZ-0
        for (level in 0 until 100) {
            buildGraph(grid, graph, portals, level)
            val path = graph.shortestPathAndWeight("AA-0", "ZZ-0")
            if (path.first.isNotEmpty()) {
                println(path.second.toInt())
                break
            }
        }
    }

    private fun buildGrid(lines: List<String>): MatrixString {
        val width = lines.maxOf { it.length }
        val height = lines.size
        val grid = MatrixString.buildDefault(width, height, " ")
        val split = splitLines(lines)
        for (y in split.indices) {
            val line = split[y]
            for (x in line.indices) {
                grid.set(x, y, line[x])
            }
        }
        return grid
    }

    private fun findPortals(grid: MatrixString): List<Position> {
        // Check out all points. When a location has a letter and a neighbour contains also a letter and the
        // opposite location contains a '.', remember the '.' as a portal location.
        val portals = mutableListOf<List<Point>>()
        // Read portal names from left to right or top to bottom
        for (point in grid.allPoints()) {
            if (isPortal(grid, point)) {
                val left = grid.getLeft(point)
                val right = grid.getRight(point)
                if (isPortal(grid, left) && isOpen(grid, right)) {
                    portals.add(listOf(right!!, left!!, point))
                }
                if (isPortal(grid, right) && isOpen(grid, left)) {
                    portals.add(listOf(left!!, point, right!!))
                }
                val up = grid.getUp(point)
                val down = grid.getDown(point)
                if (isPortal(grid, up) && isOpen(grid, down)) {
                    portals.add(listOf(down!!, up!!, point))
                }
                if (isPortal(grid, down) && isOpen(grid, up)) {
                    portals.add(listOf(up!!, point, down!!))
                }
            }
        }
        return portals.map {
            val name = grid.get(it[1]) + grid.get(it[2])
            val point = it[0]
            // Outer when <= 5 pixels from the edge
            val maxDistance = 5
            val xRange = maxDistance..(grid.width() - maxDistance)
            val yRange = maxDistance..(grid.height() - maxDistance)
            val location = if (point.x in xRange && point.y in yRange) Location.INNER else Location.OUTER
            Position(name, point, location)
        }
    }

    private fun isPortal(grid: MatrixString, point: Point?): Boolean {
        return point != null && grid.get(point).first() in 'A'..'Z'
    }

    private fun isOpen(grid: MatrixString, point: Point?): Boolean {
        return point != null && grid.get(point) == "."
    }

    private val paths = mutableMapOf<Pair<Position, Position>, Int>()

    private fun buildGraph(
        grid: MatrixString,
        graph: Graph<Void>,
        portals: List<Position>,
        level: Int = -1
    ): Graph<Void> {
        // Build graph from each portal to each portal.
        portals.forEach { graph.addVertex(Vertex(it.getName(level), weight = 1.0)) }
        if (level >= 0) {
            graph.addVertex(Vertex("AA-$level", weight = 0.0))
            graph.addVertex(Vertex("ZZ-$level", weight = 0.0))
        } else {
            graph.addVertex(Vertex("AA", weight = 0.0))
            graph.addVertex(Vertex("ZZ", weight = 0.0))
        }
        val permutations = getPermutations(portals, 2)
        val notAllowedValues = ('A'..'Z').map { it.toString() }.toSet() + "#"

        // Calculate all paths in the maze possible
        for (permutation in permutations) {
            // Add every edge
            val from = permutation[0]
            val to = permutation[1]

            if (!paths.containsKey(from to to)) {
                val path = grid.findPathByValue(from.point, to.point, notAllowedValues)
                paths[from to to] = path.size
                paths[to to from] = path.size
            }
        }

        // Add possible paths (distance > 0)
        for ((path, distance) in paths.filter { it.value > 0 }) {
            val from = path.first
            val to = path.second
            graph.addEdge(from.getName(level), to.getName(level), weight = distance.toDouble())
        }
        return graph
    }
}

data class Position(val name: String, val point: Point, val location: Location) {

    fun getName(level: Int): String {
        // No level, default name
        if (level < 0) return name

        // Inner locations go 1 level up
        val l = when (location) {
            Location.INNER -> level + 1
            Location.OUTER -> level
        }
        return "$name-$l"
    }
}

enum class Location {
    INNER, OUTER
}
