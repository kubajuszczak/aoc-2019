import kotlinx.coroutines.runBlocking
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

private fun part1(program: List<Int>): Int = runBlocking {
    val result = runAsync(modifiedProgram(program, 12, 2))
    return@runBlocking result.memory[0]
}

private fun part2(program: List<Int>): List<Int> {
    val list = ArrayList<Int>()
    for (noun in 0..99) {
        for (verb in 0..99) {
            runBlocking {
                val result = runAsync(modifiedProgram(program, noun, verb))
                if (result.memory[0] == 19690720) {
                    list.add(100 * noun + verb)
                }
            }
        }
    }
    return list
}

private fun modifiedProgram(program: List<Int>, noun: Int, verb: Int): IntComputer {
    val memory = program.toMutableList()
    memory[1] = noun
    memory[2] = verb

    return IntComputer(memory)
}