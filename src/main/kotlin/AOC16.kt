import kotlin.math.abs
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input16.txt").readText()

    measureTimeMillis {
        println(AOC16.part1(input))
    }.also { println("${it}ms") }

    measureTimeMillis {
        println(AOC16.part2(input))
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

        fun part2(input: String): String {
            val skip = input.substring(0 until 7).toInt()
            val elementCount = input.length * 10000 - skip

            var seq = input.map { it.toString().toInt() }

            // take the remainder + even multiples of input
            // so that the list length is input * 10000 - offset
            seq = seq.drop(skip % input.length) + (0 until elementCount / input.length).flatMap { seq }

            for (i in 1..100) {
                //assumption: the offset is so far into the message that any digit before has a 0 multiplier
                // and any digit after is a 1
                seq = fft2(seq)
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

        private fun fft2(input: List<Int>): List<Int> {
            // in matrix form this would be an upper triangular matrix with all 1s along the diagonal and above
            // essentially sums from the current digit to the end

            val sums = ArrayList<Int>()
            var last = 0
            for (n in input.reversed()) {
                sums.add(n + last)
                last += n
            }
            return sums.reversed().map { it % 10 }
        }
    }
}

fun <T> Sequence<T>.repeat() = sequence { while (true) yieldAll(this@repeat) }