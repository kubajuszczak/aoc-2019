import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input02.txt").readText().split(",").map { it.toLong() }

    measureTimeMillis {
        println(part1(input))
    }.also { println("${it}ms") }

    measureTimeMillis {
        println(part2(input))
    }.also { println("${it}ms") }
}

private fun part1(program: List<Long>): Long = runBlocking {
    val result = runAsync(IntComputer(), modifiedProgram(program, 12, 2))
    return@runBlocking result.memory[0]
}

private fun part2(program: List<Long>): List<Int> {
    val list = ArrayList<Int>()
    for (noun in 0..99) {
        for (verb in 0..99) {
            runBlocking {
                val result = runAsync(IntComputer(), modifiedProgram(program, noun, verb))
                if (result.memory[0] == 19690720L) {
                    list.add((100 * noun + verb))
                }
            }
        }
    }
    return list
}

private fun modifiedProgram(program: List<Long>, noun: Int, verb: Int): List<Long> {
    val modifiedProgram = program.toMutableList()
    modifiedProgram[1] = noun.toLong()
    modifiedProgram[2] = verb.toLong()

    return modifiedProgram
}