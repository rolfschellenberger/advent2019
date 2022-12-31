package com.rolf.util

import java.util.concurrent.LinkedBlockingQueue

fun readMemory(line: String, capacity: Int = 0): MutableList<Long> {
    val memory = LongArray(capacity) { 0 }.toMutableList()
    memory.addAll(0, splitLine(line, ",").map { it.toLong() })
    return memory
}

class IntcodeState(
    private val memory: MutableList<Long>,
    val input: LinkedBlockingQueue<Long> = LinkedBlockingQueue(mutableListOf()),
    val output: LinkedBlockingQueue<Long> = LinkedBlockingQueue(mutableListOf()),
    var pointer: Int = 0,
    var relativeBase: Int = 0,
    var stop: Boolean = false
) : Runnable {
    private val initialMemory: List<Long> = memory.map { it }
    private val initialInput: List<Long> = input.map { it }
    private var isReading = false

    override fun run() {
        execute()
    }

    fun reset() {
        memory.clear()
        memory.addAll(initialMemory)
        input.clear()
        input.addAll(initialInput)
        pointer = 0
        relativeBase = 0
        output.clear()
        stop = false
        isReading = false
    }

    fun isDone(): Boolean {
        return pointer >= memory.size || stop
    }

    fun isReading(): Boolean {
        return isReading
    }

    fun execute() {
        while (!isDone()) {
            executeNext()
        }
    }

    private fun executeNext() {
        Operation.build(this).execute(this)
    }

    fun getOpcode(): Int {
        val value = memory[pointer].toString()
        if (value.length <= 2) return value.toInt()
        return value.substring(value.length - 2, value.length).toInt()
    }

    fun getParameters(parameterCount: Int): List<Parameter> {
        if (parameterCount <= 0) return emptyList()
        val value = memory[pointer].toString()

        val parameters = (0 until parameterCount)
            .map { Parameter(it + 1, ParameterMode.Position) }
            .toMutableList()
        if (value.length <= 2) return parameters

        // Update mode values when provided
        val modeValues = splitLine(value.substring(0, value.length - 2)).map { it.toInt() }.reversed()
        for ((index, mode) in modeValues.withIndex()) {
            parameters[index] = parameters[index].copy(mode = ParameterMode.fromValue(mode))
        }
        return parameters
    }

    fun getParameter(parameter: Parameter): Long {
        return getValue(pointer + parameter.position, parameter.mode)
    }

    fun setParameter(parameter: Parameter, value: Long) {
        setValue(pointer + parameter.position, value, parameter.mode)
    }

    fun read(): Long {
        isReading = true
        return try {
            input.take()
        } catch (_: InterruptedException) {
            stop = true
            0
        } finally {
            isReading = false
        }
    }

    fun write(value: Long) {
        output.add(value)
    }

    private fun getValue(pointer: Int, mode: ParameterMode): Long {
        return when (mode) {
            ParameterMode.Position -> getValue(getValue(pointer).toInt())
            ParameterMode.Immediate -> getValue(pointer)
            ParameterMode.Relative -> getValue(relativeBase + getValue(pointer).toInt())
        }
    }

    private fun getValue(pointer: Int): Long {
        return memory[pointer]
    }

    private fun setValue(pointer: Int, value: Long, mode: ParameterMode) {
        when (mode) {
            ParameterMode.Position -> setValue(getValue(pointer).toInt(), value)
            ParameterMode.Immediate -> setValue(pointer, value)
            ParameterMode.Relative -> setValue(relativeBase + getValue(pointer).toInt(), value)
        }
    }

    private fun setValue(pointer: Int, value: Long) {
        memory[pointer] = value
    }
}

data class Parameter(val position: Int, val mode: ParameterMode)

enum class ParameterMode {
    Position,
    Immediate,
    Relative;

    companion object {
        fun fromValue(mode: Int): ParameterMode {
            return when (mode) {
                0 -> Position
                1 -> Immediate
                2 -> Relative
                else -> throw Exception("Unknown parameter mode: $mode")
            }
        }
    }
}

abstract class Operation(private val opcode: Int, val parameters: List<Parameter>) {

    fun execute(state: IntcodeState) {
        val pointerDelta = executeOperation(state)
        state.pointer += pointerDelta
    }

    abstract fun executeOperation(state: IntcodeState): Int

    override fun toString(): String {
        return "Operation(opcode=$opcode, parameters=$parameters)"
    }

    companion object {
        fun build(state: IntcodeState): Operation {
            return when (val opcode = state.getOpcode()) {
                Add.OPCODE -> Add.build(state)
                Multiply.OPCODE -> Multiply.build(state)
                Read.OPCODE -> Read.build(state)
                Write.OPCODE -> Write.build(state)
                JumpIfTrue.OPCODE -> JumpIfTrue.build(state)
                JumpIfFalse.OPCODE -> JumpIfFalse.build(state)
                LessThan.OPCODE -> LessThan.build(state)
                Equals.OPCODE -> Equals.build(state)
                RelativeBase.OPCODE -> RelativeBase.build(state)
                Halt.OPCODE -> Halt.build(state)
                else -> throw Exception("New opcode found: $opcode")
            }
        }
    }
}

class Add(parameters: List<Parameter>) : Operation(OPCODE, parameters) {

    override fun executeOperation(state: IntcodeState): Int {
        val value = state.getParameter(parameters[0]) + state.getParameter(parameters[1])
        state.setParameter(
            parameters[2],
            value
        )
        return PARAMETERS + 1
    }

    companion object {
        const val OPCODE = 1
        private const val PARAMETERS = 3

        fun build(state: IntcodeState): Add {
            return Add(state.getParameters(PARAMETERS))
        }
    }
}

class Multiply(parameters: List<Parameter>) : Operation(OPCODE, parameters) {

    override fun executeOperation(state: IntcodeState): Int {
        val value = state.getParameter(parameters[0]) * state.getParameter(parameters[1])
        state.setParameter(
            parameters[2],
            value
        )
        return PARAMETERS + 1
    }

    companion object {
        const val OPCODE = 2
        private const val PARAMETERS = 3

        fun build(state: IntcodeState): Multiply {
            return Multiply(state.getParameters(PARAMETERS))
        }
    }
}

class Read(parameters: List<Parameter>) : Operation(OPCODE, parameters) {

    override fun executeOperation(state: IntcodeState): Int {
        state.setParameter(parameters[0], state.read())
        return PARAMETERS + 1
    }

    companion object {
        const val OPCODE = 3
        private const val PARAMETERS = 1

        fun build(state: IntcodeState): Read {
            return Read(state.getParameters(PARAMETERS))
        }
    }
}

class Write(parameters: List<Parameter>) : Operation(OPCODE, parameters) {

    override fun executeOperation(state: IntcodeState): Int {
        state.write(state.getParameter(parameters[0]))
        return PARAMETERS + 1
    }

    companion object {
        const val OPCODE = 4
        private const val PARAMETERS = 1

        fun build(state: IntcodeState): Write {
            return Write(state.getParameters(PARAMETERS))
        }
    }
}

class JumpIfTrue(parameters: List<Parameter>) : Operation(OPCODE, parameters) {

    override fun executeOperation(state: IntcodeState): Int {
        val value = state.getParameter(parameters[0])
        return if (value != 0L) {
            val newPointer = state.getParameter(parameters[1]).toInt()
            newPointer - state.pointer
        } else {
            PARAMETERS + 1
        }
    }

    companion object {
        const val OPCODE = 5
        private const val PARAMETERS = 2

        fun build(state: IntcodeState): JumpIfTrue {
            return JumpIfTrue(state.getParameters(PARAMETERS))
        }
    }
}

class JumpIfFalse(parameters: List<Parameter>) : Operation(OPCODE, parameters) {

    override fun executeOperation(state: IntcodeState): Int {
        val value = state.getParameter(parameters[0])
        return if (value == 0L) {
            val newPointer = state.getParameter(parameters[1]).toInt()
            newPointer - state.pointer
        } else {
            PARAMETERS + 1
        }
    }

    companion object {
        const val OPCODE = 6
        private const val PARAMETERS = 2

        fun build(state: IntcodeState): JumpIfFalse {
            return JumpIfFalse(state.getParameters(PARAMETERS))
        }
    }
}

class LessThan(parameters: List<Parameter>) : Operation(OPCODE, parameters) {

    override fun executeOperation(state: IntcodeState): Int {
        val a = state.getParameter(parameters[0])
        val b = state.getParameter(parameters[1])
        val value = if (a < b) 1L else 0L
        state.setParameter(parameters[2], value)
        return PARAMETERS + 1
    }

    companion object {
        const val OPCODE = 7
        private const val PARAMETERS = 3

        fun build(state: IntcodeState): LessThan {
            return LessThan(state.getParameters(PARAMETERS))
        }
    }
}

class Equals(parameters: List<Parameter>) : Operation(OPCODE, parameters) {

    override fun executeOperation(state: IntcodeState): Int {
        val a = state.getParameter(parameters[0])
        val b = state.getParameter(parameters[1])
        val value = if (a == b) 1L else 0L
        state.setParameter(parameters[2], value)
        return PARAMETERS + 1
    }

    companion object {
        const val OPCODE = 8
        private const val PARAMETERS = 3

        fun build(state: IntcodeState): Equals {
            return Equals(state.getParameters(PARAMETERS))
        }
    }
}

class RelativeBase(parameters: List<Parameter>) : Operation(OPCODE, parameters) {

    override fun executeOperation(state: IntcodeState): Int {
        state.relativeBase += state.getParameter(parameters[0]).toInt()
        return PARAMETERS + 1
    }

    companion object {
        const val OPCODE = 9
        private const val PARAMETERS = 1

        fun build(state: IntcodeState): RelativeBase {
            return RelativeBase(state.getParameters(PARAMETERS))
        }
    }
}

class Halt(parameters: List<Parameter>) : Operation(OPCODE, parameters) {

    override fun executeOperation(state: IntcodeState): Int {
        state.stop = true
        return PARAMETERS + 1
    }

    companion object {
        const val OPCODE = 99
        private const val PARAMETERS = 0

        fun build(state: IntcodeState): Halt {
            return Halt(state.getParameters(PARAMETERS))
        }
    }
}
