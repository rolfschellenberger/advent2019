package com.rolf.day22

import com.rolf.Day
import com.rolf.util.pushLeft
import com.rolf.util.pushRight
import java.math.BigInteger
import java.math.BigInteger.ONE
import java.math.BigInteger.ZERO

fun main() {
    Day22().run()
}

class Day22 : Day() {
    override fun solve1(lines: List<String>) {
        val deck = IntArray(10007) { n -> n }
        val instructions = lines.map { parse(it) }
        for (instruction in instructions) {
            when (instruction.type) {
                0 -> dealNewStack(deck)
                1 -> cutCards(deck, instruction.param)
                2 -> dealCards(deck, instruction.param)
            }
        }
        println(deck.indexOf(2019))
    }

    private fun parse(line: String): Instruction {
        val last = line.split(" ").last()
        return when {
            line.contains("new stack") -> Instruction(0)
            line.contains("cut") -> Instruction(1, last.toInt())
            line.contains("increment") -> Instruction(2, last.toInt())
            else -> throw Exception("Unknown instruction $line")
        }
    }

    private fun dealCards(deck: IntArray, increment: Int): IntArray {
        val copy = deck.copyOf()
        for (i in copy.indices) {
            val pos = (i * increment) % copy.size
            deck[pos] = copy[i]
        }
        return deck
    }

    private fun cutCards(deck: IntArray, cut: Int): IntArray {
        if (cut >= 0) return deck.pushLeft(cut)
        return deck.pushRight(-cut)
    }

    private fun dealNewStack(input: IntArray): IntArray {
        input.reverse()
        return input
    }

    override fun solve2(lines: List<String>) {
        println(modularArithmeticVersion(lines, 2020.toBigInteger()))
    }

    private fun modularArithmeticVersion(input: List<String>, find: BigInteger): BigInteger {
        val memory = arrayOf(ONE, ZERO)
        input.reversed().forEach { instruction ->
            when {
                "cut" in instruction ->
                    memory[1] += instruction.getBigInteger()

                "increment" in instruction ->
                    instruction.getBigInteger().modPow(NUMBER_OF_CARDS - TWO, NUMBER_OF_CARDS).also {
                        memory[0] *= it
                        memory[1] *= it
                    }

                "stack" in instruction -> {
                    memory[0] = memory[0].negate()
                    memory[1] = (memory[1].inc()).negate()
                }
            }
            memory[0] %= NUMBER_OF_CARDS
            memory[1] %= NUMBER_OF_CARDS
        }
        val power = memory[0].modPow(SHUFFLES, NUMBER_OF_CARDS)
        return ((power * find) +
                ((memory[1] * (power + NUMBER_OF_CARDS.dec())) *
                        ((memory[0].dec()).modPow(NUMBER_OF_CARDS - TWO, NUMBER_OF_CARDS))))
            .mod(NUMBER_OF_CARDS)
    }

    private fun String.getBigInteger(): BigInteger =
        this.split(" ").last().toBigInteger()

    companion object {
        val NUMBER_OF_CARDS = 119315717514047.toBigInteger()
        val SHUFFLES = 101741582076661.toBigInteger()
        val TWO = 2.toBigInteger()
    }
}

data class Instruction(val type: Int, val param: Int = 0)
