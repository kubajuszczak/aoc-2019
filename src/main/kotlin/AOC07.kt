import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.system.measureTimeMillis

fun main() {
    val input = getInput("input07.txt").readText().split(",").map { it.toInt() }

    measureTimeMillis {
        println(part1(input))
    }.also { println("${it}ms") }

    measureTimeMillis {
        println(part2(input))
    }.also { println("${it}ms") }
}

private fun part1(program: List<Int>): Int? {
    val outputs = ArrayList<Int>()

    for (permutation in permutations(listOf(0, 1, 2, 3, 4))) {
        val channels = (0..4).map { Channel<Int>(Channel.UNLIMITED) }
        val outputChannel = Channel<Int>(Channel.UNLIMITED)

        runBlocking {
            channels.forEachIndexed { index, channel -> channel.send(permutation[index]) }
            channels[0].send(0)

            runAsync(IntComputer(program, channels[0], channels[1]))
            runAsync(IntComputer(program, channels[1], channels[2]))
            runAsync(IntComputer(program, channels[2], channels[3]))
            runAsync(IntComputer(program, channels[3], channels[4]))
            runAsync(IntComputer(program, channels[4], outputChannel))

            outputs.add(outputChannel.receive())
        }
    }

    return outputs.max()
}

private fun part2(program: List<Int>): Int? {
    val outputs = ArrayList<Int>()

    for (permutation in permutations(listOf(5, 6, 7, 8, 9))) {
        val channels = (0..4).map { Channel<Int>(Channel.UNLIMITED) }

        runBlocking {
            channels.forEachIndexed { index, channel -> channel.send(permutation[index]) }
            channels[0].send(0)

            launch { runAsync(IntComputer(program, channels[0], channels[1])) }
            launch { runAsync(IntComputer(program, channels[1], channels[2])) }
            launch { runAsync(IntComputer(program, channels[2], channels[3])) }
            launch { runAsync(IntComputer(program, channels[3], channels[4])) }
            launch { runAsync(IntComputer(program, channels[4], channels[0])) }
        }

        runBlocking {
            outputs.add(channels[0].receive())
        }
    }

    return outputs.max()
}

// https://rosettacode.org/wiki/Permutations
fun <T> permutations(input: List<T>): List<List<T>> {
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