package com.rolf.day12

import com.rolf.Day
import com.rolf.util.Location
import kotlin.math.absoluteValue

fun main() {
    Day12().run()
}

class Day12 : Day() {
    override fun solve1(lines: List<String>) {
        val moons = lines.map { parseMoon(it) }

        for (i in 0 until 1000) {
            moons.forEach {
                it.updateVelocity(moons)
            }
            moons.forEach {
                it.updatePosition()
            }
        }
        println(moons.sumOf { it.energy() })
    }

    override fun solve2(lines: List<String>) {
        val moons = lines.map { parseMoon(it) }

        val cache = mutableSetOf<Moon>(moons.first())
        for (i in 0 until 10000) {
            moons.forEach {
                it.updateVelocity(moons)
            }
            moons.forEach {
                it.updatePosition()
            }
            if (!cache.add(moons.first())) {
                println("Found duplicate: $i = ${moons.first()}")
            }
        }
    }

    private fun parseMoon(line: String): Moon {
        val (x, y, z) = line
            .removePrefix("<")
            .removeSuffix(">")
            .split(", ")
            .map { it.split("=").last() }
            .map { it.toInt() }
        return Moon(Location(x, y, z))
    }
}

data class Moon(var position: Location, var velocity: Location = Location(0, 0, 0)) {

    fun energy(): Int {
        return potentialEnergy() * kineticEnergy()
    }

    private fun potentialEnergy(): Int {
        return position.x.absoluteValue + position.y.absoluteValue + position.z.absoluteValue
    }

    private fun kineticEnergy(): Int {
        return velocity.x.absoluteValue + velocity.y.absoluteValue + velocity.z.absoluteValue
    }

    fun updateVelocity(others: List<Moon>) {
        var xDiff = 0
        var yDiff = 0
        var zDiff = 0
        for (moon in others) {
            xDiff += moon.position.x.compareTo(position.x)
            yDiff += moon.position.y.compareTo(position.y)
            zDiff += moon.position.z.compareTo(position.z)
        }
        velocity = Location(
            velocity.x + xDiff,
            velocity.y + yDiff,
            velocity.z + zDiff
        )
    }

    fun updatePosition() {
        position = Location(
            position.x + velocity.x,
            position.y + velocity.y,
            position.z + velocity.z
        )
    }
}
