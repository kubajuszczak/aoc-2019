import java.lang.Exception
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input02.txt").readText().split(",").map { it.toInt() }

    measureTimeMillis {
        println(part1(input))
    }.also { println("${it}ms") }

    measureTimeMillis {
        println(part2(input))
    }.also { println("${it}ms") }
}

private fun part1(input: List<Int>): Int {
    return runProgram(setInputs(input, 12, 2))
}

private fun part2(input: List<Int>): List<Int> {
    val list = ArrayList<Int>()
    for (noun in 0..99) {
        for (verb in 0..99) {
            if (runProgram(setInputs(input, noun, verb), false) == 19690720) {
                list.add(100 * noun + verb)
            }
        }
    }
    return list
}

fun setInputs(program: List<Int>, noun: Int, verb: Int): IntComputer {
    val memory = program.toMutableList()
    memory[1] = noun
    memory[2] = verb

    return IntComputer(0, memory)
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
        return c.read(0)
    } catch (e: MemoryOverflowException) {
        println("Program pointer exceeded memory size")
        return -1
    } catch (e: UnsupportedOpcodeException){
        println("Attempted to run unsupported opcode ${e.message}")
        return -1
    }
}

class IntComputer(
    private val pointer: Int,
    private val memory: List<Int>
) {
    private fun write(address: Int, value: Int): List<Int> {
        val newMemory = memory.toMutableList()
        newMemory[address] = value
        return newMemory
    }

    private fun writeIndirect(address: Int, value: Int): List<Int> {
        return write(read(address), value)
    }

    fun read(address: Int): Int {
        return memory[address]
    }

    private fun readIndirect(address: Int): Int {
        return read(memory[address])
    }

    fun runInstruction(): IntComputer {
        if (pointer >= memory.size) {
            throw MemoryOverflowException()
        }

        when (val opcode = read(pointer)) {
            1 -> {
                return IntComputer(
                    pointer + 4,
                    writeIndirect(pointer + 3, readIndirect(pointer + 1) + readIndirect(pointer + 2))
                )
            }
            2 -> {
                return IntComputer(
                    pointer + 4,
                    writeIndirect(pointer + 3, readIndirect(pointer + 1) * readIndirect(pointer + 2))
                )
            }
            99 -> {
                throw HaltException()
            }
            else -> {
                throw UnsupportedOpcodeException("$opcode")
            }
        }
    }

    override fun toString(): String {
        return "IntComputer(pointer=$pointer, memory=$memory)"
    }
}

class HaltException() : Exception()
class MemoryOverflowException() : Exception()
class UnsupportedOpcodeException(s: String) : Exception(s)