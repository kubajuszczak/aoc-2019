import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input01.txt").readLines()

    measureTimeMillis {
        println(AOC01.part1(input))
    }.also { println("${it}ms") }

    measureTimeMillis {
        println(AOC01.part2(input))
    }.also { println("${it}ms") }
}

class AOC01 {
    companion object {
        fun part1(input: List<String>): Int {
            return input
                .map { getFuel(it.toInt()) }
                .sum()
        }

        fun part2(input: List<String>): Int {
            return input
                .map { getRecursiveFuel(it.toInt()) }
                .sum()
        }

        fun getFuel(mass: Int): Int {
            return mass / 3 - 2
        }

        fun getRecursiveFuel(mass: Int): Int {
            val x = getFuel(mass)
            return if (x <= 0) {
                0
            } else {
                getRecursiveFuel(x) + x
            }
        }
    }
}
