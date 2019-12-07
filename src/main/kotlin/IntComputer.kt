import java.lang.Exception

class IntComputer(
    private val pointer: Int,
    private val memory: List<Int>,
    private val inputFunction: () -> (Int) = { 0 },
    private val outputFunction: (Int) -> (Unit) = { println("OUTPUT: $it") }
) {
    private fun writeImmediate(address: Int, value: Int): List<Int> {
        val newMemory = memory.toMutableList()
        newMemory[address] = value
        return newMemory
    }

    private fun write(addressPointer: Int, value: Int): List<Int> {
        return writeImmediate(readImmediate(addressPointer), value)
    }

    fun readImmediate(address: Int): Int {
        return memory[address]
    }

    private fun read(addressPointer: Int): Int {
        return readImmediate(readImmediate(addressPointer))
    }

    data class Instruction(val mode1: Int, val mode2: Int, val mode3: Int, val opcode: Int)

    private fun decodeInstruction(instruction: Int): Instruction {
        return Instruction(
            instruction.div(100).rem(10),
            instruction.div(1000).rem(10),
            instruction.div(10000).rem(10),
            instruction.rem(100)
        )
    }

    private fun getParameter(mode: Int, value: Int): Int {
        return when (mode) {
            0 ->
                read(value)
            1 ->
                readImmediate(value)
            else ->
                throw InvalidInstructionException("Unknown parameter mode $mode; ${readImmediate(pointer)} at $pointer")
        }
    }

    fun runInstruction(): IntComputer {
        if (pointer >= memory.size) {
            throw MemoryOverflowException()
        }

        // fetch
        val instruction = readImmediate(pointer)

        // decode
        val (mode1, mode2, mode3, opcode) = decodeInstruction(instruction)

        // execute
        when (opcode) {
            1 -> { // ADD
                if (mode3 == 1) {
                    throw InvalidInstructionException("Immediate mode for write address parameter; $instruction at $pointer")
                }

                val argument1 = getParameter(mode1, pointer + 1)
                val argument2 = getParameter(mode2, pointer + 2)

                val result = argument1 + argument2
                return IntComputer(
                    pointer + 4,
                    write(pointer + 3, result), inputFunction, outputFunction
                )
            }
            2 -> { // MUL
                if (mode3 == 1) {
                    throw InvalidInstructionException("Immediate mode for write address parameter; $instruction at $pointer")
                }

                val argument1 = getParameter(mode1, pointer + 1)
                val argument2 = getParameter(mode2, pointer + 2)

                val result = argument1 * argument2
                return IntComputer(
                    pointer + 4,
                    write(pointer + 3, result), inputFunction, outputFunction
                )
            }
            3 -> { //INPUT
                val value = inputFunction()
                return IntComputer(pointer + 2, write(pointer + 1, value), inputFunction, outputFunction)
            }
            4 -> { //OUTPUT
                val argument1 = getParameter(mode1, pointer + 1)

                outputFunction(argument1)
                return IntComputer(pointer + 2, memory, inputFunction, outputFunction)
            }
            5 -> { //JMP NON-ZERO
                val argument1 = getParameter(mode1, pointer + 1)
                val argument2 = getParameter(mode2, pointer + 2)

                return if (argument1 != 0) {
                    IntComputer(argument2, memory, inputFunction, outputFunction)
                } else {
                    IntComputer(pointer + 3, memory, inputFunction, outputFunction)
                }
            }
            6 -> { //JMP ZERO
                val argument1 = getParameter(mode1, pointer + 1)
                val argument2 = getParameter(mode2, pointer + 2)

                return if (argument1 == 0) {
                    IntComputer(argument2, memory, inputFunction, outputFunction)
                } else {
                    IntComputer(pointer + 3, memory, inputFunction, outputFunction)
                }
            }
            7 -> { //LESS THAN
                if (mode3 == 1) {
                    throw InvalidInstructionException("Immediate mode for write address parameter; $instruction at $pointer")
                }

                val argument1 = getParameter(mode1, pointer + 1)
                val argument2 = getParameter(mode2, pointer + 2)

                return if (argument1 < argument2) {
                    IntComputer(pointer + 4, write(pointer + 3, 1), inputFunction, outputFunction)
                } else {
                    IntComputer(pointer + 4, write(pointer + 3, 0), inputFunction, outputFunction)
                }
            }
            8 -> { // EQUAL
                if (mode3 == 1) {
                    throw InvalidInstructionException("Immediate mode for write address parameter; $instruction at $pointer")
                }

                val argument1 = getParameter(mode1, pointer + 1)
                val argument2 = getParameter(mode2, pointer + 2)

                return if (argument1 == argument2) {
                    IntComputer(pointer + 4, write(pointer + 3, 1), inputFunction, outputFunction)
                } else {
                    IntComputer(pointer + 4, write(pointer + 3, 0), inputFunction, outputFunction)
                }
            }
            99 -> {  //HALT
                throw HaltException()
            }
            else -> {
                throw UnsupportedOpcodeException("Unknown opcode $opcode; $instruction at $pointer")
            }
        }
    }

    override fun toString(): String {
        return "IntComputer(pointer=$pointer, memory=$memory)"
    }
}

fun runProgram(computer: IntComputer, verbose: Boolean = false): Int {
    var c = computer
    if (verbose) println(c)

    try {
        while (true) {
            c = c.runInstruction()
            if (verbose) println(c)
        }
    } catch (e: HaltException) {
        return c.readImmediate(0)
    } catch (e: MemoryOverflowException) {
        println("Program pointer exceeded memory size")
        return -1
    } catch (e: UnsupportedOpcodeException) {
        println(e.message)
        return -1
    }
}

class HaltException : Exception()
class MemoryOverflowException : Exception()
class UnsupportedOpcodeException(s: String) : Exception(s)
class InvalidInstructionException(s: String) : Exception(s)