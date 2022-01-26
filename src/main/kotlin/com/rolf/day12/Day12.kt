package com.rolf.day12

import com.rolf.Day
import com.rolf.util.Location
import com.rolf.util.leastCommonMultiple
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

        // Find the cycle of x, y and z to find the least common multiple for the final step where they 'align' for
        // the first time.
        val firstX: List<Pair<Int, Int>> = moons.map { it.position.x to it.velocity.x }
        val firstY: List<Pair<Int, Int>> = moons.map { it.position.y to it.velocity.y }
        val firstZ: List<Pair<Int, Int>> = moons.map { it.position.z to it.velocity.z }
        var stepX = 0
        var stepY = 0
        var stepZ = 0
        var step = 0
        while (stepX == 0 || stepY == 0 || stepZ == 0) {
            step++
            moons.forEach {
                it.updateVelocity(moons)
            }
            moons.forEach {
                it.updatePosition()
            }
            stepX = checkIfEqualState(stepX, moons.map { it.position.x to it.velocity.x }, firstX, step)
            stepY = checkIfEqualState(stepY, moons.map { it.position.y to it.velocity.y }, firstY, step)
            stepZ = checkIfEqualState(stepZ, moons.map { it.position.z to it.velocity.z }, firstZ, step)
        }

        println(leastCommonMultiple(stepX.toLong(), leastCommonMultiple(stepY.toLong(), stepZ.toLong())))
    }

    private fun checkIfEqualState(
        stepValue: Int,
        state: List<Pair<Int, Int>>,
        first: List<Pair<Int, Int>>,
        step: Int
    ): Int {
        if (stepValue == 0 && state == first) {
            return step
        }
        return stepValue
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
