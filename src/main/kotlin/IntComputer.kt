import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.runBlocking
import java.lang.Exception

class IntComputer(
    val memory: List<Int>,
    private val inputChannel: ReceiveChannel<Int> = Channel(),
    private val outputChannel: SendChannel<Int> = Channel(),
    private val pointer: Int = 0
) {

    private fun readImmediate(address: Int): Int {
        return memory[address]
    }

    private fun read(addressPointer: Int): Int {
        return readImmediate(readImmediate(addressPointer))
    }

    private fun write(addressPointer: Int, value: Int): List<Int> {
        val address = readImmediate(addressPointer)
        val newMemory = memory.toMutableList()
        newMemory[address] = value
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

    private fun getParameter(mode: Int, value: Int): Int {
        return when (mode) {
            0 -> read(value)
            1 -> readImmediate(value)
            else ->
                throw InvalidInstructionException("Unknown parameter mode $mode; ${readImmediate(pointer)} at $pointer")
        }
    }

    private fun newState(memory: List<Int>, pointer: Int): IntComputer {
        return IntComputer(memory, inputChannel, outputChannel, pointer)
    }

    suspend fun runInstruction(): IntComputer {
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
                val newMemory = write(pointer + 3, result)

                return newState(newMemory, pointer + 4)
            }
            2 -> { // MUL
                if (mode3 == 1) {
                    throw InvalidInstructionException("Immediate mode for write address parameter; $instruction at $pointer")
                }

                val argument1 = getParameter(mode1, pointer + 1)
                val argument2 = getParameter(mode2, pointer + 2)

                val result = argument1 * argument2

                val newMemory = write(pointer + 3, result)

                return newState(newMemory, pointer + 4)
            }
            3 -> { //INPUT
                val value = inputChannel.receive()

                val newMemory = write(pointer + 1, value)
                return newState(newMemory, pointer + 2)
            }
            4 -> { //OUTPUT
                val argument1 = getParameter(mode1, pointer + 1)

                outputChannel.send(argument1)
                return newState(memory, pointer + 2)
            }
            5 -> { //JMP NON-ZERO
                val argument1 = getParameter(mode1, pointer + 1)
                val argument2 = getParameter(mode2, pointer + 2)

                return if (argument1 != 0) {
                    newState(memory, argument2)
                } else {
                    newState(memory, pointer + 3)
                }
            }
            6 -> { //JMP ZERO
                val argument1 = getParameter(mode1, pointer + 1)
                val argument2 = getParameter(mode2, pointer + 2)

                return if (argument1 == 0) {
                    newState(memory, argument2)
                } else {
                    newState(memory, pointer + 3)
                }
            }
            7 -> { //LESS THAN
                if (mode3 == 1) {
                    throw InvalidInstructionException("Immediate mode for write address parameter; $instruction at $pointer")
                }

                val argument1 = getParameter(mode1, pointer + 1)
                val argument2 = getParameter(mode2, pointer + 2)

                val newMemory = when (argument1 < argument2) {
                    true -> write(pointer + 3, 1)
                    false -> write(pointer + 3, 0)
                }

                return newState(newMemory, pointer + 4)
            }
            8 -> { // EQUAL
                if (mode3 == 1) {
                    throw InvalidInstructionException("Immediate mode for write address parameter; $instruction at $pointer")
                }

                val argument1 = getParameter(mode1, pointer + 1)
                val argument2 = getParameter(mode2, pointer + 2)

                val newMemory = when (argument1 == argument2) {
                    true -> write(pointer + 3, 1)
                    false -> write(pointer + 3, 0)
                }

                return newState(newMemory, pointer + 4)
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

suspend fun runAsync(computer: IntComputer): IntComputer {
    var c = computer

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