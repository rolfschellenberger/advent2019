package com.rolf.day18

import com.rolf.Day
import com.rolf.util.*

fun main() {
    Day18().run()
}

typealias Position = Pair<String, Point>

class Day18 : Day() {
    override fun solve1(lines: List<String>) {
//        val grid = MatrixString.build(splitLines(lines))
//        println(grid)
        // Make a graph from each location to each location
        // b.A.@.a ->
        // @ - a
        //  \
        //   A - b

        // ########################
        // #f.D.E.e.C.b.A.@.a.B.c.#
        // ######################.#
        // #d.....................#
        // ########################
        // @ > a > b > c > d > e > f
        // @ > a > b > c > e > d > f

        // #################
        // #i.G..c...e..H.p#
        // ########.########
        // #j.A..b...f..D.o#
        // ########@########
        // #k.E..a...g..B.n#
        // ########.########
        // #l.F..d...h..C.m#
        // #################
        // @ - c - G - i
        //   - e - H - p
        //   - b - A - j
        //   - f - D - o
        //   - a - E - k
        //   - g - B - n
        //   - d - F - l
        //   - h - C - m
        // @ -

        // Make graph with all keys + starting position being the vertices
        // Remove the starting position from the map and set current position to starting position (@)

        // 1. Find all keys on the map
        //    When there are no more keys on the map, stop the iteration
        // 2. See what keys are reachable from the current position
        //    When there are no more keys reachable, stop the iteration
        // 3. For each key reachable, remove the key + door and move the position
        // 4. Add the vertex from current position to key position with the distance as the weight
        // 5. Go to step 1

        // Find the shortest path from the starting position through all edges

        // Make graph with all keys + starting position being the vertices
        val graph = Graph<String>()
        val grid = MatrixString.build(splitLines(lines))
        val start = grid.find("@").first()
        val keys = getKeysOnGrid(grid)
        keys.map { it.first }
            .forEach { graph.addVertex(Vertex(it)) }
        graph.addVertex(Vertex("@"))
        println(graph.vertices())
        println(grid)

        travel(graph, start, grid.copy())

        // Find the shortest path from the starting position through all edges
        println(graph.vertices())
        println(graph.edges())
        val result = graph.lowestPathAndWeightVisitAll("@")
        println(result)
        println(result.second.toInt())
    }

    private fun travel(graph: Graph<String>, position: Point, grid: MatrixString) {
        // Remove the position and the door
        val value = grid.get(position)
        grid.replace(
            mapOf(
                value to ".",
                value.uppercase() to "."
            )
        )
        println(grid)

        // 1. Find all remaining keys on the map
        //    When there are no more keys on the map, stop the iteration
        val keys = getKeysOnGrid(grid)
        val notAllowedValues = keys.map { it.first.uppercase() }.toSet() + "#"
        if (keys.isNotEmpty()) {

            // 2. See what keys are reachable from the current position
            //    When there are no more keys reachable, stop the iteration
            for (key in keys) {
                val paths = grid.findPathsByValue(position, setOf(key.second), notAllowedValues, diagonal = false)
                println(key)
                println(paths)
                // 3. For each key reachable, remove the key + door and move the position
                if (paths.isNotEmpty()) {
                    // 4. Add the edge from current position to key position with the distance as the weight
                    graph.addEdge(value, key.first, EdgeType.DIRECTED, paths.minOf { it.size }.toDouble())
                    // 5. Go to step 1
                    travel(graph, key.second, grid.copy())
                }
            }
        }
    }

    private fun getKeysOnGrid(grid: MatrixString): List<Position> {
        return grid.allPoints()
            .map { Position(grid.get(it), it) }
            .filter { it.first.first() in 'a'..'z' }
    }

    private fun getDoorsOnGrid(grid: MatrixString): List<Position> {
        return grid.allPoints()
            .map { Position(grid.get(it), it) }
            .filter { it.first.first() in 'A'..'Z' }
    }

    override fun solve2(lines: List<String>) {
    }
}
