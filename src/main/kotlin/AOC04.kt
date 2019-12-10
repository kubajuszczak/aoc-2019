import kotlin.system.measureTimeMillis

fun main() {

    measureTimeMillis {
        println(AOC04.part1())
    }.also { println("${it}ms") }

    measureTimeMillis {
        println(AOC04.part2())
    }.also { println("${it}ms") }

}

class AOC04 {
    companion object {
        fun part1(): Int {
            return (240298..784956)
                .filter { hasNonDecreasingDigits(it.toString()) }
                .filter { hasTwoAdjacentSameDigits(it.toString()) }
                .count()
        }

        fun part2(): Int {
            return (240298..784956)
                .filter { hasNonDecreasingDigits(it.toString()) }
                .filter { hasAGroupOfExactlyTwoAdjacentSameDigits(it.toString()) }
                .count()
        }

        fun hasNonDecreasingDigits(password: String): Boolean {
            for (index in 1 until password.length) {
                if (password[index].toInt() < password[index - 1].toInt()) {
                    return false
                }
            }
            return true
        }

        fun hasTwoAdjacentSameDigits(password: String): Boolean {
            for (index in 1 until password.length) {
                if (password[index].toInt() == password[index - 1].toInt()) {
                    return true
                }
            }
            return false
        }

        // like run-length encoding but without saying what symbol is repeated
        fun hasAGroupOfExactlyTwoAdjacentSameDigits(password: String): Boolean {
            var currentRun = 1
            var rle = ""

            for (index in 1 until password.length) {
                if (password[index] == password[index - 1]) {
                    currentRun++
                } else {
                    rle += currentRun
                    currentRun = 1
                }
                if (index == password.length - 1) {
                    rle += currentRun
                }
            }

            return rle.contains('2')
        }
    }
}