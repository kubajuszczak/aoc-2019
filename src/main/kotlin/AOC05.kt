import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input05.txt").readText().split(",").map { it.toLong() }

    measureTimeMillis {
        AOC05.part1(input)
    }.also { println("${it}ms") }

    measureTimeMillis {
        AOC05.part2(input)
    }.also { println("${it}ms") }
}

class AOC05 {
    companion object {
        fun part1(program: List<Long>) {
            runBlocking {
                val computer = IntComputer(inputFunction = {1}, outputFunction = {println("OUTPUT: $it")})
                runAsync(computer, program)
            }
        }

        fun part2(program: List<Long>) {
            runBlocking {
                val computer = IntComputer(inputFunction = {5}, outputFunction = {println("OUTPUT: $it")})
                runAsync(computer, program)

            }
        }
    }
}
