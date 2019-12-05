import java.lang.Exception

class IntComputer(
    private val pointer: Int,
    private val memory: List<Int>,
    private val inputFunction: () -> (Int) = { 0 }
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

    fun runInstruction(): IntComputer {
        if (pointer >= memory.size) {
            throw MemoryOverflowException()
        }

        // fetch
        val instruction = readImmediate(pointer)

        // decode
        val (mode1, mode2, mode3) = Triple(
            instruction.div(100).rem(10),
            instruction.div(1000).rem(10),
            instruction.div(10000).rem(10)
        )

        // execute
        when (val opcode = instruction.rem(100)) {
            1 -> {
                if (mode3 == 1) {
                    throw InvalidInstructionException("Immediate mode for write address parameter; $instruction at $pointer")
                }

                val argument1 = when (mode1) {
                    0 ->
                        read(pointer + 1)
                    1 ->
                        readImmediate(pointer + 1)
                    else ->
                        throw InvalidInstructionException("Unknown parameter mode $mode1; $instruction at $pointer")
                }
                val argument2 = when (mode2) {
                    0 ->
                        read(pointer + 2)
                    1 ->
                        readImmediate(pointer + 2)
                    else ->
                        throw InvalidInstructionException("Unknown parameter mode $mode2; $instruction at $pointer")
                }

                val result = argument1 + argument2
                return IntComputer(
                    pointer + 4,
                    write(pointer + 3, result), inputFunction
                )
            }
            2 -> {
                if (mode3 == 1) {
                    throw InvalidInstructionException("Immediate mode for write address parameter; $instruction at $pointer")
                }

                val argument1 = when (mode1) {
                    0 ->
                        read(pointer + 1)
                    1 ->
                        readImmediate(pointer + 1)
                    else ->
                        throw InvalidInstructionException("Unknown parameter mode $mode1; $instruction at $pointer")
                }
                val argument2 = when (mode2) {
                    0 ->
                        read(pointer + 2)
                    1 ->
                        readImmediate(pointer + 2)
                    else ->
                        throw InvalidInstructionException("Unknown parameter mode $mode2; $instruction at $pointer")
                }

                val result = argument1 * argument2
                return IntComputer(
                    pointer + 4,
                    write(pointer + 3, result), inputFunction
                )
            }
            3 -> {
                val value = inputFunction()
                println("INPUT: $value")
                return IntComputer(pointer + 2, write(pointer + 1, value), inputFunction)
            }
            4 -> {
                val value = read(pointer + 1)
                println("OUTPUT: $value")
                return IntComputer(pointer + 2, memory, inputFunction)
            }
            99 -> {
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
    } catch (e: UnsupportedOpcodeException){
        println(e.message)
        return -1
    }
}

class HaltException : Exception()
class MemoryOverflowException : Exception()
class UnsupportedOpcodeException(s: String) : Exception(s)
class InvalidInstructionException(s: String) : Exception(s)