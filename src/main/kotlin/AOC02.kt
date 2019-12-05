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