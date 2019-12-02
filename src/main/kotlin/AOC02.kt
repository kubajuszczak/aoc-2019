import java.lang.Exception

fun main() {
    val input = getInput("input02.txt").readText().split(",").map { it.toInt() }

    println(iterate(setInputs(input, 12, 2)))

    for (noun in 0..99) {
        for (verb in 0..99) {
            if (iterate(setInputs(input, noun, verb), false) == 19690720) {
                println(100 * noun + verb)
            }
        }
    }

}

fun setInputs(program: List<Int>, noun: Int, verb: Int): IntComputer {
    val memory = program.toMutableList()
    memory[1] = noun
    memory[2] = verb

    return IntComputer(0, memory)
}

fun iterate(computer: IntComputer, verbose: Boolean = true): Int {
    var c = computer
    if (verbose) println(c)

    try {
        while (true) {
            c = c.runInstruction()
            if (verbose) println(c)
        }
    } catch (e: Exception) {
        return c.read(0)
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
            throw Exception("Stack Overflow")
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
                throw Exception("Halt")
            }
            else -> {
                throw Exception("Error $opcode")
            }
        }
    }

    override fun toString(): String {
        return "IntComputer(pointer=$pointer, memory=$memory)"
    }
}