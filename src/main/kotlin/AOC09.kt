import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input09.txt").readText().split(",").map { it.toLong() }

    measureTimeMillis {
        AOC09.part1(input)
    }.also { println("${it}ms") }

    measureTimeMillis {
        AOC09.part2(input)
    }.also { println("${it}ms") }
}

class AOC09 {
    companion object {
        fun part1(program: List<Long>) {
            val c = IntComputer(inputFunction = {1}, outputFunction = {println("OUTPUT: $it")})

            runBlocking {
                runAsync(c, program)
            }
        }

        fun part2(program: List<Long>) {
            val c = IntComputer(inputFunction = {2}, outputFunction = {println("OUTPUT: $it")})

            runBlocking {
                runAsync(c, program)
            }
        }
    }
}
