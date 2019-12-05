import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input05.txt").readText().split(",").map { it.toInt() }

    measureTimeMillis {
        part1(input)
    }.also { println("${it}ms") }
}

private fun part1(input: List<Int>) {
    val computer = IntComputer(0, input) { 1 }
    runProgram(computer)
}