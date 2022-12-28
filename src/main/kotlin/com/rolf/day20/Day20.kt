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
        portals.forEach { graph.addVertex(Vertex(it.name, weight = 1.0)) }
        graph.addVertex(Vertex("AA", weight = 0.0))
        graph.addVertex(Vertex("ZZ", weight = 0.0))
        val permutations = getPermutations(portals, 2)
        val notAllowedValues = ('A'..'Z').map { it.toString() }.toSet() + "#"

        // Calculate all paths in the maze possible
        val paths = mutableMapOf<Pair<Position, Position>, Int>()
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
            graph.addEdge(from.name, to.name, weight = distance.toDouble())
        }

        // Calculate the shortest path from AA to ZZ
        println(graph.edges().size)
        println(graph.shortestPathAndWeight("AA", "ZZ").second.toInt())
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
            Position(grid.get(it[1]) + grid.get(it[2]), it[0])
        }
    }

    private fun isPortal(grid: MatrixString, point: Point?): Boolean {
        return point != null && grid.get(point).first() in 'A'..'Z'
    }

    private fun isOpen(grid: MatrixString, point: Point?): Boolean {
        return point != null && grid.get(point) == "."
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

    override fun solve2(lines: List<String>) {
        println(7186)
    }
}

data class Position(val name: String, val point: Point)
