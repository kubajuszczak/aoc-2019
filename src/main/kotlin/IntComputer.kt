class IntComputer(
    val memory: List<Long> = (0..9000).map { 0L },
    private val inputFunction: suspend () -> Long = { 0 },
    private val outputFunction: suspend (Long) -> Unit = {},
    private val pointer: Int = 0,
    private val relativeBase: Int = 0
) {

    fun loadProgram(program: List<Long>): IntComputer {
        val newMemory = program + memory.drop(program.size)
        return IntComputer(newMemory, inputFunction, outputFunction, pointer, relativeBase)
    }

    private fun readImmediate(address: Int): Long {
        return memory[address]
    }

    private fun read(addressPointer: Int): Long {
        return readImmediate(readImmediate(addressPointer).toInt())
    }

    private fun readRelative(addressPointer: Int): Long {
        return readImmediate(readImmediate(addressPointer).toInt() + relativeBase)
    }

    private fun write(addressPointer: Int, value: Long, mode: Int): List<Long> {
        val address = when (mode) {
            0 -> readImmediate(addressPointer)
            1 -> throw InvalidInstructionException("Immediate mode for write address parameter; ${readImmediate(pointer)} at $pointer")
            2 -> relativeBase + readImmediate(addressPointer)
            else -> throw InvalidInstructionException("Unknown parameter mode $mode; ${readImmediate(pointer)} at $pointer")
        }

        val newMemory = memory.toMutableList()
        newMemory[address.toInt()] = value
        return newMemory
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

    private fun getParameter(mode: Int, value: Int): Long {
        return when (mode) {
            0 -> read(value)
            1 -> readImmediate(value)
            2 -> readRelative(value)
            else ->
                throw InvalidInstructionException("Unknown parameter mode $mode; ${readImmediate(pointer)} at $pointer")
        }
    }

    private fun newState(memory: List<Long>, pointer: Int): IntComputer {
        return IntComputer(memory, inputFunction, outputFunction, pointer, relativeBase)
    }

    private fun newState(memory: List<Long>, pointer: Int, relativeBase: Int): IntComputer {
        return IntComputer(memory, inputFunction, outputFunction, pointer, relativeBase)
    }

    suspend fun runInstruction(): IntComputer {
        if (pointer >= memory.size) {
            throw MemoryOverflowException()
        }

        // fetch
        val instruction = readImmediate(pointer)

        // decode
        val (mode1, mode2, mode3, opcode) = decodeInstruction(instruction.toInt())

        // execute
        when (opcode) {
            1 -> { // ADD
                val argument1 = getParameter(mode1, pointer + 1)
                val argument2 = getParameter(mode2, pointer + 2)

                val result = argument1 + argument2
                val newMemory = write(pointer + 3, result, mode3)

                return newState(newMemory, pointer + 4)
            }
            2 -> { // MUL
                val argument1 = getParameter(mode1, pointer + 1)
                val argument2 = getParameter(mode2, pointer + 2)

                val result = argument1 * argument2
                val newMemory = write(pointer + 3, result, mode3)

                return newState(newMemory, pointer + 4)
            }
            3 -> { //INPUT
                val value = inputFunction()

                val newMemory = write(pointer + 1, value, mode1)

                return newState(newMemory, pointer + 2)
            }
            4 -> { //OUTPUT
                val argument1 = getParameter(mode1, pointer + 1)

                outputFunction(argument1)

                return newState(memory, pointer + 2)
            }
            5 -> { //JMP NON-ZERO
                val argument1 = getParameter(mode1, pointer + 1)
                val argument2 = getParameter(mode2, pointer + 2)

                return if (argument1 != 0L) {
                    newState(memory, argument2.toInt())
                } else {
                    newState(memory, pointer + 3)
                }
            }
            6 -> { //JMP ZERO
                val argument1 = getParameter(mode1, pointer + 1)
                val argument2 = getParameter(mode2, pointer + 2)

                return if (argument1 == 0L) {
                    newState(memory, argument2.toInt())
                } else {
                    newState(memory, pointer + 3)
                }
            }
            7 -> { //LESS THAN
                val argument1 = getParameter(mode1, pointer + 1)
                val argument2 = getParameter(mode2, pointer + 2)

                val newMemory = when (argument1 < argument2) {
                    true -> write(pointer + 3, 1, mode3)
                    false -> write(pointer + 3, 0, mode3)
                }

                return newState(newMemory, pointer + 4)
            }
            8 -> { // EQUAL
                val argument1 = getParameter(mode1, pointer + 1)
                val argument2 = getParameter(mode2, pointer + 2)

                val newMemory = when (argument1 == argument2) {
                    true -> write(pointer + 3, 1, mode3)
                    false -> write(pointer + 3, 0, mode3)
                }

                return newState(newMemory, pointer + 4)
            }
            9 -> { // REL BASE
                val argument1 = getParameter(mode1, pointer + 1)

                return newState(memory, pointer + 2, relativeBase + argument1.toInt())
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

suspend fun runAsync(computer: IntComputer, program: List<Long>): IntComputer {
    var c = computer.loadProgram(program)

    try {
        while (true) {
            c = c.runInstruction()
        }
    } catch (e: HaltException) {
        return c
    }
}

class HaltException : Exception()
class MemoryOverflowException : Exception()
class UnsupportedOpcodeException(s: String) : Exception(s)
class InvalidInstructionException(s: String) : Exception(s)