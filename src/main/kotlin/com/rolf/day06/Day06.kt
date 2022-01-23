package com.rolf.day06

import com.rolf.Day
import com.rolf.util.EdgeType
import com.rolf.util.Graph
import com.rolf.util.Vertex
import com.rolf.util.splitLines

fun main() {
    Day06().run()
}

class Day06 : Day() {
    override fun solve1(lines: List<String>) {
        val pairs = splitLines(lines, ")").map { it[0] to it[1] }
        val graph = Graph<Int>()
        for (pair in pairs) {
            graph.addVertex(Vertex(pair.first))
            graph.addVertex(Vertex(pair.second))
            graph.addEdge(pair.first, pair.second, EdgeType.DIRECTED)
        }

        val root = graph.getRootVertex()!!
        val depth = getDepth(graph, root.id, 0)
        println(depth)
    }

    override fun solve2(lines: List<String>) {
        val pairs = splitLines(lines, ")").map { it[0] to it[1] }
        val graph = Graph<Int>()
        for (pair in pairs) {
            graph.addVertex(Vertex(pair.first))
            graph.addVertex(Vertex(pair.second))
            graph.addEdge(pair.first, pair.second, EdgeType.UNDIRECTED)
        }

        val pathWeight = graph.shortestPathAndWeight("YOU", "SAN")
        println(pathWeight.second.toInt() - 2)
    }

    private fun getDepth(graph: Graph<Int>, node: String, depth: Int): Int {
        return depth + graph.edges(node).map { getDepth(graph, it.destination, depth + 1) }.sum()
    }
}
