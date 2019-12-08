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
        val a = listOf(permutation[0], 0)
        val b = listOf(permutation[1])
        val c = listOf(permutation[2])
        val d = listOf(permutation[3])
        val e = listOf(permutation[4])

        runBlocking {
            val inputChannel = Channel<Int>(Channel.UNLIMITED)
            val outputChannel = Channel<Int>(Channel.UNLIMITED)

            val ampA = IntComputer(program, inputChannel, outputChannel)
            a.forEach { inputChannel.send(it) }
            runSync(ampA)

            val ampB = IntComputer(program, inputChannel, outputChannel)
            b.forEach { inputChannel.send(it) }
            inputChannel.send(outputChannel.receive())
            runSync(ampB)

            val ampC = IntComputer(program, inputChannel, outputChannel)
            c.forEach { inputChannel.send(it) }
            inputChannel.send(outputChannel.receive())
            runSync(ampC)

            val ampD = IntComputer(program, inputChannel, outputChannel)
            d.forEach { inputChannel.send(it) }
            inputChannel.send(outputChannel.receive())
            runSync(ampD)

            val ampE = IntComputer(program, inputChannel, outputChannel)
            e.forEach { inputChannel.send(it) }
            inputChannel.send(outputChannel.receive())
            runSync(ampE)

            outputs.add(outputChannel.receive())
        }
    }

    return outputs.max()
}

private fun part2(program: List<Int>): Int? {
    val outputs = ArrayList<Int>()

    for (permutation in permutations(listOf(5, 6, 7, 8, 9))) {
        val a = listOf(permutation[0], 0)
        val b = listOf(permutation[1])
        val c = listOf(permutation[2])
        val d = listOf(permutation[3])
        val e = listOf(permutation[4])

        val channelA = Channel<Int>(Channel.UNLIMITED)
        val channelB = Channel<Int>(Channel.UNLIMITED)
        val channelC = Channel<Int>(Channel.UNLIMITED)
        val channelD = Channel<Int>(Channel.UNLIMITED)
        val channelE = Channel<Int>(Channel.UNLIMITED)

        runBlocking {
            a.forEach { channelA.send(it) }
            b.forEach { channelB.send(it) }
            c.forEach { channelC.send(it) }
            d.forEach { channelD.send(it) }
            e.forEach { channelE.send(it) }

            launch {
                val ampA = IntComputer(program, channelA, channelB)
                runAsync(ampA)
            }
            launch {
                val ampB = IntComputer(program, channelB, channelC)
                runAsync(ampB)
            }
            launch {
                val ampC = IntComputer(program, channelC, channelD)
                runAsync(ampC)
            }
            launch {
                val ampD = IntComputer(program, channelD, channelE)
                runAsync(ampD)
            }
            launch {
                val ampE = IntComputer(program, channelE, channelA)
                runAsync(ampE)
            }
        }
        runBlocking {
            outputs.add(channelA.receive())
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