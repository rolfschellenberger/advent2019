package com.rolf.day14

import com.rolf.Day

fun main() {
    Day14().run()
}

class Day14 : Day() {
    override fun solve1(lines: List<String>) {
        val reactions = lines.map { parseReaction(it) }
//        reactions.forEach { println(it) }
        val outputMap = reactions.associateBy { it.output.name }

        val usedMaterials = mutableMapOf<String, Int>()
        produceMaterial(outputMap, "FUEL", 1, usedMaterials)
        println(usedMaterials.getValue("ORE"))
    }

    private fun produceMaterial(
        outputMap: Map<String, Reaction>,
        material: String,
        // TODO: how much to produce?
        amount: Int,
        usedMaterials: MutableMap<String, Int>,
        availableMaterials: MutableMap<String, Int> = mutableMapOf()
    ) {
        // When we want to produce some material, see if we have enough of this material to do so.
        val available = availableMaterials.computeIfAbsent(material) { 0 }
        if (available >= amount) {
            availableMaterials[material] = available - amount
            return
        }

        // When ORE is required, we can just produce it
        if (material == "ORE") {
            val used = usedMaterials.computeIfAbsent("ORE") { 0 }
            usedMaterials["ORE"] = used + amount
            // We don't need to add it to the availableMaterials, since it will be used directly
//            availableMaterials["ORE"] = available + amount
            return
        }

        // If the there isn't enough available, we need to produce it.
        val reaction = outputMap.getValue(material)
        // We need to register the production of each material.
            for (input in reaction.input) {
                produceMaterial(outputMap, input, usedMaterials, availableMaterials)
            }
            // When all input is produced,
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

data class Reaction(val output: Material, val input: List<Material>) {
    fun fromOre(): Boolean {
        return input.count { it.name == "ORE" } > 0
    }
}
