import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input16.txt").readText()

    measureTimeMillis {
        println(AOC16.part1(input))
    }.also { println("${it}ms") }

}

class AOC16 {
    companion object {
        fun part1(input: String): String {
            var seq = input.map { it.toString().toInt() }
            for (i in 1..100) {
                seq = fft(seq)
            }
            return seq.slice(0 until 8).joinToString("")
        }

        private fun fft(input: List<Int>): List<Int> {
            return input.indices.map { index ->
                val sequence = getSequence(index).drop(1).iterator()

                input.map { it * sequence.next() }.sum().let { abs(it) % 10 }
            }.toList()
        }

        private fun getSequence(index: Int): Sequence<Int> {
            val base = listOf(0, 1, 0, -1)

            return base.flatMap { baseValue -> (0..index).map { baseValue } }.asSequence().repeat()
        }
    }
}

fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }