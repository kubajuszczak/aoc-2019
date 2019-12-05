import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input05.txt").readText().split(",").map { it.toInt() }

    measureTimeMillis {
        part1(input)
    }.also { println("${it}ms") }

    measureTimeMillis {
        part2(input)
    }.also { println("${it}ms") }
}

private fun part1(program: List<Int>) {
    val computer = IntComputer(0, program) { 1 }
    runProgram(computer)
}

private fun part2(program: List<Int>) {
    val computer = IntComputer(0, program) { 5 }
    runProgram(computer)
}