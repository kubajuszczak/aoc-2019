import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input07.txt").readText().split(",").map { it.toLong() }

    measureTimeMillis {
        println(AOC07.part1(input))
    }.also { println("${it}ms") }

    measureTimeMillis {
        println(AOC07.part2(input))
    }.also { println("${it}ms") }
}

class AOC07 {
    companion object {
        fun part1(program: List<Long>): Long? {
            val outputs = ArrayList<Long>()

            for (permutation in permutations(listOf(0, 1, 2, 3, 4))) {
                val channels = (0..4).map { Channel<Long>(Channel.UNLIMITED) }
                val outputChannel = Channel<Long>(Channel.UNLIMITED)

                runBlocking {
                    channels.forEachIndexed { index, channel -> channel.send(permutation[index].toLong()) }
                    channels[0].send(0)

                    runAsync(IntComputer(inputChannel = channels[0], outputChannel = channels[1]), program)
                    runAsync(IntComputer(inputChannel = channels[1], outputChannel = channels[2]), program)
                    runAsync(IntComputer(inputChannel = channels[2], outputChannel = channels[3]), program)
                    runAsync(IntComputer(inputChannel = channels[3], outputChannel = channels[4]), program)
                    runAsync(IntComputer(inputChannel = channels[4], outputChannel = outputChannel), program)

                    outputs.add(outputChannel.receive())
                }
            }

            return outputs.max()
        }

        fun part2(program: List<Long>): Long? {
            val outputs = ArrayList<Long>()

            for (permutation in permutations(listOf(5, 6, 7, 8, 9))) {
                val channels = (0..4).map { Channel<Long>(Channel.UNLIMITED) }

                runBlocking {
                    channels.forEachIndexed { index, channel -> channel.send(permutation[index].toLong()) }
                    channels[0].send(0)

                    launch { runAsync(IntComputer(inputChannel = channels[0], outputChannel = channels[1]), program) }
                    launch { runAsync(IntComputer(inputChannel = channels[1], outputChannel = channels[2]), program) }
                    launch { runAsync(IntComputer(inputChannel = channels[2], outputChannel = channels[3]), program) }
                    launch { runAsync(IntComputer(inputChannel = channels[3], outputChannel = channels[4]), program) }
                    launch { runAsync(IntComputer(inputChannel = channels[4], outputChannel = channels[0]), program) }
                }

                runBlocking {
                    outputs.add(channels[0].receive())
                }
            }

            return outputs.max()
        }

        // https://rosettacode.org/wiki/Permutations
        private fun <T> permutations(input: List<T>): List<List<T>> {
            if (input.size == 1) return listOf(input)
            val perms = mutableListOf<List<T>>()
            val toInsert = input[0]
            for (perm in permutations(input.drop(1))) {
                for (i in 0..perm.size) {
                    val newPerm = perm.toMutableList()
                    newPerm.add(i, toInsert)
                    perms.add(newPerm)
                }
            }
            return perms
        }
    }
}

