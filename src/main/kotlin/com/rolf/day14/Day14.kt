package com.rolf.day14

import com.rolf.Day
import kotlin.math.ceil

fun main() {
    Day14().run()
}

class Day14 : Day() {
    override fun solve1(lines: List<String>) {
        val reactions = lines.map { parseReaction(it) }
        val outputMap = reactions.associateBy { it.output.name }

        val usedMaterials = mutableMapOf<String, Long>()
        produceMaterial(outputMap, "FUEL", 1, usedMaterials)
        println(usedMaterials.getValue("ORE"))
    }

    override fun solve2(lines: List<String>) {
        val reactions = lines.map { parseReaction(it) }
        val outputMap = reactions.associateBy { it.output.name }

        var fuelAmount = 0
        for (i in 1180000 until 10000000) {
            val usedMaterials = mutableMapOf<String, Long>()
            produceMaterial(outputMap, "FUEL", i.toLong(), usedMaterials)
            val ore = usedMaterials.getValue("ORE")
            if (ore > 1000000000000) {
                break
            }
            fuelAmount = i
        }
        println(fuelAmount)
    }

    private fun produceMaterial(
        outputMap: Map<String, Reaction>,
        material: String,
        amount: Long,
        usedMaterials: MutableMap<String, Long>,
        availableMaterials: MutableMap<String, Long> = mutableMapOf()
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
            return
        }

        // If the there isn't enough available, we need to produce it.
        val reaction = outputMap.getValue(material)
        // Round up, since we cannot run partial reactions
        val toProduce = amount - available
        val factor = ceil(toProduce.toDouble() / reaction.output.amount).toLong()
        val productionAmount = factor * reaction.output.amount
        for (input in reaction.input) {
            produceMaterial(outputMap, input.name, factor * input.amount, usedMaterials, availableMaterials)
        }
        // We need to register the production of this material.
        usedMaterials[material] = usedMaterials.computeIfAbsent(material) { 0 } + productionAmount
        // When all input is produced, log what is left after using the amount
        val availableNow = availableMaterials.computeIfAbsent(material) { 0 }
        availableMaterials[material] = availableNow + (productionAmount - amount)
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
}

data class Material(val name: String, val amount: Int)

data class Reaction(val output: Material, val input: List<Material>)
