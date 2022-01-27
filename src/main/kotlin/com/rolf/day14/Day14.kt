package com.rolf.day14

import com.rolf.Day
import kotlin.math.ceil

fun main() {
    Day14().run()
}

class Day14 : Day() {
    override fun solve1(lines: List<String>) {
        val reactions = lines.map { parseReaction(it) }
        reactions.forEach { println(it) }
        val outputMap = reactions.associateBy { it.output.name }
        val fuel = outputMap.getValue("FUEL")

        val requirements: MutableMap<Material, Int> = mutableMapOf()


        val oreRequirement: MutableMap<Material, Double> = mutableMapOf()
        findORE(outputMap, fuel, fuel.output.amount.toDouble(), oreRequirement)
        println(oreRequirement)

        var sum = 0.0
        for ((output, amount) in oreRequirement) {
            val ore = outputMap.getValue(output.name)
            sum += ceil(amount / ore.output.amount) * ore.output.amount
        }
        println(sum)
    }

    private fun findORE(
        outputMap: Map<String, Reaction>,
        reaction: Reaction,
        amount: Double,
        oreRequirement: MutableMap<Material, Double>
    ) {
        for (input in reaction.input) {
            if (outputMap.containsKey(input.name)) {
                val factor = input.amount / amount
                findORE(outputMap, outputMap.getValue(input.name), factor, oreRequirement)
            } else {
                // We ended up with ORE and should log the amount
                val value = oreRequirement.computeIfAbsent(reaction.output) { 0.0 } + amount
                oreRequirement[reaction.output] = value
            }
        }
    }

    private fun parseReaction(line: String): Reaction {
        val (from, to) = line.split(" => ")
        val input = from.split(", ").map { parseMaterial(it) }
        val output = parseMaterial(to)
        return Reaction(output, input)
    }

    private fun parseMaterial(line: String): Material {
        val (amount, name) = line.split(" ")
        return Material(name, amount.toInt())
    }

    override fun solve2(lines: List<String>) {
    }
}

data class Material(val name: String, val amount: Int)

data class Reaction(val output: Material, val input: List<Material>)
